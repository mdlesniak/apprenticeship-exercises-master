package fi.iki.elonen;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;
import java.util.StringTokenizer;

/**
 * Handles one session, i.e. parses the HTTP request and returns the
 * response.
 */
public class HTTPSession implements Runnable {
  private RequestComponents requestComponents;
  private BufferedReader in;
  private NanoHTTPD nano;

  private Socket mySocket;
  private Thread t;
  private StringTokenizer requestTokens;
  private String httpMethod;
  private String uri;
  private Properties header;

  
  public HTTPSession(Socket s, NanoHTTPD nano) {
    this.nano = nano;
    startThread(s);
  }

  private void startThread(Socket s) {
    mySocket = s;
    t = new Thread(this);
    t.setDaemon(true);
    t.start();
  }
  
  public RequestComponents getRequestComponents() {
    return requestComponents;
  }
  
  public BufferedReader getInputReader() {
    return in;
  }

  public void run() {
    try {
      InputStream is = mySocket.getInputStream();
      if (is == null) return;
      in = new BufferedReader(new InputStreamReader(is));

      requestComponents = parseRequest();
      Response r = nano.serve(requestComponents);
      
      if (r == null)
        sendError(nano.HTTP_INTERNALERROR,
            "SERVER INTERNAL ERROR: Serve() returned a null response.");
      else
        sendResponse(r.status, r.mimeType, r.header, r.data);

      in.close();
    } catch (IOException ioe) {
      try {
        sendError(nano.HTTP_INTERNALERROR,
            "SERVER INTERNAL ERROR: IOException: "
                + ioe.getMessage());
      } catch (Throwable t) {
      } 
    } catch (InterruptedException ie) {
      // Thrown by sendError (which one?), ignore and exit the thread.
    }
  }

  private RequestComponents parseRequest() throws IOException, InterruptedException {
    requestTokens = new StringTokenizer(in.readLine());
    httpMethod = handleBadRequestSyntax(requestTokens);
    uri = requestTokens.nextToken();

    // Decode parameters from the URI
    Properties parms = new Properties();
    uri = decodeParameters(uri, parms); 

    header = new Properties();
    parseHeaders(requestTokens, header);

    // If the method is POST, there may be parameters
    // in data section, too, read it:
    if (httpMethod.equalsIgnoreCase("POST")) {
      handlePOST(parms, header);
    }

    return new RequestComponents(uri, httpMethod, header, parms);
  }

  private void parseHeaders(StringTokenizer requestTokens,
      Properties header) throws IOException {
    // If there's another token, it's protocol version,
    // followed by HTTP headers. Ignore version but parse headers.
    // NOTE: this now forces header names uppercase since they are
    // case insensitive and vary by client.
    if (requestTokens.hasMoreTokens()) {
      String line = in.readLine();
      while (line.trim().length() > 0) {
        int p = line.indexOf(':');
        header.put(line.substring(0, p).trim().toLowerCase(),line.substring(p + 1).trim());
        line = in.readLine();
      }
    }
  }

  private String decodeParameters(String uri, Properties parms)
      throws InterruptedException {
    int qmi = uri.indexOf('?');
    if (qmi >= 0) {
      decodeParms(uri.substring(qmi + 1), parms);
      uri = decodePercent(uri.substring(0, qmi));
    } else
      uri = decodePercent(uri);
    return uri;
  }

  private String handleBadRequestSyntax(StringTokenizer requestTokens)
      throws InterruptedException {
    if (!requestTokens.hasMoreTokens())
      sendError(nano.HTTP_BADREQUEST,
          "BAD REQUEST: Syntax error. Usage: GET /example/file.html");

    String httpMethod = requestTokens.nextToken();

    if (!requestTokens.hasMoreTokens())
      sendError(nano.HTTP_BADREQUEST,
          "BAD REQUEST: Missing URI. Usage: GET /example/file.html");
    return httpMethod;
  }

  private void handlePOST(Properties parms, Properties header)
      throws IOException, InterruptedException {
    long size = 0x7FFFFFFFFFFFFFFFl;
    String contentLength = header.getProperty("content-length");
    if (contentLength != null) {
      try {
        size = Integer.parseInt(contentLength);
      } catch (NumberFormatException ex) {
      } // WTF swallowing ex
    }
    String postLine = "";
    char buf[] = new char[512];
    int read = in.read(buf);
    while (read >= 0 && size > 0 && !postLine.endsWith("\r\n")) {
      size -= read;
      postLine += String.valueOf(buf, 0, read);
      if (size > 0)
        read = in.read(buf);
    }
    postLine = postLine.trim();
    decodeParms(postLine, parms);
  }

  /**
   * Decodes the percent encoding scheme. <br/>
   * For example: "an+example%20string" -> "an example string"
   */
  private String decodePercent(String str) throws InterruptedException {
    try {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < str.length(); i++) {
        char c = str.charAt(i);
        switch (c) {
        case '+':
          sb.append(' ');
          break;
        case '%':
          sb.append((char) Integer.parseInt(str.substring(i + 1,
              i + 3), 16));
          i += 2;
          break;
        default:
          sb.append(c);
          break;
        }
      }
      return new String(sb.toString().getBytes());
    } catch (Exception e) {
      sendError(nano.HTTP_BADREQUEST, "BAD REQUEST: Bad percent-encoding.");
      return null;
    }
  }

  /**
   * Decodes parameters in percent-encoded URI-format ( e.g.
   * "name=Jack%20Daniels&pass=Single%20Malt" ) and adds them to given
   * Properties.
   */
  private void decodeParms(String parms, Properties p)
      throws InterruptedException {
    if (parms == null)
      return;

    StringTokenizer st = new StringTokenizer(parms, "&");
    while (st.hasMoreTokens()) {
      String e = st.nextToken();
      int sep = e.indexOf('=');
      if (sep >= 0)
        p.put(decodePercent(e.substring(0, sep)).trim(),
            decodePercent(e.substring(sep + 1)));
    }
  }

  /**
   * Returns an error message as a HTTP response and throws
   * InterruptedException to stop furhter request processing.
   */
  private void sendError(String status, String msg)
      throws InterruptedException {
    sendResponse(status, nano.MIME_PLAINTEXT, null,
        new ByteArrayInputStream(msg.getBytes()));
    throw new InterruptedException();
  }

  /**
   * Sends given response to the socket.
   */
  private void sendResponse(String status, String mime,
      Properties header, InputStream data) {
    try {
      if (status == null)
        throw new Error("sendResponse(): Status can't be null.");

      OutputStream out = mySocket.getOutputStream();
      PrintWriter pw = new PrintWriter(out);
      pw.print("HTTP/1.0 " + status + " \r\n");

      if (mime != null)
        pw.print("Content-Type: " + mime + "\r\n");

      if (header == null || header.getProperty("Date") == null)
        pw.print("Date: " + nano.gmtFormat.format(new Date()) + "\r\n");

      if (header != null) {
        Enumeration e = header.keys();
        while (e.hasMoreElements()) {
          String key = (String) e.nextElement();
          String value = header.getProperty(key);
          pw.print(key + ": " + value + "\r\n");
        }
      }

      pw.print("\r\n");
      pw.flush();

      if (data != null) {
        byte[] buff = new byte[2048];
        while (true) {
          int read = data.read(buff, 0, 2048);
          if (read <= 0)
            break;
          out.write(buff, 0, read);
        }
      }
      out.flush();
      out.close();
      if (data != null)
        data.close();
    } catch (IOException ioe) {
      // Couldn't write? No can do.
      try {
        mySocket.close();
      } catch (Throwable t) {
      }
    }
  }

  public void stop() throws Exception {
    mySocket.close();
    t.interrupt();
  }

}


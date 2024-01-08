package fi.iki.elonen.example;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class ThroughTheBrowserBaseFixture {
	protected static final String HTTP_LOCALHOST = "http://localhost:";
	protected static Thread httpServerThread;
	private static boolean notAlreadyRunning = true;
	protected static String port;
	protected WebClient webClient;
	protected HtmlPage page;

	protected void launchFileServerExample(final String port) {
		if (notAlreadyRunning) {
			Runnable runnable = new Runnable() {
				public void run() {
					FileServerExample.start(new String[] { port });
				}
			};
			launchServerThread(runnable);
		}
	}
	
	protected void killFileServerExample() throws Exception {
		if (!notAlreadyRunning) {
			notAlreadyRunning = true;
			FileServerExample.stop();
		}
	}
	
	protected void killHelloServerExample() throws Exception {
		if (!notAlreadyRunning) {
			notAlreadyRunning = true;
			HelloServerExample.stop();
		}
	}

	protected void launchHelloServerExample(final String port) throws Exception {
	  killHelloServerExample();
		if (notAlreadyRunning) {
			Runnable runnable = new Runnable() {
				public void run() {
					HelloServerExample.start(new String[] { port });
				}
			};
			launchServerThread(runnable);
		}
	}

	protected Thread launchServerThread(Runnable runnable) {
		httpServerThread = new Thread(runnable);
		httpServerThread.start();
		notAlreadyRunning = false;
		return httpServerThread;
	}
	
}

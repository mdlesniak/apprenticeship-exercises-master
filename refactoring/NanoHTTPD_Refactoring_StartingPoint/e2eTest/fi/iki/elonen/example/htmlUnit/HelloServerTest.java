package fi.iki.elonen.example.htmlUnit;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import fi.iki.elonen.example.ThroughTheBrowserBaseFixture;

public class HelloServerTest extends ThroughTheBrowserBaseFixture {
	@Before
	public void setup() throws Exception {
		port = "8092";
		launchHelloServerExample(port);
		webClient = new WebClient();
	}
	
	@Test
	public void canGetBack_Hello_EnteredName() throws Exception 	{
		page = webClient.getPage(HTTP_LOCALHOST + port +  "/");
		HtmlElement nameField = page.getElementByName("username");
		nameField.type("bob");
		HtmlElement submitButton = page.getElementById("submit");
		
		HtmlPage helloBobPage = submitButton.click();
		assertTrue(helloBobPage.asXml().contains("Hello, bob!"));
	}
}

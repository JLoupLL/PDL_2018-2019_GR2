package tests;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import connexionAPI.ExtractionToHTML;

public class ClasseDeTests {

	@SuppressWarnings("static-access")
	@Test
	public void testExtractionToHTML() throws IOException {
	//	fail("Not yet implemented");

		ExtractionToHTML eh = new ExtractionToHTML();
		eh.getContentHtml("https://fr.wikipedia.org/wiki/Statistiques_et_records_du_Paris_Saint-Germain");
			
	}

	@Test
	public void testExtractionToWiki() {
		fail("Not yet implemented");
	}
	@Test
	public void testgetContentHtml() {
		ExtractionToHTML ex = new ExtractionToHTML();
		String url = "https://fr.wikipedia.org/wiki/Statistiques_et_records_du_Paris_Saint-Germain";
		ex.getContentHtml(url);
		
	}
	
	
}

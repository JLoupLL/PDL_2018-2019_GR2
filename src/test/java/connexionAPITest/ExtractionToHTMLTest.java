package connexionAPITest;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.jsoup.nodes.Document;
import org.junit.Test;

import connexionAPI.ExtractionToHTML;

public class ExtractionToHTMLTest {
	ExtractionToHTML eth;

	@Test
	public void testGetContentHtml() {
		eth = new ExtractionToHTML("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		eth.getContentHtml();
		File file = new File("fichierCSV\\Loi des Douze Tables.csv");
		assertTrue("fichier existe", file.exists());
	}

	@Test
	public void testGetHtmlJsoup() {
		eth = new ExtractionToHTML("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		Document doc = this.eth.getHtmlJsoup("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		assertTrue("getHtmljsoup doit retourner une instance de document", doc instanceof Document);
	}

	@Test
	public void testGetHtmlJsoupParamShouldNotBeNull() {
		eth = new ExtractionToHTML("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		try {
			this.eth.getHtmlJsoup(null);
			fail("L'url n'est pas valide");
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}

	@Test
	public void testInsertionDonnesTableauDansFichierCSV2() {
		
		eth = new ExtractionToHTML("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		eth.getContentHtml();
		File file = new File("fichierCSV\\Loi des Douze Tables.csv");
		try {
			Scanner in = new Scanner(file);
			in.useDelimiter("\n");
			String str = in.next();
			in.close();
			assertEquals("Corpus de lois;", str);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

}

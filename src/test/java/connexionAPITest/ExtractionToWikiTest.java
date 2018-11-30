package connexionAPITest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import connexionAPI.ExtractionToWiki;

public class ExtractionToWikiTest {
 ExtractionToWiki etw;
 String url1;
 	@Before
 	public void intiTest() {
 		url1="https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";
 		etw = new ExtractionToWiki (url1);
 	}
	@Test
	public void testGetContenuePage() throws IOException {
		String s= ExtractionToWiki.getContenuePage(url1);
		 assertFalse("contenu de la page non vide",s.isEmpty());
	}

	@Test
	public void testRecupLineTable() {
	
	}

}

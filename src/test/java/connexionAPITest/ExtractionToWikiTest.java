package connexionAPITest;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import connexionAPI.ExtractionToWiki;

public class ExtractionToWikiTest {
 ExtractionToWiki etw;
	@Test
	public void testGetTabeWiki() {
	 etw = new ExtractionToWiki ("https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json");
	}

	@Test
	public void testGetContenuePage() throws IOException {
		 etw = new ExtractionToWiki ("https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json");
		String s= ExtractionToWiki.getContenuePage("https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json");
		 assertFalse("contenu de la page non vide",s.isEmpty());
	}
	

	@Test
	public void testRecupLineTable() {
	
	}

}

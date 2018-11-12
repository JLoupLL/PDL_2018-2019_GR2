package connexionAPI;

import org.wikipedia.Mediawiki;

public class ExtractionToWiki {

	public static void main(String[] args) throws Exception {
		Mediawiki wiki = new Mediawiki();
		wiki.getVersion();
		wiki.getPageHtml("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
		System.out.println(wiki);
		
		
		
	}
	
}

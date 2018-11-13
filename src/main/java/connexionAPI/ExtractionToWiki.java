package connexionAPI;

import org.wikipedia.Mediawiki;

import com.bitplan.mediawiki.japi.MediawikiApi;

public class ExtractionToWiki {

	public static void main(String[] args) throws Exception {
		Mediawiki wiki = new Mediawiki();
		wiki.getVersion();
		String url = "https://fr.wikipedia.org/wiki/Championnat_de_France_de_football";
		wiki.getPageHtml("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
		System.out.println(wiki);	
	}
	public static void getContentWikiTexte(String url) {
		
	}
}

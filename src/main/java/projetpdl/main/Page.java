package projetpdl.main;

import com.bitplan.mediawiki.japi.Mediawiki;

public class Page {

	String url;

	public Page(String url) throws Exception {
		super();
		this.url = url;
        Mediawiki wiki1= new Mediawiki(url);
        
        return;
		//ajouter 
	}
	public String getHtmlMediaWiki(String url) {
		String retour="";
		Mediawiki wiki1 = null;
		try {
			wiki1= new Mediawiki("test");
		} 
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retour=wiki1.getStringFromUrl(url);
		retour=wiki1.getStringFromUrl("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");

		return retour;	    	 
	}
	
	public String getTitre(){
		
		return null;		
	}
}

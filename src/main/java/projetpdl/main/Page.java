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
	
	public String getTitre(){
		
		return null;		
	}
}

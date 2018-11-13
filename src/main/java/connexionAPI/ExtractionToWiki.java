package connexionAPI;

import org.wikipedia.Mediawiki;

import com.bitplan.mediawiki.japi.MediawikiApi;

public class ExtractionToWiki {
//COMMENT récupérer du wikicode????  Mediawiki n'a pas de fonction explicite qui récupère le wikicode
	public static void main(String[] args) {
		try {
			getContentWikiTexte("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void getContentWikiTexte(String url) throws Exception {
		Mediawiki wiki = new Mediawiki();
		wiki.getVersion();
		wiki.getPageHtml(url);
		System.out.println(wiki);
		
	}
}
/*
//URL wiki = new URL("https://fr.wikipedia.org/wiki/Comma-separated_values");
//URLConnection yc = wiki.openConnection();
//BufferedReader in = new BufferedReader(new InputStreamReader(
//                            yc.getInputStream()));
String inputLine = null;
ArrayList<String> list = new ArrayList<String>();
Iterator<String> it =  list.iterator();
FileWriter fichier_contenue = new FileWriter("contenue.csv");
int i = 0;
String chaine1 ="https://en.wikipedia.org/wiki/List_of_decades";
String chaine2 ="table>";
String chaine3 ="<table";
try {
//https://fr.wikipedia.org/wiki/Classification_de_K%C3%B6ppen
//http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=Main%20Page&format=csv"

//Mediawiki wiki1= new Mediawiki("https://en.wikipedia.org/wiki/List_of_decades");
////String content = wiki1.getPageContent("Main Page");
////System.out.println(content);
//fichier_contenue.append(inputLine);
//
//String c =  wiki1.getStringFromUrl(chaine1);
//System.out.println("test");
//
//int cp = c.length();
//
////System.out.println(cp);
////while(cp > 0){
//if(c.contains(chaine3)){
//String test=c.substring(c.indexOf(chaine3),c.indexOf(chaine2)+6);
//System.out.println(test);
//System.out.println("test2");
////System.out.println(c.substring(0,cp));
//}
//cp--;
//}

//System.out.println(wiki1.getPageContent("wiki/List_of_decades"));



//			    fichier_contenue.append(inputLine);
//	            list.add(inputLine);
//			    	}
//			    while(it.hasNext()){
//		        	fichier_contenue.append((CharSequence) it);
//			    }
//			    while(it.hasNext()){
//			    	//System.out.println(fichier_contenue);
//		        	//fichier_contenue.append((CharSequence) it);
//			    }

//	        in.close();	


} catch (Exception e) {
// TODO: handle exception
}  
<<<<<<< HEAD


//try {
//
//} catch (Exception e) {
//Logger.getLogger(FichierCSV.class.getName()).log(Level.SEVERE, null, e);
//}
getContentHtml();
//
=======
getContentHtml();
*/
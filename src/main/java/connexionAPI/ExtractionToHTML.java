package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {

	public static void main(String[] args) {
		try {
			getContentHtml("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//anciens "commentaires" en bas de cette classe
		
	}
	/**
	 * cette fonction permet de récupérer les infos des tableaux d'une page internet
	 * il faudrait découper le code de cette fonction en plusieurs fonctions ==> plus lisible
	 * @throws IOException
	 */
	public static void getContentHtml(String url) throws IOException{//trouver un meilleur nom pour cette fonction
		Document doc=getHtmlJsoup(url);
		
		System.out.println("Début de l'éxtraction :");
		
		//Création du fichier csv avec comme titre le premier h1 de la page wikipédia
		Elements titre=doc.select("h1");
		FileWriter fileWriter = new FileWriter("fichierCSV\\"+titre.first().text()+".csv");
		//******************************************************************************
		
		String ligneDunTableau="";
		String nouvelleLigne="\n";//permettra de passer une ligne
		
		//parcours des tableaux de la page
		//attention peut-être un problème si on est sur une page wikipédia contenant le mot "table"
		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) { 
				ligneDunTableau="";
				for(Element td : row.select("td")) {
					ligneDunTableau=ligneDunTableau+td.text()+";";
				}
				fileWriter.append(ligneDunTableau);
				fileWriter.append(nouvelleLigne);
			}
		}
		fileWriter.close();
		System.out.println("Fin de l'éxtraction");
	}
	public static Document getHtmlJsoup(String url) {
		Document doc=null;
		try {
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}
	public String getHtmlWikiData(String url) {
		//A FAIRE
		return ""; 
	}
}
/*
//	        URL wiki = new URL("https://fr.wikipedia.org/wiki/Comma-separated_values");
//	        URLConnection yc = wiki.openConnection();
//	        BufferedReader in = new BufferedReader(new InputStreamReader(
//	                                    yc.getInputStream()));
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

//	Mediawiki wiki1= new Mediawiki("https://en.wikipedia.org/wiki/List_of_decades");
//	//String content = wiki1.getPageContent("Main Page");
//	//System.out.println(content);
//	fichier_contenue.append(inputLine);
//
//	String c =  wiki1.getStringFromUrl(chaine1);
//	System.out.println("test");
//
//	int cp = c.length();
//
//	//System.out.println(cp);
//	//while(cp > 0){
//	if(c.contains(chaine3)){
//		String test=c.substring(c.indexOf(chaine3),c.indexOf(chaine2)+6);
//		System.out.println(test);
//		System.out.println("test2");
//		//System.out.println(c.substring(0,cp));
//	}
//	cp--;
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
//	Logger.getLogger(FichierCSV.class.getName()).log(Level.SEVERE, null, e);
//}
getContentHtml();
//
=======
getContentHtml();
 */
package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {

	public static void main(String[] args) throws Exception {
		getContentHtml();/*
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

//			Mediawiki wiki1= new Mediawiki("https://en.wikipedia.org/wiki/List_of_decades");
//			//String content = wiki1.getPageContent("Main Page");
//			//System.out.println(content);
//			fichier_contenue.append(inputLine);
//
//			String c =  wiki1.getStringFromUrl(chaine1);
//			System.out.println("test");
//
//			int cp = c.length();
//
//			//System.out.println(cp);
//			//while(cp > 0){
//			if(c.contains(chaine3)){
//				String test=c.substring(c.indexOf(chaine3),c.indexOf(chaine2)+6);
//				System.out.println(test);
//				System.out.println("test2");
//				//System.out.println(c.substring(0,cp));
//			}
//			cp--;
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


//		try {
//			
//		} catch (Exception e) {
//			Logger.getLogger(FichierCSV.class.getName()).log(Level.SEVERE, null, e);
//		}
		getContentHtml();
//		
=======
		getContentHtml();
		 */
	}
	/**
	 * cette fonction permet de récupérer les infos des tableaux d'une page internet
	 * il faudrait découper le code de cette fonction en plusieurs fonctions ==> plus lisible
	 * @throws IOException
	 */
	public static void getContentHtml() throws IOException{
		Document doc=new Document("test"); 
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras").get();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("testtstte");
		Elements titre=doc.select("h1");
		FileWriter fileWriter = new FileWriter("fichierCSV\\"+titre.first().text()+".csv"); //création d'un fichier csv avec le nom de la page
		String chaineTest="";
		String nouvelleLigne="\n";
		//ArrayList<String> arrayliste=new ArrayList<String>();
		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) { 
				chaineTest=row.ownText();
				Elements tds = row.select("td");
				if (tds.size() > 1) {

					for(int i=0;i<tds.size();i++) {
						if(i==tds.size()-1) {
							chaineTest=chaineTest+tds.get(i).text();
						}
						else chaineTest=chaineTest+tds.get(i).text()+";";  
					}
				}
				fileWriter.append(chaineTest);
				fileWriter.append(nouvelleLigne);
				/*
				arrayliste.add(chaineTest);
				Iterator<String> it=arrayliste.iterator();
				while(it.hasNext()) {
					fileWriter.append(it.next());
					fileWriter.append(nouvelleLigne);
				}
				*/
			}
		}
		fileWriter.close();
	}
	public String getHtmlJsoup(String url) {
		String retour= "";
		Document doc=new Document("test");
		try {
			doc = Jsoup.connect(url).get();
			doc = Jsoup.connect("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football").get();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		retour=doc.toString();

		return retour;
	}
	public String getHtmlWikiData(String url) {
		//A FAIRE
		return ""; 
	}
}

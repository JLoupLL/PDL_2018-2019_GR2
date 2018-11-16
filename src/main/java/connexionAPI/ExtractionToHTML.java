package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {

	public static void main(String[] args) {
			getContentHtml("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
			getContentHtml("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
			getContentHtml("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
	}
	
	/**
	 * Cette fonction crée un fichier un CSV contenant les données des tableaux d'une page wikipédia
	 * @param url l'adresse de la page que nous voulons exploiter
	 * @throws IOException
	 */
	public static void getContentHtml(String url) {//trouver un meilleur nom pour cette fonction
		System.out.println("Début de l'extraction :");
		
		//récupération des données d'une page wikipédia dans un Document
		Document doc=getHtmlJsoup(url);	

		//Création du fichier csv avec comme titre le premier h1 de la page wikipédia
		FileWriter fileWriter = creationFichierCsv(doc);
		
		//parcours du code html et insertion dans le fichier csv des données contenues dans des tableaux
		insertionDonnesTableauDansFichierCSV(doc,fileWriter);
		
	}
	public static Document getHtmlJsoup(String url) {
		Document doc=null;
		try {
			doc = Jsoup.connect(url).get();
		}
		catch (IOException e) {
			System.out.println("erreur lors de la récupération du code html");
			e.printStackTrace();
		}
		return doc;
	}
	public static FileWriter creationFichierCsv(Document doc){ //peut être déplacer dans un autre package (createFileCSV)
		//Création du fichier csv avec comme titre le premier h1 de la page wikipédia
		Elements titre=doc.select("h1");
		FileWriter fileWriter=null;
		try {
			fileWriter = new FileWriter("fichierCSV\\"+titre.first().text()+".csv");
		} catch (IOException e) {
			System.out.println("erreur lors de la création du fichier .CSV");
			e.printStackTrace();
		}
		return fileWriter;
	}
	public static void insertionDonnesTableauDansFichierCSV(Document doc, FileWriter fileWriter) {
		String ligneDunTableau="";
		String nouvelleLigne="\n";//permettra de passer une ligne

		//parcours des tableaux de la page
		//attention peut-être un problème?? si on est sur une page wikipédia contenant le mot "table"
		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) { 
				ligneDunTableau="";
				for(Element td : row.select("td")) {
					ligneDunTableau=ligneDunTableau+td.text()+";";
				}
				try {
					fileWriter.append(ligneDunTableau);
					fileWriter.append(nouvelleLigne);
				} catch (IOException e) {
					System.out.println("erreur lors de l'ajout d'une ligne dans le fichier .CSV");
					e.printStackTrace();
				}				
			}
		}
		try {
			fileWriter.close();
		} catch (IOException e) {
			System.out.println("erreur lors de la fermeture du fichier .CSV");
			e.printStackTrace();
		}
	}
}

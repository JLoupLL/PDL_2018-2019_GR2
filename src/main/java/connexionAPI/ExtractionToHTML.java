package connexionAPI;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractionToHTML {
	private String url;
	
	public ExtractionToHTML(String url) {
		this.url = url;
	}

	public static void main(String[] args) {
		ExtractionToHTML html = new ExtractionToHTML("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
		html.getContentHtml();
		html.setUrl("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football");
		html.getContentHtml();
		html.setUrl("https://fr.wikipedia.org/wiki/Loi_des_Douze_Tables");
		html.getContentHtml();
	}

	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * Cette fonction cr�e un fichier un CSV contenant les donn�es des tableaux
	 * d'une page wikip�dia
	 * 
	 * @param url
	 *            l'adresse de la page que nous voulons exploiter
	 * @throws IOException
	 */
	public void getContentHtml() {// trouver un meilleur nom pour cette fonction
		System.out.println("D�but de l'extraction :");

		// r�cup�ration des donn�es d'une page wikip�dia dans un Document
		Document doc = getHtmlJsoup(this.url);

		// Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
		FileWriter fileWriter = creationFichierCsv(doc);

		// parcours du code html et insertion dans le fichier csv des donn�es contenues
		// dans des tableaux
		insertionDonnesTableauDansFichierCSV(doc, fileWriter);
	}
	
	public void urltrue(String url) throws IOException {
		if ( !url.contains("https://en.wikipedia.org") || !url.contains("https://fr.wikipedia.org") ){
			throw new IOException();
		}
	}

	public Document getHtmlJsoup(String url) {
		Document doc = null;
		try {
			urltrue(url);
			doc = Jsoup.connect(url).get();
		} catch (IOException e) {
			System.out.println("erreur lors de la r�cup�ration du code html");
			e.printStackTrace();
		}
		return doc;
	}

	private FileWriter creationFichierCsv(Document doc) { // peut �tre d�placer dans un autre package
																// (createFileCSV)
		// Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
		Elements titre = doc.select("h1");
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("fichierCSV\\" + titre.first().text() + ".csv");
		} catch (IOException e) {
			System.out.println("erreur lors de la cr�ation du fichier .CSV");
			e.printStackTrace();
		}
		return fileWriter;
	}

	private void insertionDonnesTableauDansFichierCSV(Document doc, FileWriter fileWriter) {
		
		// parcours des tableaux de la page
		// attention peut-�tre un probl�me?? si on est sur une page wikip�dia contenant
		// le mot "table"
		for (Element table : doc.select("table")) {
			for (Element row : table.select("tr")) {
				String ligneDunTableau = "";
				for (Element td : row.select("td")) {
					ligneDunTableau += td.text().trim() + ";";
				}
				try {
					fileWriter.append(ligneDunTableau);
					fileWriter.append("\n");
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

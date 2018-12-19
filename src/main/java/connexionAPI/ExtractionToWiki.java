package connexionAPI;

import java.net.URL;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.json.*;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import com.google.inject.spi.Element;
import  info.bliki.wiki.model.WikiModel ;
import  info.bliki.wiki.model.Configuration ;
import  info.bliki.wiki.tags. * ;
import  java.io. * ;


public class ExtractionToWiki extends Object{

	private String url;
	public ExtractionToWiki() {
	}

	// Fonction pour essayeer de Parcer le wikiText avec Parse

	//          public static void getTabeWiki(String chaine) {// que fait cette fonction?

	//                       String text = "{" + chaine + "}";

	//                       //\\{(.*?)\\}", "

	//                       Pattern p = Pattern.compile("\\{(.*?)\\}\",");

	//                       java.util.regex.Matcher m = p.matcher(text);

	//                       String result = "";

	//

	//                       while (m.find()) {

	//

	//                                    result = m.replaceAll("");

	//                                    System.out.println("\ntest:");

	//                                    System.out.println(result);

	//                                    // ici on récupère tout le wikicode de la page je crois

	//                                    System.out.println("\ntest2:");

	//                       }

	//          }



	// Recupération contenue wikitext en format Json

	public static String getContenuePage(String url) throws IOException {

		URL wikipedia = new URL(url);

		StringBuilder result = new StringBuilder();

		URLConnection yc = wikipedia.openConnection();

		BufferedReader in = new BufferedReader(new InputStreamReader(yc.getInputStream()));

		String inputLine;



		while ((inputLine = in.readLine()) != null) {

			result.append(inputLine);

		}

		in.close();

		return result.toString();

	}

	//cette fonction format le contenu en format tableau wikitext

	public static String getTableFormatwikitext(String json) {

		String content = "";

		if(json != null) {

			try {

				JSONObject objetJson = new JSONObject(json);

				String docs = objetJson.getString("parse");

				JSONObject objetJson2 = new JSONObject(docs);

				String wikitext = objetJson2.getString("wikitext");

				JSONObject objetJson3 = new JSONObject(wikitext);

				content = objetJson3.getString("*");

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		return content;

	}

	//formater le code et enlever



	//plus besoin de cette fonction, elle est donnee par les profs (cf norme de nommage)

	public static FileWriter creationFichierCsv(Document doc, String titre) { 
		// peut �tre d�placer dans un autre package

		// (createFileCSV)

		// Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia

		//Elements titre = doc.select("h1");

		FileWriter fileWriter = null;

		try {

			fileWriter = new FileWriter("output\\wikitext\\" + titre + ".csv");

		} catch (IOException e) {

			System.out.println("erreur lors de la cr�ation du fichier .CSV");

			e.printStackTrace();

		}

		return fileWriter;

	}









	//



	/**

	 * @param args

	 * @throws Exception

	 */



	public static String getTitreUrl(String url) throws IOException {

		String titreformater ="";

		Document doc = Jsoup.connect(url).get();

		Elements titreh1 = doc.select("h1");

		String titre = titreh1.first().text();    
		for (int i = 0; i < titre.length(); i++) {

			if(titre.charAt(i)==' ') {

				titreformater += '_';

			}else {

				titreformater += titre.charAt(i);

			}

		}

		return titreformater ; 

	}

	//Fonction pour générer les documents de format HTML à partir du code wiki et le Parseur de bliki en utilisant WikiModel.toHtml

	public Document getdocumentToFormatHtmp(String url) throws IOException {

		String ulrwiki = getUrlFormatRequeteJson(url); 

		Document doc = null;

		String formatWiki ="";

		String list1 = getContenuePage(ulrwiki);

		if(getTableFormatwikitext(list1)!=null) {

			formatWiki  = getTableFormatwikitext(list1);

		}               

		//doc = Jsoup.connect(WikiModel.toHtml(formatWiki)).get();

		if(Jsoup.parse(WikiModel.toHtml(formatWiki))!=null){

			doc = Jsoup.parse(WikiModel.toHtml(formatWiki));

		}else {

			doc = null;

		}

		return doc ;

	}

	public static String getUrlFormatRequeteJson(String url) throws IOException {

		// String titre =  getTitreUrl(url);

		String newTitre ="";

		newTitre += "https://en.wikipedia.org//w/api.php?action=parse&page="+url+"&prop=wikitext&format=json";

		/*

                           if (url.contains("https://fr.wikipedia.org")) {

                                        newTitre = newTitre+"https://fr.wikipedia.org//w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json";

                           }

                           else if( url.contains("https://en.wikipedia.org")){

                                        newTitre = newTitre+"https://en.wikipedia.org//w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json";

                           }

                          else {

                                        return null;

                           }

		 */

		return  newTitre;

	}



	public void insertionDonnesTableauWikiDansFichierCSV(Document doc) {





		ExtractionToHTML et1 = new ExtractionToHTML();

		String titreformater ="";

		try {

			doc = Jsoup.connect(url).get();

		} catch (IOException e) {

			// TODO Auto-generated catch block

			e.printStackTrace();

		}

		Elements titreh1 = doc.select("h1");



		String titre = titreh1.first().text();    



		for (int i = 0; i < titre.length(); i++) {

			if(titre.charAt(i)==' ') {

				titreformater += '_';

			}else {

				titreformater += titre.charAt(i);

			}

		}

		// System.out.print(doc);



		FileWriter f =  creationFichierCsv(doc,titreformater);

		et1.pourTousLesTableaux(doc, titreformater);



	}



	public static void main(String[] args) throws Exception {



		//String Url1 = "https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";

		String Url1 = "Comparison_of_Canon_EOS_digital_cameras";

		//String Url3 = "Liste_des_pays_par_PIB_nominal";

		//String Url3 = "https://fr.wikipedia.org//w/api.php?action=parse&page=Liste_des_pays_par_PIB_nominal&prop=wikitext&format=json";



		String ulrwiki = getUrlFormatRequeteJson(Url1);         

		String list1 = getContenuePage(ulrwiki);

		String formatWiki = getTableFormatwikitext(list1);



		Document doc = null; //



		//doc = Jsoup.connect(WikiModel.toHtml(formatWiki)).get();

		doc = Jsoup.parse(WikiModel.toHtml(formatWiki));

		System.out.println(doc);
		FileWriter fichierCsv = creationFichierCsv(doc,Url1);



		ExtractionToHTML et1 = new ExtractionToHTML();

		et1.pourTousLesTableaux(doc, Url1);


	}









}
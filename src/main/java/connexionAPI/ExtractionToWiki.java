package connexionAPI;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.eclipse.persistence.internal.oxm.schema.model.Content;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.wikipedia.Mediawiki;

import com.sun.xml.txw2.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ExtractionToWiki {
	private String url;
	public ExtractionToWiki(String url) {
		this.url= url;
	}

	// Fonction pour essayeer de Parcer le wikiText avec Parse
//	public static void getTabeWiki(String chaine) {// que fait cette fonction?
//		String text = "{" + chaine + "}";
//		//\\{(.*?)\\}", "
//		Pattern p = Pattern.compile("\\{(.*?)\\}\",");
//		java.util.regex.Matcher m = p.matcher(text);
//		String result = "";
//
//		while (m.find()) {
//
//			result = m.replaceAll("");
//			System.out.println("\ntest:");
//			System.out.println(result);
//			// ici on récupère tout le wikicode de la page je crois
//			System.out.println("\ntest2:");
//		}
//	}

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
	return content;
	}

	// formater par ligne du tableau le contenu
	public static ArrayList recupLineTable(String chaine) {
		ArrayList resultat = new ArrayList<String>();
		String  [] ligne = null;
		List characters = new ArrayList();
        List contentList = new ArrayList();
        char nligne ='\n'+' ';
		char currentChar = '|';
		char currentCharEst =' ';
		for (int i = 0; i < chaine.length(); i++) {
			if (chaine.charAt(i) == currentChar) {
				System.out.println(chaine.charAt(i));
			}else 					
			{
				currentCharEst = chaine.charAt(i);
				resultat.add(currentCharEst);
				System.out.print(chaine.charAt(i));
			}
		}	
		return resultat;
	}
	
	public static String urlTitre(String url) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.connect(url).get();  
        String titre = doc.title();     
        titre = titre.replace(" ","_");
        titre = titre.replace("_-_Wikipedia","");
		return titre;  
	}

	public static void main(String[] args) throws Exception {

		String Url1 = "https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";

		String Url3 = "https://fr.wikipedia.org//w/api.php?action=parse&page=Liste_des_pays_par_PIB_nominal&prop=wikitext&format=json";
		// System.out.println(wiki.fromXML(getContenuePage(Url)));
		// System.out.println(getContenuePage(Url));
		String lis1 = getContenuePage(Url1);
		System.out.println(getTableFormatwikitext(lis1));
//		ArrayList list = recupLineTable(getTableFormatwikitext(lis1));
//		Iterator<String> it = list.iterator();
//		while(it.hasNext())
//		{
//			System.out.print(it.next());
//		}   
				
	}
}

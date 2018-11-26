package connexionAPI;

import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;

import org.eclipse.persistence.internal.oxm.schema.model.Content;
import org.json.JSONObject;
import org.jsoup.Jsoup;
<<<<<<< HEAD
import org.jsoup.nodes.Element;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.example.TextConverter;
import org.sweble.wikitext.parser.parser.LinkTargetException;
=======
>>>>>>> 9408d5b434390fde1c429e60deda9a4ebd40dcf2
import org.wikipedia.Mediawiki;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.xml.txw2.Document;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.xml.sax.InputSource;

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
		Document doc;
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
	//formater le code et enlever 
	// formater par ligne du tableau le contenu
	public static void recupLineTableAndSavetoCSV(String chaine) {
		ArrayList resultat = new ArrayList<String>();
		String  [] ligne = null;
		String tabAremove[] = {"<span>","</spam>"} ;
		List characters = new ArrayList();
        List contentList = new ArrayList();
        char nligne ='\n'+' ';
		char currentChar = '!';
		char currentCharEst =' ';
		
	}
<<<<<<< HEAD
	//
	private static org.w3c.dom.Node convertStringToXMLDocument(String xmlString)
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            org.w3c.dom.Node doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
	//
	public static String convertWikiText(String wikiText) throws EngineException, LinkTargetException  {
	    // Set-up a simple wiki configuration
	    WikiConfig config = DefaultConfigEnWp.generate();
	    // Instantiate a compiler for wiki pages
	    WtEngineImpl engine = new WtEngineImpl(config);
	    // Retrieve a page
	    String pageT = "Comparison_of_Canon_EOS_digital_cameras";
	    PageTitle pageTitle = PageTitle.make(config, pageT);
	   
	    PageId pageId = new PageId(pageTitle, -1);
	    //Compile the retrieved page
	    EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
	    TextConverter p = new TextConverter(config,100);
		return (String)p.go(cp.getPage());
	}
	
	
	/**
	 * @param args
	 * @throws Exception
	 */
=======
	
	public static String getTitreUrl(String url) throws IOException {
		org.jsoup.nodes.Document doc = Jsoup.connect(url).get();  
        String titre = doc.title();     
        titre = titre.replace(" ","_");
        titre = titre.replace("_-_Wikipedia","");
		return titre;  
	}

	
	public String getUrlFormatRequeteJson(String url) throws IOException {
		String titre =  getTitreUrl(url);
		String newTitre ="";
		if (url.contains("https://fr.wikipedia.org")) {
			newTitre = newTitre+"https://fr.wikipedia.org//w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json";
		}
		else if( url.contains("https://en.wikipedia.org")){
			newTitre = newTitre+"https://en.wikipedia.org//w/api.php?action=parse&page="+titre+"&prop=wikitext&format=json";
		}
		else {
			return null;
		}
		return  newTitre;
	}
	
>>>>>>> 9408d5b434390fde1c429e60deda9a4ebd40dcf2
	public static void main(String[] args) throws Exception {

		String Url1 = "https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";

		String Url3 = "https://fr.wikipedia.org//w/api.php?action=parse&page=Liste_des_pays_par_PIB_nominal&prop=wikitext&format=json";
		// System.out.println(wiki.fromXML(getContenuePage(Url)));
		// System.out.println(getContenuePage(Url));
		String lis1 = getContenuePage(Url1);
<<<<<<< HEAD
		//System.out.println(getTableFormatwikitext(lis1));   
	    
		
		DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		org.jsoup.nodes.Document doc = Jsoup.parse(getTableFormatwikitext(lis1));
		System.out.println(doc);

		Element fileWriter = null;
		

		
=======
		System.out.println(getTableFormatwikitext(lis1));
//		ArrayList list = recupLineTable(getTableFormatwikitext(lis1));
//		Iterator<String> it = list.iterator();
//		while(it.hasNext())
//		{
//			System.out.print(it.next());
//		}   
				
>>>>>>> 9408d5b434390fde1c429e60deda9a4ebd40dcf2
	}
}

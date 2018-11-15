package connexionAPI;

import java.io.BufferedInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;

import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sweble.wikitext.engine.EngineException;
import org.sweble.wikitext.engine.PageId;
import org.sweble.wikitext.engine.PageTitle;
import org.sweble.wikitext.engine.WtEngineImpl;
import org.sweble.wikitext.engine.config.WikiConfig;
import org.sweble.wikitext.engine.nodes.EngPage;
import org.sweble.wikitext.engine.nodes.EngProcessedPage;
import org.sweble.wikitext.engine.utils.DefaultConfigEnWp;
import org.sweble.wikitext.parser.parser.LinkTargetException;
import org.wikipedia.Mediawiki;

import com.bitplan.mediawiki.japi.MediawikiApi;
import com.google.inject.matcher.Matcher;


public class ExtractionToWiki extends Mediawiki {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7865476179224701840L;
	WikiConfig conf;
	int maxLine;

	public ExtractionToWiki() {
		super();
		this.conf = conf;
		this.maxLine = maxLine;
	}
	public EngPage convertWikiText(String title, String wikiText, int maxLineLength) throws LinkTargetException, EngineException {
		// Set-up a simple wiki configuration
		WikiConfig config = DefaultConfigEnWp.generate();
		// Instantiate a compiler for wiki pages
		WtEngineImpl engine = new WtEngineImpl(config);
		// Retrieve a page
		PageTitle pageTitle = PageTitle.make(config, title);
		PageId pageId = new PageId(pageTitle, -1);
		// Compile the retrieved page
		EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
		//ExtractionToWiki p = new ExtractionToWiki(config, maxLineLength);
		return cp.getPage();
	}
	public String getRenderedText(String title) throws IOException
	{
		// @revised 0.13 genericised to parse any wikitext
		return parse("{{:" + title + "}}");
	}
	private String parse(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void getTabeWiki(String chaine) {
		String prefix ="|";	 
		String text="|-"+chaine+"|"; 
		Pattern p = Pattern.compile("<[^>]+>"); 
		java.util.regex.Matcher m = p.matcher(text); 
		String result =""; 

		while(m.find()) { 
			result = m.replaceAll(""); 
			System.out.println(result); 
		} 
	}
	public static void main(String[] args) throws Exception {
		System.out.print("Début du programme :");
		//	        HttpURLConnection conn = (HttpURLConnection) new URL(
		//	                "https://meta.wikimedia.org/wiki/Help:Table/fr").openConnection();
		//	        conn.connect();
		//	  
		//	        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
		//	       
		//	        byte[] bytes = new byte[1024];
		//	        int tmp ;
		//	        while( (tmp = bis.read(bytes) ) != -1 ) {
		//	            String chaine = new String(bytes,0,tmp);
		//	            System.out.print(chaine);
		//	        }
		//	          
		//	        conn.disconnect();

		Document doc=new Document("test"); 
		try {
			doc = Jsoup.connect("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras").get();
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpURLConnection conn = (HttpURLConnection) new URL(
				"https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras").openConnection();
		conn.connect();

		BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());

		byte[] bytes = new byte[1024];
		int tmp ;
		while( (tmp = bis.read(bytes) ) != -1 ) {

			String chaine = new String(bytes,0,tmp);
			getTabeWiki(chaine);
			//	            System.out.print(chaine);
		}

		System.out.print(bis);

		conn.disconnect();

		String target = "https://meta.wikimedia.org/wiki/Help:Table/fr";
		Elements titre =doc.select("h1");
		String title  = titre.first().text();
		// Set-up a simple wiki configuration

		ExtractionToWiki wiki = new ExtractionToWiki();
		URL link = new URL(target);
		//			//wiki.convertWikiText(title, , 1000);
		//		    WikiConfig config = DefaultConfigEnWp.generate();
		//		    // Instantiate a compiler for wiki pages
		//		    WtEngineImpl engine = new WtEngineImpl(config);
		//		    // Retrieve a page
		//		    PageTitle pageTitle = PageTitle.make(config, title);
		//		    PageId pageId = new PageId(pageTitle, -1);
		// Compile the retrieved page
		// EngProcessedPage cp = engine.postprocess(pageId, wikiText, null);
		// ExtractionToWiki p = new ExtractionToWiki(config, maxLineLength);
		//return cp.getPage();
	}
}

/* A retirer plus tard
public class ExtractionToWiki {
//COMMENT r�cup�rer du wikicode????  Mediawiki n'a pas de fonction explicite qui r�cup�re le wikicode
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
package connexionAPI;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStreamReader;
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


public class ExtractionToWiki {

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
	
	//Fonction pour essayeer de Parcer le wikiText avec Parse
	public static void getTabeWiki(String chaine) {//que fait cette fonction?
		String prefix ="|";	 
		String text="{|"+chaine+"|}"; 
		Pattern p = Pattern.compile("<[^>]+>"); 
		java.util.regex.Matcher m = p.matcher(text); 
		String result =""; 

		while(m.find()) { 

			result = m.replaceAll("");
			System.out.println("\ntest:");
			System.out.println(result);   //ici on récupère tout le wikicode de la page je crois
			System.out.println("\ntest2:");
		} 
	}
	//Recupération contenue wikitext
	public static void getContenuePage(String url) throws IOException {
			String balisedebut ="{|";
			String baliseFin   = "|}";
			String nvligne     = "\n";
	        URL yahoo = new URL(url);
	        URLConnection yc = yahoo.openConnection();
	        BufferedReader in = new BufferedReader(
	                                new InputStreamReader(
	                                yc.getInputStream()));
	        String inputLine;
	 
	        while ((inputLine = in.readLine()) != null) 
	        {
	        	getTabeWiki(inputLine); //appel  de la fonction getTablexiki 
	        }
	        in.close();
	}
	
	public static void main(String[] args) throws Exception {
		
		String Url ="https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras";
		
		getContenuePage(Url);
		
	}
}
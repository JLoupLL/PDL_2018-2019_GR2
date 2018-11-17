package connexionAPI;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.File;
import java.io.IOException;

import org.eclipse.persistence.internal.oxm.schema.model.List;
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
             

             public ExtractionToWiki() {
                           
             }
             
             //Fonction pour essayeer de Parcer le wikiText avec Parse
             public static void getTabeWiki(String chaine) {//que fait cette fonction?
                           ArrayList<String> listTAbleau = new ArrayList<String>();
            	 			String prefix ="|";       
                           String text="{"+chaine+"}"; 
                           Pattern p = Pattern.compile("<[^>]+>"); 
                           java.util.regex.Matcher m = p.matcher(text); 
                           String result =""; 

                           while(m.find()) { 

                                        result = m.replaceAll("");                                      
                                        System.out.println("\ntest:");                                             		
                                        System.out.println(result);              
                                        //ici on récupère tout le wikicode de la page je crois
                                        System.out.println("\ntest2:");
                           }                      
             }
             //Recupération contenue wikitext
             public static String getContenuePage(String url) throws IOException {
                                        String balisedebut ="{|";
                                        String baliseFin   = "|}";
                                        String nvligne     = "\n";
                     String jsonUrl = url+"/w/api.php?action=parse&page=\"+titre+\"&prop=wikitext&format=json";
                     
                     URL wikipedia = new URL(url);
                    // "https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json"
                     StringBuilder result = new StringBuilder();
                     URLConnection yc = wikipedia.openConnection();
                     BufferedReader in = new BufferedReader(
                                             new InputStreamReader(
                                             yc.getInputStream()));
                     String inputLine;
             
                     while ((inputLine = in.readLine()) != null) 
                     {
                    	 result.append(inputLine);     	 
                    	 //getTabeWiki(result..append(inputLine+"\n"));
                    	 //getTabeWiki(inputLine); //appel  de la fonction getTablexiki 
                     }
                     in.close();
                     return result.toString();
                    // return  wikipedia.getUserInfo();
             }
          //formater par ligne du tableau le contenu
		  public static ArrayList<String> recupLineTable(String chaine)
		    {
		    	 ArrayList<String> resultat = new ArrayList<String>(); 
		    	char sepateurLine = '|';
		 		for(int i = 0;i<chaine.length();i++ )
		 		{
		 			if(chaine.charAt(i) ==  sepateurLine)
		 			{
		 				System.out.println("");
		 			}else
		 			{
		 				System.out.print(chaine.charAt(i));
		 				
		 			}		 			
		 		}
				return resultat;
		    }
					
	public static void main(String[] args) throws Exception {
		
		String Url1 ="https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";
		
		String Url3 ="https://fr.wikipedia.org//w/api.php?action=parse&page=Liste_des_pays_par_PIB_nominal&prop=wikitext&format=json";
		
		//System.out.println(wiki.fromXML(getContenuePage(Url)));
		//System.out.println(getContenuePage(Url));
		
		
		
		String lis1 = getContenuePage(Url1);
		
		recupLineTable(lis1);
//		Iterator<String> it = lis1.iterator();
//		while(it.hasNext())
//		{
//			
//		}
	}
}

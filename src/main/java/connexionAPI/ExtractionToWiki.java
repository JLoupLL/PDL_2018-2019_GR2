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
             public ExtractionToWiki(String url) {
                           this.url= url;
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
           //plus besoin de cette fonction, elle est donnee par les profs (cf norme de nommage)
         	public static FileWriter creationFichierCsv(Document doc) { // peut �tre d�placer dans un autre package
         		// (createFileCSV)
         		// Cr�ation du fichier csv avec comme titre le premier h1 de la page wikip�dia
         		Elements titre = doc.select("p");
         		FileWriter fileWriter = null;
         		try {
         			fileWriter = new FileWriter("output\\wikitext\\" + titre.first().text() + ".csv");
         		} catch (IOException e) {
         			System.out.println("erreur lors de la cr�ation du fichier .CSV");
         			e.printStackTrace();
         		}
         		return fileWriter;
         	}




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

             /**
             * @param args
             * @throws Exception
             */

             public static String getTitreUrl(String url) throws IOException {
                           org.jsoup.nodes.Document doc = Jsoup.connect(url).get();  
                           String titre = doc.title();     
                           titre = titre.replace(" ","_");
                           titre = titre.replace("_-_Wikipedia","");
                           return titre;  
             }


             public static String getUrlFormatRequeteJson(String url) throws IOException {
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


             public static void main(String[] args) throws Exception {

                           String Url1 = "https://en.wikipedia.org/w/api.php?action=parse&page=Comparison_of_Canon_EOS_digital_cameras&prop=wikitext&format=json";
                           String Url3 = "https://fr.wikipedia.org//w/api.php?action=parse&page=Liste_des_pays_par_PIB_nominal&prop=wikitext&format=json";
                                                   
                           String list1 = getContenuePage(Url1);
                           String formatWiki = getTableFormatwikitext(list1);
                         //  System.out.print(formatWiki);
                           
                           Document doc = null; //
                           
                           //doc = Jsoup.connect(WikiModel.toHtml(formatWiki)).get();
                           doc = Jsoup.parse(WikiModel.toHtml(formatWiki));
                           ExtractionToHTML et1 = new ExtractionToHTML();
                           System.out.print(doc);
                           FileWriter f =  creationFichierCsv(doc);
                           et1.insertionDonnesTableauDansFichierCSV(doc, f);
                           /*
                           //System.out.println(tasse.getString()); //Rouge
                           Configuration conf = Configuration.DEFAULT_CONFIGURATION;
                           // Allow custom user <IMG> tags
                           conf.addTokenTag("table", new HTMLTag("table"));

                           // Ignore custom <INPUTBOX> tags
                           conf.addTokenTag("inputbox", new IgnoreTag("inputbox"));

                           WikiModel wiki = new WikiModel(conf,formatWiki , "${title}");

                           try {
                               BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
                               StringBuilder sb = new StringBuilder();
                               String newLine = System.getProperty("line.separator");
                               String line;
                               while ((line = reader.readLine()) != null) {
                                   sb.append(line);
                                   sb.append(newLine);
                               }
                               String htmlText = wiki.render(sb.toString());
                               System.out.print(htmlText);

                           } catch (IOException e) {
                               e.printStackTrace();
                           }*/
                       }
                           
                      
                         

    }

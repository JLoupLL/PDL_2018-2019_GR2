package projetpdl.main;


import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import org.wikipedia.Wiki;
import com.bitplan.mediawiki.japi.Mediawiki;


public class Extraction {
	//test commit JL 2
	// test 2 commit momo
	//test 1 commit linda 

	    public static void main(String[] args) throws Exception {
	    	
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
		    String chaine2 ="/table>";
		    try {
		    	//https://fr.wikipedia.org/wiki/Classification_de_K%C3%B6ppen
		    	//http://en.wikipedia.org/w/api.php?action=query&prop=revisions&rvprop=content&titles=Main%20Page&format=csv"

		    	Mediawiki wiki1= new Mediawiki("https://en.wikipedia.org/wiki/List_of_decades");
		        //String content = wiki1.getPageContent("Main Page");
		        //System.out.println(content);
		        fichier_contenue.append(inputLine);
		        
		        String c =  wiki1.getStringFromUrl(chaine1);
		       
		        System.out.println(c);
		        int cp = c.length();
		        
		        //System.out.println(cp);
		        while(cp > 0){
		        	if(c.contains(chaine2)){
		    	        System.out.println(c);
		        	}
		        	cp--;
		        }

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
		    
	        
		}
	    
	     public void getContentHtml(){
	    		// TODO Auto-generated method stub    https://fr.wikipedia.org/wiki/Championnat_de_France_de_football 
	    		Document doc=new Document("test"); 
	    		try {
	    			//doc = Jsoup.connect("http://espn.go.com/mens-college-basketball/conferences/standings/_/id/2/year/2012/acc-conference").get();
	    			doc = Jsoup.connect("https://fr.wikipedia.org/wiki/Championnat_de_France_de_football").get();
	    		} catch (IOException e) {
	    			// TODO Auto-generated catch block
	    			e.printStackTrace();
	    		}
	    		System.out.println("testtstte");
	        	for (Element table : doc.select("table")) {
	        		for (Element row : table.select("tr")) { 
	        			Elements tds = row.select("td");
	        			if (tds.size() > 6) {
	        				System.out.println(tds.get(0).text() + ":" + tds.get(1).text());     
	        			}
	        		}
	        	}
	    	

	    }
}

package mainProjet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.json.JSONArray;

import connexionAPI.ExtractionToHTML;
import connexionAPI.ExtractionToWiki;

public class WikiMatrix {


	public static void main(String[] args) {
		new WikiMatrix();
		System.out.println(utils.Messages.INTRO);
		System.out.println(utils.Messages.ENTRERCHOIX);
		String choix="";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {
			choix = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while (!choix.equals("1")&&!choix.equals("2")&&!choix.equals("q")) {
			System.out.println("Erreur veuillez choisir parmi les choix proposés");
			System.out.println(utils.Messages.ENTRERCHOIX);
			try {
				choix = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		System.out.println(utils.Messages.DEMANDERURL);
		String url="";
		try {
			url = br.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(choix.equals("1")) {
			ExtractionToHTML eth=new ExtractionToHTML();
			eth.getContentHtml(url);
		}
		else if(choix.equals("2")) {
			ExtractionToWiki eth=new ExtractionToWiki();
			//eth.
		}
		System.out.println(utils.Messages.MESSAGEDEFIN);
	}

}
//faire une verification de l'url, elle doit commencer par 'https://en.wikipedia.org/wiki/'
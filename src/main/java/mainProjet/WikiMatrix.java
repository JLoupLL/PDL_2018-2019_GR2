package mainProjet;

import java.io.IOException;

import org.json.JSONArray;

import connexionAPI.ExtractionToHTML;

public class WikiMatrix {


	public static void main(String[] args) {
		new WikiMatrix();
		JSONArray jsonArraySearch = null;

		System.out.println(utils.Messages.PATIENT);// Patientez..


		ExtractionToHTML eth=new ExtractionToHTML();
		try {
			eth.getContentHtml("https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}

package tests;

import junit.framework.*;
import junit.textui.TestRunner;

import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;

import connexionAPI.ExtractionToHTML;

public class testJuinit {
	
	
	public boolean testgetContenentHtm() {
		
		String url = "https://en.wikipedia.org/wiki/Comparison_of_Canon_EOS_digital_cameras";
		ExtractionToHTML exhtml = new ExtractionToHTML();
		assertTrue(exhtml.getContenthtml(url));
		return false;	
	}

	private void assertTrue(Document contenthtml) {
		// TODO Auto-generated method stub
		
	}
	public static void main (String[] args) {
		   
		   }
		}

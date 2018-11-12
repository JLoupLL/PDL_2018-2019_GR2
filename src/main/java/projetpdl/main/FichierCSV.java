package projetpdl.main;

import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.persistence.internal.oxm.schema.model.List;

public class FichierCSV<T> {

	String nomFichier;
	String separator = ";";
	String nLine ="\n";
	String header = "";
	private FileWriter mycsv;
	
		
	public FichierCSV() {
		try {
			FileWriter mycsv = new FileWriter("FichierCSV\\tab.csv");
			mycsv.append("bonjour");
		} catch (Exception e) {
			Logger.getLogger(FichierCSV.class.getName()).log(Level.SEVERE, null, e);
		}
	}
}

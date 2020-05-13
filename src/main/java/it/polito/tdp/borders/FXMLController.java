
package it.polito.tdp.borders;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;

import it.polito.tdp.borders.model.Country;
import it.polito.tdp.borders.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;
    
    @FXML
    private ComboBox<Country> CountryCB;

    @FXML
    private Button TrovaVicini;


    @FXML // fx:id="txtAnno"
    private TextField txtAnno; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaConfini(ActionEvent event) {
    	txtResult.clear();
    	Integer anno;
    	try {
    		anno = Integer.parseInt(txtAnno.getText());
    	}catch (NumberFormatException e) {
			txtResult.setText("L'anno deve essere espresso sotto forma di numeri");
			return;
			}
    	
    	if(anno<1816 || anno > 2016) {
    		txtResult.setText("Inserire un anno compreso tra 1816 e 2016");
    		return;
    	}
    	
    	model.creaGrafo(anno);
    	//popolo la comobox
    	CountryCB.setDisable(false);
    	CountryCB.getItems().clear();
    	CountryCB.getItems().addAll(this.model.getCountries());
    	txtResult.appendText("Il grafo contiene "+ this.model.getGraph().vertexSet().size()+" vertici e "+this.model.getGraph().edgeSet().size()+" archi\n");
    	txtResult.appendText("Elenco degli stati e numeo di stati confinanti\n");
    	for(Country v : this.model.getGraph().vertexSet()) {
    		txtResult.appendText(v.getStateNme()+ " "+ model.getGraph().degreeOf(v)+ "\n");
    	}
    	
    	txtResult.appendText("Il numero di componenti connesse è: "+this.model.getConnectedComponents().size());
    	
    	
    }
    
    @FXML
    void DoTrovaVicni(ActionEvent event) {
    	txtResult.clear();
    	
    	Country stato = CountryCB.getValue();
    	
    	//1
    	Set<Country> paesiVicini = model.getConnectedComponent(stato); 	
    	txtResult.appendText("Gli stati raggiungibili a partire da "+stato+" sono "+paesiVicini.size()+": \n"+paesiVicini+"\n");
    	
    	//2
    	//a
    	List<Country> paesiVicini2a = model.getComponenteConnessaVisitaAmpiezza(stato); 	
    	txtResult.appendText("Gli stati raggiungibili a partire da "+stato+" sono "+paesiVicini2a.size()+": \n"+paesiVicini2a+"\n");
    	//b
    	List<Country> paesiVicini2b = model.getComponenteConnessaVisitaProfondita(stato); 	
    	txtResult.appendText("Gli stati raggiungibili a partire da "+stato+" sono "+paesiVicini2b.size()+": \n"+paesiVicini2b);
    	
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtAnno != null : "fx:id=\"txtAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert CountryCB != null : "fx:id=\"CountryCB\" was not injected: check your FXML file 'Scene.fxml'.";
        assert TrovaVicini != null : "fx:id=\"TrovaVicini\" was not injected: check your FXML file 'Scene.fxml'.";
        CountryCB.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}

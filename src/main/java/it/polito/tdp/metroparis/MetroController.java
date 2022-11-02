/**
 * Sample Skeleton for 'Metro.fxml' Controller Class
 */

package it.polito.tdp.metroparis;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.metroparis.model.Fermata;
import it.polito.tdp.metroparis.model.Model;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

public class MetroController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxArrivo"
    private ComboBox<Fermata> boxArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="boxPartenza"
    private ComboBox<Fermata> boxPartenza; // Value injected by FXMLLoader

    @FXML
    private TableColumn<Fermata, String> colFermata;
    
    @FXML
    private TableView<Fermata> tablePercorso;
    
    @FXML // fx:id="textResult"
    private TextArea textResult; // Value injected by FXMLLoader

    @FXML
    void handleCerca(ActionEvent event) {
    	
    	Fermata partenza = boxPartenza.getValue();
    	Fermata arrivo = boxArrivo.getValue();
    	
    	if(partenza != null && arrivo != null && !partenza.equals(arrivo)) {
    		List<Fermata> percorso = model.calcolaPercorso(partenza, arrivo);
    		
    		tablePercorso.setItems(FXCollections.observableArrayList(percorso));
    		
    		textResult.setText("Percorso trovato con "+ percorso.size() + " stazioni\n");
    	}else {
    		textResult.setText("Devi selezionare due stazioni, diverse tra loro\n");
    	}
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxArrivo != null : "fx:id=\"boxArrivo\" was not injected: check your FXML file 'Metro.fxml'.";
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Metro.fxml'.";
        assert textResult != null : "fx:id=\"textResult\" was not injected: check your FXML file 'Metro.fxml'.";
        
        colFermata.setCellValueFactory(new PropertyValueFactory<Fermata, String>("nome"));
    }
    
    public void setModel(Model model) {
    	this.model = model;
    	List<Fermata> fermate = model.getFermate();
    	
    	boxPartenza.getItems().clear();
    	boxArrivo.getItems().clear();
    	
    	boxPartenza.getItems().addAll(fermate);
    	boxArrivo.getItems().addAll(fermate);
    	
    }

}


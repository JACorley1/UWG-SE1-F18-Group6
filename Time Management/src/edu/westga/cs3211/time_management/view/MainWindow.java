package edu.westga.cs3211.time_management.view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.westga.cs3211.time_management.Main;
import edu.westga.cs3211.time_management.model.Calendar;
import edu.westga.cs3211.time_management.model.Event;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/** Codebehind for the MainWindow Scene.
 * 
 * @author Jonathan Corley
 */
public class MainWindow {

    @FXML private ResourceBundle resources;
    @FXML private URL location;
    @FXML private ListView<Event> eventList;
    @FXML private TextArea descriptionText;
    
    private Calendar calendar;

    @FXML
    void addEvent(ActionEvent event) throws IOException {
    	FXMLLoader loader = new FXMLLoader();
    	loader.setLocation(Main.class.getResource("view/AddEvent.fxml"));
    	loader.load();
    	Parent parent= loader.getRoot();
    	Scene scene= new Scene(parent);
    	Stage addEventStage= new Stage();
    	addEventStage.setTitle("Add New Event");
    	addEventStage.setScene(scene);
    	addEventStage.initModality(Modality.APPLICATION_MODAL);
    	AddEvent addEventDialog = loader.getController();
    	addEventDialog.setCalendar(this.calendar);
    	addEventStage.showAndWait();

        this.eventList.setItems(FXCollections.observableArrayList(this.calendar.getEvents()));
    }
    
    @FXML
    void deleteEvent(ActionEvent event) {
    	Event selectedEvent = this.eventList.getSelectionModel().getSelectedItem();
    	if(selectedEvent!=null) {
	    	Alert alert = new Alert(AlertType.CONFIRMATION);
	    	alert.setTitle("Confirmation Dialog");
	    	alert.setHeaderText("Delete This Event?");
	    	alert.setContentText(selectedEvent.toStringFull());
	
	    	Optional<ButtonType> result = alert.showAndWait();
	    	if (result.get() == ButtonType.OK){
	    	    this.calendar.deleteEvent(selectedEvent);
	    	    this.eventList.setItems(FXCollections.observableArrayList(this.calendar.getEvents()));
	    	}
    	}
    }
    
    @FXML
    void selectEvent(MouseEvent event) {
    	Event eventSelected = this.eventList.getSelectionModel().getSelectedItem();
    	if(eventSelected != null) {
    		this.descriptionText.setText(eventSelected.toStringFull());
    	}
    }

    @FXML
    void initialize() {
        assert eventList != null : "fx:id=\"eventList\" was not injected: check your FXML file 'MainWindow.fxml'.";
        assert descriptionText != null : "fx:id=\"eventDetailsText\" was not injected: check your FXML file 'MainWindow.fxml'.";

        this.calendar = new Calendar();
        this.eventList.setItems(FXCollections.observableArrayList(this.calendar.getEvents()));
        
        this.setListDisplayTextOfEventItems();
        
    }
    
    private void setListDisplayTextOfEventItems() {
    	this.eventList.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
            @Override
            public ListCell<Event> call(ListView<Event> param) {
                 ListCell<Event> cell = new ListCell<Event>() {
                     @Override
                    protected void updateItem(Event item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item != null) {
                            setText(item.getName());
                        } else {
                            setText(null);
                        }
                    }
                 };
                return cell;
            }
        });
    }
    
}


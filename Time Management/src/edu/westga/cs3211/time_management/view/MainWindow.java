package edu.westga.cs3211.time_management.view;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import edu.westga.cs3211.time_management.Main;
import edu.westga.cs3211.time_management.model.Calendar;
import edu.westga.cs3211.time_management.model.Event;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * Codebehind for the MainWindow Scene.
 * 
 * @author Jonathan Corley
 */
public class MainWindow {

	@FXML
	private ResourceBundle resources;
	
	@FXML
	private URL location;
	
	@FXML
	private ListView<Event> eventList;
	
	@FXML
	private TextArea descriptionText;
	
	@FXML
	private Label eventNameLabel;

	@FXML
	private Label startTimeLabel;

	@FXML
	private Label locationLabel;

	@FXML
	private Label endTimeLabel;

	@FXML
	private ListView<?> attendeesList;

	@FXML
	private Button deleteButton;

	private Calendar calendar;
	private Event lastSelected;

	@FXML
	void addEvent(ActionEvent event) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(Main.class.getResource("view/AddEvent.fxml"));
		loader.load();
		Parent parent = loader.getRoot();
		Scene scene = new Scene(parent);
		Stage addEventStage = new Stage();
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
		if (selectedEvent != null) {
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Confirmation Dialog");
			alert.setHeaderText("Delete This Event?");
			alert.setContentText(selectedEvent.toStringFull());

			Optional<ButtonType> result = alert.showAndWait();
			if (result.get() == ButtonType.OK) {
				this.calendar.deleteEvent(selectedEvent);
				this.eventList.setItems(FXCollections.observableArrayList(this.calendar.getEvents()));
			}
			
			if(this.lastSelected == selectedEvent) {
				this.clearAllText();
			}
		}
	}

	private void clearAllText() {
		this.eventNameLabel.setText("");
		this.startTimeLabel.setText("");
		this.endTimeLabel.setText("");
		this.descriptionText.setText("");
	}
	
	@FXML
	void selectEvent(MouseEvent event) {
		Event eventSelected = this.eventList.getSelectionModel().getSelectedItem();
		this.lastSelected = eventSelected;
		if (eventSelected != null) {
			//this.descriptionText.setText(eventSelected.toStringFull());
			this.eventNameLabel.setText(eventSelected.getName());
			this.startTimeLabel.setText(""+eventSelected.getStartTime());
			this.endTimeLabel.setText(""+eventSelected.getEndTime());
			this.descriptionText.setText(eventSelected.getDescription());
			
		}
	}

	@FXML
	void initialize() {
		
		assert eventList != null : "fx:id=\"eventList\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert descriptionText != null : "fx:id=\"eventDetailsText\" was not injected: check your FXML file 'MainWindow.fxml'.";
		assert deleteButton != null : "fx:id=\"deleteButton\" was not injected: check your FXML file 'MainWindow.fxml'.";

		this.lastSelected = null;
		
		this.calendar = new Calendar();
		this.eventList.setItems(FXCollections.observableArrayList(this.calendar.getEvents()));

		this.setListDisplayTextOfEventItems();
		// this.eventList.itemsProperty().isNotNull()
		System.out.println(deleteButton);
		this.deleteButton.disableProperty().bind(this.eventList.getSelectionModel().selectedItemProperty().isNull());
		
	}

	private void setListDisplayTextOfEventItems() {
		this.eventList.setCellFactory(new Callback<ListView<Event>, ListCell<Event>>() {
			@Override
			public ListCell<Event> call(ListView<Event> param) {
				ListCell<Event> cell = new ListCell<Event>() {
					@Override
					protected void updateItem(Event item, boolean empty) {
						super.updateItem(item, empty);
						if (item != null) {
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

package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityGroup;
import com.ghostcompany.mystats.Service.InvalidActivityException;
import com.ghostcompany.mystats.Service.ListViewManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;

import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class ActivityController implements Initializable {

    public TextField eventName;
    public ChoiceBox<String> eventGroupChoiceBox;
    public Label choiceBoxErr;
    public ListView<HBox> eventGroupListView;
    public ListView<HBox> eventListView;
    public Label eventNameErr;

    private Activity selectedActivity = new Activity();
    private ActivityGroup activityGroup = new ActivityGroup();
    private final ListViewManager listViewManager = new ListViewManager();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeUI();
    }

    private void initializeUI() {
        populateActivity();
        populateActivityGroup();
        eventGroupChoiceBox.setOnAction(this::setEventGroup);
    }

    public void setEventGroup(ActionEvent event) {
        selectedActivity.setGroupName(eventGroupChoiceBox.getValue());
    }

    public void populateActivityGroup() {
        try {
            eventGroupChoiceBox.getItems().clear();
            eventGroupListView.getItems().clear();

            List<String> headers = List.of("ID", "Name", "Duration");
            listViewManager.addHeader(eventGroupListView, headers);

            ObservableList<ActivityGroup> groups = activityGroup.getActivityGroups();
            for (ActivityGroup group : groups) {
                eventGroupChoiceBox.getItems().add(group.getName());
                List<String> entry = List.of(String.valueOf(group.getId()), group.getName(), group.getTotalTimeSpent());
                listViewManager.addEntry(eventGroupListView, entry);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error loading activity groups.", eventGroupListView);
        }
    }

    public void populateActivity() {
        try {
            eventListView.getItems().clear();
            List<String> headers = List.of("ID", "Name", "Group Name", "Duration");
            listViewManager.addHeader(eventListView, headers);

            ObservableList<Activity> activities = selectedActivity.getAllActivities();
            for (Activity activity : activities) {
                List<String> entry = List.of(
                        String.valueOf(activity.getId()),
                        activity.getName(),
                        activity.getGroupName(),
                        activity.getTotalTimeSpent()
                );
                listViewManager.addEntry(eventListView, entry);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error loading activities.", eventListView);
        }
    }

    public void createEvent(ActionEvent actionEvent) {
        try {
            if (isInputValid()) {
                selectedActivity.addActivity(selectedActivity);

                displaySuccess(eventNameErr, "Event Added!");
                populateActivity();
            }
        } catch (InvalidActivityException e) {
            // Handle the validation error and display it in the UI
            displayError(eventNameErr, e.getMessage());
        } catch (SQLException e) {
            // Handle the SQLException in the usual way
            handleSQLException(e, "Error adding activity.", eventNameErr);
        }
    }


    private boolean isInputValid() {
        if (eventName.getText().isEmpty()) {
            displayError(eventNameErr, "Event Name is Empty!");
            return false;
        }
        if (eventGroupChoiceBox.getValue() == null) {
            displayError(choiceBoxErr, "Group Name is Empty!");
            return false;
        }
        return true;
    }

    private void displayError(Label label, String message) {
        label.setTextFill(Paint.valueOf("red"));
        label.setText(message);
    }

    private void displaySuccess(Label label, String message) {
        label.setTextFill(Paint.valueOf("green"));
        label.setText(message);
    }

    private void handleSQLException(SQLException e, String contextMessage, ListView view) {
        view.getItems().clear();
        view.getItems().add(contextMessage + ": " + e.getMessage());
        e.printStackTrace();
    }

    private void handleSQLException(SQLException e, String contextMessage, Label label) {
        label.setTextFill(Paint.valueOf("red"));
        label.setText(contextMessage + ": " + e.getMessage());
        e.printStackTrace();
    }

    public void setEventName(ActionEvent actionEvent) {
        selectedActivity.setName(eventName.getText());
    }
}

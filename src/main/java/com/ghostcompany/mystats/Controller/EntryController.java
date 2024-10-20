package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityEntry;
import com.ghostcompany.mystats.Service.*;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.ResourceBundle;

public class EntryController implements Initializable {

    @FXML private TextField amountField;
    @FXML private TextField descriptionField;
    @FXML private TextField activityDescription;

    @FXML private Text amountErrorField;
    @FXML private Text descriptionErrorField;
    @FXML private Label startTimeErr;
    @FXML private Label endTimeErr;
    @FXML private Text activityDescriptionErr;

    @FXML private Button incomeBtn;
    @FXML private Button expenseBtn;
    @FXML private Button saveActivityBtn;
    @FXML private Button saveExpenseBtn;

    @FXML private DatePicker startDate;
    @FXML private DatePicker endDate;

    @FXML private TextField startHourField;
    @FXML private TextField startMinField;
    @FXML private ChoiceBox<String> startAmPm;

    @FXML private TextField endHourField;
    @FXML private TextField endMinField;
    @FXML private ChoiceBox<String> endAmPm;

    @FXML private ChoiceBox<String> accountSelect;
    @FXML private ChoiceBox<String> activitySelect;

    private Account selectedAccount = new Account();
    private Transaction selectedTransaction;
    private Activity selectedActivity = new Activity();
    private ActivityEntry selectedActivityEntry;

    private boolean isIncome = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize accounts and activities
            initialzieAccounts();
            intializeActivities();
            initializeTimeFields();

            expenseBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            incomeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
            isIncome = false;

        } catch (SQLException e) {
            descriptionErrorField.setText("Error loading accounts or activities.");
            e.printStackTrace();
        }
    }

    private void initialzieAccounts() throws SQLException {
        accountSelect.setOnAction(this::setAccount);

        ObservableList<Account> accounts = selectedAccount.getAccounts();
        accountSelect.getItems().addAll(accounts.stream().map(Account::getAccountName).toList());

        if (!accounts.isEmpty()) {
            selectedAccount = accounts.get(0);  // Use the first account
            initializeTransaction();
        } else {
            descriptionErrorField.setText("No accounts available.");
        }
    }

    private void intializeActivities() throws SQLException {
        activitySelect.setOnAction(this::setActivity);

        ObservableList<Activity> activities = selectedActivity.getAllActivities();
        activitySelect.getItems().addAll(activities.stream().map(Activity::getName).toList());

        if (!activities.isEmpty()) {
            selectedActivity = activities.get(0);
            selectedActivityEntry = new ActivityEntry(0, "", LocalDateTime.now(), LocalDateTime.now(), selectedActivity.getId());
        } else {
            activityDescriptionErr.setText("No activities available.");
        }
    }

    private void initializeTransaction() {
        selectedTransaction = new Transaction(0, selectedAccount.getId(), 0, "", LocalDate.now(), ETransactionType.DEPOSIT);
    }

    private void initializeTimeFields() {
        // Populate AM/PM options
        startAmPm.getItems().addAll("AM", "PM");
        endAmPm.getItems().addAll("AM", "PM");

        // Set default values (current date and time)
        LocalDate currentDate = LocalDate.now();
        startDate.setValue(currentDate);
        endDate.setValue(currentDate);

        LocalTime currentTime = LocalTime.now();
        setDefaultTime(startHourField, startMinField, startAmPm, currentTime);
        setDefaultTime(endHourField, endMinField, endAmPm, currentTime);

        addTimeChangeListeners();
    }

    private void setDefaultTime(TextField hourBox, TextField minuteBox, ChoiceBox<String> amPmBox, LocalTime time) {
        int hour = time.getHour();
        String amPm = (hour >= 12) ? "PM" : "AM";

        if (hour > 12) hour -= 12;
        else if (hour == 0) hour = 12;

        hourBox.setPromptText(String.format("%02d", hour));
        minuteBox.setPromptText(String.format("%02d", time.getMinute()));
        amPmBox.setValue(amPm);
    }

    private void addTimeChangeListeners() {
        // Add listeners to start time fields
        startDate.setOnAction(this::updateStartTime);
        startAmPm.setOnAction(this::updateStartTime);

        // Add listeners to end time fields
        endDate.setOnAction(this::updateEndTime);
        endAmPm.setOnAction(this::updateEndTime);
    }

    @FXML
    private void updateStartTime(ActionEvent event) {
        LocalDate date = startDate.getValue();
        LocalTime time = getTimeFromInputs(startHourField.getText(), startMinField.getText(), startAmPm.getValue());

        if (date != null && time != null) {
            if (selectedActivityEntry != null) {
                selectedActivityEntry.setStartTime(LocalDateTime.of(date, time));
            }
            endTimeErr.setText("");  // Clear error message
        } else {
            endTimeErr.setText("Invalid start time.");
        }
    }

    @FXML
    private void updateEndTime(ActionEvent event) {
        LocalDate date = endDate.getValue();
        LocalTime time = getTimeFromInputs(endHourField.getText(), endMinField.getText(), endAmPm.getValue());

        if (date != null && time != null) {
            if (selectedActivityEntry != null) {
                selectedActivityEntry.setEndTime(LocalDateTime.of(date, time));
            }
            endTimeErr.setText("");  // Clear error message
        } else {
            endTimeErr.setText("Invalid end time.");
        }
    }

    private LocalTime getTimeFromInputs(String hourStr, String minuteStr, String amPm) {
        try {
            int hour = Integer.parseInt(hourStr);
            int minute = Integer.parseInt(minuteStr);

            // Convert to 24-hour format
            if (amPm.equals("PM") && hour != 12) {
                hour += 12;
            } else if (amPm.equals("AM") && hour == 12) {
                hour = 0;
            }
            return LocalTime.of(hour, minute);
        } catch (NumberFormatException e) {
            return null;  // Invalid time inputs
        }
    }



    public void setAmount(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());

            if (amount < 0) {
                amountErrorField.setText("Amount can't be less than 0");
                return;
            }
            selectedTransaction.setAmount(amount);
            amountErrorField.setText("");
        } catch (NumberFormatException e) {
            amountErrorField.setText("Invalid amount. Please enter a valid number.");
        }
    }

    public void setDescription(ActionEvent event) {
        String description = descriptionField.getText().trim();
        if (!description.isEmpty()) {
            selectedTransaction.setDescription(description);
            descriptionErrorField.setText("");
        } else {
            descriptionErrorField.setText("Description cannot be empty.");
        }
    }

    public void setActivityDescription(ActionEvent event) {
        String description = activityDescription.getText().trim();
        if (!description.isEmpty()) {
            selectedActivityEntry.setDescription(description);
            activityDescriptionErr.setText("");
        } else {
            activityDescriptionErr.setText("Activity description cannot be empty.");
        }
    }

    public void setTransactionToIncome(ActionEvent event) {
        incomeBtn.setStyle("-fx-background-color: DeepSkyBlue; -fx-text-fill: white;");
        expenseBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        selectedTransaction.setTransactionType(ETransactionType.DEPOSIT);
        isIncome = true;
    }

    public void setTransactionToExpense(ActionEvent event) {
        expenseBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        incomeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        selectedTransaction.setTransactionType(ETransactionType.WITHDRAWAL);
        isIncome = false;
    }

    public void saveExpenses(ActionEvent event) {
        if (selectedTransaction.getDescription().isEmpty()) {
            descriptionErrorField.setText("Description cannot be empty.");
            return;
        }

        if (selectedTransaction.getAmount() <= 0) {
            amountErrorField.setText("Amount cannot be less than 0.");
            return;
        }

        selectedAccount.addTransaction(selectedTransaction);
        descriptionErrorField.setFill(Paint.valueOf("green"));
        descriptionErrorField.setText("Transaction saved successfully!");
    }

    public void setAccount(ActionEvent event) {
        selectedAccount.setAccountName(accountSelect.getValue());
    }

    public void setActivity(ActionEvent event) {
        selectedActivity.setName(activitySelect.getValue());
    }

    public void saveActivity(ActionEvent event) {
        try {
            if (selectedActivityEntry == null || selectedActivityEntry.getDescription().isEmpty()) {
                activityDescriptionErr.setText("Description cannot be empty.");
                return;
            }

            selectedActivityEntry.addEntry(selectedActivityEntry);
            activityDescriptionErr.setText("Activity saved successfully!");
        } catch (SQLException e) {
            activityDescriptionErr.setText("Error saving activity.");
            e.printStackTrace();
        }
    }

    public void setStartDate(ActionEvent actionEvent) {
        updateStartTime(actionEvent);
    }

    public void setEndDate(ActionEvent actionEvent) {
        updateEndTime(actionEvent);
    }


    public void selectDate(ActionEvent event) {}
}

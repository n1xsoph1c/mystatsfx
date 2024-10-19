package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityEntry;
import com.ghostcompany.mystats.Service.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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

    @FXML private ChoiceBox<String> startHour;
    @FXML private ChoiceBox<String> startMinute;
    @FXML private ChoiceBox<String> startAmPm;

    @FXML private ChoiceBox<String> endHour;
    @FXML private ChoiceBox<String> endMinute;
    @FXML private ChoiceBox<String> endAmPm;

    @FXML private ChoiceBox<String> accountSelect;
    @FXML private ChoiceBox<String> activitySelect;

    private final AccountDAO accountHandler = new AccountDAO();
    private final TransactionDAO transactionHandler = new TransactionDAO();
    private final ActivityEntryDAO activityEntryHandler = new ActivityEntryDAO();
    private final ActivityDAO activityHandler = new ActivityDAO();

    private List<Account> accounts;
    private List<Activity> activities;

    private Account selectedAccount;
    private Transaction selectedTransaction;
    private Activity selectedActivity;
    private ActivityEntry selectedActivityEntry;

    private boolean isIncome = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize accounts and activities
            accounts = accountHandler.getAllAccounts();
            activities = activityHandler.getActivities();

            // Populate ChoiceBoxes for activities and accounts
            activitySelect.getItems().addAll(activities.stream().map(Activity::getName).toList());
            accountSelect.getItems().addAll(accounts.stream().map(Account::getAccountName).toList());

            // Manually populate the hour, minute, and AM/PM ChoiceBoxes
            initializeTimeFields();

            // Set default values (current date and time)
            LocalDate currentDate = LocalDate.now();
            startDate.setValue(currentDate);
            endDate.setValue(currentDate);

            LocalTime currentTime = LocalTime.now();
            setDefaultTime(startHour, startMinute, startAmPm, currentTime);
            setDefaultTime(endHour, endMinute, endAmPm, currentTime);

            // Add listeners to update start and end time on any change
            addTimeChangeListeners();

            accountSelect.setOnAction(this::setAccount);
            activitySelect.setOnAction(this::setActivity);

            if (!accounts.isEmpty()) {
                selectedAccount = accounts.get(0);  // Use the first account
                initializeTransaction();
            } else {
                descriptionErrorField.setText("No accounts available.");
            }

            if (!activities.isEmpty()) {
                selectedActivity = activities.get(0);  // Use the first activity
            } else {
                activityDescriptionErr.setText("No activities available.");
            }

            expenseBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            incomeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
            isIncome = false;

        } catch (SQLException e) {
            descriptionErrorField.setText("Error loading accounts or activities.");
            e.printStackTrace();
        }
    }

    private void initializeTransaction() {
        selectedTransaction = new Transaction(0, selectedAccount.getId(), 0, "", LocalDate.now(), ETransactionType.DEPOSIT);
    }

    private void initializeTimeFields() {
        // Populate hours (1 to 12)
        for (int i = 1; i <= 12; i++) {
            String hour = String.format("%02d", i);
            startHour.getItems().add(hour);
            endHour.getItems().add(hour);
        }

        // Populate minutes (00, 15, 30, 45)
        for (int i = 0; i < 60; i += 15) {
            String minute = String.format("%02d", i);
            startMinute.getItems().add(minute);
            endMinute.getItems().add(minute);
        }

        // Populate AM/PM options
        startAmPm.getItems().addAll("AM", "PM");
        endAmPm.getItems().addAll("AM", "PM");
    }

    private void setDefaultTime(ChoiceBox<String> hourBox, ChoiceBox<String> minuteBox, ChoiceBox<String> amPmBox, LocalTime time) {
        int hour = time.getHour();
        String amPm = (hour >= 12) ? "PM" : "AM";

        if (hour > 12) hour -= 12;
        else if (hour == 0) hour = 12;

        hourBox.setValue(String.format("%02d", hour));
        minuteBox.setValue(String.format("%02d", time.getMinute()));
        amPmBox.setValue(amPm);
    }

    private void addTimeChangeListeners() {
        // Add listeners to start time fields
        startDate.setOnAction(this::updateStartTime);
        startHour.setOnAction(this::updateStartTime);
        startMinute.setOnAction(this::updateStartTime);
        startAmPm.setOnAction(this::updateStartTime);

        // Add listeners to end time fields
        endDate.setOnAction(this::updateEndTime);
        endHour.setOnAction(this::updateEndTime);
        endMinute.setOnAction(this::updateEndTime);
        endAmPm.setOnAction(this::updateEndTime);
    }

    private void updateStartTime(ActionEvent event) {
        LocalDate date = startDate.getValue();
        LocalTime time = getTimeFromInputs(startHour.getValue(), startMinute.getValue(), startAmPm.getValue());

        if (date != null && time != null) {
            if (selectedActivityEntry != null) {
                selectedActivityEntry.setStartTime(LocalDateTime.of(date, time));
            }
            startTimeErr.setText("");  // Clear error message
        } else {
            startTimeErr.setText("Invalid start time.");
        }
    }

    private void updateEndTime(ActionEvent event) {
        LocalDate date = endDate.getValue();
        LocalTime time = getTimeFromInputs(endHour.getValue(), endMinute.getValue(), endAmPm.getValue());

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


    public void setAccount(ActionEvent event) {
        String selectedAccountName = accountSelect.getValue();
        selectedAccount = accounts.stream()
                .filter(account -> account.getAccountName().equals(selectedAccountName))
                .findFirst()
                .orElse(null);

        if (selectedAccount != null) {
            initializeTransaction();
            amountField.setPromptText("Enter Amount");
            descriptionField.setPromptText("Enter Description");
            amountErrorField.setText("");  // Clear error message
        } else {
            descriptionErrorField.setText("Account not found!");
        }
    }

    public void setAmount(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());
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
        try {
            if (selectedTransaction.getDescription().isEmpty()) {
                descriptionErrorField.setText("Description cannot be empty.");
                return;
            }

            if (selectedTransaction.getAmount() <= 0) {
                amountErrorField.setText("Amount cannot be less than 0.");
                return;
            }

            transactionHandler.addTransaction(selectedTransaction);
            descriptionErrorField.setText("Transaction saved successfully!");
        } catch (SQLException e) {
            descriptionErrorField.setText("Error saving transaction.");
            e.printStackTrace();
        }
    }

    public void setActivity(ActionEvent event) {
        String selectedActivityName = activitySelect.getValue();
        selectedActivity = activities.stream()
                .filter(activity -> activity.getName().equals(selectedActivityName))
                .findFirst()
                .orElse(null);

        if (selectedActivity != null) {
            selectedActivityEntry = new ActivityEntry(0, "", LocalDateTime.now(), LocalDateTime.now(), selectedActivity.getId());
        }
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

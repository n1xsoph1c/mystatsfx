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
    @FXML private Text startTimeErr;
    @FXML private Text endTimeErr;
    @FXML private Text activityDescriptionErr;

    @FXML private Button incomeBtn;
    @FXML private Button expenseBtn;
    @FXML private Button saveActivityBtn;
    @FXML private Button saveExpenseBtn;

    @FXML private DatePicker datePicker;
    @FXML private DatePicker startTime;
    @FXML private DatePicker endTime;

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
            accounts = accountHandler.getAllAccounts();
            activities = activityHandler.getActivities();

            // Populate ChoiceBox for activities and accounts
            activitySelect.getItems().addAll(activities.stream().map(Activity::getName).toList());
            accountSelect.getItems().addAll(accounts.stream().map(Account::getAccountName).toList());

            accountSelect.setAccessibleText("Select Account");
            activitySelect.setAccessibleText("Select Activity");

            // Set today’s date as the default value for date pickers
            datePicker.setValue(LocalDate.now());
            startTime.setValue(LocalDate.now());
            endTime.setValue(LocalDate.now());

            accountSelect.setOnAction(this::setAccount);
            activitySelect.setOnAction(this::setActivity);

            // Check for empty lists
            if (!activities.isEmpty()) {
                selectedActivity = activities.get(0);  // Use index
            } else {
                // Handle the case where activities are empty
                activityDescriptionErr.setText("No activities available.");
            }

            if (!accounts.isEmpty()) {
                selectedAccount = accounts.get(0);  // Use index
                initializeTransaction();
            } else {
                // Handle the case where accounts are empty
                descriptionErrorField.setText("No accounts available.");
            }

            expenseBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
            incomeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
            selectedTransaction.setTransactionType(ETransactionType.WITHDRAWAL);
            isIncome = false;

        } catch (SQLException e) {
            descriptionErrorField.setText("Error loading accounts or activities.");
            e.printStackTrace();
        }
    }

    private void initializeTransaction() {
        selectedTransaction = new Transaction(0, selectedAccount.getId(), 0, "",
                LocalDate.now(), ETransactionType.DEPOSIT);
    }

    public void setAccount(ActionEvent event) {
        String selectedAccountName = accountSelect.getValue();
        selectedAccount = accounts.stream()
                .filter(account -> account.getAccountName().equals(selectedAccountName))
                .findFirst()
                .orElse(null);

        if (selectedAccount != null) {
            initializeTransaction();  // Reset transaction with the new account
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
            amountErrorField.setText("");  // Clear error if valid
        } catch (NumberFormatException e) {
            amountErrorField.setText("Invalid amount. Please enter a valid number.");
        }
    }

    public void setDescription(ActionEvent event) {
        String description = descriptionField.getText().trim();
        if (!description.isEmpty()) {
            selectedTransaction.setDescription(description);
            descriptionErrorField.setText("");  // Clear error if valid
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
            activityDescriptionErr.setText("Activity cannot be empty.");
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

            if (isIncome) selectedTransaction.setTransactionType(ETransactionType.DEPOSIT);
            else selectedTransaction.setTransactionType(ETransactionType.WITHDRAWAL);

            transactionHandler.addTransaction(selectedTransaction);
            descriptionErrorField.setText("Transaction saved successfully!");
        } catch (SQLException e) {
            descriptionErrorField.setText("Error saving transaction.");
            e.printStackTrace();
        }
    }

    public void setActivity(ActionEvent event) {
        String selectedActivityName = activitySelect.getValue();
        activitySelect.setAccessibleText(selectedActivityName);
        selectedActivity = activities.stream()
                .filter(activity -> activity.getName().equals(selectedActivityName))
                .findFirst()
                .orElse(null);

        if (selectedActivity != null) {
            selectedActivityEntry = new ActivityEntry(0, "",
                    LocalDateTime.of(startTime.getValue(), LocalTime.now()),
                    LocalDateTime.of(endTime.getValue(), LocalTime.now()),
                    selectedActivity.getId());
        }
    }

    public void saveActivity(ActionEvent event) {
        try {
            if (selectedActivityEntry == null || selectedActivityEntry.getDescription().isEmpty()) {
                activityDescriptionErr.setText("Description cannot be empty.");
                return;
            }

            activityEntryHandler.addActivityEntry(selectedActivityEntry);
            activityDescriptionErr.setText("Entry saved successfully!");
        } catch (SQLException e) {
            activityDescriptionErr.setText("Error saving entry.");
            e.printStackTrace();
        }
    }

    public void selectDate(ActionEvent actionEvent) {
        
    }

    public void setStartTime(ActionEvent actionEvent) {
    }

    public void setEndTime(ActionEvent actionEvent) {
    }
}

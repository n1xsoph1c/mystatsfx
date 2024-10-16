package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Service.AccountDAO;
import com.ghostcompany.mystats.Service.ActivityDAO;
import com.ghostcompany.mystats.Service.ActivityEntryDAO;
import com.ghostcompany.mystats.Service.TransactionDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.text.Text;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            this.accounts = accountHandler.getAllAccounts();
            this.activities = activityHandler.getActivities();

            accounts.forEach(account -> accountSelect.getItems().add(account.getAccountName()));

            accountSelect.setOnAction(this::setAccount);
            accountSelect.setValue("Select Account");

            selectedTransaction = new Transaction(0, selectedAccount.getId(), 0, "", LocalDate.now(), ETransactionType.DEPOSIT);

            // Initialize date fields to today’s date
            datePicker.setValue(LocalDate.now());
            startTime.setValue(LocalDate.now());
            endTime.setValue(LocalDate.now());

        } catch (SQLException e) {
            descriptionErrorField.setText("Error loading accounts or activities.");
            e.printStackTrace();
        }
    }

    public void setAccount(ActionEvent event) {
        String selectedAccountName = accountSelect.getValue();
        this.selectedAccount = accounts.stream()
                .filter(account -> account.getAccountName().equals(selectedAccountName))
                .findFirst()
                .orElse(null);

        if (selectedAccount != null) {
            selectedTransaction = new Transaction(
                    1, selectedAccount.getId(), 0, "", datePicker.getValue(), ETransactionType.WITHDRAWAL);
        } else {
            descriptionErrorField.setText("Account not found!");
        }
    }

    public void setAmount(ActionEvent event) {
        try {
            double amount = Double.parseDouble(amountField.getText());
            selectedTransaction.setAmount(amount);
            amountErrorField.setText(""); // Clear error if amount is valid
            System.out.println("\nAmount: " + amount + "\n");

        } catch (NumberFormatException e) {
            amountErrorField.setText("Invalid amount. Please enter a valid number.");
        }
    }

    public void setDescription(ActionEvent event) {
        String description = descriptionField.getText().trim();
        if (!description.isEmpty()) {
            selectedTransaction.setDescription(description);
            descriptionErrorField.setText(""); // Clear error if description is valid
        } else {
            descriptionErrorField.setText("Description cannot be empty.");
        }
    }

    public void setTransactionToIncome(ActionEvent event) {
        incomeBtn.setStyle("-fx-background-color: DeepSkyBlue; -fx-text-fill: white;");
        expenseBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        selectedTransaction.setTransactionType(ETransactionType.DEPOSIT);
    }

    public void setTransactionToExpense(ActionEvent event) {
        expenseBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
        incomeBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        selectedTransaction.setTransactionType(ETransactionType.WITHDRAWAL);
    }


    public void selectDate(ActionEvent actionEvent) {
    }

    public void saveExpenses(ActionEvent actionEvent) {
        try {
            if (selectedTransaction == null) {
                descriptionErrorField.setText("No account selected.");
                return;
            }
            if (selectedTransaction.getAmount() <= 0) {
                amountErrorField.setText("Amount must be greater than 0.");
                return;
            }
            if (selectedTransaction.getDescription().isEmpty()) {
                descriptionErrorField.setText("Description cannot be empty.");
                return;
            }

            transactionHandler.addTransaction(selectedTransaction);
            descriptionErrorField.setText("Transaction saved successfully!");
        } catch (SQLException e) {
            descriptionErrorField.setText("Error saving transaction.");
            e.printStackTrace();
        }
    }

    public void setStartTime(ActionEvent actionEvent) {
    }

    public void setEndTime(ActionEvent actionEvent) {
    }

    public void saveActivity(ActionEvent actionEvent) {
    }
}

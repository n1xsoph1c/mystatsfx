package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityEntry;
import com.ghostcompany.mystats.Service.AccountDAO;
import com.ghostcompany.mystats.Service.ActivityDAO;
import com.ghostcompany.mystats.Service.ActivityEntryDAO;
import com.ghostcompany.mystats.Service.TransactionDAO;
import com.ghostcompany.mystats.Service.ListViewManager;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LogController implements Initializable {
    @FXML private ListView<HBox> expenseView;
    @FXML private ListView<HBox> activityView;

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final ActivityEntryDAO activityEntryDAO = new ActivityEntryDAO();

    private final ListViewManager listViewManager = new ListViewManager();  // Initialize ListViewManager

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Load and populate Expense ListView
            populateExpenseView();

            // Load and populate Activity ListView
            populateActivityView();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void populateExpenseView() throws SQLException {
        expenseView.getItems().clear();
        List<String> expenseHeaders = List.of("Date", "Account", "Description", "Amount");
        listViewManager.addHeader(expenseView, expenseHeaders);

        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account account : accounts) {
            List<Transaction> transactions = account.getTransactions();
            for (Transaction transaction : transactions) {
                List<String> entry = List.of(
                        transaction.getDate().toString(),
                        account.getAccountName(),
                        transaction.getDescription(),
                        String.format("%.2f", transaction.getBalance())
                );
                listViewManager.addEntry(expenseView, entry);
            }
            if (account.getTransactions().size() > 0) {
                List<String> entry = List.of("", "", "Total: ", String.format("%.2f", account.getTotalAmount()));
                listViewManager.addHeader(expenseView, entry);
            }
        }

    }

    @FXML
    private void populateActivityView() throws SQLException {
        activityView.getItems().clear();
        List<String> activityHeaders = List.of("Start Time", "End Time", "Description", "Activity", "Duration", "Group");
        listViewManager.addHeader(activityView, activityHeaders);

        List<Activity> activities = activityDAO.getActivities();
        for (Activity activity : activities) {
            List<ActivityEntry> entries = activity.getEntries();
            for (ActivityEntry entry : entries) {
                List<String> activityEntry = List.of(
                        entry.getStartTime().toString(),
                        entry.getEndTime().toString(),
                        entry.getDescription(),
                        activity.getName(),
                        entry.getDuration(),
                        activity.getGroupName()
                );
                listViewManager.addEntry(activityView, activityEntry);
            }
        }
    }
}

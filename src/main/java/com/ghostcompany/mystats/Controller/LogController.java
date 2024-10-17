package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityEntry;
import com.ghostcompany.mystats.Service.AccountDAO;
import com.ghostcompany.mystats.Service.ActivityDAO;
import com.ghostcompany.mystats.Service.ActivityEntryDAO;
import com.ghostcompany.mystats.Service.TransactionDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class LogController implements Initializable {
    @FXML private ListView<HBox> expenseView;
    @FXML private ListView<HBox> activityView;

    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final ActivityEntryDAO activityEntryDAO = new ActivityEntryDAO();

    private final List<Account> accountList = new ArrayList<>();
    private final List<Activity> activityList = new ArrayList<>();
    private final HashMap<Account, List<Transaction>> accounts = new HashMap<>();
    private final HashMap<Activity, List<ActivityEntry>> activities = new HashMap<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            loadAccountsAndTransactions();
            loadActivitiesAndEntries();

            addExpenseHeader();
            populateExpenseView();

            addActivityHeader();
            populateActivityView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadAccountsAndTransactions() throws SQLException {
        accountList.addAll(accountDAO.getAllAccounts());
        for (Account account : accountList) {
            List<Transaction> transactions = account.getTransactions();
            accounts.put(account, transactions);
        }
    }

    private void loadActivitiesAndEntries() throws SQLException {
        activityList.addAll(activityDAO.getActivities());
        for (Activity activity : activityList) {
            List<ActivityEntry> entries = activity.getEntries();
            activities.put(activity, entries);
        }
    }

    private void addExpenseHeader() {
        HBox header = createLabelHBox("Date", "Account", "Description", "Amount");
        expenseView.getItems().add(header);
    }

    private void populateExpenseView() {
        for (Account account : accounts.keySet()) {
            List<Transaction> transactions = accounts.get(account);
            for (Transaction transaction : transactions) {
                HBox expenseBox = createExpenseHBox(transaction, account);
                expenseView.getItems().add(expenseBox);
            }
        }
    }

    private void addActivityHeader() {
        HBox header = createLabelHBox("Start Time", "End Time", "Description", "Activity");
        activityView.getItems().add(header);
    }

    private void populateActivityView() {
        for (Activity activity : activities.keySet()) {
            List<ActivityEntry> entries = activities.get(activity);
            for (ActivityEntry entry : entries) {
                HBox activityBox = createActivityHBox(entry, activity);
                activityView.getItems().add(activityBox);
            }
        }
    }

    private HBox createExpenseHBox(Transaction transaction, Account account) {
        Label dateLabel = new Label(transaction.getDate().toString());
        Label accountLabel = new Label(account.getAccountName());
        Label descriptionLabel = new Label(transaction.getDescription());
        Label amountLabel = new Label(String.format("%.2f", transaction.getAmount()));

        return createHBox(dateLabel, accountLabel, descriptionLabel, amountLabel);
    }

    private HBox createActivityHBox(ActivityEntry entry, Activity activity) {
        Label startTimeLabel = new Label(entry.getStartTime().toString());
        Label endTimeLabel = new Label(entry.getEndTime().toString());
        Label descriptionLabel = new Label(entry.getDescription());
        Label activityLabel = new Label(activity.getName());

        return createHBox(startTimeLabel, endTimeLabel, descriptionLabel, activityLabel);
    }

    private HBox createLabelHBox(String... labels) {
        List<Label> labelList = new ArrayList<>();
        for (String text : labels) {
            Label label = new Label(text);
            label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            labelList.add(label);
        }
        return createHBox(labelList.toArray(new Label[0]));
    }

    private HBox createHBox(Label... labels) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(35.0);
        hBox.setPrefWidth(600.0);

        for (Label label : labels) {
            VBox vBox = createVBox(label, 100.0, 10.0, 10.0);
            hBox.getChildren().add(vBox);
        }

        return hBox;
    }

    private VBox createVBox(Label label, double width, double leftPadding, double rightPadding) {
        VBox vBox = new VBox(label);
        vBox.setPrefWidth(width);
        VBox.setMargin(label, new Insets(0, rightPadding, 0, leftPadding));
        HBox.setHgrow(vBox, Priority.ALWAYS);  // Set HGrow to ALWAYS
        return vBox;
    }
}

package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.AccountGroup;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityGroup;
import com.ghostcompany.mystats.Service.ListViewManager;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
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

public class AccountController implements Initializable {


    @FXML
    private ListView<HBox> accountListView;
    @FXML private ListView<HBox> accountGroupListView;
    @FXML private TextField accountName;
    @FXML private ChoiceBox<String> accountGroupChoiceBox;

    @FXML private Label choiceBoxErr;
    @FXML private Label accountNameErr;

    private final ListViewManager listViewManager = new ListViewManager();
    private Account selectedAccount = new Account();
    private final AccountGroup selectedAccountGroup = new AccountGroup();




    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        populateAccountGroup();
        populateAccount();

        accountGroupChoiceBox.setAccessibleText("Select Account Group");
        accountGroupChoiceBox.setOnAction(this::setAccountGroup);

    }

    public void populateAccountGroup() {
        try {
            accountGroupListView.getItems().clear();
            accountGroupChoiceBox.getItems().clear();

            List<String> headers = List.of("ID", "Name", "Group Total");
            listViewManager.addHeader(accountGroupListView, headers);

            ObservableList<AccountGroup> accountGroups = selectedAccountGroup.getAccountGroups();
            for (AccountGroup accountGroup : accountGroups) {
                accountGroupChoiceBox.getItems().add(accountGroup.getName());
                List<String> entry = List.of(
                        String.valueOf(accountGroup.getId()), accountGroup.getName(),
                        String.format("%.2f", accountGroup.getGroupTotal())
                );
                listViewManager.addEntry(accountGroupListView, entry);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error loading account groups.", accountGroupListView);
        }
    }

    public void populateAccount() {
        try {
            accountListView.getItems().clear();

            List<String> headers = List.of("ID", "Name", "Balance");
            listViewManager.addHeader(accountListView, headers);

            ObservableList<Account> accounts = selectedAccount.getAccounts();
            for (Account account : accounts) {
                List<String> entry = List.of(
                        String.valueOf(account.getId()), account.getAccountName(),
                        String.format("%.2f", account.getTotalAmount())
                );
                listViewManager.addEntry(accountListView, entry);
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error loading accounts.", accountListView);
        }
    }



    public void setAccountGroup(ActionEvent event) {
        selectedAccount.setGroupName(accountGroupChoiceBox.getValue());
    }

    public void createAccount(ActionEvent actionEvent) {
        try {
            if (isInputValid()) {
                selectedAccount.addAccount(selectedAccount);
                displaySuccess(accountNameErr, "Account Created Successfully!");
                populateAccount(); // Refresh the list after creation
            }
        } catch (SQLException e) {
            handleSQLException(e, "Error creating account.", accountNameErr);
        }
    }

    private boolean isInputValid() {
        if (accountName.getText().isEmpty()) {
            displayError(accountNameErr, "Account Name is Empty!");
            return false;
        }
        if (accountGroupChoiceBox.getValue() == null) {
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

    public void setAccountName(ActionEvent actionEvent) {
        selectedAccount.setAccountName(accountName.getText());
    }
}

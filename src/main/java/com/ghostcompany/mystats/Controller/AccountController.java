package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.AccountGroup;
import com.ghostcompany.mystats.Service.AccountDAO;
import com.ghostcompany.mystats.Service.AccountGroupDAO;
import com.ghostcompany.mystats.Service.ListViewManager;
import javafx.event.ActionEvent;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {

    @FXML private ListView<HBox> accountListView;
    @FXML private ListView<HBox> accountGroupListView;
    @FXML private TextField accountName;
    @FXML private ChoiceBox<String> accountGroupChoiceBox;

    @FXML private Label choiceBoxErr;
    @FXML private Label accountNameErr;


    private final ListViewManager listViewManager = new ListViewManager();

    private AccountDAO accountDAO = new AccountDAO();
    private AccountGroupDAO accountGroupDAO = new AccountGroupDAO();
    private Account selectedAccount;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            populateAccountGroup();


            populateAccount();

            accountGroupChoiceBox.setAccessibleText("Select Account Group");
            accountGroupChoiceBox.setOnAction(this::setAccountGroup);
            selectedAccount = new Account();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private ListView<HBox> accountList;

    public void populateAccountGroup() throws SQLException {
        // Clear the ListView before populating
        accountGroupListView.getItems().clear(); // Clears the existing items
        accountGroupChoiceBox.getItems().clear(); // Clears existing items in ChoiceBox

        List<String> accountGroupHeaders = List.of("id", "Name", "Group Total");
        listViewManager.addHeader(accountGroupListView, accountGroupHeaders);

        List<AccountGroup> accountGroups = accountGroupDAO.getAllAccountGroups();
        for (AccountGroup accountGroup : accountGroups) {
            accountGroupChoiceBox.getItems().add(accountGroup.getName());
            List<String> entry = List.of(
                    "" + accountGroup.getId(),
                    accountGroup.getName(),
                    String.format("%.2f", accountGroup.getGroupTotal())
            );
            listViewManager.addEntry(accountGroupListView, entry);
        }
    }

    public void populateAccount() throws SQLException {
        // Clear the ListView before populating
        accountListView.getItems().clear(); // Clears the existing items

        List<String> accountHeaders = List.of("ID", "Name", "Balance");
        listViewManager.addHeader(accountListView, accountHeaders);

        List<Account> accounts = accountDAO.getAllAccounts();
        for (Account account : accounts) {
            List<String> entry = List.of(
                    "" + account.getId(),
                    account.getAccountName(),
                    String.format("%.2f", account.getTotalAmount())
            );
            listViewManager.addEntry(accountListView, entry);
        }
    }



    public void setAccountGroup(ActionEvent event) {
        selectedAccount.setGroupName(accountGroupChoiceBox.getValue());
    }

    public void setAccountName(ActionEvent actionEvent) {
        selectedAccount.setAccountName(accountName.getText());
    }

    public void createAccount(ActionEvent actionEvent) {
        try {
            if ( selectedAccount.getAccountName() == null || selectedAccount.getAccountName().isEmpty()) {
                accountNameErr.setText("Account Name is Empty!");
            } else if (selectedAccount.getGroupName() == null || selectedAccount.getGroupName().isEmpty()) {
                choiceBoxErr.setText("Group Name is Empty!");
            } else {
                accountDAO.addAccount(selectedAccount);
                choiceBoxErr.setText("");
                accountNameErr.setText("Account Created Successfully!");
                accountNameErr.setTextFill(Paint.valueOf("green"));
            }
        } catch (RuntimeException | SQLException e) {
            accountNameErr.setText(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}

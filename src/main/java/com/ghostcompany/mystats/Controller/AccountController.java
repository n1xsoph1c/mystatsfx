package com.ghostcompany.mystats.Controller;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Service.AcconutDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    private List<Account> accounts = new ArrayList<>();
    private AcconutDAO accountHandler = new AcconutDAO();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
           accounts = accountHandler.getAllAccounts();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        accounts.forEach(account -> {
            TextField nameField = new TextField(account.getAccountName());
            TextField idField = new TextField(String.valueOf(account.getId()));
            TextField balanceField = new TextField(String.valueOf(account.getTotalAmount()));
//            TextField
        });
    }

    @FXML private ListView<HBox> accountList;


}

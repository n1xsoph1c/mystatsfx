package com.ghostcompany.mystats.Controller.Account;

import com.ghostcompany.mystats.Model.Account.Account;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AccountController implements Initializable {
    private List<Account> accounts = new com.ghostcompany.mystats.Service.Account().getAccounts();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        accounts.forEach(account -> {
            TextField nameField = new TextField(account.getName());
            TextField idField = new TextField(String.valueOf(account.getId()));
            TextField descriptionField = new TextField(account.getDescription());
            TextField balanceField = new TextField(String.valueOf(account.getTotalAmount()));
//            TextField
        });
    }

    @FXML private ListView<HBox> accountList;


}

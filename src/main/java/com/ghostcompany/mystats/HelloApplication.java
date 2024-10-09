package com.ghostcompany.mystats;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.AccountGroup;
import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Date;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        AccountGroup cash = new AccountGroup(1, "cash", "Stores all my cash");
        AccountGroup card = new AccountGroup(2, "card", "Stores all my card");

        Account wallet = new Account(1, "wallet", "My Wallet", cash);
        Account MRT = new Account(2, "MRT", "My MRT", card);

        wallet.addTransaction(new Transaction(1, new Date(), 100, "Ate breakfast", ETransactionType.WITHDRAWAL));
        wallet.addTransaction(new Transaction(2, new Date(), 100, "Ate dinner", ETransactionType.WITHDRAWAL));
        wallet.addTransaction(new Transaction(3, new Date(), 200, "transfer", ETransactionType.DEPOSIT));

        MRT.addTransaction(new Transaction(1, new Date(), 100, "Went to Farmgate", ETransactionType.WITHDRAWAL));
        MRT.addTransaction(new Transaction(2, new Date(), 150, "Transfer", ETransactionType.DEPOSIT));

        cash.addAccount(wallet);
        cash.addAccount(MRT);

        System.out.println(cash);
        System.out.println(card);

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
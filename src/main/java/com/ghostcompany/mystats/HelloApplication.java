package com.ghostcompany.mystats;

import com.ghostcompany.mystats.Model.Account.Account;
import com.ghostcompany.mystats.Model.Account.AccountGroup;
import com.ghostcompany.mystats.Model.Account.ETransactionType;
import com.ghostcompany.mystats.Model.Account.Transaction;
import com.ghostcompany.mystats.Model.Activity.Activity;
import com.ghostcompany.mystats.Model.Activity.ActivityEntry;
import com.ghostcompany.mystats.Model.Activity.ActivityGroup;
import com.ghostcompany.mystats.Service.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
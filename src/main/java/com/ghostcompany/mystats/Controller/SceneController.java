package com.ghostcompany.mystats.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SceneController implements Initializable {

    @FXML
    private SubScene mainScene;
    @FXML
    private SubScene navBarScene;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            // Load and set navBar.fxml in the navBarScene
            FXMLLoader navLoader = loadFXML("/com/ghostcompany/mystats/navBar.fxml");
            navBarScene.setRoot(navLoader.getRoot());

            // Pass the SceneController instance to NavBarController
            NavBarController navBarController = navLoader.getController();
            navBarController.setSceneController(this);

            // Set initial content in mainScene (e.g., entry.fxml)
            switchMainScene("/com/ghostcompany/mystats/entry.fxml");
        } catch (IOException e) {
            throw new RuntimeException("Error loading FXML resources", e);
        }
    }

    /**
     * Switches the content of the mainScene to the specified FXML.
     */
    public void switchMainScene(String fxmlPath) throws IOException {
        try {
            FXMLLoader loader = loadFXML(fxmlPath);
            mainScene.setRoot(loader.getRoot());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Switches the entire scene to login.fxml (for logout).
     */
    public void switchSceneToLogin() throws IOException {
        try {
            FXMLLoader loader = loadFXML("/com/ghostcompany/mystats/login.fxml");
            Stage stage = (Stage) mainScene.getScene().getWindow();
            stage.setScene(new Scene(loader.getRoot()));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException("FXML File not found: /com/ghostcompany/mystats/login.fxml");
        }
    }

    /**
     * Helper method to load FXML and return its FXMLLoader.
     */
    private FXMLLoader loadFXML(String resourcePath) throws IOException {
        URL resource = getClass().getResource(resourcePath);
        if (resource == null) {
            throw new IOException("FXML file not found: " + resourcePath);
        }
        FXMLLoader loader = new FXMLLoader(resource);
        loader.load();  // Ensure the FXML is fully loaded
        return loader;
    }
}

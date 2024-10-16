package com.ghostcompany.mystats.Controller;

import javafx.fxml.FXML;

import java.io.IOException;

public class NavBarController {

    private SceneController sceneController;

    // Inject SceneController instance (called by SceneController during initialization)
    public void setSceneController(SceneController sceneController) {
        this.sceneController = sceneController;
    }

    @FXML
    private void showAddEntry() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/entry.fxml");
    }

    @FXML
    private void showLog() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/log.fxml");
    }

    @FXML
    private void showAccounts() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/accounts.fxml");
    }

    @FXML
    private void showActivities() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/activities.fxml");
    }

    @FXML
    private void showSettings() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/settings.fxml");
    }

    @FXML
    private void showReport() throws IOException {
        sceneController.switchMainScene("/com/ghostcompany/mystats/report.fxml");
    }

    @FXML
    private void logout() throws IOException {
        sceneController.switchSceneToLogin();
    }
}

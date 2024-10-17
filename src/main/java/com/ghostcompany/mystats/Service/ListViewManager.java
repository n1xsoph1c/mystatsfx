package com.ghostcompany.mystats.Service;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import java.util.List;

public class ListViewManager {

    /**
     * Adds a header row to the ListView.
     *
     * @param listView The ListView to which the header will be added.
     * @param headers A list of header labels.
     */
    public void addHeader(ListView<HBox> listView, List<String> headers) {
        HBox header = createRow(headers, true);  // Create header row with bold text
        listView.getItems().add(header);
    }

    /**
     * Adds an entry row to the ListView.
     *
     * @param listView The ListView to which the entry will be added.
     * @param values A list of values to display in the entry.
     */
    public void addEntry(ListView<HBox> listView, List<String> values) {
        HBox entry = createRow(values, false);  // Create normal entry row
        listView.getItems().add(entry);
    }

    /**
     * Creates a row (either header or entry) with the provided values.
     *
     * @param values A list of values for the row.
     * @param isHeader If true, the row will be styled as a header.
     * @return A fully configured HBox representing the row.
     */
    private HBox createRow(List<String> values, boolean isHeader) {
        HBox hBox = new HBox();
        hBox.setPrefHeight(35.0);
        hBox.setPrefWidth(600.0);

        for (String value : values) {
            Label label = new Label(value);
            if (isHeader) {
                label.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
            }
            VBox vBox = createVBox(label, 100.0, 10.0, 10.0);
            hBox.getChildren().add(vBox);
        }

        return hBox;
    }

    /**
     * Creates a VBox containing the provided label, with specified width and padding.
     *
     * @param label The label to display inside the VBox.
     * @param width The preferred width of the VBox.
     * @param leftPadding The left padding for the label.
     * @param rightPadding The right padding for the label.
     * @return A fully configured VBox containing the label.
     */
    private VBox createVBox(Label label, double width, double leftPadding, double rightPadding) {
        VBox vBox = new VBox(label);
        vBox.setPrefWidth(width);
        VBox.setMargin(label, new Insets(0, rightPadding, 0, leftPadding));
        HBox.setHgrow(vBox, Priority.ALWAYS);  // Set HGrow to ALWAYS
        return vBox;
    }
}

package edu.wpi.cs3733.c22.teamA.entities.servicerequests;

import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Window;

/**
 *
 * Uses a combobox tooltip as the suggestion for auto complete and updates the
 * combo box itens accordingly <br />
 * It does not work with space, space and escape cause the combobox to hide and
 * clean the filter ... Send me a PR if you want it to work with all characters
 * -> It should be a custom controller - I KNOW!
 *
 * @author wsiqueir
 *
 * @param <T>
 */
public class AutoCompleteBox<T> {

    private ComboBox<T> cmb;
    String filter = "";
    private ObservableList<T> originalItems;

    public AutoCompleteBox(ComboBox<T> cmb) {
        this.cmb = cmb;
        originalItems = FXCollections.observableArrayList(cmb.getItems());
        cmb.setOnMousePressed(this::handleOnMousePressed);
        cmb.setOnKeyPressed(this::handleOnKeyPressed);
        cmb.setOnHidden(this::handleOnHiding);
    }
    public void handleOnMousePressed(MouseEvent e) {
        cmb.getSelectionModel().clearSelection();
    }


    public void handleOnKeyPressed(KeyEvent e) {
        ObservableList<T> filteredList = FXCollections.observableArrayList();
        KeyCode code = e.getCode();

        if (code.isLetterKey()) {
            filter += e.getText();
            cmb.getItems().setAll(originalItems);
        } else if (code == KeyCode.BACK_SPACE && filter.length() > 0) {
            filter = filter.substring(0, filter.length() - 1);
            cmb.getItems().setAll(originalItems);
        } else if (code == KeyCode.ESCAPE) {
            filter = "";
        } else {
            cmb.getItems().setAll(originalItems);
        }

        if (filter.length() == 0) {
            cmb.getItems().setAll(originalItems);
        } else {
            Stream<T> itens = cmb.getItems().stream();
            String txtUsr = filter.toLowerCase();
            itens.filter(el -> el.toString().toLowerCase().contains(txtUsr)).forEach(filteredList::add);
            cmb.getItems().setAll(filteredList);
        }

        cmb.setPromptText(filter);
    }

    public void handleOnHiding(Event e) {
        T s = cmb.getSelectionModel().getSelectedItem();
        cmb.getSelectionModel().select(s);
    }

}
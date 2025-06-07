package com.iesfernandoaguilar.perezgonzalez.util;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertManager {
    public static void alertInfo(String titulo, String msg) {
        Alert infoAlert = new Alert(AlertType.INFORMATION);
        infoAlert.setTitle(titulo);
        infoAlert.setHeaderText(null);
        infoAlert.setContentText(msg);
        infoAlert.showAndWait();
    }

    public static void alertError(String titulo, String msg, String hojaDeEstilo) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.getDialogPane().getStylesheets().add(hojaDeEstilo);
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }
}

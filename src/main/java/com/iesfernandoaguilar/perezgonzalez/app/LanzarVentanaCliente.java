package com.iesfernandoaguilar.perezgonzalez.app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanzarVentanaCliente extends Application {
    @Override
    public void start(Stage primStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        // Parent root = FXMLLoader.load(getClass().getResource("/view/FXML_CompraVendedor.fxml"));
        primStage.setTitle("Wheel Trader");

        Scene scene = new Scene(root);
        primStage.setScene(scene);
        primStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

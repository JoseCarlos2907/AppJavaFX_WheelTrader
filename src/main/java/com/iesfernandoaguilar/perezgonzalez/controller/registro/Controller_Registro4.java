package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Registro4 {
    private Lector_InicioSesion hiloLector;

    @FXML
    private Button Btn_IniciarSesion;

    @FXML
    void handleBtnIniciarSesionAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_InicioSesion controller = loader.getController();
        hiloLector.setController(controller);
        controller.setHiloLector(hiloLector);

        Stage stage2 = (Stage) Btn_IniciarSesion.getScene().getWindow();
        stage2.close();
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Registro4 implements ILogin{
    private Lector_InicioSesion hiloLector;

    @FXML
    private Button Btn_IniciarSesion;

    @FXML
    void handleBtnIniciarSesionAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        
        Controller_InicioSesion controller = loader.getController();
        hiloLector.setController(controller);
        controller.setHiloLector(hiloLector);

        Stage stage = (Stage) Btn_IniciarSesion.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    @Override
    public void siguientePaso() throws IOException {
        // Vacío
    }
}

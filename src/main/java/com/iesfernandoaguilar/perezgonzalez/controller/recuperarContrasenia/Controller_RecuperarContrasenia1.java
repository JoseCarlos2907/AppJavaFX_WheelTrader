package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia1 implements Initializable{
    private Lector_InicioSesion hiloLector;
    // private DataOutputStream dos;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextField TxtF_Correo;

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        this.siguientePaso();
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {

    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia2.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_RecuperarContrasenia2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        // this.hiloLector.setRecupContraController2(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia3 implements Initializable{
    private Lector_InicioSesion hiloLector;
    // private DataOutputStream dos;

    @FXML
    private Button Btn_CambiarContrasenia;

    @FXML
    private Button Btn_Volver;

    @FXML
    private PasswordField TxtF_ConfContra;

    @FXML
    private PasswordField TxtF_Contra;

    @FXML
    void handleBtnCambiarContraseniaAction(MouseEvent event) throws IOException {
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_InicioSesion controller = loader.getController();
        controller.setHiloLector(hiloLector);
        // this.hiloLector.setInicioSesionController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

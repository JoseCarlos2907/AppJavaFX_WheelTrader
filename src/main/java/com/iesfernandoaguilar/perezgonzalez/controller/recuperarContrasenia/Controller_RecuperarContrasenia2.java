package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.IOException;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia2 implements ILogin{
    private Lector_InicioSesion hiloLector;
    
    private byte[] salt;
    
    @FXML
    private Button Btn_Siguiente;
    
    @FXML
    private Button Btn_Volver;
    
    @FXML
    private TextField TxtF_Codigo;


    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        this.hiloLector.intentaCodigo(new String(this.TxtF_Codigo.getText()));
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia1.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_RecuperarContrasenia1 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
    
    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia3.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_RecuperarContrasenia3 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.setSalt(this.salt);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void setSalt(byte[] salt){
        this.salt = salt;
    }

    public void codigoIncorrecto(){
        AlertManager.alertError(
            "Código incorrecto",
            "El código que se te ha enviado por correo no es el que has indicado",
            getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
        );
    }
}

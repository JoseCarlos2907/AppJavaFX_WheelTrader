package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia2 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    @FXML
    private Button Btn_Siguiente;
    
    @FXML
    private Button Btn_Volver;
    
    @FXML
    private TextField TxtF_Codigo;

    private byte[] salt;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        Mensaje msg = new Mensaje();
        msg.setTipo("INTENTA_CODIGO");
        msg.addParam(new String(this.TxtF_Codigo.getText()));
        this.dos.writeUTF(Serializador.codificarMensaje(msg));
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
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Código incorrecto");
        alert.setHeaderText(null);
        alert.setContentText("El código que se te ha enviado por correo no es el que has indicado");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }
}

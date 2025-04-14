package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.model.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;
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
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia3 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    @FXML
    private Button Btn_CambiarContrasenia;

    @FXML
    private Button Btn_Volver;

    @FXML
    private PasswordField TxtF_ConfContra;
    
    @FXML
    private PasswordField TxtF_Contra;

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
    void handleBtnCambiarContraseniaAction(MouseEvent event) throws IOException {
        if(
            this.TxtF_Contra.getText().length() < 1 ||
            this.TxtF_ConfContra.getText().length() < 1
        ){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Debe rellenar todos los campos para poder continuar con el registro");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else if(!this.TxtF_Contra.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato incorrecto");
            alert.setHeaderText(null);
            alert.setContentText("El formato de la contraseña no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else if(!this.TxtF_Contra.getText().equals(this.TxtF_ConfContra.getText())){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Contraseñas diferentes");
            alert.setHeaderText(null);
            alert.setContentText("Las dos contraseñas deben ser exactamente iguales");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else{
            String contraseniaHasheada = SecureUtils.generate512(new String(this.TxtF_Contra.getText()), salt);

            Mensaje msg = new Mensaje();
            msg.setTipo("REINICIAR_CONTRASENIA");
            msg.addParam(contraseniaHasheada);

            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }
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
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Contraseña cambiada");
        alert.setHeaderText(null);
        alert.setContentText("La contraseña se ha cambiado correctamente");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.showAndWait();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_InicioSesion controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setInicioSesionController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void setSalt(byte[] salt){
        this.salt = salt;
    }
}

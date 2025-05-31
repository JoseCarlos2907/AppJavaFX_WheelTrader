package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;

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

public class Controller_Registro3 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;

    public static Usuario usuario;

    @FXML
    private Button Btn_Anterior;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private PasswordField TxtF_ConfContra;

    @FXML
    private PasswordField TxtF_Contra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        usuario = Controller_Registro2.usuario;
    }

    @FXML
    void handleBtnAnteriorAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro2.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Registro2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Anterior.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
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
            byte[] salt = SecureUtils.getSalt();
            String contraseniaHasheada = SecureUtils.generate512(new String(this.TxtF_Contra.getText()), salt);
            usuario.setContrasenia(contraseniaHasheada);
            usuario.setSalt(Base64.getEncoder().encodeToString(salt));

            ObjectMapper mapper = new ObjectMapper();
            String usuarioJSON = mapper.writeValueAsString(usuario);

            this.hiloLector.registrarUsuario(usuarioJSON);
        }
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso() throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro4.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Registro4 controller = loader.getController();
        controller.setHiloLector(hiloLector);

        Stage stage2 = (Stage) Btn_Anterior.getScene().getWindow();
        stage2.close();
    }
}

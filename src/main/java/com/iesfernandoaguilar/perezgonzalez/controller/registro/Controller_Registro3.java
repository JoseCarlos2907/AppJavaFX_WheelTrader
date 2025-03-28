package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Registro3 implements Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    private Usuario usuario;

    @FXML
    private Button Btn_Anterior;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private TextField TxtF_ConfContra;

    @FXML
    private TextField TxtF_Contra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.hiloLector.setRegistroController3(this);
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleBtnAnteriorAction(MouseEvent event) throws IOException {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_Registro2.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Registro2 controller = loader.getController();
        controller.setUsuario(usuario);
        controller.setHiloLector(hiloLector);

        Stage stage = (Stage) Btn_Siguiente.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);
        scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    // TODO: Hacer las comprobaciones de contraseña
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
            this.usuario.setContrasenia(contraseniaHasheada);
            this.usuario.setSalt(Base64.getEncoder().encodeToString(salt));

            ObjectMapper mapper = new ObjectMapper();
            String usuarioJSON = mapper.writeValueAsString(this.usuario);

            Mensaje msg = new Mensaje();
            msg.setTipo("REGISTRAR_USUARIO");
            msg.addParam(usuarioJSON);

            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso(){
        System.out.println("Registro completado");
    }
}

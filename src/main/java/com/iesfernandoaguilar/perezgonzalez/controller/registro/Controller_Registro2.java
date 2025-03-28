package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
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

public class Controller_Registro2 implements Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    private Usuario usuario;

    @FXML
    private Button Btn_Anterior;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private TextField TxtF_Correo;

    @FXML
    private TextField TxtF_CorreoPP;

    @FXML
    private TextField TxtF_NombreUsuario;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.TxtF_NombreUsuario.setText(this.usuario.getNombreUsuario().length() < 1 ? "" : new String(this.usuario.getNombreUsuario()));
        this.TxtF_Correo.setText(this.usuario.getCorreo().length() < 1 ? "" : new String(this.usuario.getCorreo()));
        this.TxtF_CorreoPP.setText(this.usuario.getCorreoPP().length() < 1 ? "" : new String(this.usuario.getCorreoPP()));

        System.out.println("Hola");

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleBtnAnteriorAction(MouseEvent event) throws IOException {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_Registro1.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Registro1 controller = loader.getController();
        controller.setUsuario(usuario);
        controller.setHiloLector(hiloLector);

        Stage stage = (Stage) Btn_Siguiente.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);
        scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
    
    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {

        if(
            this.TxtF_NombreUsuario.getText().length() < 1 ||
            this.TxtF_Correo.getText().length() < 1 ||
            this.TxtF_CorreoPP.getText().length() < 1
        ){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Debe rellenar todos los campos para poder continuar con el registro");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else if(!this.TxtF_Correo.getText().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,3}$")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato incorrecto");
            alert.setHeaderText(null);
            alert.setContentText("El formato del correo no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else{
            Mensaje msg = new Mensaje();
            msg.setTipo("COMPROBAR_NOMUSU_CORREO");
            msg.addParam(new String(this.TxtF_NombreUsuario.getText()));
            msg.addParam(new String(this.TxtF_Correo.getText()));
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void usuarioExistente(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Usuario existente");
        alert.setHeaderText(null);
        alert.setContentText("Ya hay un usuario registrado con ese mismo correo o nombre de usuario");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }

    public void siguientePaso() throws IOException{
        this.usuario.setNombreUsuario(null);
        this.usuario.setCorreo(null);
        this.usuario.setCorreoPP(null);

        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_Registro3.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Registro3 controller = loader.getController();
        controller.setUsuario(usuario);
        controller.setHiloLector(hiloLector);

        Stage stage = (Stage) Btn_Siguiente.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);
        scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;

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

public class Controller_Registro2 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;

    public static Usuario usuario;

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
        usuario = Controller_Registro1.usuario;
        this.TxtF_NombreUsuario.setText(usuario.getNombreUsuario() == null ? "" : new String(usuario.getNombreUsuario()));
        this.TxtF_Correo.setText(usuario.getCorreo() == null ? "" : new String(usuario.getCorreo()));
        this.TxtF_CorreoPP.setText(usuario.getCorreoPP() == null ? "" : new String(usuario.getCorreoPP()));
    }

    @FXML
    void handleBtnAnteriorAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro1.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Registro1 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Anterior.getScene().getWindow();
        stage2.close();
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
            this.hiloLector.comprobarNombreUsuarioYCorreo(new String(this.TxtF_NombreUsuario.getText()), new String(this.TxtF_Correo.getText()));
        }
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
        usuario.setNombreUsuario(new String(this.TxtF_NombreUsuario.getText()));
        usuario.setCorreo(new String(this.TxtF_Correo.getText()));
        usuario.setCorreoPP(new String(this.TxtF_CorreoPP.getText()));

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro3.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Registro3 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Anterior.getScene().getWindow();
        stage2.close();
    }
}

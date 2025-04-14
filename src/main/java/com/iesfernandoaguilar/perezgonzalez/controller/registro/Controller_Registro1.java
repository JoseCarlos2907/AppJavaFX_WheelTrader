package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.model.ILogin;
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

public class Controller_Registro1 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    public static Usuario usuario;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextField TxtF_Apellidos;

    @FXML
    private TextField TxtF_DNI;

    @FXML
    private TextField TxtF_Direccion;

    @FXML
    private TextField TxtF_Nombre;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(usuario == null){
            usuario = new Usuario();
        }else{
            this.TxtF_Nombre.setText(new String(usuario.getNombre()));
            this.TxtF_Apellidos.setText(new String(usuario.getApellidos()));
            this.TxtF_DNI.setText(new String(usuario.getDni()));
            this.TxtF_Direccion.setText(new String(usuario.getDireccion()));
        }

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        if(
            this.TxtF_Nombre.getText().length() < 1 ||
            this.TxtF_Apellidos.getText().length() < 1 ||
            this.TxtF_DNI.getText().length() < 1 ||
            this.TxtF_Direccion.getText().length() < 1
        ){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Campos incompletos");
            alert.setHeaderText(null);
            alert.setContentText("Debe rellenar todos los campos para poder continuar con el registro");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else if(!this.TxtF_DNI.getText().matches("^[0-9]{8}[A-Z]$")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato incorrecto");
            alert.setHeaderText(null);
            alert.setContentText("El formato del DNI no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else{
            Mensaje msg = new Mensaje();
            msg.setTipo("COMPROBAR_DNI");
            msg.addParam(new String(this.TxtF_DNI.getText()));
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent nuevaVista = loader.load();

        Controller_InicioSesion controller = loader.getController();
        hiloLector.setController(controller);
        controller.setHiloLector(this.hiloLector);
        Stage stage = (Stage) Btn_Siguiente.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);
        scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void dniExistente(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Usuario existente");
        alert.setHeaderText(null);
        alert.setContentText("Ya hay un usuario registrado con ese mismo DNI");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }

    public void siguientePaso() throws IOException{
        usuario.setNombre(this.TxtF_Nombre.getText());
        usuario.setApellidos(this.TxtF_Apellidos.getText());
        usuario.setDni(this.TxtF_DNI.getText());
        usuario.setDireccion(this.TxtF_Direccion.getText());

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro2.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Registro2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

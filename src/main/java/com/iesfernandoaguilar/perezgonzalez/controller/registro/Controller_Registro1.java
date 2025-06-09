package com.iesfernandoaguilar.perezgonzalez.controller.registro;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Registro1 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;

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
    }

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        if(
            this.TxtF_Nombre.getText().length() < 1 ||
            this.TxtF_Apellidos.getText().length() < 1 ||
            this.TxtF_DNI.getText().length() < 1 ||
            this.TxtF_Direccion.getText().length() < 1
        ){
            AlertManager.alertError(
                "Campos incompletos",
                "Debe rellenar todos los campos para poder continuar con el registro",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(!this.TxtF_DNI.getText().matches("^[0-9]{8}[A-Z]$")){
            AlertManager.alertError(
                "Formato incorrecto",
                "El formato del DNI no es el correcto",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else{
            this.hiloLector.comprobarDNI(new String(this.TxtF_DNI.getText()));
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();

        Controller_InicioSesion controller = loader.getController();
        hiloLector.setController(controller);
        controller.setHiloLector(this.hiloLector);
        
        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void dniExistente(){
        AlertManager.alertError(
            "Usuario existente",
            "Ya hay un usuario registrado con ese mismo DNI",
            getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
        );
    }

    public void siguientePaso() throws IOException{
        usuario.setNombre(this.TxtF_Nombre.getText());
        usuario.setApellidos(this.TxtF_Apellidos.getText());
        usuario.setDni(this.TxtF_DNI.getText());
        usuario.setDireccion(this.TxtF_Direccion.getText());

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro2.fxml"));
        Parent parent = loader.load();
        
        Controller_Registro2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }
}

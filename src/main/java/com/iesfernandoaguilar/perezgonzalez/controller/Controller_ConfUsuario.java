package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_ConfUsuario implements IApp, Initializable{
    private Lector_App hiloLector;

    private DataOutputStream dos;

    @FXML
    private Button Btn_CambiarContrasenia;

    @FXML
    private Button Btn_CerrarSesion;

    @FXML
    private Button Btn_MisAnuncios;

    @FXML
    private Button Btn_MisCompras;

    @FXML
    private Button Btn_MisGuardados;

    @FXML
    private Button Btn_MisPagos;

    @FXML
    private Button Btn_MisReuniones;

    @FXML
    private Button Btn_MisValoraciones;

    @FXML
    private Button Btn_Volver;

    @FXML
    private Label Lbl_Apellidos;

    @FXML
    private Label Lbl_Correo;

    @FXML
    private Label Lbl_CorreoPP;

    @FXML
    private Label Lbl_DNI;

    @FXML
    private Label Lbl_Direccion;

    @FXML
    private Label Lbl_Nombre;

    @FXML
    private Label Lbl_NombreUsuario;

    @FXML
    private PasswordField TxtF_ConfContra;

    @FXML
    private PasswordField TxtF_Contra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Usuario usuario = Session.getUsuario();

        this.Lbl_Nombre.setText("Nombre: " + usuario.getNombre());
        this.Lbl_Apellidos.setText("Apellidos: " + usuario.getApellidos());
        this.Lbl_DNI.setText("DNI: " + usuario.getDni());
        this.Lbl_Direccion.setText("Dirección: " + usuario.getDireccion());

        this.Lbl_NombreUsuario.setText("Nombre de Usuario: " + usuario.getNombreUsuario());
        this.Lbl_Correo.setText("Correo: " + usuario.getCorreo());
        this.Lbl_CorreoPP.setText("Correo de PayPal" + usuario.getCorreoPP());

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnCambiarContraseniaAction(MouseEvent event) {

    }

    @FXML
    void handleBtnCerrarSesionAction(MouseEvent event) throws IOException {
        Session.setHiloNoCreado();
        Session.cerrarSession();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnMisAnunciosAction(MouseEvent event) {

    }

    @FXML
    void handleBtnMisComprasAction(MouseEvent event) {

    }

    @FXML
    void handleBtnMisGuardadosAction(MouseEvent event) {

    }

    @FXML
    void handleBtnMisPagosAction(MouseEvent event) {

    }

    @FXML
    void handleBtnMisReunionesAction(MouseEvent event) {

    }

    @FXML
    void handleBtnMisValoracionesAction(MouseEvent event) {

    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }
}

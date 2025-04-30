package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroGuardados;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPublicados;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
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

    private FiltroGuardados filtroGuardados;
    private FiltroPublicados filtroPublicados;

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
    void handleBtnMisAnunciosAction(MouseEvent event) throws IOException {
        filtroPublicados = new FiltroPublicados(Session.getUsuario().getNombreUsuario(), 1, 10);

        Mensaje msg = new Mensaje();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtroPublicados);

        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(filtroPublicados.getTipoFiltro());
        msg.addParam("si");
        msg.addParam(String.valueOf(Session.getUsuario().getIdUsuario()));

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    @FXML
    void handleBtnMisComprasAction(MouseEvent event) {
        // TODO: Funcionalidad mis compras
    }

    @FXML
    void handleBtnMisGuardadosAction(MouseEvent event) throws IOException {
        filtroGuardados = new FiltroGuardados(Session.getUsuario().getNombreUsuario(), 1, 10);

        Mensaje msg = new Mensaje();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtroGuardados);

        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(filtroGuardados.getTipoFiltro());
        msg.addParam("si");
        msg.addParam(String.valueOf(Session.getUsuario().getIdUsuario()));

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    @FXML
    void handleBtnMisPagosAction(MouseEvent event) {
        // TODO: Funcionalidad mis pagos
    }

    @FXML
    void handleBtnMisReunionesAction(MouseEvent event) {
        // TODO: Funcionalidad mis reuniones
    }

    @FXML
    void handleBtnMisValoracionesAction(MouseEvent event) {
        // TODO: Funcionalidad mis valoraciones
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

    public void irListaGuardados(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisGuardados.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_MisGuardados controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes, true);
        controller.setFiltro(filtroGuardados);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void irListaPublicados(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisAnuncios.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_MisAnuncios controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes, true);
        controller.setFiltro(filtroPublicados);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

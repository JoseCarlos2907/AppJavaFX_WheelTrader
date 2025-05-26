package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
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
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_ConfUsuario implements IApp, Initializable{
    private Lector_App hiloLector;

    private DataOutputStream dos;

    private FiltroPorNombreUsuario filtroGuardados;
    private FiltroPorNombreUsuario filtroPublicados;

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
            Mensaje msg = new Mensaje();
            msg.setTipo("OBTENER_SALT_REINICIO");
            msg.addParam(Session.getUsuario().getNombreUsuario());

            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        }
    }

    public void reiniciarContrasenia(byte[] salt) throws IOException{
        String hash = SecureUtils.generate512(new String(this.TxtF_Contra.getText()), salt);

        Mensaje msg = new Mensaje();
        msg.setTipo("REINICIAR_CONTRASENIA");
        msg.addParam(Session.getUsuario().getNombreUsuario());
        msg.addParam(hash);

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void contraseniaReiniciada(){
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Contraseña reiniciada");
        alert.setHeaderText(null);
        alert.setContentText("Se ha cambiado la contraseña correctamente.");
        alert.showAndWait();
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
        filtroPublicados = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);
        filtroPublicados.setTipoFiltro("Publicados");

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
    void handleBtnMisComprasAction(MouseEvent event) throws IOException {
        FiltroPorNombreUsuario filtro = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);
        
        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtro);

        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_VENTAS");
        msg.addParam(filtroJSON);
        msg.addParam("si");
    
        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void irListaCompras(String comprasJSON) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisCompras.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_MisCompras controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirCompras(comprasJSON);
        controller.setFiltro(filtroGuardados);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnMisGuardadosAction(MouseEvent event) throws IOException {
        filtroGuardados = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);

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
        controller.aniadirAnuncios(anunciosJSON, imagenes);
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
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        controller.setFiltro(filtroPublicados);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

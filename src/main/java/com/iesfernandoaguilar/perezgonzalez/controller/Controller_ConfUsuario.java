package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;
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
    }

    @FXML
    void handleBtnCambiarContraseniaAction(MouseEvent event) throws IOException {
        if(
            this.TxtF_Contra.getText().length() < 1 ||
            this.TxtF_ConfContra.getText().length() < 1
        ){
            AlertManager.alertError(
                "Campos incompletos",
                "Debe rellenar todos los campos para poder continuar con el registro",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(!this.TxtF_Contra.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")){
            AlertManager.alertError(
                "Formato incorrecto",
                "El formato de la contraseña no es el correcto",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(!this.TxtF_Contra.getText().equals(this.TxtF_ConfContra.getText())){
            AlertManager.alertError(
                "Contraseñas diferentes",
                "Las dos contraseñas deben ser exactamente iguales",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else{
            this.hiloLector.obtenerSaltReinicio(Session.getUsuario().getNombreUsuario());
        }
    }

    public void reiniciarContrasenia(byte[] salt) throws IOException{
        String hash = SecureUtils.generate512(new String(this.TxtF_Contra.getText()), salt);

        this.hiloLector.reiniciarContrasenia(Session.getUsuario().getNombreUsuario(), hash);
    }

    public void contraseniaReiniciada(){
        AlertManager.alertInfo(
            "Contraseña reiniciada",
            "Se ha cambiado la contraseña correctamente."
        );
    }

    @FXML
    void handleBtnCerrarSesionAction(MouseEvent event) throws IOException {
        this.hiloLector.cerrarSesion(Session.getUsuario().getIdUsuario());
        Session.setHiloNoCreado();
        Session.setHiloLoginNoCreado();
        Session.cerrarSession();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnMisAnunciosAction(MouseEvent event) throws IOException {
        filtroPublicados = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);
        filtroPublicados.setTipoFiltro("Publicados");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtroPublicados);

        this.hiloLector.obtenerAnuncios(filtroJSON, filtroPublicados.getTipoFiltro(), "si", Session.getUsuario().getIdUsuario());
    }

    @FXML
    void handleBtnMisComprasAction(MouseEvent event) throws IOException {
        FiltroPorNombreUsuario filtro = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);
        
        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtro);

        this.hiloLector.obtenerVentas(filtroJSON, "si");
    }

    public void irListaCompras(String comprasJSON) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisCompras.fxml"));
        Parent parent = loader.load();

        Controller_MisCompras controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirCompras(comprasJSON);
        controller.setFiltro(filtroGuardados);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnMisGuardadosAction(MouseEvent event) throws IOException {
        filtroGuardados = new FiltroPorNombreUsuario(Session.getUsuario().getNombreUsuario(), 0, 10);

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(filtroGuardados);

        this.hiloLector.obtenerAnuncios(filtroJSON, filtroGuardados.getTipoFiltro(), "si", Session.getUsuario().getIdUsuario());
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void irListaGuardados(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisGuardados.fxml"));
        Parent parent = loader.load();

        Controller_MisGuardados controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        controller.setFiltro(filtroGuardados);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void irListaPublicados(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisAnuncios.fxml"));
        Parent parent = loader.load();

        Controller_MisAnuncios controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        controller.setFiltro(filtroPublicados);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }
}

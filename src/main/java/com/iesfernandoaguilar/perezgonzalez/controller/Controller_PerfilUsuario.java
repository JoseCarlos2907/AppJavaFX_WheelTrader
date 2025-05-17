package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.Valoracion;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_PerfilUsuario implements IListaAnuncios, Initializable{
    private Usuario usuario;
    private Anuncio anuncioSeleccionado;

    private DataOutputStream dos;

    private Lector_App hiloLector;

    @FXML
    private Button Btn_Menu;

    @FXML
    private Label Lbl_NombreUsuario;

    @FXML
    private Label Lbl_ValoracionMedia;

    @FXML
    private VBox VBox_Anuncios;

    @FXML
    private ImageView Btn_Reportar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarAnuncios(){
        // List<Anuncio> anuncios = List.of(
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL")
        // );

        // for (Anuncio anuncio : anuncios) {
        //     try {
        //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Anuncio.fxml"));
        //         Parent componente = loader.load();

        //         Controller_Anuncio controller = loader.getController();
        //         controller.setAnuncio(anuncio);
        //         controller.setDatos(anuncio.getCategoria(), anuncio.getAnio(), anuncio.getKilometraje());
        //         controller.setMarcaModelo(anuncio.getMarca(), anuncio.getModelo());
        //         controller.setPrecio(anuncio.getPrecioAlContado());
        //         controller.setUbicacion(anuncio.getProvincia(), anuncio.getCiudad());
        //         controller.setUsuario("JoseCarlos2907");
        //         controller.setController(this);
                
        //         this.VBox_Anuncios.getChildren().add(componente);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    @Override
    public void abrirAnuncio(Anuncio anuncio) {
        this.anuncioSeleccionado = anuncio;

        Mensaje msg = new Mensaje();
    
        msg.setTipo("OBTENER_IMAGENES");
        msg.addParam(String.valueOf(this.anuncioSeleccionado.getIdAnuncio()));

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnMenuAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Menu.getScene().getWindow();
        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        this.Lbl_NombreUsuario.setText("Perfil de " + usuario.getNombreUsuario());
    }

    @Override
    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas) throws JsonMappingException, JsonProcessingException {
        
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    @Override
    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException {
        // TODO: Ir detalle anuncio
    }

    @FXML
    void handleBtnReportarAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_FormularioReporte.fxml"));
        Parent nuevaVista = loader.load();

        Controller_FormularioReporte controller = loader.getController();
        controller.setUsuario(usuario);
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Menu.getScene().getWindow();
        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void abrirPerfilUsuario(Usuario usuario) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent nuevaVista = loader.load();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Menu.getScene().getWindow();
        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }
}

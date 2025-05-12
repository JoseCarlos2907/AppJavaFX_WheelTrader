package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_PerfilUsuario implements IListaAnuncios, Initializable{
    private String nombreUsuario;
    private Anuncio anuncioSeleccionado;

    private DataOutputStream dos;

    @FXML
    private Button Btn_Menu;

    @FXML
    private Label Lbl_NombreUsuario;

    @FXML
    private Label Lbl_ValoracionMedia;

    @FXML
    private VBox VBox_Anuncios;

    @FXML
    private VBox VBox_Comentarios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // this.setNombreUsuario("Juan");
        this.cargarValoraciones();
        // this.cargarAnuncios();

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cargarValoraciones(){
        int cantValoraciones = 7;   // Cantidad total de valoraciones
        int total = 31;   // Suma total de las valoraciones

        List<Valoracion> valoraciones = List.of(
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo")
        );

        for (Valoracion valoracion : valoraciones) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Valoracion.fxml"));
                Parent componente = loader.load();

                Controller_Valoracion controller = loader.getController();
                controller.setComentario(valoracion.getComentario());
                controller.setValoracion(valoracion.getValoracion());
                controller.setUsuario("JoseCarlos2908");    // En base al idValorador

                this.VBox_Comentarios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double valoracionMedia = total / (double) cantValoraciones;
        this.Lbl_ValoracionMedia.setText(String.format("%.2f", valoracionMedia));
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

        Stage stage = (Stage) Btn_Menu.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void setNombreUsuario(String nombreUsuario){
        this.nombreUsuario = nombreUsuario;
        this.Lbl_NombreUsuario.setText("Perfil de " + nombreUsuario);
    }

    @Override
    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas, boolean primeraCarga) throws JsonMappingException, JsonProcessingException {
        
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        
    }

    @Override
    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException {
        // TODO: Ir detalle anuncio
    }
}

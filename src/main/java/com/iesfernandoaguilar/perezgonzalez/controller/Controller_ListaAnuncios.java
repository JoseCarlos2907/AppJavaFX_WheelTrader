package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Controller_ListaAnuncios implements Initializable{
    @FXML
    private Button Btn_Volver;

    @FXML
    private Button Btn_Filtros;

    @FXML
    private Rectangle Rectangle_Fondo;

    @FXML
    private TextField TxtF_BarraBusqueda;

    @FXML
    private VBox VBox_Anuncios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Anuncio> anuncios = List.of(
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
            new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL")
        );

        for (Anuncio anuncio : anuncios) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Anuncio.fxml"));
                Parent componente = loader.load();

                Controller_Anuncio controller = loader.getController();
                controller.setAnuncio(anuncio);
                controller.setDatos(anuncio.getCategoria(), anuncio.getAnio(), anuncio.getKilometraje());
                controller.setMarcaModelo(anuncio.getMarca(), anuncio.getModelo());
                controller.setPrecio(anuncio.getPrecioAlContado());
                controller.setUbicacion(anuncio.getProvincia(), anuncio.getCiudad());
                controller.setUsuario("JoseCarlos2907");
                controller.setController(this);

                this.VBox_Anuncios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {
        // TODO: Volver a la ventana Home
    }

    public void abrirAnuncio(Anuncio anuncio){
        // TODO: Abrir la ventana de detalles del anuncio
    }

}

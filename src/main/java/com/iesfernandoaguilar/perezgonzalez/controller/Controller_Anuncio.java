package com.iesfernandoaguilar.perezgonzalez.controller;

import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller_Anuncio {
    @FXML
    private ImageView ImgView_Guardado;

    @FXML
    private ImageView ImgView_Vehiculo;

    @FXML
    private Label Lbl_Datos;

    @FXML
    private Label Lbl_MarcaModelo;

    @FXML
    private Label Lbl_Precio;

    @FXML
    private Label Lbl_Ubicacion;

    @FXML
    private Label Lbl_Usuario;

    @FXML
    private Pane Pane_Anuncio;

    @FXML
    private Rectangle Rectangle_Fondo;

    private Controller_ListaAnuncios controller;
    private Anuncio anuncio;

    @FXML
    void handlePaneAnuncioAction(MouseEvent event) {
        this.controller.abrirAnuncio(anuncio);
    }

    public void setAnuncio(Anuncio anuncio){
        this.anuncio = anuncio;
    }

    public void setMarcaModelo(String marca, String modelo){
        this.Lbl_MarcaModelo.setText(marca + " | " + modelo);
    }

    public void setPrecio(double precio){
        this.Lbl_Precio.setText(precio + " €");
    }

    public void setDatos(String categoria, int anio, int km){
        this.Lbl_Datos.setText(categoria + " - " + anio + " - " + km);
    }

    public void setUbicacion(String provincia, String ciudad){
        this.Lbl_Ubicacion.setText("En " + ciudad + ", " + provincia);
    }

    public void setUsuario(String usuario){
        this.Lbl_Usuario.setText(usuario);
    }

    public void setController(Controller_ListaAnuncios controller){
        this.controller = controller;
    }

    public Pane getPane(){
        return this.Pane_Anuncio;
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller_Anuncio{
    private IListaAnuncios controller;

    private Anuncio anuncio;

    private Lector_App hiloLector;

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

    @FXML
    void handleLblUsuarioAction(MouseEvent event) throws IOException {
        // Ya que tengo onClick en este label y debajo esta el rectangulo, este método me frena el evento de ratón para que no 
        // se ejecute el onClick del elemento por debajo a este
        event.consume();
        this.controller.abrirPerfilUsuario(this.anuncio.getVendedor());
    }

    @FXML
    void handlePaneAnuncioAction(MouseEvent event) throws IOException {
        this.controller.abrirAnuncio(anuncio);
    }

    @FXML
    void handleGuardarAction(MouseEvent event) throws IOException{
        if(anuncio.isGuardado()){
            anuncio.eliminarGuardado();
            this.setGuardado(false);
            this.hiloLector.guardarAnuncio(anuncio.getIdAnuncio(), Session.getUsuario().getNombreUsuario());
        }else{
            anuncio.guardar();
            this.setGuardado(true);
            this.hiloLector.eliminarAnuncioGuardados(anuncio.getIdAnuncio(), Session.getUsuario().getNombreUsuario());
        }
    }

    public void setGuardado(boolean guardado){
        Image imagen;
        
        if(guardado){
            imagen = new Image(getClass().getResourceAsStream("/img/IconoGuardado.png"));
        }else{
            imagen = new Image(getClass().getResourceAsStream("/img/IconoGuardar.png"));
        }

        this.ImgView_Guardado.setImage(imagen);
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

    public void setDatos(String tipo, String anio, String km){
        if(km.isEmpty()){
            this.Lbl_Datos.setText(tipo + " - " + anio);
        }else{
            this.Lbl_Datos.setText(tipo + " - " + anio + " - " + km);
        }
    }

    public void setUbicacion(String provincia, String ciudad){
        this.Lbl_Ubicacion.setText("En " + ciudad + ", " + provincia);
    }

    public void setUsuario(String usuario){
        this.Lbl_Usuario.setText("De " + usuario);
    }

    public void setImagen(byte[] imagen){
        ByteArrayInputStream is = new ByteArrayInputStream(imagen);
        this.ImgView_Vehiculo.setImage(new Image(is));
    }

    public void setController(IListaAnuncios controller){
        this.controller = controller;
    }

    public Pane getPane(){
        return this.Pane_Anuncio;
    }

    public void setHiloLector(Lector_App hiloLecctor){
        this.hiloLector = hiloLecctor;
    }
}

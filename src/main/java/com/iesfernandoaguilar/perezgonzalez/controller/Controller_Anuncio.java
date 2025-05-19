package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller_Anuncio implements Initializable{
    private IListaAnuncios controller;

    private DataOutputStream dos;

    private Anuncio anuncio;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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
        Mensaje msg = new Mensaje();
        if(anuncio.isGuardado()){
            msg.setTipo("ELIMINAR_ANUNCIO_GUARDADOS");
            anuncio.eliminarGuardado();
            this.setGuardado(false);
        }else{
            msg.setTipo("GUARDAR_ANUNCIO");
            anuncio.guardar();
            this.setGuardado(true);
        }

        msg.addParam(String.valueOf(this.anuncio.getIdAnuncio()));
        msg.addParam(String.valueOf(Session.getUsuario().getNombreUsuario()));

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
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
}

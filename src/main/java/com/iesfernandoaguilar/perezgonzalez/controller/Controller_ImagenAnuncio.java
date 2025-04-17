package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.ByteArrayInputStream;

import com.iesfernandoaguilar.perezgonzalez.model.Imagen;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller_ImagenAnuncio {
    private Imagen imagen;
    private Parent hbox;

    private Controller_PublicarAnuncio2 controller;

    @FXML
    private ImageView ImgView_Borrar;

    @FXML
    private ImageView ImgView_Vehiculo;

    @FXML
    private Pane Pane_Notificacion;

    @FXML
    private Rectangle Rectangle_Fondo;

    @FXML
    void handlerImgViewBorrarAction(MouseEvent event) {
        this.controller.eliminarImagen(this.imagen);
        
        ((HBox) hbox.getParent()).getChildren().remove(hbox);
    }

    public void setController(Controller_PublicarAnuncio2 controller){
        this.controller = controller;
    }

    public void setImagen(Imagen img){
        this.imagen = img;
        ByteArrayInputStream is = new ByteArrayInputStream(imagen.getImagen());
        this.ImgView_Vehiculo.setImage(new Image(is));
    }

    public void setParent(Parent parent){
        this.hbox = parent;
    }
}

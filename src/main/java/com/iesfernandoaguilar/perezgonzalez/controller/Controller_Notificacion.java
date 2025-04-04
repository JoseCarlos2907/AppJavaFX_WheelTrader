package com.iesfernandoaguilar.perezgonzalez.controller;

import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller_Notificacion {
    @FXML
    private ImageView ImgView_Icono;

    @FXML
    private ImageView ImgView_Icono2;

    @FXML
    private Label Lbl_Titulo;

    @FXML
    private Label Lbl_Usuario;

    @FXML
    private Pane Pane_Notificacion;

    @FXML
    private Rectangle Rectangle_Fondo;

    private Controller_Notificaciones controller;
    private Notificacion notificacion;

    @FXML
    void handlePaneNotificacionAction(MouseEvent event) {
        this.controller.setDatosNotificacionActiva(Pane_Notificacion, notificacion);
    }

    public void setIconoPrincipal(String tipo){
        switch (tipo) {
            case "INFORMACION":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoInformacion.png")));
                break;
            case "INCIDENCIA":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoIncidencia.png")));
                break;
            case "RESPUESTA_INCIDENCIA":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoRespuestaIncidencia.png")));
                break;
            case "VALORACION":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoVaoracion.png")));
                break;
            case "CONCRETAR_REUNION":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoConcretarReunion.png")));
                break;
            case "REUNION_RECHAZADA":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoRespuestaRechazada.png")));                
                break;
            case "REUNION_CONCRETADA":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoRespuestaConcretada.png")));
                break;
            case "CUESTIONARIO_REUNION":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoCuestionarioReunion.png")));
                break;
        }
    }

    public void setIconoLeido(String estaLeido){
        switch (estaLeido) {
            case "LEIDO":
                this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoNoLeido.png")));
                break;

            case "NO-LEIDO":
            case "RESPONDIDO":
                this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoLeido.png")));
                break;
        }
    }

    public void setNotificacion(Notificacion noti){
        this.notificacion = noti;
    }

    public void setTitulo(String titulo){
        this.Lbl_Titulo.setText(titulo);
    }

    public void setUsuario(String usuario){
        this.Lbl_Usuario.setText(usuario);
    }

    public void setController(Controller_Notificaciones controller){
        this.controller = controller;
    }

    public Pane getPane(){
        return this.Pane_Notificacion;
    }

    public void setSeleccionada(boolean seleccionada){
        if (seleccionada) {
            this.Rectangle_Fondo.getStyleClass().clear();
            this.Rectangle_Fondo.getStyleClass().setAll("seleccionada");
        }else{
            this.Rectangle_Fondo.getStyleClass().clear();
            this.Rectangle_Fondo.getStyleClass().setAll("noSeleccionada");
        }
    }
}

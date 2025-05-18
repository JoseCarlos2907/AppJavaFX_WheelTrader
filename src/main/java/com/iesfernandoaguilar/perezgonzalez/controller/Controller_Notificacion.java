package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Controller_Notificacion{

    @FXML
    private Button Btn_VerOferta;

    @FXML
    private ImageView ImgView_Icono;

    @FXML
    private ImageView ImgView_Icono2;

    @FXML
    private Label Lbl_Titulo;

    @FXML
    private Label Lbl_Mensaje;

    @FXML
    private Pane Pane_Notificacion;

    @FXML
    private Rectangle Rectangle_Fondo;

    private Controller_Notificaciones controller;
    private Notificacion notificacion;

    @FXML
    void handleBtnVerOfertaAction(MouseEvent event) {
        if("Pagar".equals(this.Btn_VerOferta.getText())){
            this.controller.abrirPagoPayPal(notificacion);
        }else{
            this.controller.abrirCompraVendedor(notificacion);
        }
    }

    @FXML
    void handleIcono2Action(MouseEvent event) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("CAMBIAR_ESTADO_NOTIFICACION");
        msg.addParam(String.valueOf(notificacion.getIdNotificacion()));
        if("NO_LEIDO".equals(notificacion.getEstado())){
            this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoLeido.png")));
            notificacion.setEstado("LEIDO");
            msg.addParam("LEIDO");
        }else if("LEIDO".equals(notificacion.getEstado())){
            this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoNoLeido.png")));
            notificacion.setEstado("NO_LEIDO");
            msg.addParam("NO_LEIDO");
        }

        if(!"RESPONDIDO".equals(notificacion.getEstado())){
            this.controller.cambiarEstadoNotificacion(msg);
        }
    }

    public void setIconoPrincipal(String tipo){
        switch (tipo) {
            case "OFERTA_ANUNCIO":
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoInformacion.png")));
                break;
            case "OFERTA_ACEPTADA":
                this.Btn_VerOferta.setText("Pagar");
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoInformacion.png")));
                break;
            default:
                this.ImgView_Icono.setImage(new Image(getClass().getResourceAsStream("/img/IconoInformacion.png")));
                break;
        }
    }

    public void setEstado(String estado){
        switch (estado) {
            case "RESPONDIDO":
                this.Btn_VerOferta.setDisable(true);
            case "LEIDO":
                this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoLeido.png")));
                break;
            
            case "NO_LEIDO":
                this.ImgView_Icono2.setImage(new Image(getClass().getResourceAsStream("/img/IconoNoLeido.png")));
                break;
        }
    }

    public void setNotificacion(Notificacion noti){
        this.notificacion = noti;
    }

    public void setTitulo(String titulo){
        this.Lbl_Titulo.setText(titulo);
    }

    public void setMensaje(String mensaje){
        this.Lbl_Mensaje.setText(mensaje);
    }

    public void setController(Controller_Notificaciones controller){
        this.controller = controller;
    }

    public Pane getPane(){
        return this.Pane_Notificacion;
    }
}

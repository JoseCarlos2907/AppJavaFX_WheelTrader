package com.iesfernandoaguilar.perezgonzalez.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class Controller_Valoracion {
    
    @FXML
    private ImageView ImgView_Reportar;

    @FXML
    private Label Lbl_Comentario;

    @FXML
    private Label Lbl_Usuario;

    @FXML
    private Label Lbl_Valoracion;

    @FXML
    void handlerImgReportarAction(MouseEvent event) {
        System.out.println("Reportado " + this.Lbl_Usuario.getText());
    }

    public void setUsuario(String usuario){
        this.Lbl_Usuario.setText("Comentario de " + usuario);
    }

    public void setValoracion(int valoracion){
        this.Lbl_Valoracion.setText(String.valueOf(valoracion));
    }

    public void setComentario(String comentario){
        this.Lbl_Comentario.setText(comentario);
    }
}

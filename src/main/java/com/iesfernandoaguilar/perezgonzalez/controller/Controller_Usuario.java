package com.iesfernandoaguilar.perezgonzalez.controller;

import com.iesfernandoaguilar.perezgonzalez.model.Usuario;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

public class Controller_Usuario {
    private Controller_HomeModerador controller;
    private Usuario usuario;

    @FXML
    private Label Lbl_CantReportes;

    @FXML
    private Label Lbl_NombreUsuario;

    @FXML
    private Label Lbl_ValoracionMedia;

    @FXML
    private Pane Pane_Usuario;

    @FXML
    void handlePaneUsuarioAction(MouseEvent event) {
        this.controller.abrirPerfilUsuario(usuario);
    }

    public void setCantReportes(long cantReportes){
        this.Lbl_CantReportes.setText(String.valueOf(cantReportes));
    }

    public void setNombreUsuario(String nombreUsuario){
        this.Lbl_NombreUsuario.setText(nombreUsuario);
    }

    public void setValoracionMedia(double valoracionMedia){
        this.Lbl_ValoracionMedia.setText(String.format("%.2f", valoracionMedia));
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }

    public void setController(Controller_HomeModerador controller){
        this.controller = controller;
    }
}

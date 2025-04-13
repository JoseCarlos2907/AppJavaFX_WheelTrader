package com.iesfernandoaguilar.perezgonzalez.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_Home implements Initializable {
    @FXML
    private Button Btn_Buscar;

    @FXML
    private Button Btn_ConfCuenta;

    @FXML
    private Button Btn_Filtros;

    @FXML
    private Button Btn_Notificaciones;

    @FXML
    private Button Btn_PublicarAnuncio;

    @FXML
    private ImageView ImgView_Coche1;

    @FXML
    private ImageView ImgView_Coche2;

    @FXML
    private Label Label_Coche1;

    @FXML
    private Label Label_Coche2;

    @FXML
    private TextField TxtF_BarraBusqueda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Bienvenido, " + Session.getUsuario().getNombreUsuario());
    }
    
}

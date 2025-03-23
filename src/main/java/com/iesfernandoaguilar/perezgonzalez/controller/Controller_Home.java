package com.iesfernandoaguilar.perezgonzalez.controller;

import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_Home implements Initializable {

    private Socket socket;

    @FXML
    private Label Lbl_Bienvenido;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Lbl_Bienvenido.setText("Bienvenido, " + Session.getUsuario().getNombreUsuario());
    }
    
    public void setSocket(Socket socket){
        this.socket = socket;
    }
}

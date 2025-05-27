package com.iesfernandoaguilar.perezgonzalez.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller_Compra {

    @FXML
    private Label Lbl_Fecha;

    @FXML
    private Label Lbl_MarcaModelo;

    @FXML
    private Label Lbl_Usuario;

    @FXML
    private Label Lbl_Precio;

    public void setFecha(String fechaFinGarantia){
        this.Lbl_Fecha.setText("La garantía acaba el " + fechaFinGarantia);
    }

    public void setUsuario(String nombreUsuario){
        this.Lbl_Usuario.setText("Comprado a " + nombreUsuario);
    }

    public void setMarcaModelo(String marca, String modelo){
        this.Lbl_MarcaModelo.setText(marca + " | "+ modelo);
    }

    public void setPrecio(Double precio){
        this.Lbl_Precio.setText(String.format("%.2f€", precio));
    }
}

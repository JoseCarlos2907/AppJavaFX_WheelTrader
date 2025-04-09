package com.iesfernandoaguilar.perezgonzalez.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class Controller_Reporte {
    @FXML
    private Label Lbl_Explicacion;

    @FXML
    private Label Lbl_Motivo;

    @FXML
    private Label Lbl_UsuarioReporta;

    @FXML
    private Label Lbl_UsuarioReportado;
    
    public void setUsuarioReporta(String nombreUsuario){
        this.Lbl_UsuarioReporta.setText("De: " + nombreUsuario);
    }

    public void setUsuarioReportado(String nombreUsuario){
        this.Lbl_UsuarioReportado.setText("A: " + nombreUsuario);
    }

    public void setMotivo(String motivo){
        this.Lbl_Motivo.setText(motivo);
    }

    public void setExplicacion(String explicacion){
        this.Lbl_Explicacion.setText(explicacion);
    }

}

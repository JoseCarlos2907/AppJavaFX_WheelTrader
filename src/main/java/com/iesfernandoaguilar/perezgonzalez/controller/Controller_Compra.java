package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Compra {
    private static final DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @FXML
    private Button Btn_Incidencia;

    @FXML
    private ImageView ImgView_Vehiculo;

    @FXML
    private Label Lbl_Fecha;

    @FXML
    private Label Lbl_MarcaModelo;

    @FXML
    private Label Lbl_Precio;

    @FXML
    private Label Lbl_Usuario;

    private LocalDateTime fechaFinGarantia;

    @FXML
    void handleBtnIncidenciaAction(MouseEvent event) throws IOException {
        if(LocalDateTime.now().isAfter(fechaFinGarantia)){
            // TODO: Alert de que se ha acabado la garantía
        }else{
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RegistrarIncidencia.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            stage.show();

            Controller_RegistrarIncidencia controller = loader.getController();
            controller.setTituloIncidencia(new String(this.Lbl_MarcaModelo.getText()));

            Stage stage2 = (Stage) Btn_Incidencia.getScene().getWindow();
            stage2.close();
        }
    }

    public void setFecha(LocalDateTime fechaFinGarantia){
        this.fechaFinGarantia = fechaFinGarantia;
        LocalDateTime ahora = LocalDateTime.now();
        this.Lbl_Fecha.setText(ahora.isBefore(fechaFinGarantia)? "La garantía acaba el " + fechaFinGarantia.format(formatoFecha) : "La garantía acabó el " + fechaFinGarantia.format(formatoFecha));
    }

    public void setUsuario(String nombreUsuario){
        this.Lbl_Usuario.setText("Comprado a " + nombreUsuario);
    }

    public void setMarcaModelo(String marca, String modelo){
        this.Lbl_MarcaModelo.setText(marca + " | "+ modelo);
    }

    public void setPrecio(double precio){
        this.Lbl_Precio.setText(precio + "€");
    }
}

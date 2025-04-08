package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.model.Venta;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Controller_MisCompras implements Initializable{
    @FXML
    private Button Btn_Volver;

    @FXML
    private VBox VBox_Compras;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Venta> ventas = List.of(
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "CONTADO"),
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(20), "CONTADO"),
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(30), "CONTADO"),
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(40), "FINANCIADO"),
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(50), "FINANCIADO"),
            new Venta(LocalDateTime.now(), LocalDateTime.now().plusDays(60), "FINANCIADO")
        );

        for (Venta venta : ventas) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Compra.fxml"));
                Parent componente = loader.load();

                Controller_Compra controller = loader.getController();
                controller.setFecha(venta.getFechaFinGarantia());
                controller.setMarcaModelo("Marca", "Modelo");   // En base al idAnuncio también
                controller.setUsuario("JoseCarlos2907");    // En base al idVendedor
                controller.setPrecio(99999);    // En base al idAnuncio

                this.VBox_Compras.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {

    }
}

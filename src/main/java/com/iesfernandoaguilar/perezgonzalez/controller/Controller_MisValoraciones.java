package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.model.Valoracion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Controller_MisValoraciones implements Initializable{
    @FXML
    private Button Btn_Volver;

    @FXML
    private Label Lbl_ValoracionMedia;

    @FXML
    private VBox VBox_Valoraciones;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        int cantValoraciones = 7;   // Cantidad total de valoraciones
        int total = 31;   // Suma total de las valoraciones

        List<Valoracion> valoraciones = List.of(
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo"),
            new Valoracion(5, "Pedazo de comentario primo"),
            new Valoracion(4, "Pedazo de comentario primo")
        );

        for (Valoracion valoracion : valoraciones) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Valoracion.fxml"));
                Parent componente = loader.load();

                Controller_Valoracion controller = loader.getController();
                controller.setComentario(valoracion.getComentario());
                controller.setValoracion(valoracion.getValoracion());
                controller.setUsuario("JoseCarlos2907");    // En base al idValorador

                this.VBox_Valoraciones.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        double valoracionMedia = total / (double) cantValoraciones;
        this.Lbl_ValoracionMedia.setText(String.format("%.2f", valoracionMedia));
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {

    }
}

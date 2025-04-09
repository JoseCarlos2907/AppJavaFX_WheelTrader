package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.model.Reporte;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_Reportes implements Initializable{
    @FXML
    private Button Btn_Volver;

    @FXML
    private Label Lbl_Titulo;

    @FXML
    private VBox VBox_Reportes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Reporte> reportes = List.of(
            new Reporte("Comentario inadecuado", "Me ha insultado en un comentario de una valoración"),
            new Reporte("Anuncio inadecuado", "Está intentando vender algo que no es adecuado en esta plataforma"),
            new Reporte("Comentario inadecuado", "Me ha insultado en un comentario de una valoración"),
            new Reporte("Anuncio inadecuado", "Está intentando vender algo que no es adecuado en esta plataforma"),
            new Reporte("Comentario inadecuado", "Me ha insultado en un comentario de una valoración")
        );

        for (Reporte reporte : reportes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Reporte.fxml"));
                Parent componente = loader.load();

                Controller_Reporte controller = loader.getController();
                controller.setMotivo(reporte.getMotivo());
                controller.setExplicacion(reporte.getExplicacion());
                controller.setUsuarioReporta("JoseCarlos2907");    // En base al idUsuarioReporta
                controller.setUsuarioReportado("JoseCarlos2908");    // En base al idUsuarioReportado

                this.VBox_Reportes.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        Parent nuevaVista = loader.load();
        
        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void setTitulo(String titulo){
        this.Lbl_Titulo.setText(titulo);
    }
}

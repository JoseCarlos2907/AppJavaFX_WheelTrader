package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro2;
import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RegistrarIncidencia implements Initializable {
    @FXML
    private Button Btn_RegistrarIncidencia;

    @FXML
    private Button Btn_Volver;

    @FXML
    private Label Lbl_Titulo;

    @FXML
    private TextArea TxtA_Desccripcion;

    private Notificacion notificacion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.notificacion = new Notificacion("", "", "PENDIENTE", "INCIDENCIA");
    }

    @FXML
    void handleBtnRegistrarIncidenciaAction(MouseEvent event) {
        // TODO: Registrar la incidencia en el servidor y redirigir al home
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_MisCompras.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void setTituloIncidencia(String marcaModelo){
        String titulo = String.format("Incidencia sobre la compra de %s", marcaModelo);
        this.Lbl_Titulo.setText(titulo);
        this.notificacion.setTitulo(titulo);
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller_Filtros implements Initializable{
    @FXML
    private Button Btn_Buscar;

    @FXML
    private Button Btn_ConfCuenta;

    @FXML
    private ChoiceBox<String> CB_Categoria;

    @FXML
    private ChoiceBox<String> CB_TipoCombustible;

    @FXML
    private ChoiceBox<String> CB_TipoMarchas;

    @FXML
    private TextField TxtF_Anio;

    @FXML
    private TextField TxtF_CV;

    @FXML
    private TextField TxtF_CantMarchas;

    @FXML
    private TextField TxtF_KmMax;

    @FXML
    private TextField TxtF_KmMin;

    @FXML
    private TextField TxtF_Marca;

    @FXML
    private TextField TxtF_Modelo;

    @FXML
    private TextField TxtF_NPuertas;

    @FXML
    private TextField TxtF_Plazas;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.CB_Categoria.getItems().addAll("Categoría", "Coche", "Moto", "Furgoneta", "Camión", "Caravana", "Maquinaria");
        this.CB_Categoria.setValue("Categoría");

        this.CB_TipoMarchas.getItems().addAll("Tipo de marchas", "Manual", "Automático");
        this.CB_TipoMarchas.setValue("Tipo de marchas");

        this.CB_TipoCombustible.getItems().addAll("Tipo de combustible", "Gasolina", "Diesel", "Híbrido", "Eléctrico");
        this.CB_TipoCombustible.setValue("Tipo de combustible");
    }

    @FXML
    void handleBtnBuscarAction(MouseEvent event) {
        // TODO: Petición al server para buscar una lista de anuncios con las condiciones
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {
        // TODO: Volver a la ventana Home
    }
}

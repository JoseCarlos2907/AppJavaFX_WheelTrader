package com.iesfernandoaguilar.perezgonzalez.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class Controller_PublicarAnuncio {
    
    @FXML
    private Button Btn_Volver;

    @FXML
    void handleBtnVolverAction(MouseEvent event) {

    }
    
    
    // Publicar Coche
    @FXML
    private Button Btn_Publicar_Coche;
    
    @FXML
    private ChoiceBox<String> CB_Coche_TipoCombustible;

    @FXML
    private ChoiceBox<String> CB_Coche_Transmision;

    @FXML
    private TextField TxtF_Coche_Anio;

    @FXML
    private TextField TxtF_Coche_CV;

    @FXML
    private TextField TxtF_Coche_CantMarchas;

    @FXML
    private TextField TxtF_Coche_KM;

    @FXML
    private TextField TxtF_Coche_Marca;

    @FXML
    private TextField TxtF_Coche_Matricula;

    @FXML
    private TextField TxtF_Coche_Modelo;

    @FXML
    private TextField TxtF_Coche_NBastidor;

    @FXML
    private TextField TxtF_Coche_NPuertas;

    @FXML
    void handleBtnPublicarCocheAction(MouseEvent event) {

    }


    // Publicar Moto
    @FXML
    private Button Btn_Publicar_Moto;
    
    @FXML
    private ChoiceBox<String> CB_Moto_TipoCombustible;

    @FXML
    private TextField TxtF_Moto_Anio;

    @FXML
    private TextField TxtF_Moto_CV;

    @FXML
    private TextField TxtF_Moto_CantMarchas;

    @FXML
    private TextField TxtF_Moto_KM;

    @FXML
    private TextField TxtF_Moto_Marca;

    @FXML
    private TextField TxtF_Moto_Matricula;

    @FXML
    private TextField TxtF_Moto_Modelo;

    @FXML
    private TextField TxtF_Moto_NBastidor;
    
    @FXML
    void handleBtnPublicarMotoAction(MouseEvent event) {

    }


    // Publicar Camioneta
    @FXML
    private Button Btn_Publicar_Camioneta;
    
    @FXML
    private ChoiceBox<String> CB_Camioneta_TipoCombustible;

    @FXML
    private ChoiceBox<String> CB_Camioneta_TipoTraccion;

    @FXML
    private TextField TxtF_Camioneta_Anio;

    @FXML
    private TextField TxtF_Camioneta_CV;

    @FXML
    private TextField TxtF_Camioneta_CantMarchas;

    @FXML
    private TextField TxtF_Camioneta_CargaMax;

    @FXML
    private TextField TxtF_Camioneta_KM;

    @FXML
    private TextField TxtF_Camioneta_Marca;

    @FXML
    private TextField TxtF_Camioneta_Matricula;

    @FXML
    private TextField TxtF_Camioneta_Modelo;

    @FXML
    private TextField TxtF_Camioneta_NBastidor;

    @FXML
    private TextField TxtF_Camioneta_NPuertas;

    @FXML
    void handleBtnPublicarCamionetaAction(MouseEvent event) {

    }


    // Publicar Camión
    @FXML
    private Button Btn_Publicar_Camion;
    
    @FXML
    private TextField TxtF_Camion_Anio;

    @FXML
    private TextField TxtF_Camion_CV;

    @FXML
    private TextField TxtF_Camion_CantMarchas;

    @FXML
    private TextField TxtF_Camion_CargaMax;

    @FXML
    private TextField TxtF_Camion_KM;

    @FXML
    private TextField TxtF_Camion_Marca;

    @FXML
    private TextField TxtF_Camion_Matricula;

    @FXML
    private TextField TxtF_Camion_Modelo;

    @FXML
    private TextField TxtF_Camion_NBastidor;

    @FXML
    void handleBtnPublicarCamionAction(MouseEvent event) {

    }


    // Publicar Maquinaria
    @FXML
    private Button Btn_Publicar_Maquinaria;
    
    @FXML
    private ChoiceBox<String> CB_Maquinaria_TIpoCombustible;

    @FXML
    private TextField TxtF_Maquinaria_Anio;

    @FXML
    private TextField TxtF_Maquinaria_Marca;

    @FXML
    private TextField TxtF_Maquinaria_Modelo;

    @FXML
    private TextField TxtF_Maquinaria_NSerieBastidor;

    @FXML
    void handleBtnPublicarMaquinariaAction(MouseEvent event) {

    }
}

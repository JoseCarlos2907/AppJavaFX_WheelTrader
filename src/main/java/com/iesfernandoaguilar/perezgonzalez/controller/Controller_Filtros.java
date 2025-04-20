package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroTodo;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_Filtros implements IApp, Initializable{
    private Lector_App hiloLector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.CB_Coche_TipoCombustible.getItems().addAll("Gasolina","Electrico","Diesel","Hibrido");
        this.CB_Coche_TipoCombustible.setValue("Tipo Combustible");

        this.CB_Coche_Transmision.getItems().addAll("Manual","Automatica");
        this.CB_Coche_Transmision.setValue("Transmision");

        this.CB_Moto_TipoCombustible.getItems().addAll("Gasolina","Electrico");
        this.CB_Moto_TipoCombustible.setValue("Tipo Combustible");

        this.CB_Camioneta_TipoTraccion.getItems().addAll("4x2","4x4");
        this.CB_Camioneta_TipoTraccion.setValue("Tipo Traccion");

        this.CB_Camioneta_TipoCombustible.getItems().addAll("Gasoleo","Electrico");
        this.CB_Camioneta_TipoCombustible.setValue("Tipo Combustible");

        this.CB_Camion_TipoCombustible.getItems().addAll("Diesel","Electrico","GNL","Hidrogeno");
        this.CB_Camion_TipoCombustible.setValue("Tipo Combustible");

        this.CB_Maquinaria_TipoCombustible.getItems().addAll("Diesel","Gasolina","Electrico","GLP");
        this.CB_Maquinaria_TipoCombustible.setValue("Tipo Combustible");
    }

    @FXML
    private Button Btn_Volver;

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }


    @FXML
    private CheckBox CB_Coche;
    
    @FXML
    private CheckBox CB_Moto;
    
    @FXML
    private CheckBox CB_Camioneta;

    @FXML
    private CheckBox CB_Camion;

    @FXML
    private CheckBox CB_Maquinaria;

    @FXML
    private TextField TxtF_Todo_AnioMax;

    @FXML
    private TextField TxtF_Todo_AnioMin;

    @FXML
    private TextField TxtF_Todo_Ciudad;

    @FXML
    private TextField TxtF_Todo_Marca;

    @FXML
    private TextField TxtF_Todo_Modelo;

    @FXML
    private TextField TxtF_Todo_PrecioMax;

    @FXML
    private TextField TxtF_Todo_PrecioMin;

    @FXML
    private TextField TxtF_Todo_Provincia;

    @FXML
    private Button Btn_Buscar_Todo;

    @FXML
    void handleBtnBuscarTodoAction(MouseEvent event) throws IOException {
        FiltroTodo filtro = new FiltroTodo();

        if(CB_Coche.isSelected()){
            filtro.addTipoVehiculo("Coche");
        }

        if(CB_Moto.isSelected()){
            filtro.addTipoVehiculo("Moto");
        }

        if(CB_Camioneta.isSelected()){
            filtro.addTipoVehiculo("Camioneta");
        }

        if(CB_Camion.isSelected()){
            filtro.addTipoVehiculo("Camion");
        }

        if(CB_Maquinaria.isSelected()){
            filtro.addTipoVehiculo("Coche");
        }

        if(filtro.getTiposVehiculo().size() < 1){
            filtro.addTipoVehiculo("Coche");
            filtro.addTipoVehiculo("Moto");
            filtro.addTipoVehiculo("Camioneta");
            filtro.addTipoVehiculo("Camion");
            filtro.addTipoVehiculo("Coche");
        }

        String anioMax = new String(this.TxtF_Todo_AnioMax.getText());
        String anioMin = new String(this.TxtF_Todo_AnioMin.getText());
        String precioMin = new String(this.TxtF_Todo_PrecioMin.getText());
        String precioMax = new String(this.TxtF_Todo_PrecioMax.getText());


        filtro.setMarca(new String(this.TxtF_Todo_Marca.getText()));
        filtro.setModelo(new String(this.TxtF_Todo_Modelo.getText()));
        filtro.setAnioMaximo(anioMax.isEmpty() ? 2025 : Integer.valueOf(anioMax));
        filtro.setAnioMinimo(anioMin.isEmpty() ? 1950 : Integer.valueOf(anioMin));
        filtro.setProvincia(new String(this.TxtF_Todo_Provincia.getText()));
        filtro.setCiudad(new String(this.TxtF_Todo_Ciudad.getText()));
        filtro.setPrecioMinimo(precioMin.isEmpty() ? 0 : Integer.valueOf(precioMin));
        filtro.setPrecioMaximo(precioMax.isEmpty() ? Double.MAX_VALUE : Integer.valueOf(precioMax));
        filtro.setPagina(0);
        filtro.setCantidadPorPagina(10);

        this.buscar(filtro);
    }


    @FXML
    private ChoiceBox<String> CB_Coche_TipoCombustible;

    @FXML
    private ChoiceBox<String> CB_Coche_Transmision;

    @FXML
    private TextField TxtF_Coche_AnioMaximo;

    @FXML
    private TextField TxtF_Coche_AnioMinimo;

    @FXML
    private TextField TxtF_Coche_CVMaximo;

    @FXML
    private TextField TxtF_Coche_CVMinimo;

    @FXML
    private TextField TxtF_Coche_CantMarchas;

    @FXML
    private TextField TxtF_Coche_Ciudad;

    @FXML
    private TextField TxtF_Coche_KMMaximo;

    @FXML
    private TextField TxtF_Coche_KMMinimo;

    @FXML
    private TextField TxtF_Coche_Marca;

    @FXML
    private TextField TxtF_Coche_Modelo;

    @FXML
    private TextField TxtF_Coche_NPuertas;

    @FXML
    private TextField TxtF_Coche_Provincia;

    @FXML
    private Button Btn_Buscar_Coche;

    @FXML
    void handleBtnBuscarCocheAction(MouseEvent event) {

    }


    @FXML
    private ChoiceBox<String> CB_Moto_TipoCombustible;

    @FXML
    private TextField TxtF_Moto_AnioMaximo;

    @FXML
    private TextField TxtF_Moto_AnioMinimo;

    @FXML
    private TextField TxtF_Moto_CVMaximo;

    @FXML
    private TextField TxtF_Moto_CVMinimo;

    @FXML
    private TextField TxtF_Moto_CantMarchas;

    @FXML
    private TextField TxtF_Moto_Ciudad;

    @FXML
    private TextField TxtF_Moto_KMMaximo;

    @FXML
    private TextField TxtF_Moto_KMMinimo;

    @FXML
    private TextField TxtF_Moto_Marca;

    @FXML
    private TextField TxtF_Moto_Modelo;

    @FXML
    private TextField TxtF_Moto_Provincia;

    @FXML
    private Button Btn_Buscar_Moto;

    @FXML
    void handleBtnBuscarMotoAction(MouseEvent event) {

    }


    @FXML
    private ChoiceBox<String> CB_Camioneta_TipoCombustible;

    @FXML
    private ChoiceBox<String> CB_Camioneta_TipoTraccion;

    @FXML
    private TextField TxtF_Camioneta_AnioMaximo;
    
    @FXML
    private TextField TxtF_Camioneta_AnioMinimo;

    @FXML
    private TextField TxtF_Camioneta_CVMaximo;

    @FXML
    private TextField TxtF_Camioneta_CVMinimo;

    @FXML
    private TextField TxtF_Camioneta_CantMarchas;

    @FXML
    private TextField TxtF_Camioneta_Ciudad;

    @FXML
    private TextField TxtF_Camioneta_KMMaximo;

    @FXML
    private TextField TxtF_Camioneta_KMMinimo;

    @FXML
    private TextField TxtF_Camioneta_Marca;

    @FXML
    private TextField TxtF_Camioneta_Modelo;

    @FXML
    private TextField TxtF_Camioneta_NPuertas;

    @FXML
    private TextField TxtF_Camioneta_Provincia;

    @FXML
    private Button Btn_Buscar_Camioneta;

    @FXML
    void handleBtnBuscarCamionetaAction(MouseEvent event) {

    }


    @FXML
    private ChoiceBox<String> CB_Camion_TipoCombustible;
    
    @FXML
    private TextField TxtF_Camion_AnioMaximo;
    
    @FXML
    private TextField TxtF_Camion_AnioMinimo;

    @FXML
    private TextField TxtF_Camion_CVMaximo;

    @FXML
    private TextField TxtF_Camion_CVMinimo;
    
    @FXML
    private TextField TxtF_Camion_CantMarchas;
    
    @FXML
    private TextField TxtF_Camion_Ciudad;
    
    @FXML
    private TextField TxtF_Camion_KMMinimo;
    
    @FXML
    private TextField TxtF_Camion_Marca;
    
    @FXML
    private TextField TxtF_Camion_Modelo;
    
    @FXML
    private TextField TxtF_Camion_Provincia;

    @FXML
    private Button Btn_Buscar_Camion;

    @FXML
    void handleBtnBuscarCamionAction(MouseEvent event) {

    }


    @FXML
    private ChoiceBox<String> CB_Maquinaria_TipoCombustible;

    @FXML
    private TextField TxtF_Maquinaria_AnioMaximo;

    @FXML
    private TextField TxtF_Maquinaria_AnioMinimo;

    @FXML
    private TextField TxtF_Maquinaria_Ciudad;

    @FXML
    private TextField TxtF_Maquinaria_Marca;

    @FXML
    private TextField TxtF_Maquinaria_Modelo;

    @FXML
    private TextField TxtF_Maquinaria_Provincia;

    @FXML
    private Button Btn_Buscar_Maquinaria;

    @FXML
    void handleBtnBuscarMaquinariaAction(MouseEvent event) {

    }


    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void buscar(IFiltro filtro) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_ListaAnuncios.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_ListaAnuncios controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);
        controller.setFiltro(filtro);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

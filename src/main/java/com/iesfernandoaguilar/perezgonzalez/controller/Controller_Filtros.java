package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroCamion;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroCamioneta;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroCoche;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroMaquinaria;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroMoto;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroTodo;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

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

    private DataOutputStream dos;

    private IFiltro filtro;

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

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
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
            filtro.addTipoVehiculo("Maquinaria");
        }

        String marca = new String(this.TxtF_Todo_Marca.getText());
        String modelo = new String(this.TxtF_Todo_Modelo.getText());
        String anioMax = new String(this.TxtF_Todo_AnioMax.getText());
        String anioMin = new String(this.TxtF_Todo_AnioMin.getText());
        String provincia = new String(this.TxtF_Todo_Provincia.getText());
        String ciudad = new String(this.TxtF_Todo_Ciudad.getText());
        String precioMin = new String(this.TxtF_Todo_PrecioMin.getText());
        String precioMax = new String(this.TxtF_Todo_PrecioMax.getText());


        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setAnioMinimo(anioMin.isEmpty() ? 1950 : Integer.valueOf(anioMin));
        filtro.setAnioMaximo(anioMax.isEmpty() ? 2025 : Integer.valueOf(anioMax));
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setPrecioMinimo(precioMin.isEmpty() ? 0 : Double.valueOf(precioMin));
        filtro.setPrecioMaximo(precioMax.isEmpty() ? Double.MAX_VALUE : Double.valueOf(precioMax));
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
    void handleBtnBuscarCocheAction(MouseEvent event) throws IOException {
        FiltroCoche filtro = new FiltroCoche();

        String tipoCombustible = new String(this.CB_Coche_TipoCombustible.getValue());
        String transmision = new String(this.CB_Coche_Transmision.getValue());
        String marca = new String(this.TxtF_Coche_Marca.getText());
        String modelo = new String(this.TxtF_Coche_Modelo.getText());
        String provincia = new String(this.TxtF_Coche_Provincia.getText());
        String ciudad = new String(this.TxtF_Coche_Ciudad.getText());
        String anioMinimo = new String(this.TxtF_Coche_AnioMinimo.getText());
        String anioMaximo = new String(this.TxtF_Coche_AnioMaximo.getText());
        String cvMinimo = new String(this.TxtF_Coche_CVMinimo.getText());
        String cvMaximo = new String(this.TxtF_Coche_CVMaximo.getText());
        String cantMarchas = new String(this.TxtF_Coche_CantMarchas.getText());
        String kmMinimo = new String(this.TxtF_Coche_KMMinimo.getText());
        String kmMaximo = new String(this.TxtF_Coche_KMMaximo.getText());
        String nPuertas = new String(this.TxtF_Coche_NPuertas.getText());

        filtro.setTipoCombustible("Tipo Combustible".equals(tipoCombustible) ? null : tipoCombustible);
        filtro.setTransmision("Transmision".equals(transmision) ? null : transmision);
        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setAnioMinimo(anioMinimo.isEmpty() ? 1950 : Integer.valueOf(anioMinimo));
        filtro.setAnioMaximo(anioMaximo.isEmpty() ? 2025 : Integer.valueOf(anioMaximo));
        filtro.setCantMarchas(cantMarchas.isEmpty() ? 0 : Integer.valueOf(cantMarchas));
        filtro.setnPuertas(nPuertas.isEmpty() ? 0 : Integer.valueOf(nPuertas));
        filtro.setCvMinimo(cvMinimo.isEmpty() ? 40 : Integer.valueOf(cvMinimo));
        filtro.setCvMaximo(cvMaximo.isEmpty() ? 1500 : Integer.valueOf(cvMaximo));
        filtro.setKmMinimo(kmMinimo.isEmpty() ? 0 : Integer.valueOf(kmMinimo));
        filtro.setKmMaximo(kmMaximo.isEmpty() ? 2000000 : Integer.valueOf(kmMaximo));
        filtro.setnPuertas(nPuertas.isEmpty() ? 0 : Integer.valueOf(nPuertas));
        filtro.setPagina(0);
        filtro.setCantidadPorPagina(10);

        this.buscar(filtro);
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
    void handleBtnBuscarMotoAction(MouseEvent event) throws IOException {
        FiltroMoto filtro = new FiltroMoto();

        String tipoCombustible = new String(this.CB_Moto_TipoCombustible.getValue());
        String anioMinimo = new String(this.TxtF_Moto_AnioMinimo.getText());
        String anioMaximo = new String(this.TxtF_Moto_AnioMaximo.getText());
        String cvMinimo = new String(this.TxtF_Moto_CVMinimo.getText());
        String cvMaximo = new String(this.TxtF_Moto_CVMaximo.getText());
        String cantMarchas = new String(this.TxtF_Moto_CantMarchas.getText());
        String ciudad = new String(this.TxtF_Moto_Ciudad.getText());
        String kmMinimo = new String(this.TxtF_Moto_KMMinimo.getText());
        String kmMaximo = new String(this.TxtF_Moto_KMMaximo.getText());
        String marca = new String(this.TxtF_Moto_Marca.getText());
        String modelo = new String(this.TxtF_Moto_Modelo.getText());
        String provincia = new String(this.TxtF_Moto_Provincia.getText());

        filtro.setTipoCombustible("Tipo Combustible".equals(tipoCombustible) ? null : tipoCombustible);
        filtro.setAnioMinimo(anioMinimo.isEmpty() ? 1950 : Integer.parseInt(anioMinimo));
        filtro.setAnioMaximo(anioMaximo.isEmpty() ? 2025 : Integer.parseInt(anioMaximo));
        filtro.setCvMinimo(cvMinimo.isEmpty() ? 50 : Integer.parseInt(cvMinimo));
        filtro.setCvMaximo(cvMaximo.isEmpty() ? 2000 : Integer.parseInt(cvMaximo));
        filtro.setCantMarchas(cantMarchas.isEmpty() ? 0 : Integer.parseInt(cantMarchas));
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setKmMinimo(kmMinimo.isEmpty() ? 0 : Integer.parseInt(kmMinimo));
        filtro.setKmMaximo(kmMaximo.isEmpty() ? 1000000 : Integer.parseInt(kmMaximo));
        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setPagina(0);
        filtro.setCantidadPorPagina(10);
        
        this.buscar(filtro);
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
    void handleBtnBuscarCamionetaAction(MouseEvent event) throws IOException {
        FiltroCamioneta filtro = new FiltroCamioneta();

        String tipoCombustible = new String(this.CB_Camioneta_TipoCombustible.getValue());
        String traccion = new String(this.CB_Camioneta_TipoTraccion.getValue());
        String anioMinimo = new String(this.TxtF_Camioneta_AnioMinimo.getText());
        String anioMaximo = new String(this.TxtF_Camioneta_AnioMaximo.getText());
        String cvMinimo = new String(this.TxtF_Camioneta_CVMinimo.getText());
        String cvMaximo = new String(this.TxtF_Camioneta_CVMaximo.getText());
        String cantMarchas = new String(this.TxtF_Camioneta_CantMarchas.getText());
        String ciudad = new String(this.TxtF_Camioneta_Ciudad.getText());
        String kmMinimo = new String(this.TxtF_Camioneta_KMMinimo.getText());
        String kmMaximo = new String(this.TxtF_Camioneta_KMMaximo.getText());
        String marca = new String(this.TxtF_Camioneta_Marca.getText());
        String modelo = new String(this.TxtF_Camioneta_Modelo.getText());
        String nPuertas = new String(this.TxtF_Camioneta_NPuertas.getText());
        String provincia = new String(this.TxtF_Camioneta_Provincia.getText());

        filtro.setTipoCombustible("Tipo Combustible".equals(tipoCombustible) ? null : tipoCombustible);
        filtro.setTraccion("Traccion".equals(traccion) ? null : traccion);
        filtro.setAnioMinimo(anioMinimo.isEmpty() ? 1950 : Integer.parseInt(anioMinimo));
        filtro.setAnioMaximo(anioMaximo.isEmpty() ? 2025 : Integer.parseInt(anioMaximo));
        filtro.setCvMinimo(cvMinimo.isEmpty() ? 50 : Integer.parseInt(cvMinimo));
        filtro.setCvMaximo(cvMaximo.isEmpty() ? 400 : Integer.parseInt(cvMaximo));
        filtro.setCantMarchas(cantMarchas.isEmpty() ? 0 : Integer.parseInt(cantMarchas));
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setKmMinimo(kmMinimo.isEmpty() ? 0 : Integer.parseInt(kmMinimo));
        filtro.setKmMaximo(kmMaximo.isEmpty() ? 1500000 : Integer.parseInt(kmMaximo));
        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setnPuertas(nPuertas.isEmpty() ? 0 : Integer.valueOf(nPuertas));
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setPagina(0);
        filtro.setCantidadPorPagina(10);

        this.buscar(filtro);
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
    private TextField TxtF_Camion_KMMaximo;
    
    @FXML
    private TextField TxtF_Camion_Marca;
    
    @FXML
    private TextField TxtF_Camion_Modelo;
    
    @FXML
    private TextField TxtF_Camion_Provincia;

    @FXML
    private Button Btn_Buscar_Camion;

    @FXML
    void handleBtnBuscarCamionAction(MouseEvent event) throws IOException {
        FiltroCamion filtro = new FiltroCamion();

        String tipoCombustible = new String(this.CB_Camion_TipoCombustible.getValue());
        String marca = new String(this.TxtF_Camion_Marca.getText());
        String modelo = new String(this.TxtF_Camion_Modelo.getText());
        String anioMaximo = new String(this.TxtF_Camion_AnioMaximo.getText());
        String anioMinimo = new String(this.TxtF_Camion_AnioMinimo.getText());
        String cvMinimo = new String(this.TxtF_Camion_CVMinimo.getText());
        String cvMaximo = new String(this.TxtF_Camion_CVMaximo.getText());
        String cantMarchas = new String(this.TxtF_Camion_CantMarchas.getText());
        String provincia = new String(this.TxtF_Camion_Provincia.getText());
        String ciudad = new String(this.TxtF_Camion_Ciudad.getText());
        String kmMinimo = new String(this.TxtF_Camion_KMMinimo.getText());
        String kmMaximo = new String(this.TxtF_Camion_KMMaximo.getText());

        filtro.setTipoCombustible("Tipo Combustible".equals(tipoCombustible) ? null : tipoCombustible);
        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setAnioMinimo(anioMinimo.isEmpty() ? 1950 : Integer.valueOf(anioMinimo));
        filtro.setAnioMaximo(anioMaximo.isEmpty() ? 2025 : Integer.valueOf(anioMaximo));
        filtro.setCvMinimo(cvMinimo.isEmpty() ? 100 : Integer.valueOf(cvMinimo));
        filtro.setCvMaximo(cvMaximo.isEmpty() ? 800 : Integer.valueOf(cvMaximo));
        filtro.setCantMarchas(cantMarchas.isEmpty() ? 0 : Integer.parseInt(cantMarchas));
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setKmMinimo(kmMinimo.isEmpty() ? 0 : Integer.parseInt(kmMinimo));
        filtro.setKmMaximo(kmMaximo.isEmpty() ? 2000000 : Integer.parseInt(kmMaximo));

        this.buscar(filtro);
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
    void handleBtnBuscarMaquinariaAction(MouseEvent event) throws IOException {
        FiltroMaquinaria filtro = new FiltroMaquinaria();

        String tipoCombustible = new String(this.CB_Maquinaria_TipoCombustible.getValue());
        String anioMinimo = new String(this.TxtF_Maquinaria_AnioMinimo.getText());
        String anioMaximo = new String(this.TxtF_Maquinaria_AnioMaximo.getText());
        String ciudad = new String(this.TxtF_Maquinaria_Ciudad.getText());
        String marca = new String(this.TxtF_Maquinaria_Marca.getText());
        String modelo = new String(this.TxtF_Maquinaria_Modelo.getText());
        String provincia = new String(this.TxtF_Maquinaria_Provincia.getText());

        filtro.setTipoCombustible("Tipo Combustible".equals(tipoCombustible) ? null : tipoCombustible);
        filtro.setAnioMinimo(anioMinimo.isEmpty() ? 1950 : Integer.parseInt(anioMinimo));
        filtro.setAnioMaximo(anioMaximo.isEmpty() ? 1950 : Integer.parseInt(anioMaximo));
        filtro.setCiudad(ciudad.isEmpty() ? null : ciudad);
        filtro.setMarca(marca.isEmpty() ? null : marca);
        filtro.setModelo(modelo.isEmpty() ? null : modelo);
        filtro.setProvincia(provincia.isEmpty() ? null : provincia);
        filtro.setPagina(0);
        filtro.setCantidadPorPagina(10);

        this.buscar(filtro);
    }


    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void buscar(IFiltro filtro) throws IOException{
        this.filtro = filtro;

        Mensaje msg = new Mensaje();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(this.filtro.getTipoFiltro());
        msg.addParam("si");

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void irListaPublicados(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_ListaAnuncios.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_ListaAnuncios controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes, true);
        controller.setFiltro(this.filtro);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

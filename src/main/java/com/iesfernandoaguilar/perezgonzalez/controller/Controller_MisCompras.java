package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.model.Venta;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_MisCompras implements IApp, Initializable{
    private Lector_App hiloLector;
    
    private List<Venta> compras;
    private boolean cargando;
    private FiltroPorNombreUsuario filtro;

    @FXML
    private Button Btn_Volver;

    @FXML
    private VBox VBox_Compras;

    @FXML
    private ScrollPane ScrollPane_Compras;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.compras = new ArrayList<>();

        this.ScrollPane_Compras.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargando) {
                cargando = true;
                try {
                    this.pedirCompras();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void pedirCompras() throws IOException{
        this.filtro.siguientePagina();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerVentas(filtroJSON, "no");
    }

    public void aniadirCompras(String notisJSON) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Venta> comprasNuevas = mapper.readValue(notisJSON, new TypeReference<List<Venta>>(){});

        if(comprasNuevas.size() < 1) {
            cargando = true;
            return;
        }
        
        this.compras.addAll(comprasNuevas);
        for (int i = 0; i < comprasNuevas.size(); i++) {
            Venta compra = comprasNuevas.get(i);

            String marca = "";
            String modelo = "";

            for (ValorCaracteristica vc : compra.getAnuncio().getValoresCaracteristicas()) {
                if(vc.getNombreCaracteristica().contains("Marca")){
                    marca = vc.getValor();
                }else if(vc.getNombreCaracteristica().contains("Modelo")){
                    modelo = vc.getValor();
                }
            }
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Compra.fxml"));
                Parent componente = loader.load();

                Controller_Compra controller = loader.getController();
                controller.setFecha(compra.getFechaFinGarantia());
                controller.setUsuario(compra.getAnuncio().getVendedor().getNombreUsuario());
                controller.setPrecio(compra.getAnuncio().getPrecio());
                controller.setMarcaModelo(marca, modelo);

                this.VBox_Compras.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.cargando = false;
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void setFiltro(FiltroPorNombreUsuario filtro){
        this.filtro = filtro;
    }
}

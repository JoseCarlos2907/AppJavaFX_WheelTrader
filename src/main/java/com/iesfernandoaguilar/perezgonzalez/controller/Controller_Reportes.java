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
import com.iesfernandoaguilar.perezgonzalez.model.Reporte;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroReportes;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_Reportes implements IApp, Initializable{

    private Lector_App hiloLector;

    private List<Reporte> reportes;
    private FiltroReportes filtro;
    private boolean cargando;
    
    @FXML
    private Button Btn_Volver;

    @FXML
    private Label Lbl_Titulo;

    @FXML
    private VBox VBox_Reportes;

    @FXML
    private ScrollPane ScrollPane_Reportes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.ScrollPane_Reportes.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargando) {
                cargando = true;
                try {
                    this.cargarReportes(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        Parent nuevaVista = loader.load();

        Controller_HomeModerador controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);
        
        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void setTitulo(String titulo){
        this.Lbl_Titulo.setText(titulo);
    }

    private void cargarReportes(boolean primeraCarga) throws IOException{
        if(primeraCarga){
            this.filtro = new FiltroReportes();
            this.reportes.clear();
            this.VBox_Reportes.getChildren().clear();
        }

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerUltimosReportesMod(filtroJSON);
        
        this.filtro.siguientePagina();
    }

    public void aniadirReportes(String reportesJSON) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Reporte> reportesNuevos = mapper.readValue(reportesJSON, new TypeReference<List<Reporte>>(){});

        this.reportes.addAll(reportesNuevos);

        for (Reporte reporte : reportes) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Reporte.fxml"));
                Parent componente = loader.load();

                Controller_Reporte controller = loader.getController();
                controller.setMotivo(reporte.getMotivo());
                controller.setExplicacion(reporte.getExplicacion());
                controller.setUsuarioReporta(reporte.getUsuarioEnvia().getNombreUsuario());
                controller.setUsuarioReportado(reporte.getUsuarioRecibe().getNombreUsuario());

                this.VBox_Reportes.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.cargando = false;
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;

        this.filtro = new FiltroReportes();
        this.reportes = new ArrayList<>();
        
        try {
            this.cargarReportes(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_PublicarAnuncio implements IApp, Initializable{

    private Anuncio anuncio;
    private DataOutputStream dos;
    private Lector_App hiloLector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.anuncio = new Anuncio();

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

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }
    
    @FXML
    private Button Btn_Volver;

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        this.anuncio = null;

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
    
    
    // Publicar Coche
    @FXML
    private Button Btn_Siguiente_Coche;
    
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
    void handleBtnSiguienteCocheAction(MouseEvent event) throws IOException {
        String matricula = new String(this.TxtF_Coche_Matricula.getText());
        String numBastidor = new String(this.TxtF_Coche_NBastidor.getText());

        if(!matricula.matches("^\\d{4}[A-Z]{1,3}$")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato de matrícula errónea");
            alert.setHeaderText(null);
            alert.setContentText("El formato de la matrícula no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else if(numBastidor.length() != 13){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato de número de bastidor");
            alert.setHeaderText(null);
            alert.setContentText("El formato del número de bastidor no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else{
            this.anuncio.setTipoVehiculo("Coche");
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_Marca.getText()), "Marca_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_Modelo.getText()), "Modelo_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_CV.getText()), "CV_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_Anio.getText()), "Anio_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_KM.getText()), "KM_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.CB_Coche_TipoCombustible.getValue()), "TipoCombustible_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.CB_Coche_Transmision.getValue()), "Transmision_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_CantMarchas.getText()), "Marchas_Coche"));
            this.anuncio.addValorCaracteristica(new ValorCaracteristica(new String(this.TxtF_Coche_NPuertas.getText()), "Puertas_Coche"));
            this.anuncio.setMatricula(matricula);
            this.anuncio.setNumSerieBastidor(numBastidor);
            
            Mensaje msg = new Mensaje();
            msg.setTipo("COMPROBAR_DATOS_VEHICULO");
            
            ObjectMapper mapper = new ObjectMapper();
            String valoresAnuncioJSON = mapper.writeValueAsString(this.anuncio.getValoresCaracteristicas());
            msg.addParam(valoresAnuncioJSON);

            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }

    }


    // Publicar Moto
    @FXML
    private Button Btn_Siguiente_Moto;
    
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
    void handleBtnSiguienteMotoAction(MouseEvent event) {

    }


    // Publicar Camioneta
    @FXML
    private Button Btn_Siguiente_Camioneta;
    
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
    void handleBtnSiguienteCamionetaAction(MouseEvent event) {

    }


    // Publicar Camión
    @FXML
    private ChoiceBox<String> CB_Camion_TipoCombustible;

    @FXML
    private Button Btn_Siguiente_Camion;
    
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
    void handleBtnSiguienteCamionAction(MouseEvent event) {

    }


    // Publicar Maquinaria
    @FXML
    private Button Btn_Siguiente_Maquinaria;
    
    @FXML
    private ChoiceBox<String> CB_Maquinaria_TipoCombustible;

    @FXML
    private TextField TxtF_Maquinaria_Anio;

    @FXML
    private TextField TxtF_Maquinaria_Marca;

    @FXML
    private TextField TxtF_Maquinaria_Modelo;

    @FXML
    private TextField TxtF_Maquinaria_NSerieBastidor;

    @FXML
    void handleBtnSiguienteMaquinariaAction(MouseEvent event) {

    }

    public void siguientePaso() throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PublicarAnuncio2.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_PublicarAnuncio2 controller = loader.getController();
        controller.setAnuncio(anuncio);
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void datosIncorrectos(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Datos del vehículo incompletos");
        alert.setHeaderText(null);
        alert.setContentText("Faltan datos del vehículo de los campos obligatorios");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();

        this.anuncio = new Anuncio();
    }
}

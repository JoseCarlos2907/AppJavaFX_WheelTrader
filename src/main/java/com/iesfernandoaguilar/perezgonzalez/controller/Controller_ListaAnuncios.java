package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
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
import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller_ListaAnuncios implements IApp, Initializable, IListaAnuncios{
    private Lector_App hiloLector;

    private DataOutputStream dos;

    private List<Anuncio> anuncios;
    private IFiltro filtro;

    @FXML
    private Button Btn_Volver;

    @FXML
    private Button Btn_Filtros;

    @FXML
    private Rectangle Rectangle_Fondo;

    @FXML
    private TextField TxtF_BarraBusqueda;

    @FXML
    private VBox VBox_Anuncios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.anuncios = new ArrayList<>();

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Hacer el listener de cuando el scroll llega casi al final
    }

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

    public void abrirAnuncio(Anuncio anuncio){
        // try {
        //     FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
        //     Parent nuevaVista;
        //     nuevaVista = loader.load();
            
        //     Controller_DetalleAnuncio controller = loader.getController();
        //     controller.setAnuncio(anuncio);
        //     controller.setController(this);
        //     controller.setMarcaModelo(anuncio.getMarca(), anuncio.getModelo());
        //     controller.setCategoria(anuncio.getCategoria());
        //     controller.setDescripcion(anuncio.getDescripcion());
        //     controller.setPrecio(anuncio.getPrecioAlContado(), anuncio.getPrecioMensual());
        //     controller.setDatos(anuncio.getCv(), anuncio.getColor(), anuncio.getPuertas(), anuncio.getAnio(), anuncio.getTipoMarchas(), anuncio.getCantMarchas(), anuncio.getKilometraje(), anuncio.getCombustible());
        //     controller.setFiltro(this.filtro);

        //     Stage stage = (Stage) Btn_Filtros.getScene().getWindow();
    
        //     Scene scene = new Scene(nuevaVista);
    
        //     stage.setScene(scene);
        //     stage.show();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }

    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void setFiltro(IFiltro filtro){
        this.filtro = filtro;
    }

    public void pedirAnuncios() throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ANUNCIOS");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        msg.addParam(filtroJSON);
        msg.addParam(this.filtro.getTipoFiltro());
        msg.addParam("no");
        msg.addParam(String.valueOf(Session.getUsuario().getNombreUsuario()));
        

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas, boolean primeraCarga) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Anuncio> anunciosNuevos = mapper.readValue(anunciosJSON, new TypeReference<List<Anuncio>>(){});

        this.anuncios.addAll(anunciosNuevos);
        for (int i = 0; i < anunciosNuevos.size(); i++) {
            Anuncio anuncio = anunciosNuevos.get(i);

            String marca = "";
            String modelo = "";
            String anio = "";
            String km = "";

            for (ValorCaracteristica valor : anuncio.getValoresCaracteristicas()) {
                if(valor.getNombreCaracteristica().contains("Anio_")){
                    anio = valor.getValor();
                }else if(valor.getNombreCaracteristica().contains("KM_")){
                    km = valor.getValor();
                }else if(valor.getNombreCaracteristica().contains("Marca_")){
                    marca = valor.getValor();
                }else if(valor.getNombreCaracteristica().contains("Modelo_")){
                    modelo = valor.getValor();
                }
            }

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Anuncio.fxml"));
                Parent componente = loader.load();

                Controller_Anuncio controller = loader.getController();
                controller.setAnuncio(anuncio);
                controller.setDatos(anuncio.getTipoVehiculo(), anio, km);
                controller.setMarcaModelo(marca, modelo);
                controller.setPrecio(anuncio.getPrecio());
                controller.setUbicacion(anuncio.getProvincia(), anuncio.getCiudad());
                controller.setUsuario(anuncio.getVendedor().getNombreUsuario());
                controller.setImagen(imagenesNuevas.get(i));
                controller.setGuardado(anuncio.isGuardado());
                controller.setController(this);

                this.VBox_Anuncios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (!primeraCarga) {
            this.filtro.siguientePagina();
        }
    }

    public void avisoGuardado(boolean guardado){
        Alert alert = new Alert(AlertType.INFORMATION);
        if(guardado){
            alert.setTitle("Anuncio guardado");
            alert.setContentText("El anuncio se ha guardado correctamente en 'Mis Guardados'");
        }else{
            alert.setTitle("Anuncio eliminado");
            alert.setContentText("El anuncio se ha eliminado correctamente en 'Mis Guardados'");
        }

        alert.setHeaderText(null);
        // alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        // alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }
}

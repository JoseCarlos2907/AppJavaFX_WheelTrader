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
import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Controller_ListaAnuncios implements Initializable, IListaAnuncios{
    private Lector_App hiloLector;

    private List<Anuncio> anuncios;
    private IFiltro filtro;
    private FiltroPorNombreUsuario filtroNU;
    private Anuncio anuncioSeleccionado;
    private boolean cargando;
    private Usuario usuarioSeleccionado;

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

    @FXML
    private ScrollPane ScrollPane_Anuncios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.anuncios = new ArrayList<>();
        
        this.ScrollPane_Anuncios.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargando) {
                cargando = true;
                try {
                    this.pedirAnuncios();
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
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void abrirPerfilUsuario(Usuario usuario) throws IOException{
        this.usuarioSeleccionado = usuario;

        this.filtroNU = new FiltroPorNombreUsuario(this.usuarioSeleccionado.getNombreUsuario(), 0, 10);
        this.filtroNU.setTipoFiltro("PerfilUsuario");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroNU);
    
        this.hiloLector.obtenerAnuncios(filtroJSON, this.filtroNU.getTipoFiltro(), "si", Session.getUsuario().getIdUsuario());
    }

    public void irPerfilUsuario(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent parent = loader.load();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setUsuario(this.usuarioSeleccionado);
        controller.setHiloLector(this.hiloLector);
        controller.setFiltro(this.filtroNU);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        this.hiloLector.setController(controller);
        
        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void abrirAnuncio(Anuncio anuncio){
        this.anuncioSeleccionado = anuncio;

        try {
            this.hiloLector.obtenerImagenes(this.anuncioSeleccionado.getIdAnuncio());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
            Parent parent = loader.load();
            
            Controller_DetalleAnuncio controller = loader.getController();
            controller.setAnuncio(this.anuncioSeleccionado);
            controller.setImagenes(bytesImagenes);
            controller.setFiltro(this.filtro);
            controller.setHiloLector(this.hiloLector);
            controller.setController(this);
            this.hiloLector.setController(controller);

            Stage stage = (Stage) Btn_Volver.getScene().getWindow();

            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void setFiltro(IFiltro filtro){
        this.filtro = filtro;
    }

    public void pedirAnuncios() throws IOException{
        this.filtro.siguientePagina();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerAnuncios(filtroJSON, this.filtro.getTipoFiltro(), "no", Session.getUsuario().getIdUsuario());
    }

    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas) throws JsonMappingException, JsonProcessingException{
        if(imagenesNuevas.size() < 1) {
            cargando = true;
            return;
        }
        
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
                controller.setHiloLector(this.hiloLector);
                controller.setController(this);

                this.VBox_Anuncios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.cargando = false;
    }

    public void avisoGuardado(boolean guardado){
        if(guardado){
            AlertManager.alertInfo(
                "Anuncio guardado",
                "El anuncio se ha guardado correctamente en 'Mis Guardados'"
            );
        }else{
            AlertManager.alertInfo(
                "Anuncio eliminado",
                "El anuncio se ha eliminado correctamente en 'Mis Guardados'"
            );
        }
    }
}

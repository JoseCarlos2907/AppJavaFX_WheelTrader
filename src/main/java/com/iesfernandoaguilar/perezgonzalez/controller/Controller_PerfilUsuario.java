package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_PerfilUsuario implements IListaAnuncios, Initializable{
    private Lector_App hiloLector;

    private Usuario usuario;
    private Anuncio anuncioSeleccionado;
    private FiltroPorNombreUsuario filtro;
    private List<Anuncio> anuncios;
    private boolean cargando;
    private Usuario usuarioSeleccionado;

    @FXML
    private Button Btn_Menu;

    @FXML
    private Label Lbl_NombreUsuario;

    @FXML
    private Label Lbl_ValoracionMedia;

    @FXML
    private VBox VBox_Anuncios;

    @FXML
    private ImageView Btn_Reportar;

    @FXML
    private ScrollPane ScrollPane_Anuncios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.anuncios = new ArrayList<>();
        this.cargando = false;

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

    public void pedirAnuncios() throws IOException{
        this.filtro.siguientePagina();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerAnuncios(filtroJSON, this.filtro.getTipoFiltro(), "no", Session.getUsuario().getIdUsuario());
    }

    @Override
    public void abrirAnuncio(Anuncio anuncio) {
        this.anuncioSeleccionado = anuncio;
        
        try {
            this.hiloLector.obtenerImagenes(this.anuncioSeleccionado.getIdAnuncio());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnMenuAction(MouseEvent event) throws IOException {
        FXMLLoader loader = null;
        if("MODERADOR".equals(Session.getUsuario().getRol())){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        }else{
            loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        }
        Parent nuevaVista = loader.load();
        
        IApp controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Menu.getScene().getWindow();
        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
        this.Lbl_NombreUsuario.setText("Perfil de " + usuario.getNombreUsuario());
    }

    @Override
    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas) throws JsonMappingException, JsonProcessingException {
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
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    @Override
    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
            Parent parent = loader.load();
            
            Controller_DetalleAnuncio controller = loader.getController();
            controller.setAnuncio(this.anuncioSeleccionado);
            controller.setImagenes(bytesImagenes);
            controller.setHiloLector(this.hiloLector);
            controller.setController(this);
            this.hiloLector.setController(this);

            Stage stage = (Stage) Btn_Menu.getScene().getWindow();

            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnReportarAction(MouseEvent event) throws IOException {
        if(this.usuario.getIdUsuario() == Session.getUsuario().getIdUsuario()){
            AlertManager.alertError(
                "Reporte incorrecto",
                "No puedes reportarte a tí mismo.",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if("MODERADOR".equals(Session.getUsuario().getRol()) && "ACTIVO".equals(usuario.getEstado())){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Banear usuario");
            alert.setHeaderText("¿Seguro que desea banear a este usuario?");
            alert.setContentText("Una vez se banee a este usuario deberá.");

            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent() && res.get() == ButtonType.OK){
                usuario.setEstado("BANEADO");

                AlertManager.alertInfo(
                    "Usuario baneado",
                    "El usuario ha sido baneado correctamente."
                );

                this.hiloLector.banearUsuario(usuario.getIdUsuario());
            }
        }else if("MODERADOR".equals(Session.getUsuario().getRol()) && "BANEADO".equals(usuario.getEstado())){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Desbanear usuario");
            alert.setHeaderText("¿Seguro que desea desbanear a este usuario?");
            alert.setContentText("Este usuario ya está baneado, por lo que si acepta se desbaneará este usuario.");

            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent() && res.get() == ButtonType.OK){
                usuario.setEstado("ACTIVO");

                AlertManager.alertInfo(
                    "Usuario desbaneado",
                    "El usuario ha sido desbaneado correctamente."
                );

                this.hiloLector.desbanearUsuario(usuario.getIdUsuario());
            }
        }else{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_FormularioReporte.fxml"));
            Parent nuevaVista = loader.load();
    
            Controller_FormularioReporte controller = loader.getController();
            controller.setUsuario(usuario);
            controller.setHiloLector(this.hiloLector);
            this.hiloLector.setController(controller);
    
            Stage stage = (Stage) Btn_Menu.getScene().getWindow();
            Scene scene = new Scene(nuevaVista);
    
            stage.setScene(scene);
            stage.show();
        }
    }

    @Override
    public void abrirPerfilUsuario(Usuario usuario) throws IOException {
        this.usuarioSeleccionado = usuario;

        this.filtro = new FiltroPorNombreUsuario(this.usuarioSeleccionado.getNombreUsuario(), 0, 10);
        this.filtro.setTipoFiltro("PerfilUsuario");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerAnuncios(filtroJSON, this.filtro.getTipoFiltro(), "si", Session.getUsuario().getIdUsuario());
        
        this.filtro.siguientePagina();
    }

    public void irPerfilUsuario(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent parent = loader.load();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setUsuario(this.usuarioSeleccionado);
        controller.setHiloLector(hiloLector);
        controller.setFiltro(this.filtro);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        this.hiloLector.setController(controller);
        
        Stage stage = (Stage) Btn_Menu.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setFiltro(FiltroPorNombreUsuario filtro){
        this.filtro = filtro;
    }
}

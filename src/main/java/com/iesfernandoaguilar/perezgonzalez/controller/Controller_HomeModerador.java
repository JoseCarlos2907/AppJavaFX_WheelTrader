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
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaUsuarios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.model.Auxiliares.UsuarioReportadosModDTO;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroBarraBusqueda;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroUsuariosConReportes;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_HomeModerador implements IListaAnuncios, IListaUsuarios, Initializable{

    private Lector_App hiloLector;

    private List<UsuarioReportadosModDTO> usuariosReportados;
    private List<Anuncio> anuncios;
    private FiltroUsuariosConReportes filtroUsuRep;
    private FiltroPorNombreUsuario filtroAnunciosUsuarioSeleccionado;
    private FiltroBarraBusqueda filtroAnuncios;
    private Anuncio anuncioSeleccionado;
    private Usuario usuarioSeleccionado;
    private boolean cargandoUsuarios;
    private boolean cargandoAnuncios;
    
    @FXML
    private Button Btn_CerrarSesion;

    @FXML
    private Button Btn_UltimosReportes;

    @FXML
    private TextField TxtF_BusquedaAnuncios;

    @FXML
    private TextField TxtF_BusquedaUsuario;

    @FXML
    private VBox VBox_Anuncios;

    @FXML
    private VBox VBox_Usuarios;

    @FXML
    private ScrollPane ScrollPane_Usuarios;

    @FXML
    private ScrollPane ScrollPane_Anuncios;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.filtroAnuncios = new FiltroBarraBusqueda();
        this.filtroAnuncios.setTiposVehiculo(List.of("Coche", "Moto", "Camioneta", "Camion", "Maquinaria"));
        this.filtroAnuncios.setAnioMinimo(1950);
        this.filtroAnuncios.setAnioMaximo(2025);
        this.filtroAnuncios.setProvincia(null);
        this.filtroAnuncios.setCiudad(null);
        this.filtroAnuncios.setPrecioMinimo(0);
        this.filtroAnuncios.setPrecioMaximo(Double.MAX_VALUE);
        this.filtroAnuncios.setCantidadPorPagina(10);
        this.filtroAnuncios.setEsModerador(true);

        this.filtroUsuRep = new FiltroUsuariosConReportes();
        this.usuariosReportados = new ArrayList<>();
        this.anuncios = new ArrayList<>();

        if(!Session.isHiloCreado()){
            new Thread(() -> {
                this.hiloLector = new Lector_App();
                this.hiloLector.setController(this);
                this.hiloLector.start();
                Session.setHiloCreado();
                System.out.println("entra en el if");
            }).start();
        }
        
        try {
            this.cargarUsuarios("", true);
            this.cargarAnuncios(true);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.ScrollPane_Anuncios.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargandoAnuncios) {
                cargandoAnuncios = true;
                try {
                    this.cargarAnuncios(false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        this.ScrollPane_Usuarios.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargandoUsuarios) {
                cargandoUsuarios = true;
                try {
                    this.cargarUsuarios("NO SE USA", false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    void onBusquedaAnuncioKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaAnuncios.getText();
            this.filtroAnuncios.setCadena(cadenaBusqueda);
            this.cargarAnuncios(true);
        }
    }

    @FXML
    void onBusquedaUsuarioKeyPressed(KeyEvent event) throws IOException {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaUsuario.getText();
            this.cargarUsuarios(cadenaBusqueda, true);
        }
    }

    public void cargarUsuarios(String cadena, boolean primeraCarga) throws IOException{
        if(primeraCarga){
            this.filtroUsuRep.setPagina(0);
            this.filtroUsuRep.setCadena(cadena);
            this.usuariosReportados.clear();
            this.VBox_Usuarios.getChildren().clear();
        }

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroUsuRep);

        this.hiloLector.obtenerReportesMod(filtroJSON);
        
        this.filtroUsuRep.siguientePagina();
    }

    public void cargarAnuncios(boolean primeraCarga) throws IOException{
        if(primeraCarga){
            this.filtroAnuncios.setPagina(0);
            this.anuncios.clear();
            this.VBox_Anuncios.getChildren().clear();
        }

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroAnuncios);

        this.hiloLector.obtenerAnuncios(filtroJSON, this.filtroAnuncios.getTipoFiltro(), primeraCarga ? "si" : "no", Session.getUsuario().getIdUsuario());
        
        this.filtroAnuncios.siguientePagina();
    }

    public void aniadirUsuario(String usuariosMod) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<UsuarioReportadosModDTO> usuariosRecogidos = mapper.readValue(usuariosMod, new TypeReference<List<UsuarioReportadosModDTO>>(){});

        this.usuariosReportados.addAll(usuariosRecogidos);
        for (UsuarioReportadosModDTO usuarioRecogido : usuariosRecogidos) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Usuario.fxml"));
                Parent componente = loader.load();

                Controller_Usuario controller = loader.getController();
                controller.setNombreUsuario(usuarioRecogido.getUsuario().getNombreUsuario());
                controller.setCantReportes(usuarioRecogido.getCantReportes());
                controller.setUsuario(usuarioRecogido.getUsuario());
                controller.setController(this);
                
                this.VBox_Usuarios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.cargandoUsuarios = false;
    }

    @FXML
    void handleBtnCerrarSesionAction(MouseEvent event) throws IOException {
        this.hiloLector.cerrarSesion(Session.getUsuario().getIdUsuario());
        Session.setHiloNoCreado();
        Session.setHiloLoginNoCreado();
        Session.cerrarSession();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        
        Stage stage = (Stage) Btn_CerrarSesion.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnUltReportesAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Reportes.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Reportes controller = loader.getController();
        controller.setTitulo("Últimos reportes");
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_CerrarSesion.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
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

    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException{
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
            Parent parent = loader.load();
            
            Controller_DetalleAnuncio controller = loader.getController();
            controller.setAnuncio(this.anuncioSeleccionado);
            controller.setImagenes(bytesImagenes);
            controller.setHiloLector(this.hiloLector);
            controller.setController(this);
            this.hiloLector.setController(this);

            Stage stage = (Stage) Btn_CerrarSesion.getScene().getWindow();

            Scene scene = new Scene(parent);

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abrirPerfilUsuario(Usuario usuario) {
        this.usuarioSeleccionado = usuario;

        this.filtroAnunciosUsuarioSeleccionado = new FiltroPorNombreUsuario(this.usuarioSeleccionado.getNombreUsuario(), 0, 10);
        this.filtroAnunciosUsuarioSeleccionado.setTipoFiltro("PerfilUsuario");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = "";
        try {
            filtroJSON = mapper.writeValueAsString(this.filtroAnunciosUsuarioSeleccionado);
            this.hiloLector.obtenerAnuncios(filtroJSON, this.filtroAnunciosUsuarioSeleccionado.getTipoFiltro(), "si", Session.getUsuario().getIdUsuario());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.filtroAnunciosUsuarioSeleccionado.siguientePagina();
    }

    public void irPerfilUsuario(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent parent = loader.load();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setUsuario(this.usuarioSeleccionado);
        controller.setHiloLector(hiloLector);
        controller.setFiltro(this.filtroAnunciosUsuarioSeleccionado);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        this.hiloLector.setController(controller);
        
        Stage stage = (Stage) Btn_CerrarSesion.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
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

        this.cargandoAnuncios = false;
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }
}

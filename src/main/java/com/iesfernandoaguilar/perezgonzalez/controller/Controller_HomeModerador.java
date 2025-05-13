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
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaUsuarios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.model.Auxiliares.UsuarioReportadosModDTO;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroBarraBusqueda;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroTodo;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroUsuariosConReportes;
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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_HomeModerador implements IListaAnuncios, IListaUsuarios, Initializable{

    private List<UsuarioReportadosModDTO> usuariosReportados;
    private List<Anuncio> anuncios;
    private FiltroUsuariosConReportes filtroUsuRep;
    private FiltroBarraBusqueda filtroAnuncios;
    private Anuncio anuncioSeleccionado;
    private boolean cargandoUsuarios;
    private boolean cargandoAnuncios;

    private Lector_App hiloLector;

    private DataOutputStream dos;

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
            this.hiloLector = new Lector_App();
            this.hiloLector.setController(this);
            this.hiloLector.start();
            Session.setHiloCreado();
            System.out.println("entra en el if");
        }
        
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        try {
            this.cargarUsuarios("", true);
            this.cargarAnuncios(true);
        } catch (JsonProcessingException e) {
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
    void onBusquedaAnuncioKeyPressed(KeyEvent event) throws JsonProcessingException {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaAnuncios.getText();
            this.filtroAnuncios.setCadena(cadenaBusqueda);
            this.cargarAnuncios(true);
            this.cargandoAnuncios = false;
        }
    }

    @FXML
    void onBusquedaUsuarioKeyPressed(KeyEvent event) throws JsonProcessingException {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaUsuario.getText();
            this.cargarUsuarios(cadenaBusqueda, true);
            this.cargandoUsuarios = false;
        }
    }

    public void cargarUsuarios(String cadena, boolean primeraCarga) throws JsonProcessingException{
        if(primeraCarga){
            this.filtroUsuRep.setPagina(0);
            this.filtroUsuRep.setCadena(cadena);
            this.usuariosReportados.clear();
            this.VBox_Usuarios.getChildren().clear();
        }

        Mensaje msg = new Mensaje();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroUsuRep);
    
        msg.setTipo("OBTENER_REPORTES_MOD");
        msg.addParam(filtroJSON);

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.filtroUsuRep.siguientePagina();
    }

    public void cargarAnuncios(boolean primeraCarga) throws JsonProcessingException{
        if(primeraCarga){
            this.filtroAnuncios.setPagina(0);
            this.anuncios.clear();
            this.VBox_Anuncios.getChildren().clear();
        }

        Mensaje msg = new Mensaje();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroAnuncios);
    
        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(this.filtroAnuncios.getTipoFiltro());
        msg.addParam(primeraCarga ? "si": "no");
        msg.addParam(Session.getUsuario().getIdUsuario().toString());

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
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
                controller.setValoracionMedia(usuarioRecogido.getMediaValoraciones());
                controller.setCantReportes(usuarioRecogido.getCantReportes());
                controller.setUsuario(usuarioRecogido.getUsuario());
                controller.setController(this);
                
                this.VBox_Usuarios.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnCerrarSesionAction(MouseEvent event) throws IOException {
        Session.setHiloNoCreado();
        Session.cerrarSession();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage2 = (Stage) Btn_CerrarSesion.getScene().getWindow();
        stage2.close();
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

        Mensaje msg = new Mensaje();
    
        msg.setTipo("OBTENER_IMAGENES");
        msg.addParam(String.valueOf(this.anuncioSeleccionado.getIdAnuncio()));

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException{
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            stage.show();
            
            Controller_DetalleAnuncio controller = loader.getController();
            controller.setAnuncio(this.anuncioSeleccionado);
            controller.setImagenes(bytesImagenes);
            controller.setFiltro(this.filtroAnuncios);
            controller.setHiloLector(this.hiloLector);
            controller.setController(this);
            this.hiloLector.setController(this);

            Stage stage2 = (Stage) Btn_CerrarSesion.getScene().getWindow();
            stage2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void abrirPerfilUsuario(Usuario usuario) {
        // TODO: Abrir perfil usuario
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
}

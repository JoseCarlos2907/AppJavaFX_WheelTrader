package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
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
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.model.Valoracion;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroPorNombreUsuario;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_PerfilUsuario implements IListaAnuncios, Initializable{
    private Usuario usuario;
    private Anuncio anuncioSeleccionado;
    private FiltroPorNombreUsuario filtro;
    private List<Anuncio> anuncios;
    private boolean cargando;
    private Usuario usuarioSeleccionado;

    private DataOutputStream dos;

    private Lector_App hiloLector;

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

        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }

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

        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ANUNCIOS");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        msg.addParam(filtroJSON);
        msg.addParam(this.filtro.getTipoFiltro());
        msg.addParam("no");
        msg.addParam(String.valueOf(Session.getUsuario().getIdUsuario()));
        
        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
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

    @FXML
    void handleBtnMenuAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Home controller = loader.getController();
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
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_DetalleAnuncio.fxml"));
            Parent parent = loader.load();
            stage.setScene(new Scene(parent));
            stage.show();
            
            Controller_DetalleAnuncio controller = loader.getController();
            controller.setAnuncio(this.anuncioSeleccionado);
            controller.setImagenes(bytesImagenes);
            controller.setFiltro(this.filtro);
            controller.setHiloLector(this.hiloLector);
            controller.setController(this);
            this.hiloLector.setController(this);

            Stage stage2 = (Stage) Btn_Menu.getScene().getWindow();
            stage2.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnReportarAction(MouseEvent event) throws IOException {
        Mensaje msg = new Mensaje();
        if("MODERADOR".equals(Session.getUsuario().getRol()) && "ACTIVO".equals(usuario.getEstado())){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Banear usuario");
            alert.setHeaderText("¿Seguro que desea banear a este usuario?");
            alert.setContentText("Una vez se banee a este usuario deberá.");

            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent() && res.get() == ButtonType.OK){
                usuario.setEstado("BANEADO");

                Alert alertInfo = new Alert(AlertType.ERROR);
                alertInfo.setTitle("Usuario baneado");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("El usuario ha sido baneado correctamente.");
                alertInfo.showAndWait();

                msg.setTipo("BANEAR_USUARIO");
                msg.addParam(String.valueOf(usuario.getIdUsuario()));

                this.dos.writeUTF(Serializador.codificarMensaje(msg));
                this.dos.flush();
            }
        }else if("MODERADOR".equals(Session.getUsuario().getRol()) && "BANEADO".equals(usuario.getEstado())){
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Desbanear usuario");
            alert.setHeaderText("¿Seguro que desea desbanear a este usuario?");
            alert.setContentText("Este usuario ya está baneado, por lo que si acepta se desbaneará este usuario.");

            Optional<ButtonType> res = alert.showAndWait();

            if(res.isPresent() && res.get() == ButtonType.OK){
                usuario.setEstado("ACTIVO");

                Alert alertInfo = new Alert(AlertType.ERROR);
                alertInfo.setTitle("Usuario desbaneado");
                alertInfo.setHeaderText(null);
                alertInfo.setContentText("El usuario ha sido desbaneado correctamente.");
                alertInfo.showAndWait();

                msg.setTipo("DESBANEAR_USUARIO");
                msg.addParam(String.valueOf(usuario.getIdUsuario()));

                this.dos.writeUTF(Serializador.codificarMensaje(msg));
                this.dos.flush();
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

        Mensaje msg = new Mensaje();

        this.filtro = new FiltroPorNombreUsuario(this.usuarioSeleccionado.getNombreUsuario(), 0, 10);
        this.filtro.setTipoFiltro("PerfilUsuario");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);
    
        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(this.filtro.getTipoFiltro());
        msg.addParam( "si");
        msg.addParam(Session.getUsuario().getIdUsuario().toString());

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.filtro.siguientePagina();
    }

    public void irPerfilUsuario(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setUsuario(this.usuarioSeleccionado);
        controller.setHiloLector(hiloLector);
        controller.setFiltro(this.filtro);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        this.hiloLector.setController(controller);
        
        Stage stage2 = (Stage) Btn_Menu.getScene().getWindow();
        stage2.close();
    }

    public void setFiltro(FiltroPorNombreUsuario filtro){
        this.filtro = filtro;
    }
}

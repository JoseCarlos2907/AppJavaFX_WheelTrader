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
import com.iesfernandoaguilar.perezgonzalez.model.Auxiliares.UsuarioReportadosModDTO;
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
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_HomeModerador implements IListaAnuncios, IListaUsuarios, Initializable{

    private List<UsuarioReportadosModDTO> usuariosReportados;
    private FiltroUsuariosConReportes filtro;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.filtro = new FiltroUsuariosConReportes();
        this.usuariosReportados = new ArrayList<>();

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
            // this.cargarAnuncios();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // TODO: Scroll infinito con la primera carga a false
    }

    @FXML
    void onBusquedaAnuncioKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaAnuncios.getText();
        }
    }

    @FXML
    void onBusquedaUsuarioKeyPressed(KeyEvent event) throws JsonProcessingException {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaUsuario.getText();
            this.cargarUsuarios(cadenaBusqueda, true);
        }
    }

    public void cargarUsuarios(String cadena, boolean primeraCarga) throws JsonProcessingException{
        if(primeraCarga){
            this.filtro = new FiltroUsuariosConReportes();
            this.usuariosReportados.clear();
            this.VBox_Usuarios.getChildren().clear();
        }

        Mensaje msg = new Mensaje();

        this.filtro.setCadena(cadena);

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);
    
        msg.setTipo("OBTENER_REPORTES_MOD");
        msg.addParam(filtroJSON);

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.filtro.siguientePagina();
    }

    public void cargarAnuncios(String cadena, boolean primeraCarga){
        // List<Usuario> usuarios = List.of(
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false),
        //     new Usuario("José Carlos", "Pérez González", "12345678A", "c/ Mi Casa n9", "JoseCarlos2907", "hash", "correo", "correPP", "salt", false)
        // );

        // for (Usuario usuario : usuarios) {
        //     try {
        //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Usuario.fxml"));
        //         Parent componente = loader.load();

        //         Controller_Usuario controller = loader.getController();
        //         controller.setNombreUsuario(usuario.getNombreUsuario());
        //         controller.setValoracionMedia(4.5); // En base a las valoraciones del usuario
        //         controller.setCantReportes(4);  // En base a la cantidad de veces que han reportado a este usuario
        //         controller.setUsuario(usuario);
        //         controller.setController(this);
                
        //         this.VBox_Usuarios.getChildren().add(componente);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
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
    
        //     Stage stage = (Stage) Btn_CerrarSesion.getScene().getWindow();
    
        //     Scene scene = new Scene(nuevaVista);
        //     scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());
    
        //     stage.setScene(scene);
        //     stage.show();
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
    }

    @Override
    public void abrirPerfilUsuario(Usuario usuario) {
        
    }

    @Override
    public void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas, boolean primeraCarga) throws JsonMappingException, JsonProcessingException {
        
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    @Override
    public void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException {
        
    }
}

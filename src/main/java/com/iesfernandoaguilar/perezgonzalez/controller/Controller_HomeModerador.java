package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaUsuarios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;

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
        this.cargarUsuarios();
        this.cargarAnuncios();
    }

    @FXML
    void onBusquedaAnuncioKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaAnuncios.getText();
        }
    }

    @FXML
    void onBusquedaUsuarioKeyPressed(KeyEvent event) {
        if (event.getCode().toString().equals("ENTER")) {
            String cadenaBusqueda = this.TxtF_BusquedaUsuario.getText();
        }
    }

    public void cargarAnuncios(){
        // List<Anuncio> anuncios = List.of(
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL"),
        //     new Anuncio(LocalDateTime.now(), LocalDateTime.now().plusDays(10), "Descripción", 99999, 999, "Cádiz", "Medina-Sidonia", "ACTIVO", "1234DDX", "Opel", "Corsa", 69, 2005, 5, 5, "MANUAL", 5, "COCHE", "1HGCM82633A004352", 170000, "Blanco", "DIESEL")
        // );

        // for (Anuncio anuncio : anuncios) {
        //     try {
        //         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Anuncio.fxml"));
        //         Parent componente = loader.load();

        //         Controller_Anuncio controller = loader.getController();
        //         controller.setAnuncio(anuncio);
        //         controller.setDatos(anuncio.getCategoria(), anuncio.getAnio(), anuncio.getKilometraje());
        //         controller.setMarcaModelo(anuncio.getMarca(), anuncio.getModelo());
        //         controller.setPrecio(anuncio.getPrecioAlContado());
        //         controller.setUbicacion(anuncio.getProvincia(), anuncio.getCiudad());
        //         controller.setUsuario("JoseCarlos2907");
        //         controller.setController(this);
                
        //         this.VBox_Anuncios.getChildren().add(componente);
        //     } catch (IOException e) {
        //         e.printStackTrace();
        //     }
        // }
    }

    public void cargarUsuarios(){
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

    @FXML
    void handleBtnCerrarSesionAction(MouseEvent event) {

    }

    @FXML
    void handleBtnUltReportesAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Reportes.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Reportes controller = loader.getController();
        controller.setTitulo("Últimos reportes");

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
}

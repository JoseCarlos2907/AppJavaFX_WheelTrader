package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.File;
import java.io.FileOutputStream;
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
import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroNotificaciones;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller_Notificaciones implements IApp, Initializable {
    @FXML
    private Button Btn_Volver;

    @FXML
    private VBox VBox_Notificaciones;

    @FXML
    private ScrollPane ScrollPane_Notificaciones;

    private List<Notificacion> notificaciones;
    private Notificacion notificacionSeleccionada;
    private boolean cargando;
    private FiltroNotificaciones filtro;

    private Lector_App hiloLector;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.notificaciones = new ArrayList<>();

        this.ScrollPane_Notificaciones.vvalueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal.doubleValue() >= 0.8 && !cargando) {
                cargando = true;
                try {
                    this.pedirNotificaciones();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
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

    public void pedirNotificaciones() throws IOException{
        this.filtro.siguientePagina();

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        this.hiloLector.obtenerNotificaciones(filtroJSON, "no");
    }

    public void aniadirNotificaciones(String notisJSON) throws JsonMappingException, JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Notificacion> notisNuevas = mapper.readValue(notisJSON, new TypeReference<List<Notificacion>>(){});

        if(notisNuevas.size() < 1) {
            cargando = true;
            return;
        }
        
        this.notificaciones.addAll(notisNuevas);
        for (int i = 0; i < notisNuevas.size(); i++) {
            Notificacion notificacion = notisNuevas.get(i);
            
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Notificacion.fxml"));
                Parent componente = loader.load();

                Controller_Notificacion controller = loader.getController();
                controller.setEstado(notificacion.getEstado());
                controller.setIconoPrincipal(notificacion.getTipo());
                controller.setTitulo(notificacion.getTitulo());
                controller.setMensaje(notificacion.getMensaje());
                controller.setNotificacion(notificacion);
                controller.setController(this);

                this.VBox_Notificaciones.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.cargando = false;
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void setFiltro(FiltroNotificaciones filtro){
        this.filtro = filtro;
    }

    public void abrirCompraVendedor(Notificacion notificacion) throws IOException{
        this.notificacionSeleccionada = notificacion;

        this.hiloLector.obtenerPDFAcuerdoVendedor(notificacion.getUsuarioEnvia().getIdUsuario(), notificacion.getAnuncio().getIdAnuncio(), Session.getUsuario().getIdUsuario().toString());
    }

    public void irCompraVendedor(byte[] documento) throws IOException{
        File temp = new File("temp");
        if(!temp.exists()) temp.mkdir();

        File pdf = new File("temp/Temp.pdf");
        if(pdf.exists()) pdf.delete();
        pdf.createNewFile();

        try (FileOutputStream fos = new FileOutputStream("temp/Temp.pdf")) {
            fos.write(documento);
        }

        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_CompraVendedor.fxml"));

        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_CompraVendedor controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.setNotificacion(notificacionSeleccionada);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void abrirPagoPayPal(Notificacion notificacion) throws IOException{
        this.notificacionSeleccionada = notificacion;
        
        double precio = notificacion.getAnuncio().getPrecio() + ((notificacion.getAnuncio().getPrecio()*5)/100);

        this.hiloLector.usuarioPaga(Session.getUsuario().getIdUsuario(), notificacion.getUsuarioEnvia().getIdUsuario(), notificacion.getAnuncio().getIdAnuncio(), precio);
    }

    public void irPagoPayPal(String url) throws IOException{
        Stage stage = new Stage();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PagoPayPal.fxml"));

        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_PagoPayPal controller = loader.getController();
        controller.setNotificacion(notificacionSeleccionada);
        controller.setUrl(url);
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroBarraBusqueda;
import com.iesfernandoaguilar.perezgonzalez.model.Filtros.FiltroNotificaciones;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_Home implements IApp, Initializable {
    private DataOutputStream dos;

    private Lector_App hiloLector;

    private FiltroBarraBusqueda filtro;
    private FiltroNotificaciones filtroNotis;

    @FXML
    private Button Btn_Buscar;

    @FXML
    private Button Btn_ConfCuenta;

    @FXML
    private Button Btn_Filtros;

    @FXML
    private Button Btn_Notificaciones;

    @FXML
    private Button Btn_PublicarAnuncio;
    
    @FXML
    private TextField TxtF_BarraBusqueda;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        try {
            if(!Session.isHiloCreado()){
                this.hiloLector = new Lector_App();
                this.hiloLector.setController(this);
                this.hiloLector.start();
                Session.setHiloCreado();
                System.out.println("entra en el if");
            }

            this.dos = new DataOutputStream(Session.getOutputStream());
            this.Btn_ConfCuenta.setText(Session.getUsuario().getNombreUsuario());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnFiltrosAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Filtros.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Filtros controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_ConfCuenta.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnBuscarAction(MouseEvent event) throws IOException {
        String cadena = new String(this.TxtF_BarraBusqueda.getText());

        this.filtro = new FiltroBarraBusqueda();
        this.filtro.setTiposVehiculo(List.of("Coche", "Moto", "Camioneta", "Camion", "Maquinaria"));
        this.filtro.setAnioMinimo(1950);
        this.filtro.setAnioMaximo(2025);
        this.filtro.setProvincia(null);
        this.filtro.setCiudad(null);
        this.filtro.setPrecioMinimo(0);
        this.filtro.setPrecioMaximo(Double.MAX_VALUE);
        this.filtro.setCadena(cadena);
        this.filtro.setEsModerador(false);

        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ANUNCIOS");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtro);

        msg.addParam(filtroJSON);
        msg.addParam(this.filtro.getTipoFiltro());
        msg.addParam("si");
        msg.addParam(String.valueOf(Session.getUsuario().getIdUsuario()));

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void irListaAnuncios(String anunciosJSON, List<byte[]> imagenes) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_ListaAnuncios.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_ListaAnuncios controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.aniadirAnuncios(anunciosJSON, imagenes);
        controller.setFiltro(this.filtro);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_ConfCuenta.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnConfCuentaAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_ConfUsuario.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_ConfUsuario controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_ConfCuenta.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnNotificacionesAction(MouseEvent event) throws IOException {
        this.filtroNotis = new FiltroNotificaciones();
        this.filtroNotis.setIdUsuario(Session.getUsuario().getIdUsuario());

        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_NOTIFICACIONES");

        ObjectMapper mapper = new ObjectMapper();
        String filtroJSON = mapper.writeValueAsString(this.filtroNotis);

        msg.addParam(filtroJSON);
        msg.addParam("si");

        this.dos.writeUTF(Serializador.codificarMensaje(msg));
        this.dos.flush();
    }

    public void irListaNotificaciones(String notisJSON) throws IOException{
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Notificaciones.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Notificaciones controller = loader.getController();
        controller.setFiltro(filtroNotis);
        controller.setHiloLector(hiloLector);
        controller.aniadirNotificaciones(notisJSON);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_ConfCuenta.getScene().getWindow();
        stage2.close();
    }

    @FXML
    void handleBtnPublicarAnuncioAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PublicarAnuncio.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_PublicarAnuncio controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_ConfCuenta.getScene().getWindow();
        stage2.close();
    }
    
    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }
}

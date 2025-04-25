package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_Home implements IApp, Initializable {
    private DataOutputStream dos;

    private Lector_App hiloLector;

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
    private ImageView ImgView_Coche1;

    @FXML
    private ImageView ImgView_Coche2;

    @FXML
    private Label Label_Coche1;

    @FXML
    private Label Label_Coche2;

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
    void handleBtnBuscarAction(MouseEvent event) {
        
    }

    @FXML
    void handleBtnConfCuentaAction(MouseEvent event) {

    }

    @FXML
    void handleBtnNotificacionAction(MouseEvent event) {
        
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

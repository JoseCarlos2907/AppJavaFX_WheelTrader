package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class Controller_PagoPayPal implements IApp, Initializable {
    @FXML
    private Button Btn_Home;

    @FXML
    private WebView WebView_PayPal;

    private Lector_App hiloLector;

    private DataOutputStream dos;

    private long idVendedor;
    private String url;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TODO: Hilo terciario para ir preguntando como va la cosa
    }

    @FXML
    void handleBtnHomeAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Home.getScene().getWindow();
        stage2.close();
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void setIdVendedor(long id){
        this.idVendedor = id;
    }

    public void setUrl(String url){
        this.url = url;
        this.WebView_PayPal.getEngine().load(url);
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
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

    private Notificacion notificacion;
    private String url;
    private boolean pagado;

    private ExecutorService scheduler;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.pagado = false;

        scheduler = Executors.newSingleThreadExecutor();
    }

    @FXML
    void handleBtnHomeAction(MouseEvent event) throws IOException {
        this.irHome();
    }

    public void irHome() throws IOException{
        WebView_PayPal.getEngine().load(null);

        this.pagado = true;

        if(scheduler != null){
            // System.out.println("Scheduler Parau");
            scheduler.shutdownNow();
        }

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

    public void error() throws IOException{
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Pago erróneo");
        alert.setHeaderText(null);
        alert.setContentText("Ha ocurrido un error al crear la transacción de pago entre cuentas");
        alert.getDialogPane().getStylesheets()
                .add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();

        this.irHome();
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void setNotificacion(Notificacion notificacion){
        this.notificacion = notificacion;

        // Hilo terciario para ir preguntando como va la cosa
        scheduler.execute(new Runnable(){
            @Override
            public void run() {
                while (!pagado) {
                    try {
                        hiloLector.obtenerEstadoPago(notificacion.getIdNotificacion(), notificacion.getAnuncio().getPrecio(), notificacion.getAnuncio().getIdAnuncio());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                    try {
                        Thread.sleep(4000);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Interrumpido");
                    }
                }
            }
        });
    }

    public void setUrl(String url){
        this.url = url;
        this.WebView_PayPal.getEngine().load(this.url);
    }
}

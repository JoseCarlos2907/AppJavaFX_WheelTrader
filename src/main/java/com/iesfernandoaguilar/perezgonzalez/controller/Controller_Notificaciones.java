package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.model.Notificacion;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class Controller_Notificaciones implements Initializable {
    @FXML
    private Button Btn_ConfCuenta;

    @FXML
    private Label Lbl_Asunto;

    @FXML
    private Label Lbl_Mensaje;

    @FXML
    private VBox VBox_Notificaciones;

    List<Controller_Notificacion> controllersNotificaciones;
    Controller_Notificacion notificacionSeleccionada;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.controllersNotificaciones = new ArrayList<>();
        List<Notificacion> notificaciones = List.of(
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 1", "El pedazo de mensaje de su gran maree", "LEIDO", "INFORMACION"),
            new Notificacion("Título 2", "El pedazo de mensaje de su gran maree", "NO-LEIDO", "INCIDENCIA")
        );

        for (Notificacion noti : notificaciones) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Notificacion.fxml"));
                Parent componente = loader.load();

                Controller_Notificacion controller = loader.getController();
                controller.setTitulo(noti.getTitulo());
                controller.setUsuario(noti.getUsuarioEnvia().getNombreUsuario());
                controller.setIconoLeido(noti.getEstado());
                controller.setIconoPrincipal(noti.getTipo());
                controller.setNotificacion(noti);
                controller.setController(this);

                this.controllersNotificaciones.add(controller);

                this.VBox_Notificaciones.getChildren().add(componente);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {
        // TODO: Volver al Home
    }

    public void setDatosNotificacionActiva(Pane pane, Notificacion noti){
        if (notificacionSeleccionada != null) {
            notificacionSeleccionada.setSeleccionada(false);
        }
        for (Controller_Notificacion controller : controllersNotificaciones) {
            if(controller.getPane() == pane){
                controller.setSeleccionada(true);
                notificacionSeleccionada = controller;
            }
        }

        this.Lbl_Asunto.setText(noti.getTitulo());
        this.Lbl_Mensaje.setText(noti.getMensaje());
    }
}

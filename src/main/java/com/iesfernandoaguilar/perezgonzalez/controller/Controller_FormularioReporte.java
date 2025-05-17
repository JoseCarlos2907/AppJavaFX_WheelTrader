package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Reporte;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
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
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_FormularioReporte implements IApp, Initializable {
    private Lector_App hiloLector;
    private DataOutputStream dos;

    private Usuario usuario;

    @FXML
    private Button Btn_Reportar;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextArea TxtField_Motivo;

    @FXML
    private TextArea TxtA_Desccripcion;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnReportarAction(MouseEvent event) throws JsonProcessingException {
        Mensaje msg = new Mensaje();

        Reporte reporte = new Reporte();
        reporte.setExplicacion(new String(this.TxtA_Desccripcion.getText()));
        reporte.setMotivo(new String(this.TxtField_Motivo.getText()));
        reporte.setUsuarioEnvia(Session.getUsuario());
        reporte.setUsuarioRecibe(usuario);

        ObjectMapper mapper = new ObjectMapper();
        String reporteJSON = mapper.writeValueAsString(reporte);
    
        msg.setTipo("REPORTAR_USUARIO");
        msg.addParam(reporteJSON);

        try {
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
        Parent parent = loader.load();

        Controller_PerfilUsuario controller = loader.getController();
        controller.setUsuario(usuario);
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void setUsuario(Usuario usuario){
        this.usuario = usuario;
    }


    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void reporteRealizado() throws IOException{
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Reporte realizado");
        alert.setHeaderText(null);
        alert.setContentText("El reporte se ha realizado correctamente");
        alert.showAndWait();

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        stage.setScene(new Scene(parent));
        stage.show();

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
    

    public void reporteYaRealizado() throws IOException{
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Reporte ya realizado");
        alert.setHeaderText(null);
        alert.setContentText("El reporte a este mismo usuario ya se ha realizado en otro momento");
        alert.showAndWait();
    }
}

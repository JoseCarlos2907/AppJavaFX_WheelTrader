package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Reporte;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_FormularioReporte implements IApp {
    private Lector_App hiloLector;

    private Usuario usuario;

    @FXML
    private Button Btn_Reportar;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextArea TxtField_Motivo;

    @FXML
    private TextArea TxtA_Desccripcion;

    @FXML
    void handleBtnReportarAction(MouseEvent event) throws IOException {
        Reporte reporte = new Reporte();
        reporte.setExplicacion(new String(this.TxtA_Desccripcion.getText()));
        reporte.setMotivo(new String(this.TxtField_Motivo.getText()));
        reporte.setUsuarioEnvia(Session.getUsuario());
        reporte.setUsuarioRecibe(usuario);

        ObjectMapper mapper = new ObjectMapper();
        String reporteJSON = mapper.writeValueAsString(reporte);
    
        this.hiloLector.reportarUsuario(reporteJSON);
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
        AlertManager.alertInfo(
            "Reporte realizado",
            "El reporte se ha realizado correctamente"
        );

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
        AlertManager.alertInfo(
            "Reporte ya realizado",
            "El reporte a este mismo usuario ya se ha realizado en otro momento"
        );
    }
}

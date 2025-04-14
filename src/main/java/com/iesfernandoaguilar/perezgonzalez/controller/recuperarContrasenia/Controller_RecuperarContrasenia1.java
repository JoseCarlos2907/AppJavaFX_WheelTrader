package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.model.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia1 implements ILogin, Initializable{
    private Lector_InicioSesion hiloLector;
    private DataOutputStream dos;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextField TxtF_Correo;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            this.dos = new DataOutputStream(Session.getOutputStream());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        if(!this.TxtF_Correo.getText().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,3}$")){
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Formato incorrecto");
            alert.setHeaderText(null);
            alert.setContentText("El formato del correo no es el correcto");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
            alert.getDialogPane().getStyleClass().add("alert-error");
            alert.showAndWait();
        }else{
            Mensaje msg = new Mensaje();
            msg.setTipo("RECUPERAR_CONTRASENIA");
            msg.addParam(new String(this.TxtF_Correo.getText()));
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_InicioSesion controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setInicioSesionController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }
    
    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso() throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia2.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_RecuperarContrasenia2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Volver.getScene().getWindow();
        stage2.close();
    }

    public void correoNoExiste(){
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Correo inexistente");
        alert.setHeaderText(null);
        alert.setContentText("El correo que has introducido no coincide con ningún usuario");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }
}

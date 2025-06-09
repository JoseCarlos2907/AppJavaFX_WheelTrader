package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia1 implements ILogin{
    private Lector_InicioSesion hiloLector;

    @FXML
    private Button Btn_Siguiente;

    @FXML
    private Button Btn_Volver;

    @FXML
    private TextField TxtF_Correo;
    
    @FXML
    void handleBtnSiguienteAction(MouseEvent event) throws IOException {
        if(!this.TxtF_Correo.getText().matches("^[a-zA-Z0-9._-]+@[a-zA-Z0-9._-]+\\.[a-zA-Z]{2,3}$")){
            AlertManager.alertError(
                "Formato incorrecto",
                "El formato del correo no es el correcto",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else{
            this.hiloLector.recuperarContrasenia(new String(this.TxtF_Correo.getText()));
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_InicioSesion.fxml"));
        Parent parent = loader.load();
        
        Controller_InicioSesion controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }
    
    public void setHiloLector(Lector_InicioSesion hiloLector){
        this.hiloLector = hiloLector;
    }

    public void siguientePaso() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia2.fxml"));
        Parent parent = loader.load();
        
        Controller_RecuperarContrasenia2 controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);
        
        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void correoNoExiste(){
        AlertManager.alertError(
            "Correo inexistente",
            "El correo que has introducido no coincide con ningún usuario",
            getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
        );
    }
}

package com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia;

import java.io.IOException;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_RecuperarContrasenia3 implements ILogin{
    private Lector_InicioSesion hiloLector;

    private byte[] salt;
    
    @FXML
    private Button Btn_CambiarContrasenia;

    @FXML
    private Button Btn_Volver;

    @FXML
    private PasswordField TxtF_ConfContra;
    
    @FXML
    private PasswordField TxtF_Contra;


    @FXML
    void handleBtnCambiarContraseniaAction(MouseEvent event) throws IOException {
        if(
            this.TxtF_Contra.getText().length() < 1 ||
            this.TxtF_ConfContra.getText().length() < 1
        ){
            AlertManager.alertError(
                "Campos incompletos",
                "Debe rellenar todos los campos para poder continuar con el registro",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(!this.TxtF_Contra.getText().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{6,}$")){
            AlertManager.alertError(
                "Formato incorrecto",
                "El formato de la contraseña no es el correcto",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(!this.TxtF_Contra.getText().equals(this.TxtF_ConfContra.getText())){
            AlertManager.alertError(
                "Contraseñas diferentes",
                "Las dos contraseñas deben ser exactamente iguales",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else{
            this.hiloLector.reiniciarContrasenia(SecureUtils.generate512(new String(this.TxtF_Contra.getText()), salt));
        }
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia1.fxml"));
        Parent parent = loader.load();
        
        Controller_RecuperarContrasenia1 controller = loader.getController();
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
        AlertManager.alertInfo(
            "Contraseña cambiada",
            "La contraseña se ha cambiado correctamente"
        );

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

    public void setSalt(byte[] salt){
        this.salt = salt;
    }
}

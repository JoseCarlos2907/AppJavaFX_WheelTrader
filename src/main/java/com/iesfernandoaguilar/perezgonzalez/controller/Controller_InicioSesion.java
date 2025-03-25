package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;

public class Controller_InicioSesion implements Initializable {
    private DataOutputStream dos;
    private Socket socket;
    
    @FXML
    private Button Btn_InicioSesion;

    @FXML
    private Button Btn_Registrarse;

    @FXML
    private Label Lbl_RecuperarContrasenia;

    @FXML
    private TextField TxtF_Contrasenia;

    @FXML
    private TextField TxtF_Correo_NomUsu;

    
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        Properties prop = new Properties();
        try {
            prop.load(new FileInputStream("src/main/resources/conf.properties"));
            
            int serverPort = Integer.parseInt(prop.getProperty("PORT"));
            String serverAddr = prop.getProperty("ADDRESS");
            
            this.socket = new Socket(serverAddr, serverPort);
            this.dos = new DataOutputStream(this.socket.getOutputStream());
            new Lector_InicioSesion(this.socket.getInputStream(), this).start();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }
    
    
    public void respuestaSalt(byte[] salt) throws IOException{
        String contraseniaStr = new String(TxtF_Contrasenia.getText());
        
        Mensaje msg = new Mensaje();
        msg.setTipo("INICIAR_SESION");
        msg.addParam(TxtF_Correo_NomUsu.getText());
        msg.addParam(SecureUtils.generate512(contraseniaStr, salt));
        // msg.addParam(contraseniaStr);
        dos.writeUTF(Serializador.codificarMensaje(msg));
    }
    
    @FXML
    void handleBtnIniciarSesionAction(MouseEvent event) throws IOException {
        String nombreUsuario = new String(TxtF_Correo_NomUsu.getText());
        

        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_SALT");
        msg.addParam(nombreUsuario);
        dos.writeUTF(Serializador.codificarMensaje(msg));
    }

    @FXML
    void handleBtnRegistrarseAction(MouseEvent event) {
        System.out.println("Todavia no");
    }

    @FXML
    void handleLblRecuperarContraseniaAction(MouseEvent event) {
        System.out.println("Todavia no");
    }

    public void inicioDeSesionIncorrecto(){
        // System.out.println("Credenciales incorrectas.");

        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Credenciales incorrectas");
        alert.setHeaderText("El nombre de usuario, el correo o la contraseña no son correctos");
        alert.getDialogPane().getStyleClass().addAll("alert");
        alert.showAndWait();
    }

    public void abrirAplicacion() throws IOException{
        FXMLLoader loader  = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Home controller = loader.getController();
        controller.setSocket(socket);
    
        Stage stage = (Stage) Btn_InicioSesion.getScene().getWindow();
        stage.setScene(new Scene(nuevaVista));
        stage.show();
        stage.centerOnScreen();
    }

    public String getNombreUsuarioCorreo(){
        return this.TxtF_Correo_NomUsu.getText();
    }

    public String getContrasenia(){
        return this.TxtF_Contrasenia.getText();
    }

    public void pedirUsuarioJSON() throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_USUARIO");
        msg.addParam(this.getNombreUsuarioCorreo());
        this.dos.writeUTF(Serializador.codificarMensaje(msg));
    }
}

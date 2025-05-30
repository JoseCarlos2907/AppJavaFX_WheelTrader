package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
// import java.util.Base64;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia1;
// import com.fasterxml.jackson.databind.ObjectMapper;
// import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro1;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_InicioSesion implements ILogin, Initializable {
    private DataOutputStream dos;
    private Lector_InicioSesion hiloLector;

    @FXML
    private Button Btn_InicioSesion;

    @FXML
    private Button Btn_Registrarse;

    @FXML
    private Label Lbl_RecuperarContrasenia;

    @FXML
    private PasswordField TxtF_Contrasenia;

    @FXML
    private TextField TxtF_Correo_NomUsu;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            if (!Session.isHiloLoginCreado()) {
                Properties prop = new Properties();
                prop.load(new FileInputStream("src/main/resources/conf.properties"));

                int serverPort = Integer.parseInt(prop.getProperty("PORT"));
                String serverAddr = prop.getProperty("ADDRESS");

                // Se hace en un hilo secundario porque si intento crear el socket en el principal
                // el que se queda pillado esperando seria el controller y no se mostraría la interfaz.
                // En este caso se queda pillado el hilo secundario en caso de que no se pueda conectar 
                // al arrancar la aplicación
                Thread hiloAux = new Thread(() -> {
                    try {
                        if(Session.getSocket() == null){
                            Session.setSocket(new Socket(serverAddr, serverPort));
                        }
                        
                        this.hiloLector = new Lector_InicioSesion(Session.getInputStream(), Session.getOutputStream(), prop);
                        this.hiloLector.setController(this);
                        this.hiloLector.start();
                        Session.setHiloLoginCreado();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
                hiloAux.start();

            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    public void respuestaSalt(byte[] salt) throws IOException {
        String contraseniaStr = new String(TxtF_Contrasenia.getText());

        this.hiloLector.iniciarSesion(TxtF_Correo_NomUsu.getText(), contraseniaStr, salt);
    }

    @FXML
    void handleBtnIniciarSesionAction(MouseEvent event) throws IOException {
        String nombreUsuario = new String(TxtF_Correo_NomUsu.getText());

        this.hiloLector.obtenerSalt(nombreUsuario);
    }

    @FXML
    void handleBtnRegistrarseAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Registro1.fxml"));
        Parent nuevaVista = loader.load();

        Controller_Registro1 controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_InicioSesion.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();

        // Usuario us = new Usuario();
        // us.setNombre("Prueba");
        // us.setApellidos("Registro 1");
        // us.setDni("22345678A");
        // us.setDireccion("c/Mi Casa, n 9");
        // us.setNombreUsuario("pruebareg1");
        // us.setCorreo("pruebareg1@gmail.com");
        // us.setCorreoPP("pruebareg1.pp@gmail.com");

        // byte[] salt = SecureUtils.getSalt();
        // String contraHash = SecureUtils.generate512("prueba1", salt);
        // us.setContrasenia(contraHash);
        // us.setSalt(Base64.getEncoder().encodeToString(salt));

        // ObjectMapper mapper = new ObjectMapper();
        // String usuarioJSON = mapper.writeValueAsString(us);

        // Mensaje msg = new Mensaje();
        // msg.setTipo("REGISTRAR_USUARIO");
        // msg.addParam(usuarioJSON);
        // this.dos.writeUTF(Serializador.codificarMensaje(msg));
    }

    @FXML
    void handleLblRecuperarContraseniaAction(MouseEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_RecuperarContrasenia1.fxml"));
        Parent nuevaVista = loader.load();

        Controller_RecuperarContrasenia1 controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_InicioSesion.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
    }

    public void inicioDeSesionIncorrecto() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Credenciales incorrectas");
        alert.setHeaderText(null);
        alert.setContentText("El nombre de usuario, el correo o la contraseña no son correctos");
        alert.getDialogPane().getStylesheets()
                .add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }

    public void usuarioBaneado() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Usuario baneado");
        alert.setHeaderText(null);
        alert.setContentText("El usuario con el que intenta iniciar sesión ha sido baneado, si cree que se le ha baneado por error contacte con el correo wheeltraderapp@gmail.com");
        alert.getDialogPane().getStylesheets()
                .add(getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("alert-error");
        alert.showAndWait();
    }

    public void siguientePaso() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent nuevaVista = loader.load();

        Stage stage = (Stage) Btn_InicioSesion.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public void irHomeModerador() throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        Parent nuevaVista = loader.load();

        Stage stage = (Stage) Btn_InicioSesion.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);

        stage.setScene(scene);
        stage.show();
        stage.centerOnScreen();
    }

    public String getNombreUsuarioCorreo() {
        return this.TxtF_Correo_NomUsu.getText();
    }

    public String getContrasenia() {
        return this.TxtF_Contrasenia.getText();
    }

    public void pedirUsuarioJSON() throws IOException {
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_USUARIO");
        msg.addParam(this.getNombreUsuarioCorreo());
        this.dos.writeUTF(Serializador.codificarMensaje(msg));
    }

    public void setHiloLector(Lector_InicioSesion hiloLector) {
        this.hiloLector = hiloLector;
    }
}

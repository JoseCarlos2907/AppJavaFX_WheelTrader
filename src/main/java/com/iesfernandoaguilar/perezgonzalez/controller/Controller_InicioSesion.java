package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia1;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro1;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Controller_InicioSesion implements ILogin, Initializable {
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

                // Se hace en un hilo secundario porque si intento crear el socket en el
                // principal
                // el que se queda pillado esperando seria el controller y no se mostraría la
                // interfaz.
                // En este caso se queda pillado el hilo secundario en caso de que no se pueda
                // conectar al arrancar la aplicación
                Thread hiloAux = new Thread(() -> {
                    try {
                        if (!prop.containsKey("PORT") || !prop.containsKey("ADDRESS")) {
                            // Creo un latch para sincronizar el hilo principal con el alert
                            CountDownLatch latch = new CountDownLatch(1);

                            Platform.runLater(() -> {
                                Alert alert = new Alert(AlertType.CONFIRMATION);
                                alert.setTitle("Configuración de conexión con el servidor");
                                alert.setHeaderText(null);
                                alert.setContentText(null);
                                alert.getDialogPane().getStylesheets().add(getClass().getResource("/styles/EstiloAlertConf.css").toExternalForm());
                                alert.getDialogPane().getStyleClass().add("alert");

                                TextField TxtF_Direccion = new TextField();
                                TxtF_Direccion.setPromptText("ej: 192.168.1.50");
                                TxtF_Direccion.getStyleClass().add("inputs");

                                TextField TxtF_Puerto = new TextField();
                                TxtF_Puerto.setPromptText("ej: 8888");
                                TxtF_Puerto.getStyleClass().add("inputs");

                                VBox vbox = new VBox(new Label("Introduce la dirección IP y el puerto del servidor: "),
                                        new Label("Dirección:"), TxtF_Direccion, new Label("Puerto:"), TxtF_Puerto);
                                vbox.setSpacing(10);
                                alert.getDialogPane().setContent(vbox);

                                ButtonType botonOK = new ButtonType("Aceptar", ButtonData.OK_DONE);
                                ButtonType botonCancel = new ButtonType("Cancelar", ButtonData.CANCEL_CLOSE);
                                
                                alert.getButtonTypes().clear();
                                alert.getButtonTypes().addAll(botonOK, botonCancel);
                                
                                DialogPane dialogPane = alert.getDialogPane();
                                dialogPane.lookupButton(botonOK).getStyleClass().add("botonOK");
                                dialogPane.lookupButton(botonCancel).getStyleClass().add("botonCancel");
                                

                                Optional<ButtonType> response = alert.showAndWait();

                                if (response.isPresent() && response.get() == botonOK) {
                                    prop.setProperty("ADDRESS", TxtF_Direccion.getText());
                                    prop.setProperty("PORT", TxtF_Puerto.getText());

                                    try {
                                        // Sobreescribo el contenido del properties en el archivo correspondiente
                                        prop.store(new FileOutputStream("src/main/resources/conf.properties"), null);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else if (response.isPresent() && response.get() == botonCancel) {
                                    // TODO: Cerrar bien la aplicación
                                    System.exit(0);
                                }

                                // Vuelvo al hilo principal
                                latch.countDown();
                            });

                            // Esperar a que el usuario responda
                            latch.await();
                        }

                        int serverPort = Integer.parseInt(prop.getProperty("PORT"));
                        String serverAddr = prop.getProperty("ADDRESS");

                        if (Session.getSocket() == null) {
                            Session.setSocket(new Socket(serverAddr, serverPort));
                        }

                        this.hiloLector = new Lector_InicioSesion(Session.getInputStream(), Session.getOutputStream(), prop);
                        this.hiloLector.setController(this);
                        this.hiloLector.start();
                        Session.setHiloLoginCreado();
                    } catch (IOException e) {
                        Platform.runLater(() -> {
                            AlertManager.alertError("Error en la conexión", "Ahora mismo el servidor con la dirección y puerto configurados no está disponible", getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
                            // TODO: Cerrar bien la aplicación
                            System.exit(0);
                        });
                    } catch (InterruptedException e) {
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
        AlertManager.alertError(
                "Credenciales incorrectas",
                "El nombre de usuario, el correo o la contraseña no son correctos",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
    }

    public void usuarioBaneado() {
        AlertManager.alertError(
                "Usuario baneado",
                "El usuario con el que intenta iniciar sesión ha sido baneado, si cree que se le ha baneado por error contacte con el correo wheeltraderapp@gmail.com",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm());
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

    public void irHomeModerador() throws IOException {
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

    public void setHiloLector(Lector_InicioSesion hiloLector) {
        this.hiloLector = hiloLector;
    }
}

package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Base64;
import java.util.Properties;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_InicioSesion;
import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia1;
import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia2;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro1;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro2;
import com.iesfernandoaguilar.perezgonzalez.interfaces.ILogin;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.SecureUtils;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Lector_InicioSesion extends Thread{
    private InputStream flujoInicioSesionEntrada;
    private OutputStream flujoInicioSesionSalida;

    private ILogin controller;

    private static String usuarioJSON;

    private DataOutputStream dos;
    private DataInputStream dis;

    private Properties confProperties;

    public Lector_InicioSesion(InputStream flujoInicioSesionEntrada, OutputStream flujoInicioSesionSalida, Properties confProperties) {
        this.flujoInicioSesionEntrada = flujoInicioSesionEntrada;
        this.flujoInicioSesionSalida = flujoInicioSesionSalida;
        this.controller = null;
        this.confProperties = confProperties;

        usuarioJSON = "";
    }

    @Override
    public void run(){
        this.dis = new DataInputStream(this.flujoInicioSesionEntrada);
        this.dos = new DataOutputStream(this.flujoInicioSesionSalida);
        boolean iniciaSesion = false;
        while (!iniciaSesion) {
            
            try {
                String linea = this.dis.readUTF();
                Mensaje msgServidor = Serializador.decodificarMensaje(linea);
                

                switch (msgServidor.getTipo()) {
                    case "ENVIA_SALT":
                        ((Controller_InicioSesion) this.controller).respuestaSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));
                        break;
                
                    case "INICIA_SESION":
                        if("si".equals(msgServidor.getParams().get(0))){
                            iniciaSesion = true;
                            usuarioJSON = msgServidor.getParams().get(1);
                        }else if("no".equals(msgServidor.getParams().get(0))){
                            Platform.runLater(() -> {
                                ((Controller_InicioSesion) this.controller).inicioDeSesionIncorrecto();
                            });
                        }else if("baneado".equals(msgServidor.getParams().get(0))){
                            Platform.runLater(() -> {
                                ((Controller_InicioSesion) this.controller).usuarioBaneado();
                            });
                        }
                        break;

                    case "DNI_EXISTE":
                        Platform.runLater(() -> {
                            if("si".equals(msgServidor.getParams().get(0))){
                                ((Controller_Registro1) this.controller).dniExistente();
                            }else if ("no".equals(msgServidor.getParams().get(0))){
                                try {
                                    this.controller.siguientePaso();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;

                    case "USUARIO_EXISTE":
                        Platform.runLater(() -> {
                            if("si".equals(msgServidor.getParams().get(0))){
                                ((Controller_Registro2) this.controller).usuarioExistente();
                            }else if("no".equals(msgServidor.getParams().get(0))){
                                try {
                                    this.controller.siguientePaso();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;

                    case "USUARIO_REGISTRADO":
                        Platform.runLater(() -> {
                            try {
                                this.controller.siguientePaso();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                        
                    case "CODIGO_ENVIADO":
                        Platform.runLater(() -> {
                            try {
                                this.controller.siguientePaso();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "CORREO_NO_EXISTE":
                        Platform.runLater(() -> {
                            ((Controller_RecuperarContrasenia1) this.controller).correoNoExiste();
                        });
                        break;
                        
                    case "CODIGO_CORRECTO":
                        Platform.runLater(() -> {
                            try {
                                ((Controller_RecuperarContrasenia2) this.controller).setSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));
                                this.controller.siguientePaso();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                        
                    case "CODIGO_INCORRECTO":
                        Platform.runLater(() -> {
                            ((Controller_RecuperarContrasenia2) this.controller).codigoIncorrecto();
                        });
                        break;

                    case "CONTRASENIA_REGISTRADA":
                        Platform.runLater(() -> {
                            try {
                                this.controller.siguientePaso();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;
                }

            } catch (EOFException e) {
                System.out.println("Se ha cerrado el flujo del socket");
                this.reconectar();
            } catch (IOException e) {
                System.out.println("Error de conexión: " + e.getMessage());
                this.reconectar();
            }
        }
        
        if(iniciaSesion){
            Platform.runLater(() -> {
                try {
                    ObjectMapper mapper = new ObjectMapper();
                    Usuario usuario = mapper.readValue(usuarioJSON, Usuario.class);
                    Session.iniciarSession(usuario);

                    if("MODERADOR".equals(usuario.getRol())){
                        ((Controller_InicioSesion) this.controller).irHomeModerador();
                    }else{
                        this.controller.siguientePaso();
                    }
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
        System.out.println("Inicia sesión");
    }

    public void setController(ILogin controller){
        this.controller = controller;
    }

    private void reconectar(){
        if(Session.getSocket() != null){
            try {
                Session.getSocket().close();
            } catch (IOException e) {
                System.out.println("Error al cerrar el socket al intentar reconectar");
            }
        }

        try {
            Socket socket = new Socket(this.confProperties.getProperty("ADDRESS"), Integer.parseInt(this.confProperties.getProperty("PORT")));
            Session.setSocket(socket);
            
            if (socket.isConnected()) {
                Session.setSocket(socket);
                this.dis = new DataInputStream(socket.getInputStream());
                this.dos = new DataOutputStream(socket.getOutputStream());
                System.out.println("Se te ha vuelto a conectar a la aplicación.");
            } else {
                System.out.println("No se ha podido establecer conexión.");
            }
        } catch (IOException e) {
            System.out.println("Error al intentar reconectar");
        }
    }

    private void enviarMensaje(Mensaje msg){
        if(Session.getSocket() != null && !Session.getSocket().isClosed()){
            try {
                this.dos.writeUTF(Serializador.codificarMensaje(msg));
                this.dos.flush();
            } catch (IOException e) {
                System.err.println("Error al enviar el mensaje");
            }
        }else{
            System.out.println("No se puede realizar esa acción por un error en la conexión");
        }
    }

    public void obtenerSalt(String nombreUsuario){
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_SALT");
        msg.addParam(nombreUsuario);
        this.enviarMensaje(msg);
    }

    public void iniciarSesion(String correo_NombreUsuario, String contraseniaHash, byte[] salt){
        Mensaje msg = new Mensaje();
        msg.setTipo("INICIAR_SESION");
        msg.addParam(correo_NombreUsuario);
        msg.addParam(SecureUtils.generate512(contraseniaHash, salt));
        this.enviarMensaje(msg);
    }

    public void comprobarDNI(String dni){
        Mensaje msg = new Mensaje();
        msg.setTipo("COMPROBAR_DNI");
        msg.addParam(dni);
        this.enviarMensaje(msg);
    }

    public void comprobarNombreUsuarioYCorreo(String nombreUsuario, String correo){
        Mensaje msg = new Mensaje();
        msg.setTipo("COMPROBAR_NOMUSU_CORREO");
        msg.addParam(nombreUsuario);
        msg.addParam(correo);
        this.enviarMensaje(msg);
    }

    public void registrarUsuario(String usuarioJSON){
        Mensaje msg = new Mensaje();
        msg.setTipo("REGISTRAR_USUARIO");
        msg.addParam(usuarioJSON);
        this.enviarMensaje(msg);
    }

    public void recuperarContrasenia(String correo){
        Mensaje msg = new Mensaje();
        msg.setTipo("RECUPERAR_CONTRASENIA");
        msg.addParam(correo);
        this.enviarMensaje(msg);
    }

    public void intentaCodigo(String codigo){
        Mensaje msg = new Mensaje();
        msg.setTipo("INTENTA_CODIGO");
        msg.addParam(codigo);
        this.enviarMensaje(msg);
    }

    public void reiniciarContrasenia(String contraseniaHash){
        Mensaje msg = new Mensaje();
        msg.setTipo("REINICIAR_CONTRASENIA");
        msg.addParam(contraseniaHash);
        this.enviarMensaje(msg);
    }
}

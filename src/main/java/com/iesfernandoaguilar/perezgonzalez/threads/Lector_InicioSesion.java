package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

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
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Lector_InicioSesion extends Thread{
    private InputStream flujoInicioSesion;

    private ILogin controller;

    private static String usuarioJSON;

    public Lector_InicioSesion(InputStream flujoInicioSesion) {
        this.flujoInicioSesion = flujoInicioSesion;
        this.controller = null;

        usuarioJSON = "";
    }

    @Override
    public void run(){
        DataInputStream dis = new DataInputStream(this.flujoInicioSesion);
        boolean iniciaSesion = false;
        while (!iniciaSesion) {
            
            try {
                String linea = dis.readUTF();
                Mensaje msgServidor = Serializador.decodificarMensaje(linea);
                

                switch (msgServidor.getTipo()) {
                    case "ENVIA_SALT":
                        // System.out.println("ENVIA_SALT");
                        ((Controller_InicioSesion) this.controller).respuestaSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));
                        break;
                
                    case "INICIA_SESION":
                        // System.out.println("INICIA_SESION");
                        if("si".equals(msgServidor.getParams().get(0))){
                            iniciaSesion = true;
                            usuarioJSON = msgServidor.getParams().get(1);
                        }else if("no".equals(msgServidor.getParams().get(0))){
                            Platform.runLater(() -> {
                                ((Controller_InicioSesion) this.controller).inicioDeSesionIncorrecto();
                            });
                        }
                        break;

                    case "DNI_EXISTE":
                        // System.out.println("DNI_EXISTE");
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
                break;
            } catch (IOException e) {
                System.err.println(e.getMessage());
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
}

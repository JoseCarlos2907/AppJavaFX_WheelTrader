package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;

import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia1;
import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia2;
import com.iesfernandoaguilar.perezgonzalez.controller.recuperarContrasenia.Controller_RecuperarContrasenia3;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro1;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro2;
import com.iesfernandoaguilar.perezgonzalez.controller.registro.Controller_Registro3;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Lector_InicioSesion extends Thread{
    private InputStream flujoInicioSesion;
    private Controller_InicioSesion controllerInicioSesion;

    private Controller_Registro1 controllerR1;
    private Controller_Registro2 controllerR2;
    private Controller_Registro3 controllerR3;

    private Controller_RecuperarContrasenia1 controllerRC1;
    private Controller_RecuperarContrasenia2 controllerRC2;
    private Controller_RecuperarContrasenia3 controllerRC3;

    private static String usuarioJSON;

    public Lector_InicioSesion(InputStream flujoInicioSesion, Controller_InicioSesion controllerInicioSesion) {
        this.flujoInicioSesion = flujoInicioSesion;
        this.controllerInicioSesion = controllerInicioSesion;

        this.controllerR1 = null;
        this.controllerR2 = null;
        this.controllerR3 = null;

        this.controllerRC1 = null;
        this.controllerRC2 = null;
        this.controllerRC3 = null;

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
                        this.controllerInicioSesion.respuestaSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));
                        break;
                
                    case "INICIA_SESION":
                        // System.out.println("INICIA_SESION");
                        if("si".equals(msgServidor.getParams().get(0))){
                            iniciaSesion = true;
                            usuarioJSON = msgServidor.getParams().get(1);
                        }else if("no".equals(msgServidor.getParams().get(0))){
                            Platform.runLater(() -> {
                                this.controllerInicioSesion.inicioDeSesionIncorrecto();
                            });
                        }
                        break;

                    case "DNI_EXISTE":
                        // System.out.println("DNI_EXISTE");
                        Platform.runLater(() -> {
                            if("si".equals(msgServidor.getParams().get(0))){
                                this.controllerR1.dniExistente();
                            }else if ("no".equals(msgServidor.getParams().get(0))){
                                try {
                                    this.controllerR1.siguientePaso();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;

                    case "USUARIO_EXISTE":
                        Platform.runLater(() -> {
                            if("si".equals(msgServidor.getParams().get(0))){
                                this.controllerR2.usuarioExistente();
                            }else if("no".equals(msgServidor.getParams().get(0))){
                                try {
                                    this.controllerR2.siguientePaso();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        break;

                    case "USUARIO_REGISTRADO":
                        Platform.runLater(() -> {
                            try {
                                this.controllerR3.siguientePaso();
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
                    this.controllerInicioSesion.abrirAplicacion();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }

    public void setInicioSesionController(Controller_InicioSesion controller){
        this.controllerInicioSesion = controller;
    }

    public void setRegistroController1(Controller_Registro1 controller){
        this.controllerR1 = controller;
    }

    public void setRegistroController2(Controller_Registro2 controller){
        this.controllerR2 = controller;
    }

    public void setRegistroController3(Controller_Registro3 controller){
        this.controllerR3 = controller;
    }

    public void setRecupContraController1(Controller_RecuperarContrasenia1 controller){
        this.controllerRC1 = controller;
    }

    public void setRecupContraController2(Controller_RecuperarContrasenia2 controller){
        this.controllerRC2 = controller;
    }

    public void setRecupContraController3(Controller_RecuperarContrasenia3 controller){
        this.controllerRC3 = controller;
    }
}

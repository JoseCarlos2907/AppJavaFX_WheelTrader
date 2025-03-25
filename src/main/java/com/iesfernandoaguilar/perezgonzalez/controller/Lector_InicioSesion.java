package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import com.fasterxml.jackson.databind.ObjectMapper;

import javafx.application.Platform;
import com.iesfernandoaguilar.perezgonzalez.model.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Lector_InicioSesion extends Thread{
    private InputStream flujoInicioSesion;
    private Controller_InicioSesion controllerInicioSesion;

    private static String usuarioJSON;

    public Lector_InicioSesion(InputStream flujoInicioSesion, Controller_InicioSesion controllerInicioSesion) {
        this.flujoInicioSesion = flujoInicioSesion;
        this.controllerInicioSesion = controllerInicioSesion;
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
                
                if("ENVIA_SALT".equals(msgServidor.getTipo())){
                    // System.out.println("ENVIA_SALT");
                    this.controllerInicioSesion.respuestaSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));

                }else if("INICIA_SESION".equals(msgServidor.getTipo())){
                    // System.out.println("INICIA_SESION");
                    if("si".equals(msgServidor.getParams().get(0))){
                        iniciaSesion = true;
                        usuarioJSON = msgServidor.getParams().get(1);
                    }else if("no".equals(msgServidor.getParams().get(0))){
                        Platform.runLater(() -> {
                            this.controllerInicioSesion.inicioDeSesionIncorrecto();
                        });
                    }
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
}

package controller;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

import javafx.application.Platform;
import model.Mensaje;
import model.Usuario;
import util.Serializador;
import util.Session;

public class Lector_InicioSesion extends Thread{
    private InputStream flujoInicioSesion;
    private Controller_InicioSesion controllerInicioSesion;

    public Lector_InicioSesion(InputStream flujoInicioSesion, Controller_InicioSesion controllerInicioSesion) {
        this.flujoInicioSesion = flujoInicioSesion;
        this.controllerInicioSesion = controllerInicioSesion;
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
                    this.controllerInicioSesion.respuestaSalt(Base64.getDecoder().decode(msgServidor.getParams().get(0)));

                }else if("INICIA_SESION".equals(msgServidor.getTipo())){
                    if("si".equals(msgServidor.getParams().get(0))){
                        iniciaSesion = true;
                    }else if("no".equals(msgServidor.getParams().get(0))){
                        this.controllerInicioSesion.inicioDeSesionIncorrecto();
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
                    Session.iniciarSession(new Usuario(this.controllerInicioSesion.getNombreUsuarioCorreo(), this.controllerInicioSesion.getContrasenia(), ""));
                    this.controllerInicioSesion.abrirAplicacion();
                } catch (IOException e) {
                    System.err.println(e.getMessage());
                }
            });
        }
    }
}

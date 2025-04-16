package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Home;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

public class Lector_App extends Thread{
    private IApp controller;

    private boolean cierraSesion;

    public Lector_App(){
        this.cierraSesion = false;
    }


    public void setController(IApp controller){
        this.controller = controller;
    }

    public void cerrarSesion(){
        this.cierraSesion = true;
    }

    @Override
    public void run() {
        try {
            DataInputStream dis = new DataInputStream(Session.getInputStream());
            while (!this.cierraSesion) {
                
                String linea = dis.readUTF();
                Mensaje msgServidor = Serializador.decodificarMensaje(linea);

                switch (msgServidor.getTipo()) {
                    case "BIENVENIDO":
                        ((Controller_Home) this.controller).bienvenida();
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

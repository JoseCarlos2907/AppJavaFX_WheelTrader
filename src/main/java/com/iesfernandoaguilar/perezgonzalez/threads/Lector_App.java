package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Home;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio2;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.application.Platform;

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
                    
                    case "DATOS_VALIDOS":
                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(0))){
                                    ((Controller_PublicarAnuncio) this.controller).siguientePaso();
                                }else if("no".equals(msgServidor.getParams().get(0))){
                                    ((Controller_PublicarAnuncio) this.controller).datosIncorrectos();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                        case "ANUNCIO_PUBLICADO":
                            Platform.runLater(() -> {
                                try {
                                    ((Controller_PublicarAnuncio2) this.controller).anuncioPublicado();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            break;
                        
                        case "ENVIA_ANUNCIOS":
                            List<byte[]> imagenes = new ArrayList<>();
                            int cantAnuncios = Integer.valueOf(msgServidor.getParams().get(1));
                            for (int i = 0; i < cantAnuncios; i++) {
                                int bytes = dis.readInt();
                                byte[] imagen = new byte[bytes];
                                dis.readFully(imagen);
                                imagenes.add(imagen);
                            }
                            Platform.runLater(() -> {
                                try {
                                    ((Controller_ListaAnuncios) this.controller).aniadirAnuncios(msgServidor.getParams().get(0), imagenes);
                                } catch (JsonMappingException e) {
                                    e.printStackTrace();
                                } catch (JsonProcessingException e) {
                                    e.printStackTrace();
                                }
                            });
                            break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

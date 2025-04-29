package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ConfUsuario;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Filtros;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_MisGuardados;
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

        
        String linea = "";
        try {
            DataInputStream dis = new DataInputStream(Session.getInputStream());
            while (!this.cierraSesion) {
                
                linea = dis.readUTF();
                // System.out.println(linea);
                Mensaje msgServidor = Serializador.decodificarMensaje(linea);

                switch (msgServidor.getTipo()) {
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
                        int cantAnuncios = Integer.valueOf(msgServidor.getParams().get(2));
                        System.out.println("Cantidad de anuncios traidos: " + cantAnuncios);
                        for (int i = 0; i < cantAnuncios; i++) {
                            int bytes = dis.readInt();
                            byte[] imagen = new byte[bytes];
                            dis.readFully(imagen);
                            imagenes.add(imagen);
                        }

                        String anunciosJSON = msgServidor.getParams().get(1);

                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(3))){
                                    if("Guardados".equals(msgServidor.getParams().get(0))){
                                        ((Controller_ConfUsuario) this.controller).irListaGuardados(anunciosJSON, imagenes);
                                    }else{
                                        ((Controller_Filtros) this.controller).irListaPublicados(anunciosJSON, imagenes);
                                    }
                                }else if("no".equals(msgServidor.getParams().get(3))){
                                    if("Guardados".equals(msgServidor.getParams().get(0))){
                                        ((Controller_MisGuardados) this.controller).aniadirAnuncios(anunciosJSON, imagenes, false);
                                    }else{
                                        ((Controller_ListaAnuncios) this.controller).aniadirAnuncios(anunciosJSON, imagenes, false);
                                    }
                                }
                            } catch (JsonMappingException e) {
                                e.printStackTrace();
                            } catch (JsonProcessingException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ANUNCIO_GUARDADO":
                        Platform.runLater(() -> {
                            ((Controller_ListaAnuncios) this.controller).avisoGuardado(true);
                        });
                        break;

                    case "ANUNCIO_ELIMINADO_GUARDADOS":
                        Platform.runLater(() -> {
                            ((Controller_ListaAnuncios) this.controller).avisoGuardado(false);
                        });
                        break;

                    default:
                        System.out.println("Problemas de lectura del flujo");
                        break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

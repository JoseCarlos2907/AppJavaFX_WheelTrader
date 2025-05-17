package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ConfUsuario;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_DetalleAnuncio;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Filtros;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_FormularioReporte;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Home;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_HomeModerador;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio2;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Reportes;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
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
                        // System.out.println("ENVIA_ANUNCIOS");
                        List<byte[]> imagenes = new ArrayList<>();
                        int cantAnuncios = Integer.valueOf(msgServidor.getParams().get(2));
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
                                    }else if("Publicados".equals(msgServidor.getParams().get(0))){
                                        ((Controller_ConfUsuario) this.controller).irListaPublicados(anunciosJSON, imagenes);
                                    }else if ("BarraBusquedaMod".equals(msgServidor.getParams().get(0))){
                                        ((Controller_HomeModerador) this.controller).aniadirAnuncios(anunciosJSON, imagenes);
                                    }else if ("BarraBusqueda".equals(msgServidor.getParams().get(0))){
                                        ((Controller_Home) this.controller).irListaAnuncios(anunciosJSON, imagenes);
                                    }else{
                                        ((Controller_Filtros) this.controller).irListaAnuncios(anunciosJSON, imagenes);
                                    }
                                }else if("no".equals(msgServidor.getParams().get(3))){
                                    IListaAnuncios controllerAux = (IListaAnuncios) this.controller;
                                    controllerAux.aniadirAnuncios(anunciosJSON, imagenes);
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

                    case "ENVIA_IMAGENES":
                        List<byte[]> imagenesAnuncio = new ArrayList<>();
                        int cantImagenes = Integer.valueOf(msgServidor.getParams().get(0));
                        for (int i = 0; i < cantImagenes; i++) {
                            int bytes = dis.readInt();
                            byte[] imagen = new byte[bytes];
                            dis.readFully(imagen);
                            imagenesAnuncio.add(imagen);
                        }

                        Platform.runLater(() -> {
                            try {
                                ((IListaAnuncios)this.controller).irDetalleAnuncio(imagenesAnuncio);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_REPORTES_MOD":
                        // System.out.println("ENVIA_REPORTES_MOD");
                        Platform.runLater(() -> {
                            try {
                                ((Controller_HomeModerador) this.controller).aniadirUsuario(msgServidor.getParams().get(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_ULTIMOS_REPORTES_MOD":
                        Platform.runLater(() -> {
                            try {
                                ((Controller_Reportes) this.controller).aniadirReportes(msgServidor.getParams().get(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "REPORTE_REALIZADO":
                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(0))){
                                    ((Controller_FormularioReporte) this.controller).reporteRealizado();
                                }else if("no".equals(msgServidor.getParams().get(0))){
                                    ((Controller_FormularioReporte) this.controller).reporteYaRealizado();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_PDF_ACUERDO":
                        int longitudDocumento = Integer.valueOf(msgServidor.getParams().get(0));
                        byte[] bytesDocumento = new byte[longitudDocumento];
                        dis.readFully(bytesDocumento);

                        Platform.runLater(() ->{
                            try {
                                ((Controller_DetalleAnuncio) this.controller).irCompraComprador(bytesDocumento);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "":
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

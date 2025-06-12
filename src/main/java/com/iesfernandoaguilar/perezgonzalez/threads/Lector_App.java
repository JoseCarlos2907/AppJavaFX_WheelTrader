package com.iesfernandoaguilar.perezgonzalez.threads;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ConfUsuario;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_DetalleAnuncio;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Filtros;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_FormularioReporte;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Home;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_HomeModerador;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_ListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_MisAnuncios;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_MisCompras;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_MisGuardados;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Notificaciones;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PagoPayPal;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PerfilUsuario;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_PublicarAnuncio2;
import com.iesfernandoaguilar.perezgonzalez.controller.Controller_Reportes;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Imagen;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Mensaje;
import com.iesfernandoaguilar.perezgonzalez.util.Serializador;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.application.Platform;

public class Lector_App extends Thread{
    private IApp controller;

    private boolean cierraSesion;

    private DataOutputStream dos;
    private DataInputStream dis;

    private Properties confProperties;

    public Lector_App() {
        this.cierraSesion = false;

        this.confProperties = new Properties();
        try {
            this.confProperties.load(new FileInputStream("/usr/share/wheeltrader/conf.properties"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setController(IApp controller){
        this.controller = controller;
    }

    @Override
    public void run() {
        String linea = "";
        try {
            this.dis = new DataInputStream(Session.getInputStream());
            this.dos = new DataOutputStream(Session.getOutputStream());
            while (!this.cierraSesion) {
                
                linea = this.dis.readUTF();
                System.out.println(linea);
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
                        for (int i = 0; i < cantAnuncios; i++) {
                            int bytes = this.dis.readInt();
                            byte[] imagen = new byte[bytes];
                            this.dis.readFully(imagen);
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
                                    }else if("PerfilUsuario".equals(msgServidor.getParams().get(0)) && this.controller instanceof Controller_ListaAnuncios){
                                        ((Controller_ListaAnuncios) this.controller).irPerfilUsuario(anunciosJSON, imagenes);
                                    }else if("PerfilUsuario".equals(msgServidor.getParams().get(0)) && this.controller instanceof Controller_PerfilUsuario){
                                        ((Controller_PerfilUsuario) this.controller).irPerfilUsuario(anunciosJSON, imagenes);
                                    }else if("PerfilUsuario".equals(msgServidor.getParams().get(0)) && this.controller instanceof Controller_HomeModerador){
                                        ((Controller_HomeModerador) this.controller).irPerfilUsuario(anunciosJSON, imagenes);
                                    }else if("PerfilUsuario".equals(msgServidor.getParams().get(0)) && this.controller instanceof Controller_MisGuardados){
                                        ((Controller_MisGuardados) this.controller).irPerfilUsuario(anunciosJSON, imagenes);
                                    }else if("PerfilUsuario".equals(msgServidor.getParams().get(0)) && this.controller instanceof Controller_MisAnuncios){
                                        ((Controller_MisAnuncios) this.controller).irPerfilUsuario(anunciosJSON, imagenes);
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
                            int bytes = this.dis.readInt();
                            byte[] imagen = new byte[bytes];
                            this.dis.readFully(imagen);
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
                        this.dis.readFully(bytesDocumento);

                        Platform.runLater(() ->{
                            try {
                                ((Controller_DetalleAnuncio) this.controller).irCompraComprador(bytesDocumento);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_NOTIFICACIONES":
                        String notificacionesJSON = msgServidor.getParams().get(0);
                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(1))){
                                    ((Controller_Home) this.controller).irListaNotificaciones(notificacionesJSON);
                                }else if("no".equals(msgServidor.getParams().get(1))){
                                    ((Controller_Notificaciones) this.controller).aniadirNotificaciones(notificacionesJSON);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_PDF_ACUERDO_VENDEDOR":
                        int longitudDocumentoVendedor = Integer.valueOf(msgServidor.getParams().get(0));
                        byte[] bytesDocumentoVendedor = new byte[longitudDocumentoVendedor];
                        this.dis.readFully(bytesDocumentoVendedor);

                        Platform.runLater(() ->{
                            try {
                                ((Controller_Notificaciones) this.controller).irCompraVendedor(bytesDocumentoVendedor);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_URL_PAGO":
                        Platform.runLater(() ->{
                            try {
                                ((Controller_Notificaciones) this.controller).irPagoPayPal(msgServidor.getParams().get(0));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_ESTADO_PAGO":
                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(0))){
                                    ((Controller_PagoPayPal) this.controller).irHome();
                                }else if("error".equals(msgServidor.getParams().get(0))){
                                    ((Controller_PagoPayPal) this.controller).error();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "ENVIA_SALT_REINICIO":
                        ((Controller_ConfUsuario) this.controller).reiniciarContrasenia(Base64.getDecoder().decode(msgServidor.getParams().get(0)));
                        break;

                    case "CONTRASENIA_REINICIADA":
                        Platform.runLater(()->{
                            ((Controller_ConfUsuario) this.controller).contraseniaReiniciada();
                        });
                        break;

                    case "ENVIA_VENTAS":
                        String ventasJSON = msgServidor.getParams().get(0);
                        Platform.runLater(() -> {
                            try {
                                if("si".equals(msgServidor.getParams().get(1))){
                                    ((Controller_ConfUsuario) this.controller).irListaCompras(ventasJSON);
                                }else if("no".equals(msgServidor.getParams().get(1))){
                                    ((Controller_MisCompras) this.controller).aniadirCompras(ventasJSON);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                        break;

                    case "SESION_CERRADA":
                        System.out.println("Sesión cerrada");
                        break;

                    default:
                        System.out.println("Problemas de lectura del flujo");
                        break;
                }
            }
        } catch (EOFException e) {
            System.out.println("Se ha cerrado el flujo del socket");
            this.reconectar();
        } catch (IOException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            this.reconectar();
        }
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

        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Hilo interrumpido: " + e.getMessage());
        }
    }

    private void enviarMensaje(Mensaje msg) throws IOException{
        if(Session.getSocket() != null && !Session.getSocket().isClosed() && Session.isHiloCreado()){
            this.dos.writeUTF(Serializador.codificarMensaje(msg));
            this.dos.flush();
        }else{
            System.out.println("No se puede realizar esa acción por un error en la conexión");
            Platform.runLater(() -> {
                AlertManager.alertError("Error al realizar la acción", "No se puede realizar esa acción por un error en la conexión", "");
            });
        }
    }

    public void cerrarSesion(Long idUsuario) throws IOException{
        this.cierraSesion = true;
        Mensaje msg = new Mensaje();
        msg.setTipo("CERRAR_SESION");
        msg.addParam(idUsuario.toString());
        this.enviarMensaje(msg);
    }

    public void comprobarDatosVehiculo(String valoresAnuncioJSON) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("COMPROBAR_DATOS_VEHICULO");
        msg.addParam(valoresAnuncioJSON);
        this.enviarMensaje(msg);
    }

    public void publicarAnuncio(String anuncioJSON, List<Imagen> imagenes) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("PUBLICAR_ANUNCIO");
        msg.addParam(anuncioJSON);
        msg.addParam(String.valueOf(imagenes.size()));

        this.enviarMensaje(msg);

        for (Imagen imagen : imagenes) {
            this.dos.writeInt(imagen.getImagen().length);
            this.dos.write(imagen.getImagen());
            this.dos.flush();
        }
    }

    public void obtenerAnuncios(String filtroJSON, String tipoFiltro, String primeraCarga, Long idUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ANUNCIOS");
        msg.addParam(filtroJSON);
        msg.addParam(tipoFiltro);
        msg.addParam(primeraCarga);
        msg.addParam(String.valueOf(idUsuario));
        this.enviarMensaje(msg);
    }

    public void guardarAnuncio(Long idAnuncio, String nombreUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("GUARDAR_ANUNCIO");
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(nombreUsuario);
        this.enviarMensaje(msg);
    }

    public void eliminarAnuncioGuardados(Long idAnuncio, String nombreUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("ELIMINAR_ANUNCIO_GUARDADOS");
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(nombreUsuario);
        this.enviarMensaje(msg);
    }

    public void obtenerImagenes(Long idAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
    
        msg.setTipo("OBTENER_IMAGENES");
        msg.addParam(String.valueOf(idAnuncio));
        this.enviarMensaje(msg);
    }

    public void obtenerReportesMod(String filtroJSON) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_REPORTES_MOD");
        msg.addParam(filtroJSON);
        this.enviarMensaje(msg);
    }

    public void obtenerUltimosReportesMod(String filtroJSON) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ULTIMOS_REPORTES_MOD");
        msg.addParam(filtroJSON);
        this.enviarMensaje(msg);
    }

    public void reportarUsuario(String reporteJSON) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("REPORTAR_USUARIO");
        msg.addParam(reporteJSON);
        this.enviarMensaje(msg);
    }

    public void banearUsuario(Long idUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("BANEAR_USUARIO");
        msg.addParam(String.valueOf(idUsuario));
        this.enviarMensaje(msg);
    }

    public void desbanearUsuario(Long idUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("DESBANEAR_USUARIO");
        msg.addParam(String.valueOf(idUsuario));
        this.enviarMensaje(msg);
    }

    public void obtenerPDFAcuerdo(Long idUsuarioComprador, Long idAnuncio, String tipoAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_PDF_ACUERDO");
        msg.addParam(String.valueOf(idUsuarioComprador));
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(tipoAnuncio);
        this.enviarMensaje(msg);
    }

    public void compradorOfreceCompra(byte[] bytesPdf, Long idComprador, Long idAnuncio, Long idVendedor) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("COMPRADOR_OFRECE_COMPRA");
        msg.addParam(String.valueOf(bytesPdf.length));
        msg.addParam(String.valueOf(idComprador));
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(String.valueOf(idVendedor));

        this.enviarMensaje(msg);

        this.dos.write(bytesPdf);
        this.dos.flush();
    }

    public void vendedorConfirmaCompra(byte[] bytesPdf, Long idComprador, Long idAnuncio, Long idVendedor, Long idNotificacion) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("VENDEDOR_CONFIRMA_COMPRA");
        msg.addParam(String.valueOf(bytesPdf.length));
        msg.addParam(String.valueOf(idComprador));
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(String.valueOf(idVendedor));
        msg.addParam(String.valueOf(idNotificacion));

        this.enviarMensaje(msg);

        this.dos.write(bytesPdf);
        this.dos.flush();
    }

    public void vendedorRechazaCompra(Long idComprador, Long idAnuncio, Long idVendedor, Long idNotificacion) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("VENDEDOR_RECHAZA_COMPRA");
        msg.addParam(String.valueOf(idComprador));
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(String.valueOf(idVendedor));
        msg.addParam(String.valueOf(idNotificacion));
        this.enviarMensaje(msg);
    }

    public void obtenerPDFAcuerdoVendedor(Long idUsuarioComprador, Long idAnuncio, String tipoAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_PDF_ACUERDO_VENDEDOR");
        msg.addParam(String.valueOf(idUsuarioComprador));
        msg.addParam(String.valueOf(idAnuncio));
        msg.addParam(tipoAnuncio);
        this.enviarMensaje(msg);
    }

    public void obtenerNotificaciones(String filtroJSON, String primeraCarga) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_NOTIFICACIONES");
        msg.addParam(filtroJSON);
        msg.addParam(primeraCarga);
        this.enviarMensaje(msg);
    }

    public void cambiarEstadoNotificacion(Long idNotificacion, String estado) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("CAMBIAR_ESTADO_NOTIFICACION");
        msg.addParam(String.valueOf(idNotificacion));
        msg.addParam(estado);
        this.enviarMensaje(msg);
    }

    public void usuarioPaga(Long idComprador, Long idVendedor, Long idAnuncio, double precio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("USUARIO_PAGA");
        msg.addParam(String.valueOf(idComprador));
        msg.addParam(String.valueOf(idVendedor));
        msg.addParam(String.valueOf(precio));
        msg.addParam(String.valueOf(idAnuncio));
        this.enviarMensaje(msg);
    }

    public void obtenerEstadoPago(Long idNotificacion, double precio, Long idAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_ESTADO_PAGO");
        msg.addParam(String.valueOf(idNotificacion));
        msg.addParam(String.valueOf(precio));
        msg.addParam(String.valueOf(idAnuncio));
        this.enviarMensaje(msg);
    }

    public void cancelarAnuncio(Long idAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("CANCELAR_ANUNCIO");
        msg.addParam(String.valueOf(idAnuncio));
        this.enviarMensaje(msg);
    }

    public void reanudarAnuncio(Long idAnuncio) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("REANUDAR_ANUNCIO");
        msg.addParam(String.valueOf(idAnuncio));
        this.enviarMensaje(msg);
    }

    public void obtenerSaltReinicio(String nombreUsuario) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_SALT_REINICIO");
        msg.addParam(nombreUsuario);
        this.enviarMensaje(msg);
    }

    public void reiniciarContrasenia(String nombreUsuario, String contraseniaHash) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("REINICIAR_CONTRASENIA");
        msg.addParam(nombreUsuario);
        msg.addParam(contraseniaHash);
        this.enviarMensaje(msg);
    }

    public void obtenerVentas(String filtroJSON, String primeraCarga) throws IOException{
        Mensaje msg = new Mensaje();
        msg.setTipo("OBTENER_VENTAS");
        msg.addParam(filtroJSON);
        msg.addParam(primeraCarga);
        this.enviarMensaje(msg);
    }
}

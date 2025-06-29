package com.iesfernandoaguilar.perezgonzalez.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.iesfernandoaguilar.perezgonzalez.model.Usuario;

public class Session {
    private static Socket socket = null;
    private static Usuario usuario = null;
    private static boolean hiloAppCreado = false;
    private static boolean hiloLoginCreado = false;

    public static void iniciarSession(Usuario usuarioIni) {
        usuario = usuarioIni;
    }

    public static void cerrarSession() {
        usuario = null;
    }

    public static Usuario getUsuario() {
        return usuario;
    }

    public boolean haIniciadoSesion() {
        return usuario != null;
    }

    public static void setSocket(Socket soc){
        socket = soc;
    }

    public static Socket getSocket(){
        return socket;
    }

    public static OutputStream getOutputStream() throws IOException{
        if(socket == null || socket.isClosed()){
            throw new IOException("Error al obtener el flujo de salida del socket");
        }
        return socket.getOutputStream();
    }

    public static InputStream getInputStream() throws IOException{
        if(socket == null || socket.isClosed()){
            throw new IOException("Error al obtener el flujo de salida del socket");
        }
        return socket.getInputStream();
    }

    public static void setHiloCreado(){
        hiloAppCreado = true;
    }

    public static void setHiloNoCreado(){
        hiloAppCreado = false;
    }

    public static boolean isHiloCreado(){
        return hiloAppCreado;
    }

    public static void setHiloLoginCreado(){
        hiloLoginCreado = true;
    }

    public static void setHiloLoginNoCreado(){
        hiloLoginCreado = false;
    }

    public static boolean isHiloLoginCreado(){
        return hiloLoginCreado;
    }
}

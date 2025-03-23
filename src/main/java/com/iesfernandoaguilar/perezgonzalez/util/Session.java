package com.iesfernandoaguilar.perezgonzalez.util;

import com.iesfernandoaguilar.perezgonzalez.model.Usuario;

public class Session {
    private static Usuario usuario;

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
}

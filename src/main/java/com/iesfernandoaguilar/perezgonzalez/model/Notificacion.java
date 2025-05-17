package com.iesfernandoaguilar.perezgonzalez.model;

public class Notificacion {
    // *-- Atributos --* //
    private Long idNotificacion;

    private String titulo;

    private String mensaje;

    private String estado;

    private String tipo;


    // *-- Relaciones --* //
    private Usuario usuarioEnvia;

    private Usuario usuarioRecibe;

    private Anuncio anuncio;


    // *-- Constructores --* //
    public Notificacion() {}

    public Notificacion(String titulo, String mensaje, String estado, String tipo){
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.estado = estado;
        this.tipo = tipo;
    }

    // *-- Getters y Setters --* //
    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Usuario getUsuarioEnvia() {
        return usuarioEnvia;
    }

    public void setUsuarioEnvia(Usuario usuarioEnvia) {
        this.usuarioEnvia = usuarioEnvia;
    }

    public Usuario getUsuarioRecibe() {
        return usuarioRecibe;
    }

    public void setUsuarioRecibe(Usuario usuarioRecibe) {
        this.usuarioRecibe = usuarioRecibe;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    // *-- Métodos --* //
    public void parse(Notificacion notificacion) {
        this.idNotificacion = notificacion.getIdNotificacion();
        this.titulo = notificacion.getTitulo();
        this.mensaje = notificacion.getMensaje();
        this.estado = notificacion.getEstado();
        this.tipo = notificacion.getTipo();

        this.usuarioEnvia = null;
        this.usuarioRecibe = null;
        this.anuncio = null;
    }
}

package com.iesfernandoaguilar.perezgonzalez.model;

public class Notificacion {
    private Long idNotificacion;

    private Long idUsuarioEnvia;

    private Long idUsuarioRecibe;

    private String titulo;

    private String mensaje;

    private String estado;

    private String tipo;

    public Notificacion(String titulo, String mensaje, String estado, String tipo) {
        this.idNotificacion = -1L;
        this.idUsuarioEnvia = -1L;
        this.idUsuarioRecibe = -1L;
        this.titulo = titulo;
        this.mensaje = mensaje;
        this.estado = estado;
        this.tipo = tipo;
    }

    public Long getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Long idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public Long getIdUsuarioEnvia() {
        return idUsuarioEnvia;
    }

    public void setIdUsuarioEnvia(Long idUsuarioEnvia) {
        this.idUsuarioEnvia = idUsuarioEnvia;
    }

    public Long getIdUsuarioRecibe() {
        return idUsuarioRecibe;
    }

    public void setIdUsuarioRecibe(Long idUsuarioRecibe) {
        this.idUsuarioRecibe = idUsuarioRecibe;
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

    
}

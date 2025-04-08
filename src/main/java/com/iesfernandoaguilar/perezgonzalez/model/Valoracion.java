package com.iesfernandoaguilar.perezgonzalez.model;

public class Valoracion {
    private Long idValorador;

    private Long idValorado;

    private int valoracion;

    private String comentario;

    public Valoracion(int valoracion, String comentario) {
        this.valoracion = valoracion;
        this.comentario = comentario;
    }

    public Long getIdValorador() {
        return idValorador;
    }

    public void setIdValorador(Long idValorador) {
        this.idValorador = idValorador;
    }

    public Long getIdValorado() {
        return idValorado;
    }

    public void setIdValorado(Long idValorado) {
        this.idValorado = idValorado;
    }

    public int getValoracion() {
        return valoracion;
    }

    public void setValoracion(int valoracion) {
        this.valoracion = valoracion;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    
}

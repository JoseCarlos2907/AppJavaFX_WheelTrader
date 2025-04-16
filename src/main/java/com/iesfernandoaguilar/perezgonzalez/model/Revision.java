package com.iesfernandoaguilar.perezgonzalez.model;

public class Revision {
    // *-- Atributos --* //
    private Long idRevision;

    private String imagenBase64;

    private String asunto;

    private String descripcion;

    private String estado;


    // *-- Relaciones --* //
    private Reunion reunion;


    // *-- Constructores --* //
    public Revision(){}


    // *-- Getters y Setters --* //
    public Long getIdRevision() {
        return idRevision;
    }

    public void setIdRevision(Long idRevision) {
        this.idRevision = idRevision;
    }

    public String getImagenBase64() {
        return imagenBase64;
    }

    public void setImagenBase64(String imagenBase64) {
        this.imagenBase64 = imagenBase64;
    }

    public String getAsunto() {
        return asunto;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    // *-- Métodos --* //
    public void parse(Revision revision){
        this.idRevision = revision.getIdRevision();
        this.imagenBase64 = revision.getImagenBase64();
        this.asunto = revision.getAsunto();
        this.descripcion = revision.getDescripcion();
        this.estado = revision.getEstado();

        this.reunion = null;
    }
}

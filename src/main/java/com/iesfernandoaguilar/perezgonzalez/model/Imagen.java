package com.iesfernandoaguilar.perezgonzalez.model;

public class Imagen {
    // *-- Atributos --* //
    private Long idImagen;

    private byte[] imagen;


    // *-- Relaciones --* //
    private Anuncio anuncio;


    // *-- Constructores --* //
    public Imagen() {}


    // *-- Getters y Setters --* //
    public Long getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Long idImagen) {
        this.idImagen = idImagen;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    // *-- Métodos --* //
    public void parse(Imagen imagen){
        this.idImagen = imagen.getIdImagen();
        this.imagen = imagen.getImagen();

        this.anuncio = null;
    }
}

package com.iesfernandoaguilar.perezgonzalez.model;

public class ValorCaracteristica {
    // *-- Atributos --* //
    private Long idValorCaracteristica;

    private String valor;


    // *-- Relaciones --* //
    private Anuncio anuncio;

    private Caracteristica caracteristica;


    // *-- Constructores --* //
    public ValorCaracteristica() {}


    // *-- Getters y Setters --* //
    public Long getIdValorCaracteristica() {
        return idValorCaracteristica;
    }

    public void setIdValorCaracteristica(Long idValorCaracteristica) {
        this.idValorCaracteristica = idValorCaracteristica;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    // *-- Métodos --* //
    public void parse(ValorCaracteristica valorCaracteristica) {
        this.idValorCaracteristica = valorCaracteristica.getIdValorCaracteristica();
        this.valor = valorCaracteristica.getValor();

        this.anuncio = null;
        this.caracteristica = null;
    }
}

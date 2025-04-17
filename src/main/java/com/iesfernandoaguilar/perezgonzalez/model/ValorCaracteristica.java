package com.iesfernandoaguilar.perezgonzalez.model;

public class ValorCaracteristica {
    // *-- Atributos --* //
    private Long idValorCaracteristica;

    private String valor;


    // *-- Relaciones --* //
    private Anuncio anuncio;

    private String nombreCaracteristica;


    // *-- Constructores --* //
    public ValorCaracteristica() {}

    public ValorCaracteristica(String valor, String nombreCaracteristica){
        this.valor = valor;
        this.nombreCaracteristica = nombreCaracteristica;
    }


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

    public String getNombreCaracteristica() {
        return this.nombreCaracteristica;
    }

    public void setNombreCaracteristica(String nombreCaracteristica) {
        this.nombreCaracteristica = nombreCaracteristica;
    }

    // *-- Métodos --* //
    public void parse(ValorCaracteristica valorCaracteristica) {
        this.idValorCaracteristica = valorCaracteristica.getIdValorCaracteristica();
        this.valor = valorCaracteristica.getValor();

        this.anuncio = null;
        this.nombreCaracteristica = null;
    }
}

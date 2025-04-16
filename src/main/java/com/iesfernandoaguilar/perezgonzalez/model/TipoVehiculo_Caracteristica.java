package com.iesfernandoaguilar.perezgonzalez.model;

public class TipoVehiculo_Caracteristica {
    // *-- Atributos --* //
    private Long idTipoVehiculo_Caracteristica;

    private boolean obligatorio;


    // *-- Relaciones --* //
    private TipoVehiculo tipoVehiculo;

    private Caracteristica caracteristica;


    // *-- Constructores --* //
    public TipoVehiculo_Caracteristica() {}


    // *-- Getters y Setters --* //
    public Long getIdTipoVehiculo_Caracteristica() {
        return idTipoVehiculo_Caracteristica;
    }

    public void setIdTipoVehiculo_Caracteristica(Long idTipoVehiculo_Caracteristica) {
        this.idTipoVehiculo_Caracteristica = idTipoVehiculo_Caracteristica;
    }

    public boolean isObligatorio() {
        return obligatorio;
    }

    public void setObligatorio(boolean obligatorio) {
        this.obligatorio = obligatorio;
    }

    public TipoVehiculo getTipoVehiculo() {
        return tipoVehiculo;
    }

    public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
        this.tipoVehiculo = tipoVehiculo;
    }

    public Caracteristica getCaracteristica() {
        return caracteristica;
    }

    public void setCaracteristica(Caracteristica caracteristica) {
        this.caracteristica = caracteristica;
    }

    // *-- Métodos --* //
    public void parse(TipoVehiculo_Caracteristica tipoVehiculo_caracteristica){
        this.idTipoVehiculo_Caracteristica = tipoVehiculo_caracteristica.getIdTipoVehiculo_Caracteristica();
        this.obligatorio = tipoVehiculo_caracteristica.isObligatorio();

        this.tipoVehiculo = null;
        this.caracteristica = null;
    }
}

package com.iesfernandoaguilar.perezgonzalez.model;

public class Cuestionario {
    // *-- Atributos --* //
    private Long idCuestionario;

    private int comodidad;

    private int rendimiento;

    private int cuidado;

    private int valoracionGeneral;


    // *-- Relaciones --* //
    private Reunion reunion;


    // *-- Constructores --* //
    public Cuestionario() {}


    // *-- Getters y Setters --* //
    public Long getIdCuestionario() {
        return idCuestionario;
    }

    public void setIdCuestionario(Long idCuestionario) {
        this.idCuestionario = idCuestionario;
    }

    public int getComodidad() {
        return comodidad;
    }

    public void setComodidad(int comodidad) {
        this.comodidad = comodidad;
    }

    public int getRendimiento() {
        return rendimiento;
    }

    public void setRendimiento(int rendimiento) {
        this.rendimiento = rendimiento;
    }

    public int getCuidado() {
        return cuidado;
    }

    public void setCuidado(int cuidado) {
        this.cuidado = cuidado;
    }

    public int getValoracionGeneral() {
        return valoracionGeneral;
    }

    public void setValoracionGeneral(int valoracionGeneral) {
        this.valoracionGeneral = valoracionGeneral;
    }

    public Reunion getReunion() {
        return reunion;
    }

    public void setReunion(Reunion reunion) {
        this.reunion = reunion;
    }

    // *-- Métodos --* //
    public void parse(Cuestionario cuestionario){
        this.idCuestionario = cuestionario.getIdCuestionario();
        this.comodidad = cuestionario.getComodidad();
        this.rendimiento = cuestionario.getRendimiento();
        this.cuidado = cuestionario.getCuidado();
        this.valoracionGeneral = cuestionario.getValoracionGeneral();

        this.reunion = null;
    }
}

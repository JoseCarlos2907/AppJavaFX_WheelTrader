package com.iesfernandoaguilar.perezgonzalez.model;

public class Reporte {
    // *-- Atributos --* //
    private Long idReporte;

    private String motivo;

    private String explicacion;


    // *-- Relaciones --* //
    private Usuario usuarioEnvia;

    private Usuario usuarioRecibe;


    // *-- Constructores --* //
    public Reporte() {}

    public Reporte(String motivo, String explicacion){
        this.motivo = motivo;
        this.explicacion = explicacion;
    }

    // *-- Getters y Setters --* //
    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
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

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }
    
    // *-- Métodos --* //
    public void parse(Reporte reporte) {
        this.idReporte = reporte.getIdReporte();
        this.motivo = reporte.getMotivo();
        this.explicacion = reporte.getExplicacion();

        this.usuarioEnvia = null;
        this.usuarioRecibe = null;
    }
}

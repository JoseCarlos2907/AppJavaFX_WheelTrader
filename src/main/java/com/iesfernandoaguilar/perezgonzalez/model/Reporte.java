package com.iesfernandoaguilar.perezgonzalez.model;

public class Reporte {
    private Long idReporte;

    private Long idUsuarioReportado;

    private Long idUsuarioReporta;

    private String motivo;

    private String explicacion;

    public Reporte(String motivo, String explicacion) {
        this.idReporte = -1L;
        this.idUsuarioReportado = -1L;
        this.idUsuarioReporta = -1L;
        this.motivo = motivo;
        this.explicacion = explicacion;
    }

    public Long getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(Long idReporte) {
        this.idReporte = idReporte;
    }

    public Long getIdUsuarioReportado() {
        return idUsuarioReportado;
    }

    public void setIdUsuarioReportado(Long idUsuarioReportado) {
        this.idUsuarioReportado = idUsuarioReportado;
    }

    public Long getIdUsuarioReporta() {
        return idUsuarioReporta;
    }

    public void setIdUsuarioReporta(Long idUsuarioReporta) {
        this.idUsuarioReporta = idUsuarioReporta;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public String getExplicacion() {
        return explicacion;
    }

    public void setExplicacion(String explicacion) {
        this.explicacion = explicacion;
    }

    
}

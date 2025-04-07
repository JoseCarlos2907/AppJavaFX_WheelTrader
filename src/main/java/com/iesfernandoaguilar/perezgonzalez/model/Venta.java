package com.iesfernandoaguilar.perezgonzalez.model;

import java.time.LocalDateTime;

public class Venta {
    private Long idVenta;

    private Long idVendedor;

    private Long idComprador;

    private Long idAnuncio;

    private LocalDateTime fechaCompra;

    private LocalDateTime fechaFinGarantia;

    private String tipo;

    public Venta(LocalDateTime fechaCompra, LocalDateTime fechaFinGarantia, String tipo) {
        this.idVenta = -1L;
        this.idVendedor = -1L;
        this.idComprador = -1L;
        this.idAnuncio = -1L;
        this.fechaCompra = fechaCompra;
        this.fechaFinGarantia = fechaFinGarantia;
        this.tipo = tipo;
    }

    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public Long getIdComprador() {
        return idComprador;
    }

    public void setIdComprador(Long idComprador) {
        this.idComprador = idComprador;
    }

    public Long getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(Long idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public LocalDateTime getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(LocalDateTime fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public LocalDateTime getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(LocalDateTime fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    
}

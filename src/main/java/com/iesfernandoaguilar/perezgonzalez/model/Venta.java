package com.iesfernandoaguilar.perezgonzalez.model;

import java.util.List;

public class Venta {
    // *-- Atributos --* //
    private Long idVenta;

    private String fechaFinGarantia;

    // *-- Relaciones --* //
    private Anuncio anuncio;

    private Usuario vendedor;

    private Usuario comprador;

    private List<Pago> pagos;

    // *-- Constructores --* //
    public Venta() {}

    public Venta(String fechaFinGarantia){
        this.fechaFinGarantia = fechaFinGarantia;
    }


    // *-- Getters y Setters --* //
    public Long getIdVenta() {
        return idVenta;
    }

    public void setIdVenta(Long idVenta) {
        this.idVenta = idVenta;
    }

    public String getFechaFinGarantia() {
        return fechaFinGarantia;
    }

    public void setFechaFinGarantia(String fechaFinGarantia) {
        this.fechaFinGarantia = fechaFinGarantia;
    }

    public Anuncio getAnuncio() {
        return anuncio;
    }

    public void setAnuncio(Anuncio anuncio) {
        this.anuncio = anuncio;
    }

    public Usuario getVendedor() {
        return vendedor;
    }

    public void setVendedor(Usuario vendedor) {
        this.vendedor = vendedor;
    }

    public Usuario getComprador() {
        return comprador;
    }

    public void setComprador(Usuario comprador) {
        this.comprador = comprador;
    }

    public List<Pago> getPagos() {
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }

    public void addPago(Pago pago) {
        this.pagos.add(pago);
    }


    // *-- Métodos --* //
    public void parse(Venta venta) {
        this.idVenta = venta.getIdVenta();
        this.fechaFinGarantia = venta.getFechaFinGarantia();

        this.anuncio = null;
        this.vendedor = null;
        this.comprador = null;
        this.pagos = null;
    }
}

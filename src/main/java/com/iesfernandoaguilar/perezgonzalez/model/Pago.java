package com.iesfernandoaguilar.perezgonzalez.model;

import java.time.LocalDateTime;

public class Pago {
    // *-- Atributos --* //
    private Long idPago;

    private double cantidad;

    private LocalDateTime fechaPago;


    // *-- Relaciones --* //
    private Venta venta;


    // *-- Constructores --* //
    public Pago() {}


    // *-- Getters y Setters --* //
    public Long getIdPago() {
        return idPago;
    }

    public void setIdPago(Long idPago) {
        this.idPago = idPago;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDateTime getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDateTime fechaPago) {
        this.fechaPago = fechaPago;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    // *-- Métodos --* //
    public void parse(Pago pago){
        this.idPago = pago.getIdPago();
        this.cantidad = pago.getCantidad();
        this.fechaPago = pago.getFechaPago();

        this.venta = null;
    }
}

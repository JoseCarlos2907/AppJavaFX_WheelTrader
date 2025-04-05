package com.iesfernandoaguilar.perezgonzalez.model;

import java.time.LocalDateTime;

public class Anuncio {
    private Long idAnuncio;

    private LocalDateTime fechaPublicacion;

    private LocalDateTime fechaExpiracion;

    private String descripcion;

    private double precioAlContado;

    private double precioMensual;

    private String provincia;

    private String ciudad;

    private String estado;


    // Datos vehiculo
    private String matricula;

    private String marca;

    private String modelo;

    private int cv;

    private int anio;

    private int puertas;

    private int plazas;

    private String tipoMarchas;

    private int cantMarchas;

    private String categoria;

    private String numBastidor;

    private int kilometraje;

    private String color;

    private String combustible;
    
    private Long idVendedor;

    public Anuncio(LocalDateTime fechaPublicacion, LocalDateTime fechaExpiracion, String descripcion, double precioAlContado, double precioMensual, String provincia, String ciudad, String estado, String matricula, String marca, String modelo, int cv, int anio, int puertas, int plazas, String tipoMarchas, int cantMarchas, String categoria, String numBastidor, int kilometraje, String color, String combustible) {
        this.idAnuncio = -1L;
        this.fechaPublicacion = fechaPublicacion;
        this.fechaExpiracion = fechaExpiracion;
        this.descripcion = descripcion;
        this.precioAlContado = precioAlContado;
        this.precioMensual = precioMensual;
        this.provincia = provincia;
        this.ciudad = ciudad;
        this.estado = estado;
        this.matricula = matricula;
        this.marca = marca;
        this.modelo = modelo;
        this.cv = cv;
        this.anio = anio;
        this.puertas = puertas;
        this.plazas = plazas;
        this.tipoMarchas = tipoMarchas;
        this.cantMarchas = cantMarchas;
        this.categoria = categoria;
        this.numBastidor = numBastidor;
        this.kilometraje = kilometraje;
        this.color = color;
        this.combustible = combustible;
        this.idVendedor = -1L;
    }

    public Long getIdAnuncio() {
        return idAnuncio;
    }

    public void setIdAnuncio(Long idAnuncio) {
        this.idAnuncio = idAnuncio;
    }

    public LocalDateTime getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(LocalDateTime fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public LocalDateTime getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDateTime fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecioAlContado() {
        return precioAlContado;
    }

    public void setPrecioAlContado(double precioAlContado) {
        this.precioAlContado = precioAlContado;
    }

    public double getPrecioMensual() {
        return precioMensual;
    }

    public void setPrecioMensual(double precioMensual) {
        this.precioMensual = precioMensual;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Long getIdVendedor() {
        return idVendedor;
    }

    public void setIdVendedor(Long idVendedor) {
        this.idVendedor = idVendedor;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getCv() {
        return cv;
    }

    public void setCv(int cv) {
        this.cv = cv;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public int getPuertas() {
        return puertas;
    }

    public void setPuertas(int puertas) {
        this.puertas = puertas;
    }

    public int getPlazas() {
        return plazas;
    }

    public void setPlazas(int plazas) {
        this.plazas = plazas;
    }

    public String getTipoMarchas() {
        return tipoMarchas;
    }

    public void setTipoMarchas(String tipoMarchas) {
        this.tipoMarchas = tipoMarchas;
    }

    public int getCantMarchas() {
        return cantMarchas;
    }

    public void setCantMarchas(int cantMarchas) {
        this.cantMarchas = cantMarchas;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNumBastidor() {
        return numBastidor;
    }

    public void setNumBastidor(String numBastidor) {
        this.numBastidor = numBastidor;
    }

    public int getKilometraje() {
        return kilometraje;
    }

    public void setKilometraje(int kilometraje) {
        this.kilometraje = kilometraje;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    
}

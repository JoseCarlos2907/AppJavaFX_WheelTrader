package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import java.util.ArrayList;
import java.util.List;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroTodo implements IFiltro{
    private String marca;
    private String modelo;
    private int anioMinimo;
    private int anioMaximo;
    private String provincia;
    private String ciudad;
    private double precioMinimo;
    private double precioMaximo;
    private List<String> tiposVehiculo;
    private int pagina;
    private int cantidadPorPagina;

    public FiltroTodo(){
        this.tiposVehiculo = new ArrayList<>();
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

    public int getAnioMinimo() {
        return anioMinimo;
    }

    public void setAnioMinimo(int anioMinimo) {
        this.anioMinimo = anioMinimo;
    }

    public int getAnioMaximo() {
        return anioMaximo;
    }

    public void setAnioMaximo(int anioMaximo) {
        this.anioMaximo = anioMaximo;
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

    public double getPrecioMinimo() {
        return precioMinimo;
    }

    public void setPrecioMinimo(double precioMinimo) {
        this.precioMinimo = precioMinimo;
    }

    public double getPrecioMaximo() {
        return precioMaximo;
    }

    public void setPrecioMaximo(double precioMaximo) {
        this.precioMaximo = precioMaximo;
    }

    public List<String> getTiposVehiculo() {
        return tiposVehiculo;
    }

    public void setTiposVehiculo(List<String> tiposVehiculo) {
        this.tiposVehiculo = tiposVehiculo;
    }

    public void addTipoVehiculo(String tipo){
        this.tiposVehiculo.add(tipo);
    }

    public int getPagina() {
        return pagina;
    }

    public void setPagina(int pagina) {
        this.pagina = pagina;
    }

    public int getCantidadPorPagina() {
        return cantidadPorPagina;
    }

    public void setCantidadPorPagina(int cantidadPorPagina) {
        this.cantidadPorPagina = cantidadPorPagina;
    }

    @Override
    public void siguientePagina() {
        this.pagina++;
    }

    @Override
    public String getTipoFiltro() {
        return "Todo";
    }
}

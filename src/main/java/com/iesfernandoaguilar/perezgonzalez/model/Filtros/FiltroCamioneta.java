package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroCamioneta implements IFiltro{
    private String marca;
    private String modelo;
    private int anioMinimo;
    private int anioMaximo;
    private int kmMinimo;
    private int kmMaximo;
    private String tipoCombustible;
    private int cvMinimo;
    private int cvMaximo;
    private int cantMarchas;
    private int nPuertas;
    private String provincia;
    private String ciudad;
    private String traccion;
    private int pagina;
    private int cantidadPorPagina;

    public FiltroCamioneta() {}

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

    public int getKmMinimo() {
        return kmMinimo;
    }

    public void setKmMinimo(int kmMinimo) {
        this.kmMinimo = kmMinimo;
    }

    public int getKmMaximo() {
        return kmMaximo;
    }

    public void setKmMaximo(int kmMaximo) {
        this.kmMaximo = kmMaximo;
    }

    public String getTipoCombustible() {
        return tipoCombustible;
    }

    public void setTipoCombustible(String tipoCombustible) {
        this.tipoCombustible = tipoCombustible;
    }

    public int getCvMinimo() {
        return cvMinimo;
    }

    public void setCvMinimo(int cvMinimo) {
        this.cvMinimo = cvMinimo;
    }

    public int getCvMaximo() {
        return cvMaximo;
    }

    public void setCvMaximo(int cvMaximo) {
        this.cvMaximo = cvMaximo;
    }

    public int getCantMarchas() {
        return cantMarchas;
    }

    public void setCantMarchas(int cantMarchas) {
        this.cantMarchas = cantMarchas;
    }

    public int getnPuertas() {
        return nPuertas;
    }

    public void setnPuertas(int nPuertas) {
        this.nPuertas = nPuertas;
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

    public String getTraccion() {
        return traccion;
    }

    public void setTraccion(String traccion) {
        this.traccion = traccion;
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

    public String getTipoFiltro() {
        return "Camioneta";
    }

    @Override
    public void siguientePagina() {
        this.pagina++;
    }
}

package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroPorNombreUsuario implements IFiltro{
    private String nombreUsuario;
    private int pagina;
    private int cantidadPorPagina;
    private String tipoFiltro;
    
    public FiltroPorNombreUsuario() {
        this.tipoFiltro = "Guardado";
    }

    public FiltroPorNombreUsuario(String nombreUsuario, int pagina, int cantidadPorPagina) {
        this.nombreUsuario = nombreUsuario;
        this.pagina = pagina;
        this.cantidadPorPagina = cantidadPorPagina;
        this.tipoFiltro = "Guardado";
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
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

    public void setTipoFiltro(String tipo){
        this.tipoFiltro = tipo;
    }

    @Override
    public String getTipoFiltro() {
        return this.tipoFiltro;
    }
}

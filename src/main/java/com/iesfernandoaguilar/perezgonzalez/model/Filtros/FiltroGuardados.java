package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroGuardados implements IFiltro{
    private String nombreUsuario;
    private int pagina;
    private int cantidadPorPagina;
    
    public FiltroGuardados() {}

    public FiltroGuardados(String nombreUsuario, int pagina, int cantidadPorPagina) {
        this.nombreUsuario = nombreUsuario;
        this.pagina = pagina;
        this.cantidadPorPagina = cantidadPorPagina;
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

    @Override
    public String getTipoFiltro() {
        return "Guardados";
    }

    
}

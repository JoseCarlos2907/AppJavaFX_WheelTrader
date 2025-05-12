package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroUsuariosConReportes implements IFiltro{
    private String cadena;
    private int pagina;
    private int cantidadPorPagina;
    
    public FiltroUsuariosConReportes() {
        this.cadena = "";
        this.pagina = 0;
        this.cantidadPorPagina = 10;
    }

    public FiltroUsuariosConReportes(String cadena, int pagina, int cantidadPorPagina) {
        this.cadena = cadena;
        this.pagina = pagina;
        this.cantidadPorPagina = cantidadPorPagina;
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

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }

    @Override
    public void siguientePagina() {
        this.pagina++;
    }

    @Override
    public String getTipoFiltro() {
        return "UsuariosConReportes";
    }
}

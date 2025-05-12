package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroReportes implements IFiltro{
    private int pagina;
    private int cantidadPorPagina;
    
    public FiltroReportes() {
        this.pagina = 0;
        this.cantidadPorPagina = 10;
    }

    public FiltroReportes(int pagina, int cantidadPorPagina) {
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

    @Override
    public void siguientePagina() {
        this.pagina++;
    }

    @Override
    public String getTipoFiltro() {
        return "Reportes";
    }
}

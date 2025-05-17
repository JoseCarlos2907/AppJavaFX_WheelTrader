package com.iesfernandoaguilar.perezgonzalez.model.Filtros;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;

public class FiltroNotificaciones implements IFiltro{
    private long idUsuario;
    private int pagina;
    private int cantidadPorPagina;
    
    public FiltroNotificaciones() {
        this.idUsuario = -1;
        this.pagina = 0;
        this.cantidadPorPagina = 10;
    }

    public FiltroNotificaciones(long idUsuario, int pagina, int cantidadPorPagina) {
        this.idUsuario = idUsuario;
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

    public long getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(long idUsuario) {
        this.idUsuario = idUsuario;
    }

    @Override
    public void siguientePagina() {
        this.pagina++;
    }

    @Override
    public String getTipoFiltro() {
        return "Notificaciones";
    }
}

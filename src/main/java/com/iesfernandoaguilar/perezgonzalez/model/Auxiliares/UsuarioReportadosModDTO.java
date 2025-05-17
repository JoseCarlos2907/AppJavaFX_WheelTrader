package com.iesfernandoaguilar.perezgonzalez.model.Auxiliares;

import com.iesfernandoaguilar.perezgonzalez.model.Usuario;

public class UsuarioReportadosModDTO {
    private Usuario usuario;
    private long cantReportes;
    
    public UsuarioReportadosModDTO() {}

    public UsuarioReportadosModDTO(Usuario usuario, long cantReportes) {
        this.usuario = usuario;
        this.cantReportes = cantReportes;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public long getCantReportes() {
        return cantReportes;
    }

    public void setCantReportes(long cantReportes) {
        this.cantReportes = cantReportes;
    }
}

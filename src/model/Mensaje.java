package model;

import java.util.ArrayList;
import java.util.List;

public class Mensaje {
    private String tipo;
    private List<String> parametros;
    
    public Mensaje() {
        this.tipo = "";
        this.parametros = new ArrayList<>();
    }

    public String getTipo() {
        return tipo;
    }

    public List<String> getParams() {
        return parametros;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void addParam(String parametro) {
        this.parametros.add(parametro);
    }

    public void clearParams(){
        this.parametros.clear();
    }
}

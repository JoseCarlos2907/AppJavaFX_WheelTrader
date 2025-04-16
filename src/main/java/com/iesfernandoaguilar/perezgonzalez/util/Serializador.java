package com.iesfernandoaguilar.perezgonzalez.util;

public class Serializador {
    public static String codificarMensaje(Mensaje msg){
        return msg.getTipo() + ";" + String.join(";", msg.getParams());
    }

    public static Mensaje decodificarMensaje(String cadena){
        Mensaje msg = new Mensaje();

        String[] cadenaSplit = cadena.split(";");

        msg.setTipo(cadenaSplit[0]);
        
        if(cadenaSplit.length > 1){
            for (int i = 1; i < cadenaSplit.length; i++) {
                msg.addParam(cadenaSplit[i]);
            }
        }
        
        return msg;
    }
}

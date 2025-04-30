package com.iesfernandoaguilar.perezgonzalez.interfaces;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;

public interface IListaAnuncios extends IApp{
    public void abrirAnuncio(Anuncio anuncio);

    void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas, boolean primeraCarga) throws JsonMappingException, JsonProcessingException;
}

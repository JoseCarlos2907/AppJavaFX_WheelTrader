package com.iesfernandoaguilar.perezgonzalez.interfaces;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;

public interface IListaAnuncios extends IApp{
    public void abrirAnuncio(Anuncio anuncio);

    void irDetalleAnuncio(List<byte[]> bytesImagenes) throws IOException;

    void aniadirAnuncios(String anunciosJSON, List<byte[]> imagenesNuevas) throws JsonMappingException, JsonProcessingException;
}

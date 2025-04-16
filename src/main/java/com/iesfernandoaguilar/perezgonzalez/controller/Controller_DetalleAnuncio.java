package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class Controller_DetalleAnuncio implements Initializable {
    @FXML
    private Button Btn_AnteriorImg;

    @FXML
    private Button Btn_Comprar;

    @FXML
    private Button Btn_Reunirse;

    @FXML
    private Button Btn_SiguienteImg;

    @FXML
    private Button Btn_Volver;

    @FXML
    private ImageView ImgView_ImagenCoche;

    @FXML
    private Label Lbl_Anio;

    @FXML
    private Label Lbl_CV;

    @FXML
    private Label Lbl_CantMarchas;

    @FXML
    private Label Lbl_Categoria;

    @FXML
    private Label Lbl_Color;

    @FXML
    private Label Lbl_Descripcion;

    @FXML
    private Label Lbl_Km;

    @FXML
    private Label Lbl_MarcaModelo;

    @FXML
    private Label Lbl_PrecioContado;

    @FXML
    private Label Lbl_PrecioFinanciadoTotal;

    @FXML
    private Label Lbl_PrecioMensual;

    @FXML
    private Label Lbl_Puertas;

    @FXML
    private Label Lbl_TipoCombustible;

    @FXML
    private Label Lbl_TipoMarchas;

    @FXML
    private Label Lbl_Usuario;

    private Anuncio anuncio;
    private List<String> imagenes;
    private String nombreUsuario;

    private IListaAnuncios controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // TODO: Cargar imagenes de la BD y mostrar la primera
    }

    @FXML
    void handleAnteriorImgAction(MouseEvent event) {
        
    }
    
    @FXML
    void handleSiguienteImgAction(MouseEvent event) {

    }

    @FXML
    void handleBtnComprarAction(MouseEvent event) {

    }

    @FXML
    void handleBtnReunirseAction(MouseEvent event) {

    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        FXMLLoader loader = null;
        if(this.controller instanceof Controller_ListaAnuncios){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_ListaAnuncios.fxml"));
        }else if(this.controller instanceof Controller_MisAnuncios){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_MisAnuncios.fxml"));
        }else if(this.controller instanceof Controller_MisGuardados){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_MisGuardados.fxml"));
        }else if(this.controller instanceof Controller_HomeModerador){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        }else if(this.controller instanceof Controller_PerfilUsuario){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_PerfilUsuario.fxml"));
            ((Controller_PerfilUsuario)this.controller).setNombreUsuario(nombreUsuario);
        }

        Parent nuevaVista = loader.load();

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(nuevaVista);
        scene.getStylesheets().addAll(BootstrapFX.bootstrapFXStylesheet());

        stage.setScene(scene);
        stage.show();
    }

    public void setController(IListaAnuncios controller){
        this.controller = controller;
    }

    public void setAnuncio(Anuncio anuncio){
        this.anuncio = anuncio;
    }

    public void setMarcaModelo(String marca, String modelo){
        this.Lbl_MarcaModelo.setText(marca + " | " + modelo);
    }

    public void setCategoria(String categoria){
        this.Lbl_Categoria.setText(categoria);
    }

    public void setDescripcion(String descripcion){
        this.Lbl_Descripcion.setText(descripcion);
    }

    public void setUsuario(String nombreUsuario){
        this.nombreUsuario = nombreUsuario;
        this.Lbl_Usuario.setText("De " + nombreUsuario);
    }

    public void setDatos(int cv, String color, int cantPuertas, int anio, String tipoMarchas, int cantMarchas, int kilometraje, String tipoCombustible){

        this.Lbl_CV.setText(cv + " CV");
        this.Lbl_Color.setText("Color " + color.toLowerCase());
        this.Lbl_Puertas.setText(cantPuertas + " Puertas");
        this.Lbl_Anio.setText("Año " + anio);
        this.Lbl_TipoMarchas.setText("Cambio " + tipoMarchas);
        this.Lbl_CantMarchas.setText(cantMarchas + " Marchas");
        this.Lbl_Km.setText(kilometraje + " Km");
        this.Lbl_TipoCombustible.setText(tipoCombustible);
    }

    public void setPrecio(double precioContado, double precioMensual){
        this.Lbl_PrecioContado.setText(precioContado + "€");
        this.Lbl_PrecioFinanciadoTotal.setText((precioMensual*10) + "€");
        this.Lbl_PrecioMensual.setText(precioMensual + "€ /mes");
    }
}

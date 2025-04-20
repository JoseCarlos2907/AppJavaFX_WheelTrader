package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import org.kordamp.bootstrapfx.BootstrapFX;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IFiltro;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    private Label Lbl_Categoria;

    @FXML
    private Label Lbl_Descripcion;

    @FXML
    private Label Lbl_MarcaModelo;

    @FXML
    private Label Lbl_Precio;

    @FXML
    private Label Lbl_Usuario;

    @FXML
    private VBox VBox_Caracteristicas;

    private Anuncio anuncio;
    private List<String> imagenes;
    private String nombreUsuario;
    private IFiltro filtro;

    private IListaAnuncios controller;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
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

    public void setFiltro(IFiltro filtro){
        this.filtro = filtro;
    }
}

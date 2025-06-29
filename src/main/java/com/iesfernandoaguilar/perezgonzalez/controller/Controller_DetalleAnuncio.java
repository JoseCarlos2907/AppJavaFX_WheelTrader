package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IListaAnuncios;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Usuario;
import com.iesfernandoaguilar.perezgonzalez.model.ValorCaracteristica;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

public class Controller_DetalleAnuncio implements IApp, Initializable {
    private Lector_App hiloLector;
    
    private Anuncio anuncio;
    private Usuario usuario;
    private List<byte[]> imagenes;
    private int posImagenActual;

    private IListaAnuncios controller;

    @FXML
    private Button Btn_AnteriorImg;

    @FXML
    private Button Btn_Comprar;

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

    @FXML
    private Button Btn_CambiarEstadoAnuncio;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(!"MODERADOR".equals(Session.getUsuario().getRol())){
            this.Btn_CambiarEstadoAnuncio.setDisable(true);
            this.Btn_CambiarEstadoAnuncio.setVisible(false);
        }
    }

    @FXML
    void handleAnteriorImgAction(MouseEvent event) {
        this.posImagenActual--;

        if(this.posImagenActual < this.imagenes.size()-1){
            this.Btn_SiguienteImg.setDisable(false);
        }

        if(this.posImagenActual <= 0){
            this.Btn_AnteriorImg.setDisable(true);
        }

        this.cambiarImagen(this.imagenes.get(posImagenActual));
    }
    
    @FXML
    void handleSiguienteImgAction(MouseEvent event) {
        this.posImagenActual++;

        if(this.posImagenActual > 0){
            this.Btn_AnteriorImg.setDisable(false);
        }

        if(this.posImagenActual >= this.imagenes.size()-1){
            this.Btn_SiguienteImg.setDisable(true);
        }

        this.cambiarImagen(this.imagenes.get(posImagenActual));
    }

    @FXML
    public void handleBtnCEAAction() throws IOException{
        if("CANCELADO".equals(anuncio.getEstado())){
            this.Btn_CambiarEstadoAnuncio.setText("Cancelar anuncio");
            anuncio.setEstado("EN_VENTA");
            this.hiloLector.reanudarAnuncio(anuncio.getIdAnuncio());
        }else{
            this.Btn_CambiarEstadoAnuncio.setText("Reanudar anuncio");
            anuncio.setEstado("CANCELADO");
            this.hiloLector.cancelarAnuncio(anuncio.getIdAnuncio());
        }

        AlertManager.alertInfo(
            "Estado del anuncio cambiado",
            "El estado del anuncio ha sido cambiado correctamente."
        );
    }

    @FXML
    void handleBtnComprarAction(MouseEvent event) throws IOException {
        if(anuncio.getVendedor().getIdUsuario() == Session.getUsuario().getIdUsuario()){
            AlertManager.alertError(
                "Compra incorrecta",
                "No puedes comprar un vehículo que has publicado tu.",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else{
            
            this.hiloLector.obtenerPDFAcuerdo(Session.getUsuario().getIdUsuario(), this.anuncio.getIdAnuncio(), anuncio.getTipoVehiculo());
        }
    }

    public void irCompraComprador(byte[] documento) throws IOException{
        File temp = new File("temp");
        if(!temp.exists()) temp.mkdir();

        File pdf = new File("temp/Temp.pdf");
        if(pdf.exists()) pdf.delete();
        pdf.createNewFile();

        try (FileOutputStream fos = new FileOutputStream("temp/Temp.pdf")) {
            fos.write(documento);
        }

        
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_CompraComprador.fxml"));
        Parent parent = loader.load();

        Controller_CompraComprador controller = loader.getController();
        controller.setHiloLector(hiloLector);
        controller.setAnuncio(anuncio);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {

        FXMLLoader loader = null;
        if(this.controller instanceof Controller_HomeModerador){
            loader = new FXMLLoader(getClass().getResource("/view/FXML_HomeModerador.fxml"));
        }else{
            loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        }
        Parent parent = loader.load();

        IApp controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setController(IListaAnuncios controller){
        this.controller = controller;
    }

    public void setAnuncio(Anuncio anuncio) throws IOException{
        this.anuncio = anuncio;

        String marca = "";
        String modelo = "";
        String textoAux = "";
        String nombreCar = "";
        for (ValorCaracteristica vc : this.anuncio.getValoresCaracteristicas()) {
            nombreCar = vc.getNombreCaracteristica();
            if(nombreCar.contains("Marca_")){
                marca = vc.getValor();
            }else if(nombreCar.contains("Modelo_")){
                modelo = vc.getValor();
            }else if(nombreCar.contains("CV_")){
                textoAux = "CV";
            }else if(nombreCar.contains("Cilindrada_")){
                textoAux = "Cilindrada";
            }else if(nombreCar.contains("Anio_")){
                textoAux = "Año";
            }else if(nombreCar.contains("KM_")){
                textoAux = "KM";
            }else if(nombreCar.contains("TipoCombustible_")){
                textoAux = "Tipo de Combustible";
            }else if(nombreCar.contains("Transmision_")){
                textoAux = "Transmisión";
            }else if(nombreCar.contains("Marchas_")){
                textoAux = "Marchas";
            }else if(nombreCar.contains("Puertas_")){
                textoAux = "Nº de Puertas";
            }else if(nombreCar.contains("TipoTraccion_")){
                textoAux = "Tipo de Tracción";
            }else if(nombreCar.contains("CargaKg_")){
                textoAux = "Carga Máxima (Kg)";
            }

            if(!nombreCar.contains("Marca") && !nombreCar.contains("Modelo")){
                Label label = new Label(textoAux + ": " + vc.getValor());
                label.setStyle("-fx-text-fill:white; -fx-font-size: 26px;");
    
                Line linea = new Line();
                linea.setStartX(0);
                linea.setEndX(300);
                linea.setStrokeWidth(3);
                linea.setStyle("-fx-stroke: #84aedd");
    
                this.VBox_Caracteristicas.getChildren().addAll(label, linea);
            }

        }

        this.Lbl_MarcaModelo.setText(marca + " | " + modelo);
        this.Lbl_Precio.setText(anuncio.getPrecio() + "€");
        this.Lbl_Categoria.setText(anuncio.getTipoVehiculo());
        this.Lbl_Descripcion.setText(anuncio.getDescripcion());
        this.usuario = anuncio.getVendedor();
        this.Lbl_Usuario.setText("De " + this.usuario.getNombreUsuario());
        this.Lbl_Categoria.setText(this.anuncio.getTipoVehiculo());

        if("CANCELADO".equals(anuncio.getEstado())){
            this.Btn_CambiarEstadoAnuncio.setText("Reanudar anuncio");
        }else{
            this.Btn_CambiarEstadoAnuncio.setText("Cancelar anuncio");
        }

        if(!"EN_VENTA".equals(anuncio.getEstado()) && !"EN_PROCESO".equals(anuncio.getEstado())){
            this.Btn_Comprar.setDisable(true);
        }
    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void setImagenes(List<byte[]> bytesImagenes){
        this.imagenes = bytesImagenes;
        this.cambiarImagen(this.imagenes.get(posImagenActual));

        if(this.imagenes.size() == 1){
            this.Btn_SiguienteImg.setDisable(true);
        }
    }

    private void cambiarImagen(byte[] imagen){
        ByteArrayInputStream is = new ByteArrayInputStream(imagen);
        this.ImgView_ImagenCoche.setImage(new Image(is));

        this.ImgView_ImagenCoche.setPreserveRatio(true);
        this.ImgView_ImagenCoche.setSmooth(true);
        this.ImgView_ImagenCoche.setFitHeight(300);
        this.ImgView_ImagenCoche.setFitWidth(300);
    }
}

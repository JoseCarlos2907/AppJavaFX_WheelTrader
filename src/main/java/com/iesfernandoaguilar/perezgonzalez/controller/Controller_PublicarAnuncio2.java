package com.iesfernandoaguilar.perezgonzalez.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.model.Imagen;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Controller_PublicarAnuncio2 implements IApp, Initializable{
    private Lector_App hiloLector;

    private List<Imagen> imagenes;
    private Anuncio anuncio;

    @FXML
    private Button Btn_AniadirImg;

    @FXML
    private Button Btn_Publicar;

    @FXML
    private Button Btn_Cancelar;

    @FXML
    private HBox HBox_Imagenes;

    @FXML
    private TextArea TxtA_Desccripcion;

    @FXML
    private TextField TxtF_Ciudad;

    @FXML
    private TextField TxtF_Precio;

    @FXML
    private TextField TxtF_Provincia;

    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.imagenes = new ArrayList<>();
    }

    @FXML
    void handleBtnAniadirImgAction(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Selecciona una imagen");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Imágenes",   "*.png", "*.jpg", "*.jpeg"));
        
        Stage stage = (Stage) Btn_Cancelar.getScene().getWindow();
        File imagenFile = fileChooser.showOpenDialog(stage);
        if(imagenFile != null){
            try {
                FileInputStream fis = new FileInputStream(imagenFile);
                byte[] imagenBytes = fis.readAllBytes();
                fis.close();

                Imagen img = new Imagen();
                img.setImagen(imagenBytes);
                this.imagenes.add(img);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_ImagenAnuncio.fxml"));
                Parent componente = loader.load();

                Controller_ImagenAnuncio controller = loader.getController();
                controller.setImagen(img);
                controller.setController(this);
                controller.setParent(componente);

                this.HBox_Imagenes.getChildren().add(componente);

            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    @FXML
    void handleBtnPublicarAction(MouseEvent event) throws IOException {
        double precio = 0.0;
        try {
            precio = Double.parseDouble(new String(this.TxtF_Precio.getText()));
        } catch (Exception e) {
            AlertManager.alertError(
                "Formato del precio incorrecto",
                "El formato del precio no es el correcto",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }

        String provincia = new String(this.TxtF_Provincia.getText());
        String ciudad = new String(this.TxtF_Ciudad.getText());
        String descripcion = new String(this.TxtA_Desccripcion.getText());
        
        if(this.imagenes.size() < 1){
            AlertManager.alertError(
                "No hay imagenes",
                "El anuncio debe tener al menos una imagen",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
            );
        }else if(provincia.isEmpty() || ciudad.isEmpty() || descripcion.isEmpty()){
            AlertManager.alertError(
                "Datos incompletos",
                "Es necesario rellenar todos los datos del anuncio",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
                );
        }else if(precio <= 0.0){
            AlertManager.alertError(
                "Precio no válido",
                "El precio no puede ser menor o igual a 0",
                getClass().getResource("/styles/EstiloGeneral.css").toExternalForm()
                );
        }else{
            this.anuncio.setProvincia(provincia);
            this.anuncio.setCiudad(ciudad);
            this.anuncio.setDescripcion(descripcion);
            this.anuncio.setPrecio(precio);

            this.anuncio.setVendedor(Session.getUsuario());

            ObjectMapper mapper = new ObjectMapper();
            String anuncioJSON = mapper.writeValueAsString(this.anuncio);

            this.hiloLector.publicarAnuncio(anuncioJSON, new ArrayList<>(imagenes));
        }
    }

    @FXML
    void handleBtnCancelarAction(MouseEvent event) throws IOException {
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Cancelar.getScene().getWindow();
        stage2.close();
    }

    public void eliminarImagen(Imagen img){
        this.imagenes.remove(img);
    }

    public void setAnuncio(Anuncio anuncio){
        this.anuncio = anuncio;
    }

    public void setHiloLector(Lector_App hiloLector){
        this.hiloLector = hiloLector;
    }

    public void anuncioPublicado() throws IOException{
        AlertManager.alertInfo(
            "Anuncio publicado",
            "El anuncio se ha publicado correctamente"
        );

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        stage.setScene(new Scene(parent));
        stage.show();

        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage2 = (Stage) Btn_Cancelar.getScene().getWindow();
        stage2.close();
    }
}

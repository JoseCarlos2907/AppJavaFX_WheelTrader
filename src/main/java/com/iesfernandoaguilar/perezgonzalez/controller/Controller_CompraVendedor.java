package com.iesfernandoaguilar.perezgonzalez.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class Controller_CompraVendedor implements Initializable {
    @FXML
    private Button Btn_Limpiar;

    @FXML
    private Button Btn_ConfirmarAcuerdo;

    @FXML
    private Button Btn_RechazarAcuerdo;

    @FXML
    private Button Btn_Volver;

    @FXML
    private Button Btn_ZoomIn;

    @FXML
    private Button Btn_ZoomOut;

    @FXML
    private Canvas Canvas_Firma;

    @FXML
    private ScrollPane Pane_PDF;

    @FXML
    private VBox VBox_PDF;

    private double lastX;
    private double lastY;

    private PDDocument pddDocument;
    private List<ImageView> imagenesPaginas;
    private double zoom;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        zoom = 1;
        imagenesPaginas = new ArrayList<>();
        this.Btn_ZoomOut.setDisable(true);
        // this.setPrecios(99999);

        // Preparar el Canvas para poder dibujar en él
        GraphicsContext context = Canvas_Firma.getGraphicsContext2D();

        Canvas_Firma.setOnMousePressed(event -> {
            lastX = event.getX();
            lastY = event.getY();
        });

        Canvas_Firma.setOnMouseDragged(event -> {
            context.strokeLine(lastX, lastY, event.getX(), event.getY());
            lastX = event.getX();
            lastY = event.getY();
        });

        // /home/josecarlos/Descargas/prueba_Python_27_02_25.pdf
        try {
            File archivoPDF = new File("/home/josecarlos/Descargas/prueba_Python_27_02_25.pdf");
            pddDocument = Loader.loadPDF(archivoPDF);
            PDFRenderer renderer = new PDFRenderer(pddDocument);

            int totalPaginas = pddDocument.getNumberOfPages();

            for (int i = 0; i < totalPaginas; i++) {
                BufferedImage bim = renderer.renderImageWithDPI(i, 100);
                WritableImage fxImage = SwingFXUtils.toFXImage(bim, null);
                ImageView imageView = new ImageView(fxImage);
                imageView.setPreserveRatio(true);
                imageView.setFitWidth(610);
                this.imagenesPaginas.add(imageView);

                this.VBox_PDF.getChildren().add(imageView);
            }

            pddDocument.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleBtnLimpiarAction(MouseEvent event) {
        GraphicsContext context = Canvas_Firma.getGraphicsContext2D();
        context.clearRect(0, 0, Canvas_Firma.getWidth(), Canvas_Firma.getHeight());
    }

    @FXML
    void handleBtnZoomInAction(MouseEvent event) {
        if(zoom + 0.5 == 2.5){
            this.Btn_ZoomIn.setDisable(true);
        }else{
            this.Btn_ZoomOut.setDisable(false);
        }
        zoom += 0.5;
        this.aplicarZoom();
    }

    @FXML
    void handleBtnZoomOutAction(MouseEvent event) {
        if(zoom - 0.5 == 1){
            this.Btn_ZoomOut.setDisable(true);
        }else{
            this.Btn_ZoomIn.setDisable(false);
        }
        zoom -= 0.5;
        this.aplicarZoom();
    }

    public void aplicarZoom(){
        for (ImageView imageView : imagenesPaginas) {
            imageView.setFitWidth(610 * zoom);
        }
    }

    @FXML
    void handleBtnConfirmarAcuerdoAction(MouseEvent event) {

    }

    @FXML
    void handleBtnRechazarAcuerdoAction(MouseEvent event) {

    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) {

    }
}

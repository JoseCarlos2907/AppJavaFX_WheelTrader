package com.iesfernandoaguilar.perezgonzalez.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.iesfernandoaguilar.perezgonzalez.interfaces.IApp;
import com.iesfernandoaguilar.perezgonzalez.model.Anuncio;
import com.iesfernandoaguilar.perezgonzalez.threads.Lector_App;
import com.iesfernandoaguilar.perezgonzalez.util.AlertManager;
import com.iesfernandoaguilar.perezgonzalez.util.Session;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Controller_CompraComprador implements IApp, Initializable{
    private Lector_App hiloLector;

    private double lastX;
    private double lastY;

    private PDDocument pddDocument;
    private List<ImageView> imagenesPaginas;
    private double zoom;

    private Anuncio anuncio;
    
    @FXML
    private Button Btn_OfrecerAcuerdo;

    @FXML
    private Button Btn_Volver;

    @FXML
    private Canvas Canvas_Firma;

    @FXML
    private Label Lbl_Comision;

    @FXML
    private Label Lbl_PrecioVehiculo;

    @FXML
    private Label Lbl_Total;

    @FXML
    private ScrollPane Pane_PDF;

    @FXML
    private VBox VBox_PDF;

    @FXML
    private Button Btn_ZoomIn;

    @FXML
    private Button Btn_ZoomOut;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        zoom = 1;
        imagenesPaginas = new ArrayList<>();
        this.Btn_ZoomOut.setDisable(true);

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

        try {
            File archivoPDF = new File("temp/Temp.pdf");
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
    void handleBtnZoomOutAction(MouseEvent event) {
        if(zoom - 0.5 == 1){
            this.Btn_ZoomOut.setDisable(true);
        }else{
            this.Btn_ZoomIn.setDisable(false);
        }
        zoom -= 0.5;
        this.aplicarZoom();
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

    public void aplicarZoom(){
        for (ImageView imageView : imagenesPaginas) {
            imageView.setFitWidth(610 * zoom);
        }
    }
    
    @FXML
    void handleBtnOfrecerAcuerdoAction(MouseEvent event) throws IOException, DocumentException {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Confirmación de oferta");
        alert.setHeaderText("¿Seguro que desea proceder con la compra de este vehículo?");
        alert.setContentText("Una vez hecha la oferta, si el vendedor acepta la oferta le llegará una notificación para proceder con el pago a través de su cuenta de PayPal.");

        Optional<ButtonType> res = alert.showAndWait();

        if(!res.isPresent() || res.isPresent() && res.get() != ButtonType.OK) return;
        // Capturo el Canvas y obtengo una imagen del contenido
        WritableImage wrImage = new WritableImage(
            (int) Canvas_Firma.getWidth(),
            (int) Canvas_Firma.getHeight()
        );
        SnapshotParameters params = new SnapshotParameters();
        params.setFill(Color.TRANSPARENT);

        Image imagen = Canvas_Firma.snapshot(params, wrImage);
        BufferedImage bImagen = SwingFXUtils.fromFXImage(imagen, null);

        PdfReader reader = new PdfReader("temp/Temp.pdf");
        ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
        PdfStamper stamper = new PdfStamper(reader, pdfOut);
        
        // Una vez tengo la imagen del canvas, la plasmo en el campo imagen de la zona de firmas del PDF en base a la id del mismo campo
        PdfContentByte pdfCanvas = stamper.getOverContent(2);
        com.itextpdf.text.Image imagenPdf = com.itextpdf.text.Image.getInstance(bImagen, null);
        imagenPdf.scaleToFit(200, 100);
        imagenPdf.setAbsolutePosition(55, 400);
        pdfCanvas.addImage(imagenPdf);
        
        stamper.close();
        reader.close();
        
        // Guardar el PDF corregido
        Files.write(Paths.get("temp/Temp.pdf"), pdfOut.toByteArray());

        byte[] bytesPdf = Files.readAllBytes(Paths.get("temp/Temp.pdf"));

        // Se envia al servidor
        this.hiloLector.compradorOfreceCompra(bytesPdf, Session.getUsuario().getIdUsuario(), anuncio.getIdAnuncio(), anuncio.getVendedor().getIdUsuario());

        File pdf = new File("temp/Temp.pdf");
        pdf.delete();
        
        AlertManager.alertInfo(
            "Oferta enviada",
            "La oferta ha sido enviada al vendedor, cuando acepte la oferta le llegará una notificación para proceder con el pago"
        );

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        
        Controller_Home controller = loader.getController();
        controller.setHiloLector(this.hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void handleBtnVolverAction(MouseEvent event) throws IOException {
        File pdf = new File("temp/Temp.pdf");
        pdf.delete();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/FXML_Home.fxml"));
        Parent parent = loader.load();
        
        Controller_Home controller = loader.getController();
        controller.setHiloLector(hiloLector);
        this.hiloLector.setController(controller);

        Stage stage = (Stage) Btn_Volver.getScene().getWindow();

        Scene scene = new Scene(parent);

        stage.setScene(scene);
        stage.show();
    }

    public void setPrecios(double precio){
        this.Lbl_PrecioVehiculo.setText(String.format("%.2f€",precio));
        
        double comision = (precio*5)/100;
        this.Lbl_Comision.setText(String.format("%.2f€",comision));

        double total = precio + comision;
        this.Lbl_Total.setText(String.format("%.2f€",total));
    }

    @Override
    public void setHiloLector(Lector_App hiloLector) {
        this.hiloLector = hiloLector;
    }

    public void setAnuncio(Anuncio anuncio){
        this.anuncio = anuncio;
        this.setPrecios(anuncio.getPrecio());
    }
}

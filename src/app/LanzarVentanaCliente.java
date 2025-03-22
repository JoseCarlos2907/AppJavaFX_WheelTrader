package app;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LanzarVentanaCliente extends Application {
    @Override
    public void start(Stage primStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../view/FXML_InicioSesion.fxml"));
        primStage.setTitle("Wheel Trader");
        primStage.setScene(new Scene(root));
        primStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

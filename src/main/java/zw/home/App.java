package zw.home;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;


public class App  extends Application {

        public void start( Stage stage) throws Exception {
        Scene scene = new Scene(new StackPane(new Label("JavaFX 11")), 300, 200);
        stage.setScene(scene);
        stage.show();
    }
        public static void main(String[] args) {
        launch();
    }
}

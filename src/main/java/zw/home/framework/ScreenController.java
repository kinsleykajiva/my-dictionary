package zw.home.framework;

import java.io.IOException;
import javafx.fxml.FXMLLoader;
public class ScreenController {
    private ScreenController(){
        new Exception ("Can not create an Object from this Class");
    }


    public static void setScreen(ViewController screen) {
        switch ( screen ) {
            case DEFAULT_VIEW:
                try {
                    StageManager.setPaneFragment(FXMLLoader.load(ScreenController.class.getResource(ViewController.DEFAULT_VIEW.getFxmlFile())));
                } catch ( IOException e ) {
                    e.printStackTrace();

                }
                break;
            default:
                break;


        }
    }
}

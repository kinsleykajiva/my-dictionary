package zw.home;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import zw.home.framework.StageManager;
import zw.home.framework.ViewController;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.logging.LogManager;

import static zw.home.framework.StageManager.getStage;


public class App  extends Application {

    private StageManager stageManager = null;
    // one icon location is shared between the application tray icon and task bar icon.
    // you could also use multiple icons to allow for clean display of tray icons on hi-dpi devices.
    private static final String iconImageLoc =
            "tray-icon.png";
    // a timer allowing the tray icon to provide a periodic notification event.
    //private Timer notificationTimer = new Timer();

    // format used to display the current time in a tray icon notification.
    private DateFormat timeFormat = SimpleDateFormat.getTimeInstance();
    @Override
    public void start(Stage stage) throws Exception {
        // instructs the javafx system not to exit implicitly when the last application window is shut.
        Platform.setImplicitExit(false);
        // sets up the tray icon (using awt code run on the swing thread).
        javax.swing.SwingUtilities.invokeLater(this::addAppToTray);
        stageManager = new StageManager(stage);
        stageManager.switchScene( ViewController.DEFAULT_VIEW);
    }
    /**
     * Sets up a system tray icon
     */
    private void addAppToTray() {
        try {
            // ensure awt toolkit is initialized.
            java.awt.Toolkit.getDefaultToolkit();

            // app requires system tray support, just exit if there is no support.
            if (!java.awt.SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }

            // set up a system tray icon.
            java.awt.SystemTray tray = java.awt.SystemTray.getSystemTray();
            //  Image img = new Image(getClass().getResource("/resources/drawables/dictionary.png").toURI().toString());
            URL imageLoc = new URL( getClass().getResource("/resources/drawables/tray-icon.png").toURI().toString() );
            // URL imageLoc = new URL( iconImageLoc );
            java.awt.Image image = ImageIO.read(imageLoc);
            java.awt.TrayIcon trayIcon = new java.awt.TrayIcon(image);

            // if the user double-clicks on the tray icon, show the main app stage.
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            // if the user selects the default menu item (which includes the app name),
            // show the main app stage.
            java.awt.MenuItem openItem = new java.awt.MenuItem("Open App");
            openItem.addActionListener(event -> Platform.runLater(this::showStage));

            // the convention for tray icons seems to be to set the default icon for opening
            // the application stage in a bold font.
            java.awt.Font defaultFont = java.awt.Font.decode(null);
            java.awt.Font boldFont = defaultFont.deriveFont(java.awt.Font.BOLD);
            openItem.setFont(boldFont);

            // to really exit the application, the user must go to the system tray icon
            // and select the exit option, this will shutdown JavaFX and remove the
            // tray icon (removing the tray icon will also shut down AWT).
            java.awt.MenuItem exitItem = new java.awt.MenuItem("Exit");
            exitItem.addActionListener(event -> {
                // notificationTimer.cancel();
                Platform.exit();
                tray.remove(trayIcon);
            });

            // setup the popup menu for the application.
            final java.awt.PopupMenu popup = new java.awt.PopupMenu();
            popup.add(openItem);
            popup.addSeparator();
            popup.add(exitItem);
            trayIcon.setPopupMenu(popup);

            tray.add(trayIcon);
        } catch (java.awt.AWTException | IOException | URISyntaxException e) {
            System.out.println("Unable to init system tray");
            e.printStackTrace();
        }
    }
    /**
     * Shows the application stage and ensures that it is brought ot the front of all stages.
     */
    private void showStage() {
        if (getStage() != null) {
            getStage().show();
            getStage().toFront();
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        LogManager.getLogManager().reset();
        launch(args);
    }
}

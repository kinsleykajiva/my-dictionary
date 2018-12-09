package zw.home.framework;

public enum ViewController {


    DEFAULT_VIEW{
        @Override
        public String getTitle () {
            return "My Dictionary";
        }

        @Override
        public  String getFxmlFile () {
            return LAY_OUT + "DefaultController.fxml";
        }
    };

    public abstract String getTitle ();
    public abstract String getFxmlFile();

    public final static String LAY_OUT = "/resources/layouts/";
}

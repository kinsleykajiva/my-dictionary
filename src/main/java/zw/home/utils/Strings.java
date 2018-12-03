package zw.home.utils;

public final  class  Strings {

    /**This is a  path to the local resources of the sysytem*/
    public static final String RESOURCES = "/resources/";
    /**This is the default file to load as dictionary. */
    public static final String PATH_DEFAULT_DICTIONARY = RESOURCES + "raw/dictionary/dictionary.json";
    /**This is the secondary file to load as dictionary. */
    public static final String PATH_SECONADRY_DICTIONARY = RESOURCES +"raw/webstar/dictionary.json";
    /**Path to the internal database*/
    public static final String PATH_INJAR_DB = "jdbc:sqlite::resource:resources/raw/dictionary.db";

}

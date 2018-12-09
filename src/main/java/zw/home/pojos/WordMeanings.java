package zw.home.pojos;

/**
 * This will be used to create a word object in serializing the json
 * */
public class WordMeanings {

    private int ID ;
    private String word , definition ;


    /**
     * For serialisation and Objections
     * */
    public WordMeanings (String word , String definition) {
        this.word = word;
        this.definition = definition;
    }

    /**
     * For stored references may for database storage
     * */
    public WordMeanings (int ID , String word , String definition) {
        this.ID = ID;
        this.word = word;
        this.definition = definition;
    }


    public int getID () {
        return ID;
    }

    public String getWord () {
        return word;
    }

    public String getDefinition () {
        return definition;
    }

    @Override
    public String toString () {
        return "WordMeanings{" +
                "word='" + word + '\'' +
                ", definition='" + definition + '\'' +
                '}';
    }
}

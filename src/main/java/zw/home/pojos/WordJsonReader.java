package zw.home.pojos;

import com.google.gson.annotations.SerializedName;

public class WordJsonReader {

        @SerializedName( "word" )
    private String word;
    @SerializedName( "wordtype" )
private String wordtype;
    @SerializedName( "definition" )
private String definition;

    public String getWord () {
        return word;
    }

    public String getWordtype () {
        return wordtype;
    }

    public String getDefinition () {
        return definition;
    }
}

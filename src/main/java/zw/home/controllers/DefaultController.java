package zw.home.controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


import com.jfoenix.controls.*;
import com.jfoenix.effects.JFXDepthManager;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.eclipse.collections.api.block.procedure.Procedure;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.collector.Collectors2;
import zw.home.framework.StageManager;
import zw.home.pojos.WordMeanings;
import zw.home.utils.SqliteDB;

import static com.google.common.collect.MoreCollectors.onlyElement;
import static zw.home.framework.StageManager.getStage;

public class DefaultController {
    @FXML
    private AnchorPane parentAncherPane;
    @FXML
    private JFXButton closButton, btnSearch, minButton, btnBack;
    @FXML
    private JFXTextField txtSearch;
    @FXML
    private FontAwesomeIconView clearSearch;

    private final int shadowSize = 50;

    private JFXSpinner spinner;
    @FXML
    private VBox vboxresults, vBoxExplanation;
    private boolean isReading = false;
    @FXML
    private JFXListView < Label > listview;
    @FXML
    private Text txtdefined;
    @FXML
    private Label status, txtWord;

    private ImmutableList < WordMeanings > words = null;
    private JFXDepthManager depthManager = new JFXDepthManager();
    private Timeline timelinSlideLeft, timelinSlideRight;
    private DoubleProperty lastXListView;
    private Task < ImmutableList < WordMeanings > > getWordsTask = new Task < ImmutableList < WordMeanings > >() {
        @Override
        protected ImmutableList < WordMeanings > call () throws Exception {
            return new SqliteDB().getAllWords();
        }
    };


    public void initialize () {
        initViews();
        initClickListeners();
        clicksHandlers();
        initData();
    }

    private void clicksHandlers () {
        closButton.setOnMouseEntered( e -> getStage().getScene().setCursor( Cursor.HAND ) );
        closButton.setOnMouseExited( e -> getStage().getScene().setCursor( Cursor.DEFAULT ) );
        closButton.setOnAction( ev -> {
            getStage().hide();
        } );
        minButton.setOnAction( er -> {
            StageManager.getStage().setIconified( true );
        } );
        btnBack.setOnAction( er -> {
            timelinSlideRight.play();

        } );
        clearSearch.setOnMouseEntered( e -> getStage().getScene().setCursor( Cursor.HAND ) );
        clearSearch.setOnMouseExited( e -> getStage().getScene().setCursor( Cursor.DEFAULT ) );
        clearSearch.setOnMouseClicked( e->{
            txtSearch.setText( "" );
        } );
        btnSearch.setOnAction( ev -> {
            // simpleSnackBar( parentAncherPane , "Error in Loading data" );
            if(isReading) {
                timelinSlideRight.play();
            }else{
                timelinSlideLeft.play();
            }


        } );
        txtSearch.textProperty().addListener( (observable , oldValue , newValue) -> {
                    if(!isReading){
                        listview.getItems().clear();
                        spinner.setVisible( true );

                        words.stream()
                                .filter( each ->
                                        each.getWord().contains( newValue.toUpperCase()

                                        )
                                ).forEachOrdered( i -> {
                                    //System.out.println( i.getWord() )

                                    listview.getItems().add( new Label( i.getWord() ) );
                                }
                        );
                        if ( listview.getItems().isEmpty() ) {
                            status.setVisible( true );
                            status.setText( "Word not found." );

                        } else {
                            status.setVisible( false );
                        }
                        spinner.setVisible( false );
                    }

                }
        );
        listview.setOnMouseClicked( ev->{
            if( ev.getClickCount() == 2){
                txtWord.setText( listview.getSelectionModel().getSelectedItem().getText() );
                Task < String > task = new Task < String >() {
                    @Override
                    protected String call () throws Exception {
                        WordMeanings meaning = words.stream().filter(
                                wr -> wr.getWord().equals( listview.getSelectionModel().getSelectedItem().getText() )
                        ).collect( onlyElement() );
                        return meaning.getDefinition();
                    }
                };
                new Thread( task ).start();
                task.setOnSucceeded( er -> {
                    txtdefined.setText( task.getValue() );
                    timelinSlideLeft.play();
                } );
                task.setOnFailed( we -> {
                    timelinSlideLeft.play();
                    txtdefined.setText( "-------------------------------" );
                } );
            }
        } );
    }

    private void initData () {

        new Thread( getWordsTask ).start();

        getWordsTask.setOnSucceeded( ev->{
            spinner.setVisible( false );

            if(getWordsTask.getValue().isEmpty()){
                status.setVisible( true );
                status.setText( "Error in Loading data . Restart The Application" );
            } else{
                listview.getItems().clear();
                getWordsTask.getValue().forEach( wordObject ->
                        listview.getItems().add( new Label( wordObject.getWord() ) )
                );
            }
        } );
        getWordsTask.setOnFailed( ev -> {

            spinner.setVisible( false );

            status.setVisible( true );
            status.setText( "Error in Loading data" );
        } );
    }

    private void initClickListeners () {
        var onFinished = ( EventHandler < ActionEvent > ) t -> {
            vBoxExplanation.setVisible( true );
            txtSearch.setDisable( true );
            isReading = true;
        };
        var onFinished2 = ( EventHandler < ActionEvent > ) t -> {
            vBoxExplanation.setVisible( false );
            txtSearch.setDisable( false );
            isReading = false;
        };
        lastXListView = listview.translateXProperty();
        final KeyValue kvUp2 = new KeyValue( vBoxExplanation.translateXProperty() , - 360 );
        final KeyValue kvUp3 = new KeyValue( vboxresults.translateXProperty() , - 360 ); // visible at 340
        final KeyFrame kfUp = new KeyFrame( Duration.millis( 500 ) , onFinished , kvUp3 , kvUp2 );
        timelinSlideLeft.getKeyFrames().add( kfUp );
        //
        final KeyValue kvp2 = new KeyValue( vBoxExplanation.translateXProperty() , 00 );
        final KeyValue kvp3 = new KeyValue( vboxresults.translateXProperty() , 00 );
        final KeyFrame kfp = new KeyFrame( Duration.millis( 500 ) , onFinished2 , kvp2 , kvp3 );
        timelinSlideRight.getKeyFrames().add( kfp );
    }

    private void initViews () {
        timelinSlideLeft = new Timeline();
        timelinSlideRight = new Timeline();
        timelinSlideLeft.setCycleCount( 1 );
        timelinSlideLeft.setAutoReverse( true );

        timelinSlideRight.setCycleCount( 1 );
        timelinSlideRight.setAutoReverse( true );

        parentAncherPane.setStyle( "-fx-background-color: rgba(230, 234, 234, 0.54); -fx-background-radius: 20;" ); // Shadow effect

        getStage().setAlwaysOnTop( true );

        parentAncherPane.setStyle(
                "-fx-background-color: rgba(255, 255, 255, 0.5);" +
                        "-fx-background-insets: " + shadowSize + ";"
        );

        listview.depthProperty().set( 1 );
        listview.setExpanded( true );
        spinner.setVisible( true );

        status.setVisible( false );
        spinner.managedProperty().bind( spinner.visibleProperty() );
        status.managedProperty().bind( status.visibleProperty() );
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package elmp3delosdioses;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

/**
 *
 * @author Alex
 */
public class ElMP3delosdioses extends Application {
    
    String song;
    File direc;
    String locator;
    Media thesong;
    MediaPlayer player;
    
    /**
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        Button btn = new Button();
        Button pausebtn = new Button();
        song = "Blumenkranz.mp3";
        direc = new File(song);
        locator = direc.toURI().toString();
        thesong = new Media(locator);
        player = new MediaPlayer(thesong);
        pausebtn.setText("Pause");
        btn.setText("Play 'Blumenkranz'");
        btn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World!");
                player.play();
            }
        });
        pausebtn.setOnAction(new EventHandler<ActionEvent>() {
            
            @Override
            public void handle(ActionEvent event){
                player.pause();
            }
        });
        
        StackPane root = new StackPane();
        root.getChildren().add(btn);
        root.getChildren().add(pausebtn);
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Application.launch();
        launch(args);
    }
    
}

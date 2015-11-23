/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author icysniper
 */
public class HyperlinkDialogue extends Stage{
    TextArea textArea;
    TextField selectedText;
    VBox screen;
    HBox top;
    HBox mid;
    HBox bot;
    Button addHyperlink;
    TextField url;
    Pane pane;
    Scene scene;
    
    public HyperlinkDialogue(String text) {
        pane = new Pane();
        textArea = new TextArea(text);
        textArea.setWrapText(true);
        selectedText = new TextField();
        textArea.setOnMouseReleased( e-> {
            String str = textArea.getSelectedText();
            System.out.println("Ho" + str);//
            if (str != null && !str.equals(""))
                selectedText.setText(str);
        });
        screen = new VBox();
        top = new HBox();
        mid = new HBox();
        bot = new HBox();
        addHyperlink = new Button("Add Hyperlink");
        url = new TextField();
        
        top.getChildren().addAll(textArea);
        mid.getChildren().addAll(new Label("Selected Text: "), selectedText);
        bot.getChildren().addAll(new Label("Hyperlink URL: "), url);
        
        screen.getChildren().addAll(top, mid, bot, addHyperlink);
        pane.getChildren().add(screen);
        scene = new Scene(pane, 500, 500);
        
        this.setScene(scene);
        this.setTitle("Add Hyperlink");
        
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px");
    }
    
}

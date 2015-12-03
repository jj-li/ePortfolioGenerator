/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import epm.model.HyperlinkComponent;
import epm.model.Page;
import epm.model.TextComponent;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 *
 * @author icysniper
 */
public class HyperlinkDialogue extends Stage{
    TextArea textArea;
    Text selectedText;
    VBox screen;
    HBox top;
    HBox mid;
    HBox bot;
    Button addHyperlink;
    TextField url;
    Pane pane;
    Scene scene;
    
    public HyperlinkDialogue(String text, TextComponent textComponent) {
        pane = new Pane();
        textArea = new TextArea(text);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        selectedText = new Text();
        selectedText.setWrappingWidth(360);
        selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
        textArea.setOnMouseClicked( e-> {
            String str = textArea.getSelectedText();
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
        addHyperlink.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        
        addHyperlink.setOnAction(e-> {
            HyperlinkComponent component = new HyperlinkComponent(textArea.getText(), selectedText.getText(), url.getText());
            textComponent.addHyperlink(component);
            this.hide();
        });
    }
    
    public HyperlinkDialogue(ArrayList<String> items, TextComponent textComponent) {
        ListView<String> list = new ListView<String>();
        for (String s : items)
            list.getItems().add(s);
        selectedText = new Text();
        list.setOnMouseClicked (e-> {
            int selectedIdx = list.getSelectionModel().getSelectedIndex();
             if (selectedIdx != -1) {
                selectedText.setText(list.getSelectionModel().getSelectedItem());
             }
        });
        pane = new Pane();
        selectedText.setWrappingWidth(360);
        selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
        screen = new VBox();
        top = new HBox();
        mid = new HBox();
        bot = new HBox();
        addHyperlink = new Button("Add Hyperlink");
        url = new TextField();
        
        top.getChildren().addAll(list);
        mid.getChildren().addAll(new Label("Selected Text: "), selectedText);
        bot.getChildren().addAll(new Label("Hyperlink URL: "), url);
        
        screen.getChildren().addAll(top, mid, bot, addHyperlink);
        pane.getChildren().add(screen);
        scene = new Scene(pane, 500, 600);
        
        this.setScene(scene);
        this.setTitle("Add Hyperlink");
        
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px");
        addHyperlink.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        
        addHyperlink.setOnAction(e-> {
            HyperlinkComponent component = new HyperlinkComponent(list.getSelectionModel().getSelectedIndex(), items, url.getText());
            textComponent.addHyperlink(component);
            this.hide();
        });
        
        list.prefWidthProperty().bind(scene.widthProperty());
    }
    
    public HyperlinkDialogue(String text, ArrayList<HyperlinkComponent> links) {
        pane = new Pane();
        textArea = new TextArea(text);
        textArea.setWrapText(true);
        ScrollPane sp = new ScrollPane();
        for (HyperlinkComponent link : links) {
            selectedText = new Text(link.getSelectedText());
            selectedText.setWrappingWidth(360);
            selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
            textArea.setOnMouseClicked( e-> {
                String str = textArea.getSelectedText();
                if (str != null && !str.equals(""))
                    selectedText.setText(str);
            });
            screen = new VBox();
            top = new HBox();
            mid = new HBox();
            bot = new HBox();
            addHyperlink = new Button("Edit Hyperlink");
            url = new TextField(link.getUrl());

            top.getChildren().addAll(textArea);
            mid.getChildren().addAll(new Label("Selected Text: "), selectedText);
            bot.getChildren().addAll(new Label("Hyperlink URL: "), url);

            screen.getChildren().addAll(top, mid, bot, addHyperlink);
            pane.getChildren().add(screen);
        }
        
        sp.setContent(pane);
        scene = new Scene(sp, 500, 500);
        
        this.setScene(scene);
        this.setTitle("Edit Hyperlink");
        
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px");
        addHyperlink.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author icysniper
 */
public class TextComponentDialogue extends Stage{
    
    Pane pane;
    ToggleGroup types;
    RadioButton rb1 = new RadioButton("Paragraph");
    RadioButton rb2 = new RadioButton("List");
    RadioButton rb3 = new RadioButton("Header");
    Scene scene;
    Button addItemButton;
    TextField addItemField;
    TextArea textArea;
    ListView<String> list;
    HBox top;
    HBox bottom;
    HBox last = new HBox();
    VBox screen;
    Label textTypes;
    Button addComponent = new Button("Add Text Component");
    
    public TextComponentDialogue() {
        textArea = new TextArea();
        textTypes = new Label("Text Component Type: ");
        pane = new Pane();
        top = new HBox();
        bottom = new HBox();
        screen = new VBox();
        addItemButton = new Button("Add Text");
        addItemField = new TextField();
        list = new ListView<String>();
        addItemButton.setOnMouseClicked( e-> {
            list.getItems().add(addItemField.getText());
        });
        
        types = new ToggleGroup();
        rb1.setToggleGroup(types);
        rb2.setToggleGroup(types);
        rb3.setToggleGroup(types);
        VBox buttons = new VBox();
        buttons.getChildren().addAll(rb1, rb2, rb3);
        
        top.getChildren().addAll(textTypes, buttons);
        screen.getChildren().add(top);
        
        rb1.setOnMouseClicked( e-> {
            screen.getChildren().clear();
            bottom.getChildren().clear();
            last.getChildren().clear();
            screen.getChildren().add(top);
            bottom.getChildren().add(textArea);
            last.getChildren().add(addComponent);
            screen.getChildren().addAll(bottom, last);
        });
        
        rb2.setOnMouseClicked( e-> {
            screen.getChildren().clear();
            screen.getChildren().add(top);
            bottom.getChildren().clear();
            bottom.getChildren().add(list);
            last.getChildren().clear();
            HBox bottom2 = new HBox();
            last.getChildren().add(addComponent);
            bottom2.getChildren().clear();
            bottom2.getChildren().addAll(addItemField, addItemButton);
            screen.getChildren().addAll(bottom, bottom2, last);
            screen.prefHeightProperty().bind(scene.heightProperty());
            list.prefWidthProperty().bind(scene.widthProperty());
        });
        
        rb3.setOnMouseClicked( e-> {
            screen.getChildren().clear();
            bottom.getChildren().clear();
            last.getChildren().clear();
            last.getChildren().add(addComponent);
            screen.getChildren().add(top);
            bottom.getChildren().add(textArea);
            screen.getChildren().addAll(bottom, last);
        });
        
        last.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottom.setStyle("-fx-padding: 10px 0px 0px 0px");
        pane.getChildren().add(screen);
        scene = new Scene(pane, 295, 500);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        this.setScene(scene);
    }
}

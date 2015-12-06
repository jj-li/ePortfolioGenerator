/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import epm.model.TextComponent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author icysniper
 */
public class FontDialogue extends Stage{
    VBox screen;
    ComboBox fontFamily;
    ComboBox fontStyle;
    TextField fontSize;
    HBox top;
    HBox mid;
    HBox bottom;
    Button changeFont;
    Scene scene;
    Pane pane;
    
    public FontDialogue(TextComponent component) {
        screen = new VBox();
        fontFamily = new ComboBox();
        fontStyle = new ComboBox();
        fontSize = new TextField("" + component.getSize());
        top = new HBox();
        mid = new HBox();
        bottom = new HBox();
        changeFont = new Button("Change Font");
        pane = new Pane();
        
        fontFamily.getItems().addAll("Tinos", "Comic Sans MS", "Montserrat", "Merriweather", "Josefin Sans");
        if (component.getFont().equals(""))
            fontFamily.setValue("Tinos");
        else
            fontFamily.setValue(component.getFont());
        
        fontStyle.getItems().addAll("Normal", "Bold", "Italics");
        if (component.getStyle().equals(""))
            fontStyle.setValue("Normal");
        else
            fontStyle.setValue(component.getStyle());
        
        top.getChildren().addAll(new Label("Font Family: "), fontFamily);
        mid.getChildren().addAll(new Label("Font Style: "), fontStyle);
        bottom.getChildren().addAll(new Label("Font Size: "), fontSize);
        
        screen.getChildren().addAll(top, mid, bottom, changeFont);
        pane.getChildren().add(screen);
        scene = new Scene(pane, 300, 185);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px");
        this.setScene(scene);
        this.setTitle("Select Font");
        
        changeFont.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0);");
        changeFont.setOnAction(e-> {
            try {
                component.setStyle((String)fontStyle.getValue());
                component.setFont((String)fontFamily.getValue());
                component.setSize(Integer.parseInt(fontSize.getText()));
                this.hide();
            }
            catch (NumberFormatException e1) {
            
            }
        });
    }
}

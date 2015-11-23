/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import epm.controller.ImageSelectionController;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class ImageComponentDialogue extends Stage{
    Button selectImage;
    Text imagePath;
    HBox top;
    HBox last;
    VBox screen;
    Pane pane;
    Scene scene;
    Label widthLabel;
    Label heightLabel;
    TextField widthField;
    TextField heightField;
    Button addComponent = new Button("Add Image");
    ComboBox imagePosition;
    Label imagePositionLabel;
    
    public ImageComponentDialogue() {
        selectImage = new Button("Select Image");
        imagePath = new Text();
        top = new HBox();
        last = new HBox();
        screen = new VBox();
        widthLabel = new Label("Image Width: ");
        heightLabel = new Label("Image Height: ");
        widthField = new TextField();
        heightField = new TextField();
        pane = new Pane();
        imagePositionLabel = new Label("Image Float: ");
        imagePosition = new ComboBox();
        imagePosition.getItems().addAll("Left", "Right", "Neither");
        imagePosition.setValue("Left");
        
        selectImage.setOnMouseClicked( e-> {
            try {
                ImageSelectionController selectImage = new ImageSelectionController();
                imagePath.setText(selectImage.processSelectImage()); 
            }
            catch (NullPointerException e1){
                
            }
        });
        
        top.getChildren().add(selectImage);
        HBox bottomOne = new HBox();
        HBox bottomTwo = new HBox();
        HBox bottomThree = new HBox();
        bottomOne.getChildren().addAll(imagePositionLabel, imagePosition);
        bottomTwo.getChildren().addAll(widthLabel, widthField);
        bottomThree.getChildren().addAll(heightLabel, heightField);
        last.getChildren().add(addComponent);
        screen.getChildren().addAll(top, imagePath, bottomOne, bottomTwo, bottomThree, last);
        pane.getChildren().add(screen);
        
        last.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomOne.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomTwo.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomThree.setStyle("-fx-padding: 10px 0px 0px 0px");
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        
        scene = new Scene(pane, 380, 230);
        imagePath.setWrappingWidth(scene.getWidth());
        scene.getStylesheets().add(STYLE_SHEET_UI);
        this.setScene(scene);
        
        selectImage.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        addComponent.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
    }
    
    public ImageComponentDialogue(String url, String position, double width, double height) {
        selectImage = new Button("Select Image");
        imagePath = new Text(url);
        top = new HBox();
        last = new HBox();
        screen = new VBox();
        widthLabel = new Label("Image Width: ");
        heightLabel = new Label("Image Height: ");
        widthField = new TextField("" + width);
        heightField = new TextField("" + height);
        pane = new Pane();
        imagePositionLabel = new Label("Image Float: ");
        imagePosition = new ComboBox();
        imagePosition.getItems().addAll("Left", "Right", "Neither");
        imagePosition.setValue(position);
        
        selectImage.setOnMouseClicked( e-> {
            try {
                ImageSelectionController selectImage = new ImageSelectionController();
                imagePath.setText(selectImage.processSelectImage()); 
            }
            catch (NullPointerException e1){
                
            }
        });
        
        Button editImage = new Button("Edit Image");
        top.getChildren().add(selectImage);
        HBox bottomOne = new HBox();
        HBox bottomTwo = new HBox();
        HBox bottomThree = new HBox();
        bottomOne.getChildren().addAll(imagePositionLabel, imagePosition);
        bottomTwo.getChildren().addAll(widthLabel, widthField);
        bottomThree.getChildren().addAll(heightLabel, heightField);
        last.getChildren().add(editImage);
        screen.getChildren().addAll(top, imagePath, bottomOne, bottomTwo, bottomThree, last);
        pane.getChildren().add(screen);
        
        last.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomOne.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomTwo.setStyle("-fx-padding: 10px 0px 0px 0px");
        bottomThree.setStyle("-fx-padding: 10px 0px 0px 0px");
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        
        scene = new Scene(pane, 380, 230);
        imagePath.setWrappingWidth(scene.getWidth());
        scene.getStylesheets().add(STYLE_SHEET_UI);
        this.setScene(scene);
        
        selectImage.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        editImage.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
    }
}

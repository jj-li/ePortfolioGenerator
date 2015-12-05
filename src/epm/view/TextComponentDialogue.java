/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.view;

import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD_TITLE;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.error.ErrorHandler;
import epm.model.HyperlinkComponent;
import epm.model.Page;
import epm.model.TextComponent;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;

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
    TextField textField;
    ListView<String> list;
    HBox top;
    HBox bottom;
    HBox last = new HBox();
    VBox screen;
    Label textTypes;
    Button addComponent = new Button("Add Text Component");
    ComboBox paragraphFonts;
    Label paragraphFontsLabel;
    Button removeItemButton;
    
    public TextComponentDialogue(Page page) {
        addComponent.setOnMouseClicked( e-> {
            addTextComponent(page);
        });
        removeItemButton = new Button("Remove Item");
        textField = new TextField();
        textArea = new TextArea();
        textTypes = new Label("Text Component Type: ");
        pane = new Pane();
        top = new HBox();
        bottom = new HBox();
        screen = new VBox();
        addItemButton = new Button("Add Item");
        addItemField = new TextField();
        list = new ListView<String>();
        addItemButton.setOnMouseClicked( e-> {
            list.getItems().add(addItemField.getText());
            addItemField.clear();
        });
        removeItemButton.setOnMouseClicked( e-> {
            int selectedIdx = list.getSelectionModel().getSelectedIndex();
             if (selectedIdx != -1) {
                list.getItems().remove(selectedIdx);
             }
        });
        
        types = new ToggleGroup();
        rb1.setToggleGroup(types);
        rb2.setToggleGroup(types);
        rb3.setToggleGroup(types);
        VBox buttons = new VBox();
        buttons.getChildren().addAll(rb1, rb2, rb3);
        
        top.getChildren().addAll(textTypes, buttons);
        screen.getChildren().add(top);
        
        paragraphFonts = new ComboBox();
        paragraphFonts.getItems().addAll("Times New Roman", "Comic Sans MS", "Montserrat", "Merriweather", "Josefin Sans");
        paragraphFonts.setValue("Times New Roman");
        paragraphFontsLabel = new Label("Paragraph Font: ");
        
        rb1.setOnMouseClicked( e-> {
            screen.getChildren().clear();
            bottom.getChildren().clear();
            last.getChildren().clear();
            screen.getChildren().add(top);
            HBox bottom1 = new HBox();
            bottom1.getChildren().addAll(paragraphFontsLabel, paragraphFonts);
            bottom1.setStyle("-fx-padding: 10px 0px 0px 0px");
            bottom.getChildren().add(textArea);
            last.getChildren().add(addComponent);
            screen.getChildren().addAll(bottom1, bottom, last);
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
            bottom2.getChildren().addAll(addItemField, addItemButton, removeItemButton);
            screen.getChildren().addAll(bottom, bottom2, last);
            screen.prefHeightProperty().bind(scene.heightProperty());
            list.prefWidthProperty().bind(scene.widthProperty());
        });
        
        rb3.setOnMouseClicked( e-> {
            screen.getChildren().clear();
            bottom.getChildren().clear();
            last.getChildren().clear();
            last.getChildren().add(addComponent);
            addComponent.prefWidthProperty().bind(scene.widthProperty());
            screen.getChildren().add(top);
            bottom.getChildren().add(textField);
            screen.getChildren().addAll(bottom, last);
        });
        
        last.setStyle("-fx-padding: 10px 0px 5px 0px");
        bottom.setStyle("-fx-padding: 10px 0px 0px 0px");
        pane.getChildren().add(screen);
        scene = new Scene(pane, 405, 500);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        this.setScene(scene);
        this.setTitle("Add Text Component");
        
        addComponent.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
    }
    
    public TextComponentDialogue(String type, String data, TextComponent component) {
        textArea = new TextArea(data);
        textArea.setWrapText(true);
        textField = new TextField(data);
        paragraphFonts = new ComboBox();
        paragraphFonts.getItems().addAll("Times New Roman", "Comic Sans MS", "Montserrat", "Merriweather", "Josefin Sans");
        paragraphFonts.setValue(component.getFont());
        paragraphFontsLabel = new Label("Paragraph Font: ");
        screen = new VBox();
        pane = new Pane();
        HBox top = new HBox();
        HBox bottom = new HBox();
        Button editHeader = new Button("Edit Header");
        Button editParagraph = new Button("Edit Paragraph");
        if (type.equalsIgnoreCase("paragraph")) {
            top.getChildren().addAll(paragraphFontsLabel, paragraphFonts);
            bottom.getChildren().addAll(textArea);
            bottom.setStyle("-fx-padding: 10px 0px 10px 0px");
            screen.getChildren().addAll(top, bottom, editParagraph);
            pane.getChildren().add(screen);
            scene = new Scene(pane, 600, 350);
            this.setTitle("Edit Paragraph");
        }
        else {
            top.getChildren().addAll(textField);
            top.setStyle("-fx-padding: 0px 0px 10px 0px");
            screen.getChildren().addAll(top, editHeader);
            pane.getChildren().add(screen);
            scene = new Scene(pane, 300, 200);
            textField.prefWidthProperty().bind(scene.widthProperty());
            this.setTitle("Edit Header");
        }
        scene.getStylesheets().add(STYLE_SHEET_UI);
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        this.setScene(scene);
        
        editHeader.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        editParagraph.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        
        editHeader.setOnAction(e-> {
            component.setData(textField.getText());
            this.hide();
        });
        editParagraph.setOnAction(e-> {
            boolean checked = checkHyperlinks(component, textArea.getText());
            if (checked) {
                component.setData(textArea.getText());
                component.setFont((String)paragraphFonts.getValue());
            }
            this.hide();
            if (!checked)
                displayMessage();
        });
        
    }
    
    public boolean checkHyperlinks(TextComponent component, String data) {
        ArrayList<HyperlinkComponent> links = component.getHyperlinks();
        boolean checked = true;
        for (HyperlinkComponent link : links) {
            String selected = link.getSelectedText();
            int num = data.indexOf(selected);
            if (num == -1)
               checked = false;
        }
        return checked;
    }
    
    public TextComponentDialogue(String type, ArrayList<String> data, TextComponent component) {
        Button editItemButton = new Button("Edit Item");
        removeItemButton = new Button("Remove Item");
        addItemButton = new Button("Add Item");
        addItemField = new TextField();
        list = new ListView<String>();
        addItemButton.setOnMouseClicked( e-> {
            list.getItems().add(addItemField.getText());
            addItemField.clear();
        });
        removeItemButton.setOnMouseClicked( e-> {
            int selectedIdx = list.getSelectionModel().getSelectedIndex();
             if (selectedIdx != -1) {
                list.getItems().remove(selectedIdx);
             }
        });
        list.setOnMouseClicked( e-> {
            addItemField.setText((String)list.getSelectionModel().getSelectedItem());
        });
        editItemButton.setOnMouseClicked( e-> {
            int selectedIdx = list.getSelectionModel().getSelectedIndex();
            if (selectedIdx != -1) {
                list.getItems().set(selectedIdx, addItemField.getText());
            }
        });
        Button editList = new Button("Edit List");
        for (String s : data)
            list.getItems().add(s);
        
        HBox top = new HBox();
        top.getChildren().addAll(list);
        HBox bottom = new HBox();
        bottom.getChildren().addAll(addItemField, editItemButton, addItemButton, removeItemButton);
        bottom.setStyle("-fx-padding: 10px 0px 10px 0px; -fx-spacing: 5px;");
        pane = new Pane();
        screen = new VBox();
        screen.getChildren().addAll(top, bottom, editList);
        pane.getChildren().add(screen);
        scene = new Scene(pane, 510, 500);
        scene.getStylesheets().add(STYLE_SHEET_UI);
        list.prefWidthProperty().bind(scene.widthProperty());
        screen.prefWidthProperty().bind(scene.widthProperty());
        screen.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-hgap: 10px");
        this.setScene(scene);
        this.setTitle("Edit List");
        
        editList.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        editList.setOnAction(e-> {
            ArrayList<String> items = new ArrayList<String>();
            for (String s : list.getItems())
                items.add(s);
            boolean checked = checkHyperlinks(component, items);
            if (checked)
                component.setList(items);
            this.hide();
            if (!checked) {
                displayMessage();
            }
        });
    }
    
    private void displayMessage() {
        // POP OPEN A DIALOG TO DISPLAY TO THE USER
                Button btOK = new Button("OK");
                Text text = new Text("Please remove the hyper link first before editing the text associated with the hyper link.");
                Pane pane = new Pane();
                text.setWrappingWidth(400);
                pane.getStyleClass().add("error_box");
                VBox box = new VBox();
                box.getChildren().addAll(text, btOK);
                box.setStyle("-fx-padding: 10px 10px 0px 10px;");
                pane.getChildren().add(box);
                Scene scene = new Scene(pane, 500, 200);
                scene.getStylesheets().add(STYLE_SHEET_UI);
                Stage stage = new Stage();
                btOK.setOnAction(e1 -> {
                    stage.hide();
                });
                stage.setScene(scene);
                stage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
                stage.setTitle("Error Editing Component");
                stage.showAndWait();
    }
    
    public boolean checkHyperlinks(TextComponent component, ArrayList<String> items) {
        ArrayList<HyperlinkComponent> links = component.getHyperlinks();
        ArrayList<HyperlinkComponent> temp = new ArrayList<HyperlinkComponent>();
        int counter = 0;
        boolean checked = true;
        for (HyperlinkComponent link : links) {
            temp.add(link);
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).equals(link.getIndexItem())) {
                    link.setIndex(i);
                    link.setItems(items);
                    counter++;
                    checked = true;
                }
                    
            }
        }
        if (counter != links.size()) {
            component.setHyperlinks(temp);
            checked = false;
        }
        else
            checked = true;
        return checked;
    }
    
    public void addTextComponent(Page page) {
        RadioButton selected = (RadioButton)types.getSelectedToggle();
        String textualType = selected.getText();
        if (textualType.equalsIgnoreCase("paragraph")) {
            TextComponent component = new TextComponent(textualType, textArea.getText(), (String)paragraphFonts.getValue());
            page.addTextComponent(component);
        }
        else if (textualType.equalsIgnoreCase("header")) {
            TextComponent component = new TextComponent(textualType, textField.getText(), "Times New Roman");
            page.addTextComponent(component);
        }
        else {
            ArrayList<String> data = new ArrayList<String>();
            for (String s : list.getItems())
                data.add(s);
            TextComponent component = new TextComponent(textualType, data, "Times New Roman");
            page.addTextComponent(component);
        }
        this.hide();
    }
}

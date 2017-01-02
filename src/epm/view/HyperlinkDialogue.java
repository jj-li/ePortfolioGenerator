package epm.view;

import static epm.StartupConstants.STYLE_SHEET_UI;
import epm.model.HyperlinkComponent;
import epm.model.TextComponent;
import java.util.ArrayList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
 * @author Jia Li
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
            String check1 = "http://www.";
            String check2 = "www.";
            String link = "";
            if (url.getText().indexOf(check1) == -1) {
                if (url.getText().indexOf(check2) == -1)
                    link = check1 + url.getText();
                else
                    link = "http://" + url.getText();
            }
            HyperlinkComponent component = new HyperlinkComponent(textArea.getText(), selectedText.getText(), link);
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
            String check1 = "http://www.";
            String check2 = "www.";
            String link = "";
            if (url.getText().indexOf(check1) == -1) {
                if (url.getText().indexOf(check2) == -1)
                    link = check1 + url.getText();
                else
                    link = "http://" + url.getText();
            }
            HyperlinkComponent component = new HyperlinkComponent(list.getSelectionModel().getSelectedIndex(), items, link);
            textComponent.addHyperlink(component);
            this.hide();
        });
        
        list.prefWidthProperty().bind(scene.widthProperty());
    }
    
    public HyperlinkDialogue(String text, ArrayList<HyperlinkComponent> links, TextComponent component) {
        Button editHyperlink = new Button("Edit Hyperlink");
        editHyperlink.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        VBox everything = new VBox();
        textArea = new TextArea(text);
        textArea.setWrapText(true);
        textArea.setEditable(false);
        ScrollPane sp = new ScrollPane();
        everything.getChildren().add(textArea);
        ArrayList<TextField> urls = new ArrayList<TextField>();
        ArrayList<CheckBox> checks = new ArrayList<CheckBox>();
        for (HyperlinkComponent link : links) {
            selectedText = new Text(link.getSelectedText());
            selectedText.setWrappingWidth(350);
            selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
            
            mid = new HBox();
            bot = new HBox();
            addHyperlink = new Button("Edit Hyperlink");
            url = new TextField(link.getUrl());

            mid.getChildren().addAll(new Label("Selected Text: "), selectedText);
            bot.getChildren().addAll(new Label("Hyperlink URL: "), url);
            
            urls.add(url);
            
            CheckBox cb = new CheckBox("Remove Hyperlink");
            checks.add(cb);
            HBox checking = new HBox();
            checking.getChildren().addAll(bot, cb);
            checking.setStyle("-fx-spacing: 5px");
            everything.getChildren().addAll(mid, checking);
        }
        everything.getChildren().add(editHyperlink);
        everything.setStyle("-fx-background-color: #add8e6; -fx-spacing: 10px;");
        sp.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px; -fx-background: #add8e6");
        sp.setContent(everything);
       
        scene = new Scene(sp, 575, 500);
        
        this.setScene(scene);
        this.setTitle("Edit Hyperlink");
        
        scene.getStylesheets().add(STYLE_SHEET_UI);

        editHyperlink.setOnAction( e-> {
            for (int i = urls.size()-1; i >= 0; i--) {
                String check1 = "http://www.";
                String check2 = "www.";
                String link = "";
                if (urls.get(i).getText().indexOf(check1) == -1) {
                    if (urls.get(i).getText().indexOf(check2) == -1)
                        link = check1 + urls.get(i).getText();
                    else
                        link = "http://" + urls.get(i).getText();
                    urls.get(i).setText(link);
                }
                if (checks.get(i).selectedProperty().get() == false) {
                    HyperlinkComponent temp = links.get(i);
                    temp.setUrl(urls.get(i).getText());
                }
                else {
                    component.removeHyperlink(i);
                }
            }
            this.hide();
        });
    }
    
    
     public HyperlinkDialogue(ArrayList<String> items, ArrayList<HyperlinkComponent> links, TextComponent textComponent) {
        ListView<String> list = new ListView<String>();
        for (String s : items)
            list.getItems().add(s);
        selectedText = new Text();
        selectedText.setWrappingWidth(360);
        selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
        
        Button editHyperlink = new Button("Edit Hyperlink");
        editHyperlink.setStyle("-fx-font-weight: bolder; -fx-border-color: rgb(0,0,0)");
        url = new TextField();
    
        VBox everything = new VBox();

        ScrollPane sp = new ScrollPane();
        everything.getChildren().add(list);
        ArrayList<TextField> urls = new ArrayList<TextField>();
        ArrayList<Integer> positions  = new ArrayList<Integer>();
        ArrayList<CheckBox> checks = new ArrayList<CheckBox>();
        for (HyperlinkComponent link : links) {
            selectedText = new Text(list.getItems().get(link.getIndex()));
            selectedText.setWrappingWidth(350);
            selectedText.setStyle("-fx-border-color: rgb(0,0,0);");
            
            mid = new HBox();
            bot = new HBox();
            url = new TextField(link.getUrl());

            mid.getChildren().addAll(new Label("Selected Item: "), selectedText);
            bot.getChildren().addAll(new Label("Hyperlink URL: "), url);
            
            urls.add(url);
            positions.add(link.getIndex());
            
            CheckBox cb = new CheckBox("Remove Hyperlink");
            checks.add(cb);
            HBox checking = new HBox();
            checking.getChildren().addAll(bot, cb);
            checking.setStyle("-fx-spacing: 5px");
            everything.getChildren().addAll(mid, checking);
        }
        everything.getChildren().add(editHyperlink);
        everything.setStyle("-fx-background-color: #add8e6; -fx-spacing: 10px;");
        sp.setStyle("-fx-padding: 10px 10px 0px 10px; -fx-spacing: 10px; -fx-background: #add8e6");
        sp.setContent(everything);
       
        scene = new Scene(sp, 575, 500);
        
        this.setScene(scene);
        this.setTitle("Edit Hyperlink");
        
        scene.getStylesheets().add(STYLE_SHEET_UI);

        editHyperlink.setOnAction( e-> {
            for (int i = urls.size()-1; i >= 0; i--) {
                String check1 = "http://www.";
                String check2 = "www.";
                String link = "";
                if (urls.get(i).getText().indexOf(check1) == -1) {
                    if (urls.get(i).getText().indexOf(check2) == -1)
                        link = check1 + urls.get(i).getText();
                    else
                        link = "http://" + urls.get(i).getText();
                    urls.get(i).setText(link);
                }
                if (checks.get(i).selectedProperty().get() == false) {
                    HyperlinkComponent temp = links.get(i);
                    temp.setUrl(urls.get(i).getText());
                }
                else {
                    textComponent.removeHyperlink(i);
                }
            }
            this.hide();
        });
        
       
        
        list.prefWidthProperty().bind(scene.widthProperty());
    }
}

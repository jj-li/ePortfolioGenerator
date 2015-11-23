/**
 * @author Jia Li
 **/
package epm.view;

import java.io.File;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.LanguagePropertyType.MISSING_IMAGE;
import static epm.LanguagePropertyType.MISSING_IMAGE_TITLE;
import static epm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static epm.EPortfolioMaker.CORRUPTED_SLIDE;
import static epm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.controller.ImageSelectionController;
import epm.model.Page;
import static epm.file.EPortfolioFileManager.SLASH;
import epm.model.ImageComponent;
import epm.model.TextComponent;
import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.stage.Screen;

/**
 * This UI component has the controls for editing a single slide
 * in a slide show, including controls for selected the slide image
 * and changing its caption.
 * 
 * 
 */
public class PageEditView extends VBox {
    // SLIDE THIS COMPONENT EDITS
    Page page;
    
    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;
    
    Label title;
    TextField titleField;
    Label name;
    TextField nameField;
    Label layoutLabel;
    ComboBox layout;
    Label cssLabel;
    ComboBox css;
    Label fontLabel;
    ComboBox font;
    Label footerLabel;
    TextArea footerField;
    
    Tab tab;
    
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;

    /**
     * THis constructor initializes the full UI for this component, using
     * the initSlide data for initializing values./
     * 
     * @param initSlide The slide to be edited by this component.
     */
    public PageEditView(Page initPage, Tab tab) {
        imageController = new ImageSelectionController();
        imageSelectionView = new ImageView();
	this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
	page = initPage;
	this.tab = tab;
        
        initTitleAndName();
        HBox titleSection = new HBox();
        HBox nameSection = new HBox();
        
        initLayoutFontAndCSS();
        HBox layoutCSSFont = new HBox();
        
        initFooter();
        HBox footer = new HBox();
        
        
        titleSection.getChildren().addAll(title, titleField);
        nameSection.getChildren().addAll(name, nameField);
        layoutCSSFont.getChildren().addAll(layoutLabel, layout, cssLabel, css, fontLabel, font);
        footer.getChildren().addAll(footerLabel, footerField);
        getChildren().addAll(layoutCSSFont, titleSection, nameSection, initBannerImg(), footer);

        setStyle("-fx-background: #ffffb2");
	
    }
    
    public Page getPage() {
        return page;
    }
    
    private void initTitleAndName() {
        title = new Label("Page Title: ");
        titleField = new TextField(page.getTitle());
        titleField.setOnKeyReleased( e-> {
            page.setTitle(titleField.getText());
            tab.setText(page.getTitle());
        });
        name = new Label("Student Name: ");
        nameField = new TextField(page.getStudentName());
        nameField.setOnKeyReleased( e-> {
            page.setStudentName(nameField.getText());
        });
    }
    
    private void initLayoutFontAndCSS() {
        layoutLabel = new Label("Page Layout: ");
        layout = new ComboBox();
        layout.getItems().addAll("Top-Left Nagivation", "Left Navgiation", "Middle-Left Navigation", "Middle-Right Navigation", "Middle Navigation");
        layout.setValue("Top-Left Navation");
        
        cssLabel = new Label("     Page Color Scheme: ");
        css = new ComboBox();
        css.getItems().addAll("Blue/Yellow", "Cyan/Red", "Orange/Yellow", "Red/Green", "Green/Blue");
        css.setValue("Blue/Yellow");
        
        fontLabel = new Label("     Page Font: ");
        font = new ComboBox();
        font.getItems().addAll("PT Sans", "Dosis", "Yanone Kaffeesatz", "Oxygen", "Nunito");
        font.setValue("PT Sans");
    }
    
    private void initFooter() {
        footerLabel = new Label("Page Footer: ");
        footerField = new TextArea();

    }
    
    private HBox initBannerImg() {
        HBox bannerImg = new HBox();
        Button selectImg = new Button("Select Banner Image");
        selectImg.setOnMouseClicked(e-> {
            String path = imageController.processSelectImage();
            File file = new File(path);
            try {
                // GET AND SET THE IMAGE
                URL fileURL = file.toURI().toURL();
                Image image = new Image(fileURL.toExternalForm());
                imageSelectionView.setImage(image);
                double scaledWidth = 100;
                double perc = scaledWidth / image.getWidth();
                double scaledHeight = image.getHeight() * perc;
                imageSelectionView.setFitWidth(scaledWidth);
                imageSelectionView.setFitHeight(scaledHeight);
            }
            catch (Exception e1) {
                
            }
        });
        bannerImg.getChildren().addAll(selectImg, imageSelectionView);
        bannerImg.setStyle("-fx-spacing: 10px;");
        return bannerImg;
    }
    
    public void reloadComponents() {
        Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();
        ArrayList<TextComponent> textComponents = page.getTextComponents();
        ArrayList<ImageComponent> imageComponents = page.getImageComponents();
        for (TextComponent component : textComponents) {
            String textType = component.getTextType();
            if (textType.equalsIgnoreCase("paragraph")){
                Label paragraphLabel = new Label("Paragraph: ");
                Text paragraphField = new Text(component.getData());
                HBox paragraphComponent = new HBox();
                paragraphComponent.getChildren().addAll(paragraphLabel, paragraphField);
                paragraphComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                paragraphField.setWrappingWidth(bounds.getWidth()-500);
                getChildren().add(paragraphComponent);
            }
            else if (textType.equalsIgnoreCase("list")) {
                Label listLabel = new Label("List: ");
                ArrayList<String> data = component.getList();
                ListView<String> list = new ListView<String>();
                for (String s : data)
                    list.getItems().add(s);
                HBox listComponent = new HBox();
                listComponent.getChildren().addAll(listLabel, list);
                listComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                getChildren().add(listComponent);
            }
            else {
                Label headerLabel = new Label("Header: ");
                Text headerField = new Text(component.getData());
                HBox headerComponent = new HBox();
                headerComponent.getChildren().addAll(headerLabel, headerField);
                headerComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                headerField.setWrappingWidth(bounds.getWidth()-500);
                getChildren().add(headerComponent);
            }
                
        };
        
        for (ImageComponent component : imageComponents) {
            Label imageLabel = new Label("Image: ");
            ImageView imageView = component.getImageView();
            HBox imageComponent = new HBox();
            imageComponent.getChildren().addAll(imageLabel, imageView);
            imageComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            getChildren().add(imageComponent);
        };
    }
    
    
}
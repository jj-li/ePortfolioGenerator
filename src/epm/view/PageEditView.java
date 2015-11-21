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
    
    // CONTROLS FOR EDITING THE CAPTION
    Label title;
    TextField titleField;
    Label name;
    TextField nameField;
    
    
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;

    /**
     * THis constructor initializes the full UI for this component, using
     * the initSlide data for initializing values./
     * 
     * @param initSlide The slide to be edited by this component.
     */
    public PageEditView(Page initPage) {
	// FIRST SELECT THE CSS STYLE CLASS FOR THIS CONTAINER
	this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
	// KEEP THE SLIDE FOR LATER
	page = initPage;
	
	
        
	// LAY EVERYTHING OUT INSIDE THIS COMPONENT
	getChildren().add(imageSelectionView);

	// SETUP THE EVENT HANDLERS
	imageController = new ImageSelectionController();
	imageSelectionView.setOnMousePressed(e -> {
	    imageController.processSelectImage(page, this);
	});
    }
    
    /**
     * This function gets the image for the slide and uses it to
     * update the image displayed.
     */
    public void updateSlideImage() {
        //TO FIX
	/*
        String imagePath = page.getImagePath() + SLASH + page.getImageFileName();
	File file = new File(imagePath);
	try {
            //checks if file is valid
            if (!file.exists())
                throw new Exception();
            
	    // GET AND SET THE IMAGE
	    URL fileURL = file.toURI().toURL();
	    Image slideImage = new Image(fileURL.toExternalForm());
	    imageSelectionView.setImage(slideImage);
	    
	    // AND RESIZE IT
	    double scaledWidth = DEFAULT_THUMBNAIL_WIDTH;
	    double perc = scaledWidth / slideImage.getWidth();
	    double scaledHeight = slideImage.getHeight() * perc;
	    imageSelectionView.setFitWidth(scaledWidth);
	    imageSelectionView.setFitHeight(scaledHeight);
	} catch (Exception e) {
           CORRUPTED_SLIDE = page;
	   PropertiesManager props = PropertiesManager.getPropertiesManager();
           Button btOK = new Button("OK");
           Text text = new Text(page.getImageFileName() + props.getProperty(MISSING_IMAGE));
           GridPane pane = new GridPane();
           pane.getStyleClass().add("error_box");
           pane.add(text, 1, 1);
           pane.add(btOK, 1, 2);
           Scene scene = new Scene(pane, 600, 100);
           scene.getStylesheets().add(STYLE_SHEET_UI);
           Stage stage = new Stage();
           btOK.setOnAction(e1 -> {
               stage.close();
           });
           stage.setScene(scene);
           stage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
           stage.setTitle(props.getProperty(MISSING_IMAGE_TITLE));
           stage.show();
	}
        */
    }
    
    public ImageView getImageView()
    {
        return imageSelectionView;
    }        
}
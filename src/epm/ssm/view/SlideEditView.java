package epm.ssm.view;

import static epm.file.EPortfolioFileManager.SLASH;
import java.io.File;
import java.net.URL;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import properties_manager.PropertiesManager;
import epm.ssm.LanguagePropertyType;
import static epm.ssm.StartupConstants.CSS_CLASS_CAPTION_PROMPT;
import static epm.ssm.StartupConstants.CSS_CLASS_CAPTION_TEXT_FIELD;
import static epm.ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static epm.ssm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import epm.ssm.controller.ImageSelectionController;
import epm.ssm.model.Slide;

/**
 * This UI component has the controls for editing a single slide
 * in a slide show, including controls for selected the slide image
 * and changing its caption.
 * 
 * @author McKilla Gorilla & _____________
 */
public class SlideEditView extends HBox {
    SlideShowMakerView ui;
    
    // SLIDE THIS COMPONENT EDITS
    Slide slide;
    
    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;
    
    // CONTROLS FOR EDITING THE CAPTION
    VBox captionVBox;
    Label captionLabel;
    TextField captionTextField;
    
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;

    /**
     * THis constructor initializes the full UI for this component, using
     * the initSlide data for initializing values./
     * 
     * @param initSlide The slide to be edited by this component.
     */
    public SlideEditView(SlideShowMakerView initUi, Slide initSlide) {
	// KEEP THIS FOR LATER
	ui = initUi;
	
	// FIRST SELECT THE CSS STYLE CLASS FOR THIS CONTAINER
	this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
	
	// KEEP THE SLIDE FOR LATER
	slide = initSlide;
	
	// MAKE SURE WE ARE DISPLAYING THE PROPER IMAGE
	imageSelectionView = new ImageView();
	updateSlideImage();

	// SETUP THE CAPTION CONTROLS
	captionVBox = new VBox();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	captionLabel = new Label(props.getProperty(LanguagePropertyType.LABEL_CAPTION));
	captionTextField = new TextField();
	captionTextField.setText(slide.getCaption());
	captionVBox.getChildren().add(captionLabel);
	captionVBox.getChildren().add(captionTextField);

	// LAY EVERYTHING OUT INSIDE THIS COMPONENT
	getChildren().add(imageSelectionView);
	getChildren().add(captionVBox);

	// SETUP THE EVENT HANDLERS
	imageController = new ImageSelectionController(ui);
	imageSelectionView.setOnMousePressed(e -> {
	    imageController.processSelectImage(slide, this);
	});
	captionTextField.textProperty().addListener(e -> {
	    String text = captionTextField.getText();
	    slide.setCaption(text);	 
	    ui.updateFileToolbarControls(false);
	});
	
	// CHOOSE THE STYLE
	captionLabel.getStyleClass().add(CSS_CLASS_CAPTION_PROMPT);
	captionTextField.getStyleClass().add(CSS_CLASS_CAPTION_TEXT_FIELD);
    }
    
    /**
     * This function gets the image for the slide and uses it to
     * update the image displayed.
     */
    public void updateSlideImage() {
	String imagePath = slide.getImagePath() + SLASH + slide.getImageFileName();
	File file = new File(imagePath);
	try {
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
	}
    }    
}
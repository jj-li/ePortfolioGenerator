package epm.controller;

import java.io.File;
import javafx.stage.FileChooser;

/**
 * This controller provides a controller for when the user chooses to
 * select an image for the slide show.
 * 
 * @author Jia Li
 */
public class ImageSelectionController {
    
    /**
     * Default constructor doesn't need to initialize anything
     */
    public ImageSelectionController() {    }
    
    /**
     * This function provides the response to the user's request to
     * select an image.
     */
    public String processSelectImage() {
	FileChooser imageFileChooser = new FileChooser();

	// LET'S ONLY SEE IMAGE FILES
	FileChooser.ExtensionFilter jpgFilter = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
	FileChooser.ExtensionFilter pngFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
	FileChooser.ExtensionFilter gifFilter = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF");
	imageFileChooser.getExtensionFilters().addAll(jpgFilter, pngFilter, gifFilter);
	
	// LET'S OPEN THE FILE CHOOSER
	File file = imageFileChooser.showOpenDialog(null);
        return file.getPath();
    }
}

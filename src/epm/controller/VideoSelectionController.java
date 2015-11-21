/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.controller;

import java.io.File;
import javafx.stage.FileChooser;

/**
 *
 * @author icysniper
 */
public class VideoSelectionController {
    
    public String processSelectVideo() {
	FileChooser imageFileChooser = new FileChooser();
	
	
	
	// LET'S ONLY SEE IMAGE FILES
	FileChooser.ExtensionFilter mp4Filter = new FileChooser.ExtensionFilter("MP4 files (*.mp4)", "*.MP4");
	imageFileChooser.getExtensionFilters().addAll(mp4Filter);
	
	// LET'S OPEN THE FILE CHOOSER
	File file = imageFileChooser.showOpenDialog(null);
        return file.getPath();
        //TO FIX
	/*if (file != null) {
	    String path = file.getPath().substring(0, file.getPath().indexOf(file.getName()));
	    String fileName = file.getName();
	    slideToEdit.setImage(path, fileName);
	    view.updateSlideImage();
	}	    
	else {
	    // @todo provide error message for no files selected
	}*/
    }
}

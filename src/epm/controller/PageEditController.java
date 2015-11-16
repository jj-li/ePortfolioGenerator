package epm.controller;

import properties_manager.PropertiesManager;
import static epm.StartupConstants.DEFAULT_SLIDE_IMAGE;
import static epm.StartupConstants.PATH_SLIDE_SHOW_IMAGES;
import epm.model.EPortfolioModel;
import epm.view.EPortfolioMakerView;

/**
 * This controller provides responses for the slideshow edit toolbar,
 * which allows the user to add, remove, and reorder slides.
 * 
 * @author McKilla Gorilla & Jia Li
 */
public class PageEditController {
    // APP UI
    private EPortfolioMakerView ui;
    
    /**
     * This constructor keeps the UI for later.
     */
    public PageEditController(EPortfolioMakerView initUI) {
	ui = initUI;
    }
    
    /**
     * Provides a response for when the user wishes to add a new
     * slide to the slide show.
     */
    
}

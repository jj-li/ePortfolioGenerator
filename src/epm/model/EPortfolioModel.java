/**
 * @coauthor Jia Li
 **/
package epm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.LanguagePropertyType.MOVE_DOWN_SLIDE1;
import static epm.LanguagePropertyType.MOVE_DOWN_SLIDE2;
import static epm.LanguagePropertyType.MOVE_DOWN_SLIDE3;
import static epm.LanguagePropertyType.MOVE_UP_SLIDE1;
import static epm.LanguagePropertyType.MOVE_UP_SLIDE2;
import static epm.LanguagePropertyType.MOVE_UP_SLIDE3;
import static epm.LanguagePropertyType.REMOVE_SLIDE;
import static epm.LanguagePropertyType.REMOVE_SLIDE_ERROR_TITLE;
import epm.error.ErrorHandler;
import epm.view.EPortfolioMakerView;

/**
 * This class manages all the data associated with a slideshow.
 * 
 * @author McKilla Gorilla & _____________
 */
public class EPortfolioModel {
    EPortfolioMakerView ui;
    String title;
    ObservableList<Page> pages;
    Page selectedSlide;
    
    public EPortfolioModel(EPortfolioMakerView initUI) {
	ui = initUI;
	pages = FXCollections.observableArrayList();
        selectedSlide = null;
	reset();	
    }

    // ACCESSOR METHODS
    public boolean isSlideSelected() {
	return selectedSlide != null;
    }
    
    public ObservableList<Page> getPages() {
	return pages;
    }
    
    public Page getSelectedSlide() {
	return selectedSlide;
    }

    public String getTitle() { 
	return title; 
    }
    
    // MUTATOR METHODS
    public void setSelectedSlide(Page initSelectedSlide) {
	selectedSlide = initSelectedSlide;
    }
    
    public void setTitle(String initTitle) { 
	title = initTitle; 
    }

    // SERVICE METHODS
    
    /**
     * Resets the slide show to have no slides and a default title.
     */
    public void reset() {
	pages.clear();
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	title = props.getProperty(LanguagePropertyType.DEFAULT_SLIDE_SHOW_TITLE);
	selectedSlide = null;
    }

    /**
     * Adds a slide to the slide show with the parameter settings.
     * @param initImageFileName File name of the slide image to add.
     * @param initImagePath File path for the slide image to add.
     */
    public void addPage(   String initImageFileName,
			    String initImagePath) {
	Page slideToAdd = new Page(initImageFileName, initImagePath);
	pages.add(slideToAdd);
	ui.reloadSlideShowPane(this);
    }
    
    
    
}
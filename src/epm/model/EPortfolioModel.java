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
    Page selectedPage;
    
    public EPortfolioModel(EPortfolioMakerView initUI) {
	ui = initUI;
	pages = FXCollections.observableArrayList();
        selectedPage = null;
	reset();	
    }

    // ACCESSOR METHODS
    public boolean isPageSelected() {
	return selectedPage != null;
    }
    
    public ObservableList<Page> getPages() {
	return pages;
    }
    
    public Page getSelectedPage() {
	return selectedPage;
    }

    public String getTitle() { 
	return title; 
    }
    
    // MUTATOR METHODS
    public void setSelectedPage(Page initSelectedSlide) {
	selectedPage = initSelectedSlide;
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
	selectedPage = null;
    }

    /**
     * Adds a slide to the slide show with the parameter settings.
     * @param initImageFileName File name of the slide image to add.
     * @param initImagePath File path for the slide image to add.
     */
    public void addPage(){
        Page page = new Page();
        selectedPage = page;
        pages.add(page);
	ui.reloadSlideShowPane(this);
    }
    
    public void addPage(String title, String name){
        Page page = new Page(title, name);
        pages.add(page);
	ui.reloadSlideShowPane(this);
    }
    
    
    public void removePage(Page selectedPage) {
        if (!isPageSelected())
            return;
        for (int i = pages.size()-1; i >= 0; i--) {
            Page page = pages.get(i);
            if (selectedPage.equals(page)){
                pages.remove(i);
            }
        }
        ui.reloadSlideShowPane(this);
    }
}
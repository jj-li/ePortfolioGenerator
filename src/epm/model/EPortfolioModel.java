package epm.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import epm.view.EPortfolioMakerView;

/**
 * This class manages all the data associated with an ePortfolio.
 * 
 * @author Jia Li
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
    public String getStudentName() {
        return ui.getStudentName();
    }
    
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
    
    public Page addPage(String title, String name, String layout, String color, String font, String footer, String bannerPath, String bannerName){
        Page page = new Page(title, name);
        page.setLayout(layout);
        page.setColor(color);
        page.setFont(font);
        page.setBannerImgPath(bannerPath);
        page.setBannerImgName(bannerName);
        page.setFooter(footer);
        pages.add(page);
        ui.setStudentName(name);
        selectedPage = page;
        return page;
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
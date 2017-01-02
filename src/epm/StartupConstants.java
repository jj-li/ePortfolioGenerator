package epm;

/**
 * This class provides setup constants for initializing the application
 * that are NOT language dependent.
 * 
 * @author Jia Li
 */
public class StartupConstants {

    // WE'LL LOAD ALL THE UI AND LANGUAGE PROPERTIES FROM FILES,
    // BUT WE'LL NEED THESE VALUES TO START THE PROCESS

    public static String PROPERTY_TYPES_LIST = "property_types.txt";
    public static String UI_PROPERTIES_FILE_NAME = "properties_EN.xml";
    public static String PROPERTIES_SCHEMA_FILE_NAME = "properties_schema.xsd";
    public static String PATH_DATA = "data/";
    public static String PATH_EPORTFOLIOS = PATH_DATA + "ePortfolios/";
    public static String PATH_IMAGES = "images/";
    public static String PATH_ICONS = PATH_IMAGES + "icons/";
    public static String PATH_CSS = "epm/style/";
    public static String STYLE_SHEET_UI = PATH_CSS + "EPortfolioMakerStyle.css";

    // HERE ARE THE LANGUAGE INDEPENDENT GUI ICONS
    public static String ICON_NEW_PORTFOLIO = "New.png";
    public static String ICON_LOAD_PORTFOLIO = "Load.png";
    public static String ICON_SAVE_PORTFOLIO = "Save.png";
    public static String ICON_SAVE_AS_PORTFOLIO = "SaveAs.png";
    public static String ICON_EXPORT_PORTFOLIO= "Export.png";
    public static String ICON_EDIT_PORTFOLIO = "Edit.png";
    public static String ICON_VIEW_PORTFOLIO = "View.png";
    public static String ICON_EXIT = "Exit.png";
    public static String ICON_ADD_PAGE = "Add.png";
    public static String ICON_REMOVE_PAGE = "Remove.png";
    public static String ICON_MOVE_UP = "MoveUp.png";
    public static String ICON_MOVE_DOWN = "MoveDown.png";
    public static String ICON_PREVIOUS = "Previous.png";
    public static String ICON_NEXT = "Next.png";
    public static String WINDOWS_ICON = "WindowsIcon.png";
    public static String ICON_ADD_TEXT = "Text.png";
    public static String ICON_ADD_IMAGE = "Image.png";
    public static String ICON_ADD_SLIDESHOW = "Slideshow.png";
    public static String ICON_ADD_VIDEO = "Video.png";
    public static String ICON_EDIT_FONT = "Font.png";
    public static String ICON_REMOVE_COMPONENT = "RemoveComponent.png";
    public static String ICON_EDIT_COMPONENT = "EditComponent.png";
    public static String ICON_ADD_HYPERLINK = "Hyperlink.png";
    public static String ICON_EDIT_HYPERLINK = "EditHyperlink.png";
    
    
    // CSS STYLE SHEET CLASSES
    public static String    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON = "vertical_toolbar_button";
    public static String    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON = "horizontal_toolbar_button";
    public static String    CSS_CLASS_EPORTFOLIO_EDIT_VBOX = "eportfolio_edit_vbox";
    public static String    CSS_CLASS_EPORTFOLIO_EDIT_VIEW = "eportfolio_edit_view";
    public static String    CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW = "left_toolbar_buttons";
    public static String    CSS_CLASS_PAGE_EDIT_VIEW = "page_edit_view";
    
    // UI LABELS
    public static String    LABEL_SLIDE_SHOW_TITLE = "slide_show_title";
    
    
}

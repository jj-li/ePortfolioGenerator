package epm;

/**
 * This class provides the types that will be need to be loaded from
 * language-dependent XML files.
 * 
 * @author McKilla Gorilla & Jia Li
 */
public enum LanguagePropertyType {
    // TITLE FOR WINDOW TITLE BAR
    // TITLE FOR WINDOW TITLE BAR
    TITLE_WINDOW,
    
    // APPLICATION TOOLTIPS FOR BUTTONS
    TOOLTIP_NEW_SLIDE_SHOW,
    TOOLTIP_LOAD_SLIDE_SHOW,
    TOOLTIP_SAVE_SLIDE_SHOW,
    TOOLTIP_SAVE_AS_SLIDE_SHOW,
    TOOLTIP_EXPORT_EPORTFOLIO,
    TOOLTIP_EDIT_EPORTFOLIO,
    TOOLTIP_VIEW_EPORTFOLIO,
    TOOLTIP_EXIT,
    TOOLTIP_ADD_SLIDE,
    TOOLTIP_REMOVE_SLIDE,
    TOOLTIP_MOVE_UP,
    TOOLTIP_MOVE_DOWN,
    TOOLTIP_PREVIOUS_SLIDE,
    TOOLTIP_NEXT_SLIDE,
    TOOLTIP_EDIT_COMPONENT,
    TOOLTIP_ADD_TEXT,
    TOOLTIP_ADD_VIDEO,
    TOOLTIP_ADD_IMAGE,
    TOOLTIP_ADD_SLIDESHOW,
    TOOLTIP_ADD_HYPERLINK,
    TOOLTIP_EDIT_HYPERLINK,
    TOOLTIP_EDIT_FONT,
    TOOLTIP_REMOVE_COMPONENT,
    
    // DEFAULT VALUES
    DEFAULT_IMAGE_CAPTION,
    DEFAULT_SLIDE_SHOW_TITLE,
    BUTTON_OKAY,
    SAVE_TEXT,
    
    // UI LABELS
    LABEL_CAPTION,
    SLIDE_SHOW_TITLE_LABEL,
      
    /* THESE ARE FOR LANGUAGE-DEPENDENT ERROR HANDLING,
     LIKE FOR TEXT PUT INTO DIALOG BOXES TO NOTIFY
     THE USER WHEN AN ERROR HAS OCCURED */
    ERROR_DATA_FILE_LOADING,
    ERROR_PROPERTIES_FILE_LOADING,
    REMOVE_SLIDE,
    REMOVE_SLIDE_ERROR_TITLE,
    MOVE_UP_SLIDE1,
    MOVE_UP_SLIDE2,
    MOVE_UP_SLIDE3,
    MOVE_DOWN_SLIDE1,
    MOVE_DOWN_SLIDE2,
    MOVE_DOWN_SLIDE3,
    NO_SLIDESHOW,
    NO_SLIDESHOW_TITLE,
    MISSING_IMAGE,
    MISSING_IMAGE_TITLE,
    FAILED_SLIDE_SHOW_CREATED,
    FAILED_SLIDE_SHOW_CREATED_TITLE,
    FAILED_SLIDE_SHOW_LOAD,
    FAILED_SLIDE_SHOW_LOAD_TITLE,
    FAILED_SLIDE_SHOW_SAVE,
    FAILED_SLIDE_SHOW_SAVE_TITLE,
    FAILED_SLIDE_SHOW_EXIT,
    FAILED_SLIDE_SHOW_EXIT_TITLE,
    
    /*
     THESE ARE FOR LANGUAGE-DEPENDENT SUCCESS POP-UPS,
     LIKE FOR TEXT PUT INTO DIALOG BOXES TO NOTIFY
     THE USER WHEN A SUCCESS HAS OCCURED */
    SLIDE_SHOW_CREATED,
    SLIDE_SHOW_CREATED_TITLE,
}

package epm.ssm.view;

import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.ssm.LanguagePropertyType;
import static epm.ssm.LanguagePropertyType.LABEL_SLIDESHOW_TITLE;
import static epm.ssm.LanguagePropertyType.TOOLTIP_ADD_SLIDE;
import static epm.ssm.LanguagePropertyType.TOOLTIP_EXIT;
import static epm.ssm.LanguagePropertyType.TOOLTIP_LOAD_SLIDE_SHOW;
import static epm.ssm.LanguagePropertyType.TOOLTIP_MOVE_DOWN;
import static epm.ssm.LanguagePropertyType.TOOLTIP_MOVE_UP;
import static epm.ssm.LanguagePropertyType.TOOLTIP_NEW_SLIDE_SHOW;
import static epm.ssm.LanguagePropertyType.TOOLTIP_REMOVE_SLIDE;
import static epm.ssm.LanguagePropertyType.TOOLTIP_SAVE_SLIDE_SHOW;
import static epm.ssm.LanguagePropertyType.TOOLTIP_VIEW_SLIDE_SHOW;
import static epm.ssm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_PANE;
import static epm.ssm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_PANE;
import static epm.ssm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON;
import static epm.ssm.StartupConstants.CSS_CLASS_SELECTED_SLIDE_EDIT_VIEW;
import static epm.ssm.StartupConstants.CSS_CLASS_SLIDES_EDITOR_PANE;
import static epm.ssm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static epm.ssm.StartupConstants.CSS_CLASS_TITLE_PANE;
import static epm.ssm.StartupConstants.CSS_CLASS_TITLE_PROMPT;
import static epm.ssm.StartupConstants.CSS_CLASS_TITLE_TEXT_FIELD;
import static epm.ssm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_BUTTON;
import static epm.ssm.StartupConstants.CSS_CLASS_WORKSPACE;
import static epm.ssm.StartupConstants.ICON_ADD_SLIDE;
import static epm.ssm.StartupConstants.ICON_EXIT;
import static epm.ssm.StartupConstants.ICON_LOAD_SLIDE_SHOW;
import static epm.ssm.StartupConstants.ICON_MOVE_DOWN;
import static epm.ssm.StartupConstants.ICON_MOVE_UP;
import static epm.ssm.StartupConstants.ICON_NEW_SLIDE_SHOW;
import static epm.ssm.StartupConstants.ICON_REMOVE_SLIDE;
import static epm.ssm.StartupConstants.ICON_SAVE_SLIDE_SHOW;
import static epm.ssm.StartupConstants.ICON_VIEW_SLIDE_SHOW;
import static epm.ssm.StartupConstants.PATH_ICONS;
import static epm.ssm.StartupConstants.STYLE_SHEET_UI;
import epm.ssm.controller.SlideShowEditController;
import epm.ssm.model.Slide;
import epm.ssm.model.SlideShowModel;
import java.io.File;
import java.util.ArrayList;

/**
 * This class provides the User Interface for this application,
 * providing controls and the entry points for creating, loading, 
 * saving, editing, and viewing slide shows.
 * 
 * @author McKilla Gorilla & _____________
 */
public class SlideShowMakerView {

    // THIS IS THE MAIN APPLICATION UI WINDOW AND ITS SCENE GRAPH
    Stage primaryStage;
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane ssmPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button addSlideShow;
    Button exitButton;
    
    // WORKSPACE
    BorderPane workspace;

    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    FlowPane slideEditToolbar;
    Button addSlideButton;
    Button removeSlideButton;
    Button moveSlideUpButton;
    Button moveSlideDownButton;
    
    // FOR THE SLIDE SHOW TITLE
    FlowPane titlePane;
    Label titleLabel;
    TextField titleTextField;
    
    // AND THIS WILL GO IN THE CENTER
    ScrollPane slidesEditorScrollPane;
    VBox slidesEditorPane;

    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    SlideShowModel slideShow;

    
    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private SlideShowEditController editController;

    /**
     * Default constructor, it initializes the GUI for use, but does not yet
     * load all the language-dependent controls, that needs to be done via the
     * startUI method after the user has selected a language.
     */
    public SlideShowMakerView(Stage stage) {
	// MAKE THE DATA MANAGING MODEL
	slideShow = new SlideShowModel(this);
        primaryStage = stage;

    }

    // ACCESSOR METHODS
    public SlideShowModel getSlideShow() {
	return slideShow;
    }

    public Stage getWindow() {
	return primaryStage;
    }


    /**
     * Initializes the UI controls and gets it rolling.
     * 
     * @param initPrimaryStage The window for this application.
     * 
     * @param windowTitle The title for this window.
     */
    public void startUI(Stage initPrimaryStage, String windowTitle) {
	// THE TOOLBAR ALONG THE NORTH
	initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
	// TO THE WINDOW YET
	initWorkspace();

	// NOW SETUP THE EVENT HANDLERS
	initEventHandlers();

	// AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
	// KEEP THE WINDOW FOR LATER
	primaryStage = initPrimaryStage;
	initWindow(windowTitle);
    }
    
    //TO DO - ADD in the ability to load paths and captions!
    public void startUI(Stage initPrimaryStage, String windowTitle, ArrayList<String> paths, ArrayList<String> captions) {
        
        for (int i = 0; i < paths.size(); i++) {
            File file = new File(paths.get(i));
	    String path = file.getPath().substring(0, file.getPath().indexOf(file.getName()));
            String fileName = file.getName();
            slideShow.addSlide(fileName, fileName, path);
	}
        
	// THE TOOLBAR ALONG THE NORTH
	initFileToolbar();

        // INIT THE CENTER WORKSPACE CONTROLS BUT DON'T ADD THEM
	// TO THE WINDOW YET
	initWorkspace();

	// NOW SETUP THE EVENT HANDLERS
	initEventHandlers();

	// AND FINALLY START UP THE WINDOW (WITHOUT THE WORKSPACE)
	// KEEP THE WINDOW FOR LATER
	primaryStage = initPrimaryStage;
	initWindow(windowTitle);
    }

    // UI SETUP HELPER METHODS
    private void initWorkspace() {
	// FIRST THE WORKSPACE ITSELF, WHICH WILL CONTAIN TWO REGIONS
	workspace = new BorderPane();
	
	// THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
	slideEditToolbar = new FlowPane();
	addSlideButton = this.initChildButton(slideEditToolbar,		ICON_ADD_SLIDE,	    TOOLTIP_ADD_SLIDE,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	removeSlideButton = this.initChildButton(slideEditToolbar,	ICON_REMOVE_SLIDE,  TOOLTIP_REMOVE_SLIDE,   CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	moveSlideUpButton = this.initChildButton(slideEditToolbar,	ICON_MOVE_UP,	    TOOLTIP_MOVE_UP,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	moveSlideDownButton = this.initChildButton(slideEditToolbar,	ICON_MOVE_DOWN,	    TOOLTIP_MOVE_DOWN,	    CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	
	// AND THIS WILL GO IN THE CENTER
	slidesEditorPane = new VBox();
	slidesEditorScrollPane = new ScrollPane(slidesEditorPane);
	slidesEditorScrollPane.setFitToWidth(true);
	slidesEditorScrollPane.setFitToHeight(true);
	initTitleControls();
	
	// NOW PUT THESE TWO IN THE WORKSPACE
	workspace.setLeft(slideEditToolbar);
	workspace.setCenter(slidesEditorScrollPane);

	// SETUP ALL THE STYLE CLASSES
	workspace.getStyleClass().add(CSS_CLASS_WORKSPACE);
	slideEditToolbar.getStyleClass().add(CSS_CLASS_VERTICAL_TOOLBAR_PANE);
	slidesEditorPane.getStyleClass().add(CSS_CLASS_SLIDES_EDITOR_PANE);
	slidesEditorScrollPane.getStyleClass().add(CSS_CLASS_SLIDES_EDITOR_PANE);
    }

    private void initEventHandlers() {
	// FIRST THE FILE CONTROLS
	SlideShowModel slideShow = getSlideShow();
	slideShow.reset();	
        reloadSlideShowPane();
        
        
	// THEN THE SLIDE SHOW EDIT CONTROLS
	editController = new SlideShowEditController(this);
	addSlideButton.setOnAction(e -> {
	    editController.processAddSlideRequest();
	});
	removeSlideButton.setOnAction(e -> {
	    editController.processRemoveSlideRequest();
	});
	moveSlideUpButton.setOnAction(e -> {
	    editController.processMoveSlideUpRequest();
	});
	moveSlideDownButton.setOnAction(e -> {
	    editController.processMoveSlideDownRequest();
	});
    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
	fileToolbarPane = new FlowPane();
	fileToolbarPane.getStyleClass().add(CSS_CLASS_HORIZONTAL_TOOLBAR_PANE);

        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
	// START AS ENABLED (false), WHILE OTHERS DISABLED (true)
        addSlideShow = new Button("Add Slideshow Component");
        addSlideShow.setStyle("-fx-padding: 10px 10px 10px 10px; -fx-alignment: center");
        fileToolbarPane.getChildren().add(addSlideShow);

    }

    private void initWindow(String windowTitle) {
	// SET THE WINDOW TITLE
	primaryStage.setTitle(windowTitle);

	// GET THE SIZE OF THE SCREEN
	Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();

	// AND USE IT TO SIZE THE WINDOW
	primaryStage.setWidth(bounds.getWidth()*0.75);
	primaryStage.setHeight(bounds.getHeight()*0.75);

        // SETUP THE UI, NOTE WE'LL ADD THE WORKSPACE LATER
	ssmPane = new BorderPane();
	ssmPane.getStyleClass().add(CSS_CLASS_WORKSPACE);
	ssmPane.setBottom(fileToolbarPane);
        ssmPane.setCenter(workspace);
	primaryScene = new Scene(ssmPane);
	
        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
	// WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
	primaryScene.getStylesheets().add(STYLE_SHEET_UI);
	primaryStage.setScene(primaryScene);

    }
    
    /**
     * This helps initialize buttons in a toolbar, constructing a custom button
     * with a customly provided icon and tooltip, adding it to the provided
     * toolbar pane, and then returning it.
     */
    public Button initChildButton(
	    Pane toolbar, 
	    String iconFileName, 
	    LanguagePropertyType tooltip, 
	    String cssClass,
	    boolean disabled) {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String imagePath = "file:" + PATH_ICONS + iconFileName;
	Image buttonImage = new Image(imagePath);
	Button button = new Button();
	button.getStyleClass().add(cssClass);
	button.setDisable(disabled);
	button.setGraphic(new ImageView(buttonImage));
	Tooltip buttonTooltip = new Tooltip(props.getProperty(tooltip.toString()));
	button.setTooltip(buttonTooltip);
	toolbar.getChildren().add(button);
	return button;
    }
    
    /**
     * Updates the enabled/disabled status of all toolbar
     * buttons.
     * 
     * @param saved 
     */
    public void updateFileToolbarControls(boolean saved) {
	// FIRST MAKE SURE THE WORKSPACE IS THERE
	
	updateSlideshowEditToolbarControls();
    }
    
    public void updateSlideshowEditToolbarControls() {
	// AND THE SLIDESHOW EDIT TOOLBAR
	addSlideButton.setDisable(false);
	boolean slideSelected = slideShow.isSlideSelected();
	removeSlideButton.setDisable(!slideSelected);
	moveSlideUpButton.setDisable(!slideSelected);
	moveSlideDownButton.setDisable(!slideSelected);	
    }

    /**
     * Uses the slide show data to reload all the components for
     * slide editing.
     * 
     * @param slideShowToLoad SLide show being reloaded.
     */
    public void reloadSlideShowPane() {
	slidesEditorPane.getChildren().clear();
	for (Slide slide : slideShow.getSlides()) {
	    SlideEditView slideEditor = new SlideEditView(this, slide);
	    if (slideShow.isSelectedSlide(slide))
		slideEditor.getStyleClass().add(CSS_CLASS_SELECTED_SLIDE_EDIT_VIEW);
	    else
		slideEditor.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
	    slidesEditorPane.getChildren().add(slideEditor);
	    slideEditor.setOnMousePressed(e -> {
                slideShow.setSelectedSlide(slide);
		this.reloadSlideShowPane();
	    });
	}
	updateSlideshowEditToolbarControls();
    }
    
    private void initTitleControls() {
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	String labelPrompt = props.getProperty(LABEL_SLIDESHOW_TITLE);
	
    }
    
}

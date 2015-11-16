/**
 * @coauthor Jia Li
 **/
package epm.view;


import java.io.File;
import java.net.URL;
import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.LanguagePropertyType.DEFAULT_SLIDE_SHOW_TITLE;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD;
import static epm.LanguagePropertyType.FAILED_SLIDE_SHOW_LOAD_TITLE;
import static epm.LanguagePropertyType.NO_SLIDESHOW;
import static epm.LanguagePropertyType.NO_SLIDESHOW_TITLE;
import static epm.LanguagePropertyType.SLIDE_SHOW_TITLE_LABEL;
import static epm.LanguagePropertyType.TITLE_WINDOW;
import static epm.LanguagePropertyType.TOOLTIP_ADD_SLIDE;
import static epm.LanguagePropertyType.TOOLTIP_EXIT;
import static epm.LanguagePropertyType.TOOLTIP_LOAD_SLIDE_SHOW;
import static epm.LanguagePropertyType.TOOLTIP_NEW_SLIDE_SHOW;
import static epm.LanguagePropertyType.TOOLTIP_NEXT_SLIDE;
import static epm.LanguagePropertyType.TOOLTIP_PREVIOUS_SLIDE;
import static epm.LanguagePropertyType.TOOLTIP_REMOVE_SLIDE;
import static epm.LanguagePropertyType.TOOLTIP_SAVE_SLIDE_SHOW;
import static epm.LanguagePropertyType.TOOLTIP_SAVE_AS_SLIDE_SHOW;
import static epm.EPortfolioMaker.CORRUPTED_SLIDE;
import static epm.LanguagePropertyType.TOOLTIP_EXPORT_EPORTFOLIO;
import static epm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON;
import static epm.StartupConstants.CSS_CLASS_SLIDE_SHOW_EDIT_VBOX;
import static epm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_BUTTON;
import static epm.StartupConstants.ICON_ADD_SLIDE;
import static epm.StartupConstants.ICON_EXIT;
import static epm.StartupConstants.ICON_EXPORT_SLIDE_SHOW;
import static epm.StartupConstants.ICON_LOAD_SLIDE_SHOW;
import static epm.StartupConstants.ICON_MOVE_DOWN;
import static epm.StartupConstants.ICON_MOVE_UP;
import static epm.StartupConstants.ICON_NEW_SLIDE_SHOW;
import static epm.StartupConstants.ICON_NEXT;
import static epm.StartupConstants.ICON_PREVIOUS;
import static epm.StartupConstants.ICON_REMOVE_SLIDE;
import static epm.StartupConstants.ICON_SAVE_SLIDE_SHOW;
import static epm.StartupConstants.ICON_SAVE_AS_SLIDE_SHOW;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.controller.FileController;
import epm.controller.PageEditController;
import epm.model.Page;
import epm.model.EPortfolioModel;
import epm.error.ErrorHandler;
import epm.file.EPortfolioFileManager;

/**
 * This class provides the User Interface for this application,
 * providing controls and the entry points for creating, loading, 
 * saving, editing, and viewing slide shows.
 * 
 * @author McKilla Gorilla & _____________
 */
public class EPortfolioMakerView {

    // THIS IS THE MAIN APPLICATION UI WINDOW AND ITS SCENE GRAPH
    Stage primaryStage;
    Scene primaryScene;

    // THIS PANE ORGANIZES THE BIG PICTURE CONTAINERS FOR THE
    // APPLICATION GUI
    BorderPane epmPane;

    // THIS IS THE TOP TOOLBAR AND ITS CONTROLS
    FlowPane fileToolbarPane;
    Button newEPortfolioButton;
    Button loadEPortfolioButton;
    Button saveEPortfolioButton;
    Button saveAsEPortfolioButton;
    Button exportEPortfolioButton;
    Button exitButton;
    TextField ePortfolioTitle;
    
    // WORKSPACE
    HBox workspace;

    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    VBox slideEditToolbar;
    Button addSlideButton;
    Button removeSlideButton;
    Button moveUpSlideButton;
    Button moveDownSlideButton;
    
    // AND THIS WILL GO IN THE CENTER
    ScrollPane slidesEditorScrollPane;
    VBox slidesEditorPane;

    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    EPortfolioModel ePortfolio;

    // THIS IS FOR SAVING AND LOADING SLIDE SHOWS
    EPortfolioFileManager fileManager;

    // THIS CLASS WILL HANDLE ALL ERRORS FOR THIS PROGRAM
    private ErrorHandler errorHandler;

    // THIS CONTROLLER WILL ROUTE THE PROPER RESPONSES
    // ASSOCIATED WITH THE FILE TOOLBAR
    private FileController fileController;
    
    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private PageEditController editController;
    
    private int selectedSlides = 0;
    private PageEditView chosenSlideEditView = null;
    
    private BorderPane viewer = new BorderPane();
    private int slidePos = 0;
    private Button nextSlideShowButton;
    private Button prevSlideShowButton;
    private Pane leftPane = new Pane();
    private Pane rightPane = new Pane();
    private Text imageCaption = new Text();
    private Text title;
    private ImageView imageSelectionView;
    private Page page;
    private Scene scene = new Scene(viewer, Screen.getPrimary().getVisualBounds().getMinX(), Screen.getPrimary().getVisualBounds().getMinY());
   

    /**
     * Default constructor, it initializes the GUI for use, but does not yet
     * load all the language-dependent controls, that needs to be done via the
     * startUI method after the user has selected a language.
     */
    public EPortfolioMakerView(EPortfolioFileManager initFileManager) {
	// FIRST HOLD ONTO THE FILE MANAGER
	fileManager = initFileManager;
	
	// MAKE THE DATA MANAGING MODEL
	ePortfolio = new EPortfolioModel(this);

	// WE'LL USE THIS ERROR HANDLER WHEN SOMETHING GOES WRONG
	errorHandler = new ErrorHandler(this);
        
    }

    // ACCESSOR METHODS
    public EPortfolioModel getEPortfolio() {
	return ePortfolio;
    }

    public Stage getWindow() {
	return primaryStage;
    }

    public ErrorHandler getErrorHandler() {
	return errorHandler;
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

    // UI SETUP HELPER METHODS
    private void initWorkspace() {
	// FIRST THE WORKSPACE ITSELF, WHICH WILL CONTAIN TWO REGIONS
	workspace = new HBox();
        workspace.getStyleClass().add("workspace_spacing");
        // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
	slideEditToolbar = new VBox();
	slideEditToolbar.getStyleClass().add(CSS_CLASS_SLIDE_SHOW_EDIT_VBOX);
	addSlideButton = this.initChildButton(slideEditToolbar,	ICON_ADD_SLIDE,	 TOOLTIP_ADD_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        removeSlideButton = this.initChildButton(slideEditToolbar, ICON_REMOVE_SLIDE, TOOLTIP_REMOVE_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        moveUpSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_UP, TOOLTIP_ADD_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
        moveDownSlideButton = this.initChildButton(slideEditToolbar, ICON_MOVE_DOWN, TOOLTIP_REMOVE_SLIDE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  true);
	
	// AND THIS WILL GO IN THE CENTER
	slidesEditorPane = new VBox();
        slidesEditorPane.getStyleClass().add("slide_spacing");
	slidesEditorScrollPane = new ScrollPane(slidesEditorPane);
        slidesEditorScrollPane.getStyleClass().add("slide_panes");
	// NOW PUT THESE TWO IN THE WORKSPACE
	workspace.getChildren().add(slideEditToolbar);
	workspace.getChildren().add(slidesEditorScrollPane);
    }
    
    private void initEventHandlers() {
	// FIRST THE FILE CONTROLS
	fileController = new FileController(this, fileManager);
	newEPortfolioButton.setOnAction(e -> {
	    fileController.handleNewSlideShowRequest();
	});
	loadEPortfolioButton.setOnAction(e -> {
	    fileController.handleLoadSlideShowRequest();
	});
	saveEPortfolioButton.setOnAction(e -> {
	    fileController.handleSaveSlideShowRequest();
	});
	exitButton.setOnAction(e -> {
	    fileController.handleExitRequest();
	});
        saveAsEPortfolioButton.setOnAction( e -> {
            fileController.handleViewSlideShowRequest();
        });
	
	// THEN THE SLIDE SHOW EDIT CONTROLS
	editController = new PageEditController(this);
	
    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
	fileToolbarPane = new FlowPane();
        fileToolbarPane.getStyleClass().add("horizontal_toolbar");
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
	// START AS ENABLED (false), WHILE OTHERS DISABLED (true)
	PropertiesManager props = PropertiesManager.getPropertiesManager();
	newEPortfolioButton = initChildButton(fileToolbarPane, ICON_NEW_SLIDE_SHOW,	TOOLTIP_NEW_SLIDE_SHOW,	    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	loadEPortfolioButton = initChildButton(fileToolbarPane, ICON_LOAD_SLIDE_SHOW,	TOOLTIP_LOAD_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	saveEPortfolioButton = initChildButton(fileToolbarPane, ICON_SAVE_SLIDE_SHOW,	TOOLTIP_SAVE_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        saveAsEPortfolioButton = initChildButton(fileToolbarPane, ICON_SAVE_AS_SLIDE_SHOW,	TOOLTIP_SAVE_AS_SLIDE_SHOW,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	exportEPortfolioButton = initChildButton(fileToolbarPane, ICON_EXPORT_SLIDE_SHOW,	TOOLTIP_EXPORT_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        exitButton = initChildButton(fileToolbarPane, ICON_EXIT, TOOLTIP_EXIT, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        ePortfolioTitle = new TextField(ePortfolio.getTitle());
        ePortfolioTitle.setText(props.getProperty(DEFAULT_SLIDE_SHOW_TITLE.toString()));
        ePortfolio.setTitle(ePortfolioTitle.getText());
        ePortfolioTitle.setOnKeyReleased(e -> {
            saveEPortfolioButton.setDisable(false);
            ePortfolio.setTitle(ePortfolioTitle.getText());
        });
        Label label1 = new Label(props.getProperty(DEFAULT_SLIDE_SHOW_TITLE.toString()) + ": ");
        fileToolbarPane.getChildren().addAll(label1, ePortfolioTitle);
    }

    private void initWindow(String windowTitle) {
	// SET THE WINDOW TITLE
        primaryStage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
	primaryStage.setTitle(windowTitle);
        
	// GET THE SIZE OF THE SCREEN
	Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();

	// AND USE IT TO SIZE THE WINDOW
	primaryStage.setX(bounds.getMinX());
	primaryStage.setY(bounds.getMinY());
	primaryStage.setWidth(bounds.getWidth());
	primaryStage.setHeight(bounds.getHeight());

        // SETUP THE UI, NOTE WE'LL ADD THE WORKSPACE LATER
	epmPane = new BorderPane();
	epmPane.setTop(fileToolbarPane);
	primaryScene = new Scene(epmPane);
	
        // NOW TIE THE SCENE TO THE WINDOW, SELECT THE STYLESHEET
	// WE'LL USE TO STYLIZE OUR GUI CONTROLS, AND OPEN THE WINDOW
	primaryScene.getStylesheets().add(STYLE_SHEET_UI);
	primaryStage.setScene(primaryScene);
	primaryStage.show();
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
    public void updateToolbarControls(boolean saved) {
	// FIRST MAKE SURE THE WORKSPACE IS THERE
	epmPane.setCenter(workspace);
	
	// NEXT ENABLE/DISABLE BUTTONS AS NEEDED IN THE FILE TOOLBAR
	saveEPortfolioButton.setDisable(saved);
    }
    
    public void updateSidebarControls(boolean saved)
    {
        removeSlideButton.setDisable(saved);
        moveUpSlideButton.setDisable(saved);
        moveDownSlideButton.setDisable(saved);
    }
    
    /**
     * Uses the slide show data to reload all the components for
     * slide editing.
     * 
     * @param slideShowToLoad SLide show being reloaded.
     */
    public void reloadSlideShowPane(EPortfolioModel ePortfolioToLoad) {
        ePortfolioTitle.setText(ePortfolioToLoad.getTitle());
	slidesEditorPane.getChildren().clear();
	for (Page page : ePortfolioToLoad.getPages()) {
	    PageEditView pageEditor = new PageEditView(page);
	}
    }
    
    
    
    public void viewSlideShow(EPortfolioModel ePortfolioToShow)
    {
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        WebView page = new WebView(); 
        WebEngine webEngine = page.getEngine(); 
        page.resize(bounds.getWidth(), bounds.getHeight());
       
        File htmlFile = new File("sites/" + ePortfolioToShow.getTitle() + "/index.html");
        try {
           URL fileURL = htmlFile.toURI().toURL();
           webEngine.load(fileURL.toExternalForm());
        }
        catch (Exception e) {
            ErrorHandler handle = new ErrorHandler(this);
            handle.processError(FAILED_SLIDE_SHOW_LOAD, prop.getProperty(FAILED_SLIDE_SHOW_LOAD_TITLE));
        }
        Scene webScene = new Scene(page);
        Stage webStage = new Stage();
        webStage.getIcons().add(new Image("file:" + PATH_ICONS + WINDOWS_ICON));
        webStage.setTitle(prop.getProperty(ePortfolioToShow.getTitle()));

	webStage.setX(bounds.getMinX());
	webStage.setY(bounds.getMinY());
	webStage.setWidth(bounds.getWidth());
	webStage.setHeight(bounds.getHeight());
        
        webStage.setScene(webScene);
        webStage.show();
        
        
    
        }
    }
 
    

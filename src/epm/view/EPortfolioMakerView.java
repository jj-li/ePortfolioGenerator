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
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.LanguagePropertyType.FAILED_EPORTFOLIO_LOAD;
import static epm.LanguagePropertyType.FAILED_EPORTFOLIO_TITLE;
import static epm.LanguagePropertyType.TOOLTIP_ADD_PAGE;
import static epm.LanguagePropertyType.TOOLTIP_EXIT;
import static epm.LanguagePropertyType.TOOLTIP_LOAD_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_NEW_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_REMOVE_PAGE;
import static epm.LanguagePropertyType.TOOLTIP_SAVE_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_SAVE_AS_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_ADD_HYPERLINK;
import static epm.LanguagePropertyType.TOOLTIP_ADD_IMAGE;
import static epm.LanguagePropertyType.TOOLTIP_ADD_SLIDESHOW;
import static epm.LanguagePropertyType.TOOLTIP_ADD_TEXT;
import static epm.LanguagePropertyType.TOOLTIP_ADD_VIDEO;
import static epm.LanguagePropertyType.TOOLTIP_EDIT_COMPONENT;
import static epm.LanguagePropertyType.TOOLTIP_EDIT_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_EDIT_FONT;
import static epm.LanguagePropertyType.TOOLTIP_EDIT_HYPERLINK;
import static epm.LanguagePropertyType.TOOLTIP_EXPORT_EPORTFOLIO;
import static epm.LanguagePropertyType.TOOLTIP_REMOVE_COMPONENT;
import static epm.LanguagePropertyType.TOOLTIP_VIEW_EPORTFOLIO;
import static epm.StartupConstants.CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON;
import static epm.StartupConstants.CSS_CLASS_EPORTFOLIO_EDIT_VBOX;
import static epm.StartupConstants.CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW;
import static epm.StartupConstants.CSS_CLASS_PAGE_EDIT_VIEW;
import static epm.StartupConstants.CSS_CLASS_VERTICAL_TOOLBAR_BUTTON;
import static epm.StartupConstants.ICON_ADD_HYPERLINK;
import static epm.StartupConstants.ICON_ADD_IMAGE;
import static epm.StartupConstants.ICON_ADD_PAGE;
import static epm.StartupConstants.ICON_ADD_SLIDESHOW;
import static epm.StartupConstants.ICON_ADD_TEXT;
import static epm.StartupConstants.ICON_ADD_VIDEO;
import static epm.StartupConstants.ICON_EDIT_COMPONENT;
import static epm.StartupConstants.ICON_EDIT_FONT;
import static epm.StartupConstants.ICON_EDIT_HYPERLINK;
import static epm.StartupConstants.ICON_EDIT_PORTFOLIO;
import static epm.StartupConstants.ICON_EXIT;
import static epm.StartupConstants.ICON_EXPORT_PORTFOLIO;
import static epm.StartupConstants.ICON_LOAD_PORTFOLIO;
import static epm.StartupConstants.ICON_NEW_PORTFOLIO;
import static epm.StartupConstants.ICON_REMOVE_COMPONENT;
import static epm.StartupConstants.ICON_REMOVE_PAGE;
import static epm.StartupConstants.ICON_SAVE_PORTFOLIO;
import static epm.StartupConstants.ICON_VIEW_PORTFOLIO;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.controller.FileController;
import epm.controller.PageEditController;
import epm.model.Page;
import epm.model.EPortfolioModel;
import epm.error.ErrorHandler;
import epm.file.EPortfolioFileManager;
import epm.model.TextComponent;
import epm.ssm.SlideShowMaker;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;

/**
 * This class provides the User Interface for this application,
 * providing controls and the entry points for customizing and creating
 * the user's portfolio website.
 * 
 * @author Jia Li
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
    FlowPane fileToolbarPaneLeft;
    FlowPane fileToolbarPaneRight;
    Button newEPortfolioButton;
    Button loadEPortfolioButton;
    Button saveEPortfolioButton;
    Button saveAsEPortfolioButton;
    Button exportEPortfolioButton;
    Button editEPortfolioButton;
    Button viewEPortfolioButton;
    Button exitButton;
    TextField ePortfolioTitle;
    
    // WORKSPACE
    HBox workspace;

    // THIS WILL GO IN THE LEFT SIDE OF THE SCREEN
    VBox pageEditToolbar;
    Button addPageButton;
    Button removePageButton;
    Button addTextButton;
    Button addImageButton;
    Button addVideoButton;
    Button addSlideshowButton;
    Button addHyperlinkButton;
    Button editHyperlinkButton;
    Button editComponentButton;
    Button removeComponentButton;
    Button editFontButton;

    
    // AND THIS WILL GO IN THE CENTER
    TabPane pageEditorPane;

    // THIS IS THE SLIDE SHOW WE'RE WORKING WITH
    EPortfolioModel ePortfolio;

    // THIS IS FOR SAVING AND LOADING SLIDE SHOWS
    EPortfolioFileManager fileManager;

    // THIS CLASS WILL HANDLE ALL ERRORS FOR THIS PROGRAM
    private final ErrorHandler errorHandler;

    // THIS CONTROLLER WILL ROUTE THE PROPER RESPONSES
    // ASSOCIATED WITH THE FILE TOOLBAR
    private FileController fileController;
    
    // THIS CONTROLLER RESPONDS TO SLIDE SHOW EDIT BUTTONS
    private PageEditController editController;
        
    private final BorderPane viewer = new BorderPane();
    private Pane leftPane = new Pane();

    private Scene scene = new Scene(viewer, Screen.getPrimary().getVisualBounds().getMinX(), Screen.getPrimary().getVisualBounds().getMinY());
   
    private String studentName = "";

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
	pageEditToolbar = new VBox();
	pageEditToolbar.getStyleClass().add(CSS_CLASS_EPORTFOLIO_EDIT_VBOX);
        HBox top = new HBox();
	addPageButton = this.initChildButton(top, ICON_ADD_PAGE, TOOLTIP_ADD_PAGE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        removePageButton = this.initChildButton(top, ICON_REMOVE_PAGE, TOOLTIP_REMOVE_PAGE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        top.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        
        VBox bottom = new VBox();
        HBox one = new HBox();
        HBox two = new HBox();
        HBox three = new HBox();
        HBox four = new HBox();
        HBox five = new HBox();
        addTextButton = this.initChildButton(one, ICON_ADD_TEXT, TOOLTIP_ADD_TEXT, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        addImageButton = this.initChildButton(one, ICON_ADD_IMAGE, TOOLTIP_ADD_IMAGE, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        addVideoButton = this.initChildButton(two, ICON_ADD_VIDEO, TOOLTIP_ADD_VIDEO, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        addSlideshowButton = this.initChildButton(two, ICON_ADD_SLIDESHOW, TOOLTIP_ADD_SLIDESHOW, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        addHyperlinkButton = this.initChildButton(three, ICON_ADD_HYPERLINK, TOOLTIP_ADD_HYPERLINK, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        editHyperlinkButton = this.initChildButton(three, ICON_EDIT_HYPERLINK, TOOLTIP_EDIT_HYPERLINK, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        editComponentButton = this.initChildButton(four, ICON_EDIT_COMPONENT, TOOLTIP_EDIT_COMPONENT, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        removeComponentButton = this.initChildButton(four, ICON_REMOVE_COMPONENT, TOOLTIP_REMOVE_COMPONENT, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        editFontButton = this.initChildButton(five, ICON_EDIT_FONT, TOOLTIP_EDIT_FONT, CSS_CLASS_VERTICAL_TOOLBAR_BUTTON,  false);
        one.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        two.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        three.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        four.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        five.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        bottom.getStyleClass().add(CSS_CLASS_EPORTFOLIO_LEFT_TOOLBAR_VIEW);
        
        Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();
        double totalHeight = bounds.getHeight();
        double firstHeight = top.getHeight();
        double secondHeight = totalHeight/4;
        double finalHeight = secondHeight - firstHeight;
        
        bottom.getChildren().addAll(one, two, three, four, five);
        pageEditToolbar.getChildren().add(top);
        pageEditToolbar.getChildren().add(bottom);
        pageEditToolbar.setStyle("-fx-spacing:" + finalHeight + "px;");
        
        pageEditorPane = new TabPane();
        Tab tab = new Tab();
        tab.setText("Tab 1");
        HBox hbox = new HBox();
        hbox.getChildren().add(new Label("Tab1"));
        tab.setContent(hbox);
        pageEditorPane.getTabs().add(tab);
        pageEditorPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
        pageEditorPane.prefWidthProperty().bind(workspace.widthProperty());
        pageEditorPane.getStyleClass().add(CSS_CLASS_PAGE_EDIT_VIEW);
        
        
	// NOW PUT THESE TWO IN THE WORKSPACE
	workspace.getChildren().add(pageEditToolbar);
	workspace.getChildren().add(pageEditorPane);
        VBox temp = new VBox();
        temp.setMinWidth(109);
        workspace.getChildren().add(temp);
    }
    
    public ObservableList<Tab> getTabs() {
        return pageEditorPane.getTabs();
    }
    
    private void initEventHandlers() {
	// FIRST THE FILE CONTROLS
	fileController = new FileController(this, fileManager);
	newEPortfolioButton.setOnAction(e -> {
	    fileController.handleNewEPortfolioRequest();
            topControls(false);
	});
	loadEPortfolioButton.setOnAction(e -> {
	    fileController.handleLoadEPortfolioRequest();
            topControls(false);
            addComponents(false);
	});
	saveEPortfolioButton.setOnAction(e -> {
	    fileController.handleSaveEPortfolioRequest();
	});
	exitButton.setOnAction(e -> {
	    fileController.handleExitRequest();
	});
        saveAsEPortfolioButton.setOnAction( e -> {
            fileController.handleSaveAsEPortfolioRequest();
        });
        exportEPortfolioButton.setOnAction( e-> {
            if (ePortfolio != null)
                if (!ePortfolio.getPages().isEmpty())
                    fileController.handleExportEPortfolioRequest();
        });
        viewEPortfolioButton.setOnAction( e-> {
            if (ePortfolio != null) {
                if (!ePortfolio.getPages().isEmpty()) {
                    fileController.handleExportEPortfolioRequest();
                    viewPage(ePortfolio);
                }
            }
        });
        editEPortfolioButton.setOnAction( e-> {
            fileController.handleEditEPortfolioRequest();
        });
        
	// THEN THE SLIDE SHOW EDIT CONTROLS
	editController = new PageEditController(this);
	addPageButton.setOnAction(e -> {
            editController.addPage();
            addComponents(false);
            selectComponents(true);
        });
        removePageButton.setOnAction( e-> {
            editController.removePage();
            if (ePortfolio.getPages().isEmpty()) 
                addComponents(true);
            selectComponents(true);
        });
        
        addTextButton.setOnAction( e-> {
            TextComponentDialogue addComponent = new TextComponentDialogue(ePortfolio.getSelectedPage());
            addComponent.showAndWait();
            reloadSlideShowPane(ePortfolio);
            selectComponents(true);
        });
        
        addImageButton.setOnAction ( e-> {
           ImageComponentDialogue imageComponent = new ImageComponentDialogue(ePortfolio.getSelectedPage()); 
           imageComponent.showAndWait();
           reloadSlideShowPane(ePortfolio);
           selectComponents(true);
        });
        
        addVideoButton.setOnAction ( e->{
            VideoComponentDialogue videoComponent = new VideoComponentDialogue(ePortfolio.getSelectedPage()); 
            videoComponent.showAndWait();
            reloadSlideShowPane(ePortfolio);
            selectComponents(true);
        });
        
        addSlideshowButton.setOnAction ( e-> {
            SlideShowMaker slideShow = new SlideShowMaker(ePortfolio.getSelectedPage());
            slideShow.showAndWait();
            reloadSlideShowPane(ePortfolio);
            selectComponents(true);
        });
        
        editComponentButton.setOnAction (e -> {
            editController.editComponent();
            reloadSlideShowPane(ePortfolio);
            selectComponents(true);
        });
        
        editFontButton.setOnAction (e-> {
            Page page = ePortfolio.getSelectedPage();
            if (page != null) {
                PageEditView editView = page.getSelectedPageEditView();
                if (editView != null) {
                    if (editView.isTextSelected()) {
                        FontDialogue dialogue = new FontDialogue(editView.getTextComponent());
                        dialogue.showAndWait();
                        reloadSlideShowPane(ePortfolio);
                        selectComponents(true);
                    }
                }  
            }
        });
        
        addHyperlinkButton.setOnAction (e-> {
            Page page = ePortfolio.getSelectedPage();
            if (page != null) {
                PageEditView editView = page.getSelectedPageEditView();
                if (editView != null) {
                    if (editView.isParagraphSelected()) {
                        TextComponent component = editView.getTextComponent();
                        HyperlinkDialogue dialogue = new HyperlinkDialogue(component.getData(), component);
                        dialogue.showAndWait();
                        reloadSlideShowPane(ePortfolio);
                        selectComponents(true);
                    }
                    if (editView.isListSelected()) {
                        TextComponent component = editView.getTextComponent();
                        HyperlinkDialogue dialogue = new HyperlinkDialogue(component.getList(), component);
                        dialogue.showAndWait();
                        reloadSlideShowPane(ePortfolio);
                        selectComponents(true);
                    }
                }  
            }
        });
        
        editHyperlinkButton.setOnAction ( e-> {
            Page page = ePortfolio.getSelectedPage();
            if (page != null) {
                PageEditView editView = page.getSelectedPageEditView();
                if (editView != null) {
                    if (editView.isParagraphSelected()) {
                        TextComponent component = editView.getTextComponent();
                        HyperlinkDialogue dialogue = new HyperlinkDialogue(component.getData(), component.getHyperlinks(), component);
                        dialogue.showAndWait();
                        reloadSlideShowPane(ePortfolio);
                        selectComponents(true);
                    }
                    if (editView.isListSelected()) {
                        TextComponent component = editView.getTextComponent();
                        HyperlinkDialogue dialogue = new HyperlinkDialogue(component.getList(), component.getHyperlinks(), component);
                        dialogue.showAndWait();
                        reloadSlideShowPane(ePortfolio);
                        selectComponents(true);
                    }
                }  
            }
        });
        
        removeComponentButton.setOnAction (e-> {
            editController.removeComponent();
            reloadSlideShowPane(ePortfolio);
            selectComponents(true);
        });
    }

    /**
     * This function initializes all the buttons in the toolbar at the top of
     * the application window. These are related to file management.
     */
    private void initFileToolbar() {
	fileToolbarPane = new FlowPane();
        fileToolbarPaneLeft = new FlowPane();
        fileToolbarPaneRight = new FlowPane();
        fileToolbarPane.getStyleClass().add("horizontal_toolbar");
        fileToolbarPaneLeft.getStyleClass().add("horizontal_toolbar");
        fileToolbarPaneRight.getStyleClass().add("horizontal_toolbar");
        fileToolbarPaneRight.setPrefWrapLength(85);
        
        Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();
        double total = bounds.getWidth();
        double one = fileToolbarPaneLeft.getPrefWrapLength();
        double two = fileToolbarPaneRight.getPrefWrapLength();
        double three = total - (one + two) - 60;
        
        fileToolbarPane.setStyle("-fx-hgap:" +  three + "px;");
        
        // HERE ARE OUR FILE TOOLBAR BUTTONS, NOTE THAT SOME WILL
	// START AS ENABLED (false), WHILE OTHERS DISABLED (true)
	newEPortfolioButton = initChildButton(fileToolbarPaneLeft, ICON_NEW_PORTFOLIO,	TOOLTIP_NEW_EPORTFOLIO,	    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	loadEPortfolioButton = initChildButton(fileToolbarPaneLeft, ICON_LOAD_PORTFOLIO,	TOOLTIP_LOAD_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	saveEPortfolioButton = initChildButton(fileToolbarPaneLeft, ICON_SAVE_PORTFOLIO,	TOOLTIP_SAVE_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        saveAsEPortfolioButton = initChildButton(fileToolbarPaneLeft, ICON_SAVE_PORTFOLIO,	TOOLTIP_SAVE_AS_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
	exportEPortfolioButton = initChildButton(fileToolbarPaneLeft, ICON_EXPORT_PORTFOLIO,	TOOLTIP_EXPORT_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        exitButton = initChildButton(fileToolbarPaneLeft, ICON_EXIT, TOOLTIP_EXIT, CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        editEPortfolioButton = initChildButton(fileToolbarPaneRight, ICON_EDIT_PORTFOLIO,	TOOLTIP_EDIT_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        viewEPortfolioButton = initChildButton(fileToolbarPaneRight, ICON_VIEW_PORTFOLIO,	TOOLTIP_VIEW_EPORTFOLIO,    CSS_CLASS_HORIZONTAL_TOOLBAR_BUTTON, false);
        editEPortfolioButton.getStyleClass().add("rightToolbar");
        viewEPortfolioButton.getStyleClass().add("rightToolbar");
        
        fileToolbarPane.getChildren().addAll(fileToolbarPaneLeft, fileToolbarPaneRight);
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
        addComponents(true);
        selectComponents(true);
        topControls(true);
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
	
    }
    
    public void topControls(boolean saved) {
        saveEPortfolioButton.setDisable(saved);
        saveAsEPortfolioButton.setDisable(saved);
        exportEPortfolioButton.setDisable(saved);
        editEPortfolioButton.setDisable(saved);
        viewEPortfolioButton.setDisable(saved);
    }
    
    public void addComponents(boolean saved) {
        addTextButton.setDisable(saved);
        addVideoButton.setDisable(saved);
        addImageButton.setDisable(saved);
        addSlideshowButton.setDisable(saved);
    }
    
    public void selectComponents(boolean saved) {
        editComponentButton.setDisable(saved);
        removeComponentButton.setDisable(saved);
        addHyperlinkButton.setDisable(saved);
        editHyperlinkButton.setDisable(saved);
        editFontButton.setDisable(saved);
        removeComponentButton.setDisable(saved);
    }
    
    /**
     * Uses the ePortfolio data to reload all the components for
     * page editing.
     * 
     */
    public void reloadSlideShowPane(EPortfolioModel ePortfolioToLoad) {
        getTabs().clear();
	for (Page page : ePortfolioToLoad.getPages()) {
            Tab tab = new Tab(page.getTitle());
            PageEditView pageEdit = new PageEditView(page, tab, this);
            /*if (page != null) {
                if (page.getSelectedPageEditView() != null) {
                    if (page.equals(ePortfolioToLoad.getSelectedPage()))
                        pageEdit.setSelectedComponents(page.getSelectedPageEditView());
                } 
            }*/
            pageEdit.reloadComponents();
            ScrollPane scrollPane = new ScrollPane();
            scrollPane.setContent(pageEdit);
            scrollPane.setStyle("-fx-background: #ffffe5");
            tab.setContent(scrollPane);
	    getTabs().add(tab);
            pageEditorPane.setOnMouseClicked( e-> {
                SingleSelectionModel<Tab> selectionModel = pageEditorPane.getSelectionModel();
                Tab selectedTab = selectionModel.getSelectedItem();
                if (selectedTab != null) {
                    ScrollPane selectedScrollPane = (ScrollPane)(selectedTab.getContent());
                    PageEditView selectedPageEditView = (PageEditView)selectedScrollPane.getContent();
                    Page selectedPage = selectedPageEditView.getPage();
                    ePortfolioToLoad.setSelectedPage(selectedPage);
                    pageEdit.reloadStudentName(pageEdit);
                }
            });
            if (ePortfolioToLoad.getSelectedPage().equals(page)) {
                pageEditorPane.getSelectionModel().select(tab);
            }
	}
    }
    
    
    
    public void viewPage(EPortfolioModel ePortfolioToShow)
    {
        workspace.getChildren().clear();
        PropertiesManager prop = PropertiesManager.getPropertiesManager();
        WebView page = new WebView(); 
        WebEngine webEngine = page.getEngine(); 
        page.prefWidthProperty().bind(workspace.widthProperty());
        page.prefHeightProperty().bind(workspace.heightProperty());
        
        String titles = "";
        String[] pieces = ePortfolioToShow.getSelectedPage().getTitle().split(" ");
        for (String s : pieces)
            titles += s;
        File htmlFile = new File("sites/" + ePortfolioToShow.getTitle() + "/" + titles + ".html");
        try {
           URL fileURL = htmlFile.toURI().toURL();
           webEngine.load(fileURL.toExternalForm());
           webEngine.reload();
        }
        catch (Exception e) {
            ErrorHandler handle = new ErrorHandler(this);
            handle.processError(FAILED_EPORTFOLIO_LOAD, prop.getProperty(FAILED_EPORTFOLIO_TITLE));
        }
        workspace.getChildren().add(page);
    
    }
    
    public void editWorkspace(EPortfolioModel ePortfolioToShow)
    {
        workspace.getChildren().clear();
        workspace.getChildren().add(pageEditToolbar);
	workspace.getChildren().add(pageEditorPane);
        reloadSlideShowPane(ePortfolioToShow);
    }
    
    public void setStudentName(String name) {
        studentName = name;
    }
    
    public String getStudentName() {
        return studentName;
    }
}
 
    

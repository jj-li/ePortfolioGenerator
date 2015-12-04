/**
 * @author Jia Li
 **/
package epm.view;

import java.io.File;
import java.net.URL;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import properties_manager.PropertiesManager;
import epm.LanguagePropertyType;
import static epm.LanguagePropertyType.MISSING_IMAGE;
import static epm.LanguagePropertyType.MISSING_IMAGE_TITLE;
import static epm.StartupConstants.CSS_CLASS_SLIDE_EDIT_VIEW;
import static epm.EPortfolioMaker.CORRUPTED_SLIDE;
import static epm.StartupConstants.DEFAULT_THUMBNAIL_WIDTH;
import static epm.StartupConstants.PATH_ICONS;
import static epm.StartupConstants.STYLE_SHEET_UI;
import static epm.StartupConstants.WINDOWS_ICON;
import epm.controller.ImageSelectionController;
import epm.model.Page;
import static epm.file.EPortfolioFileManager.SLASH;
import epm.model.HyperlinkComponent;
import epm.model.ImageComponent;
import epm.model.SlideShowComponent;
import epm.model.TextComponent;
import epm.model.VideoComponent;
import epm.ssm.SlideShowMaker;
import java.util.ArrayList;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.stage.Screen;

/**
 * This UI component has the controls for editing a single slide
 * in a slide show, including controls for selected the slide image
 * and changing its caption.
 * 
 * 
 */
public class PageEditView extends VBox {
    // SLIDE THIS COMPONENT EDITS
    Page page;
    
    // DISPLAYS THE IMAGE FOR THIS SLIDE
    ImageView imageSelectionView;
    
    Label title;
    TextField titleField;
    Label name;
    TextField nameField;
    Label layoutLabel;
    ComboBox layout;
    Label cssLabel;
    ComboBox css;
    Label fontLabel;
    ComboBox font;
    Label footerLabel;
    TextArea footerField;
    
    Tab tab;
    
    // PROVIDES RESPONSES FOR IMAGE SELECTION
    ImageSelectionController imageController;
    
    HBox selectedHBox;
    TextComponent selectedTextComponent;
    ImageComponent selectedImageComponent;
    VideoComponent selectedVideoComponent;
    SlideShowComponent selectedSlideShowComponent;

    /**
     * THis constructor initializes the full UI for this component, using
     * the initSlide data for initializing values./
     * 
     * @param initSlide The slide to be edited by this component.
     */
    public PageEditView(Page initPage, Tab tab) {
        imageController = new ImageSelectionController();
        imageSelectionView = new ImageView();
	this.getStyleClass().add(CSS_CLASS_SLIDE_EDIT_VIEW);
	page = initPage;
	this.tab = tab;
        
        initTitleAndName();
        HBox titleSection = new HBox();
        HBox nameSection = new HBox();
        
        initLayoutFontAndCSS();
        HBox layoutCSSFont = new HBox();
        
        initFooter();
        HBox footer = new HBox();
        
        
        titleSection.getChildren().addAll(title, titleField);
        nameSection.getChildren().addAll(name, nameField);
        layoutCSSFont.getChildren().addAll(layoutLabel, layout, cssLabel, css, fontLabel, font);
        footer.getChildren().addAll(footerLabel, footerField);
        getChildren().addAll(layoutCSSFont, titleSection, nameSection, initBannerImg(), footer);

        setStyle("-fx-background: #ffffe5");
	
    }
    
    public Page getPage() {
        return page;
    }
    
    private void initTitleAndName() {
        title = new Label("Page Title: ");
        titleField = new TextField(page.getTitle());
        titleField.setOnKeyReleased( e-> {
            page.setTitle(titleField.getText());
            tab.setText(page.getTitle());
        });
        name = new Label("Student Name: ");
        nameField = new TextField(page.getStudentName());
        nameField.setOnKeyReleased( e-> {
            page.setStudentName(nameField.getText());
        });
    }
    
    private void initLayoutFontAndCSS() {
        layoutLabel = new Label("Page Layout: ");
        layout = new ComboBox();
        layout.getItems().addAll("Top-Left Nagivation", "Left Navgiation", "Middle-Left Navigation", "Middle-Right Navigation", "Middle Navigation");
        layout.setValue("Top-Left Navation");
        
        cssLabel = new Label("     Page Color Scheme: ");
        css = new ComboBox();
        css.getItems().addAll("Blue/Yellow", "Cyan/Red", "Orange/Yellow", "Red/Green", "Green/Blue");
        css.setValue("Blue/Yellow");
        
        fontLabel = new Label("     Page Font: ");
        font = new ComboBox();
        font.getItems().addAll("PT Sans", "Dosis", "Yanone Kaffeesatz", "Oxygen", "Nunito");
        font.setValue("PT Sans");
    }
    
    private void initFooter() {
        footerLabel = new Label("Page Footer: ");
        footerField = new TextArea();

    }
    
    private HBox initBannerImg() {
        HBox bannerImg = new HBox();
        Button selectImg = new Button("Select Banner Image");
        selectImg.setOnMouseClicked(e-> {
            String path = imageController.processSelectImage();
            File file = new File(path);
            try {
                // GET AND SET THE IMAGE
                URL fileURL = file.toURI().toURL();
                Image image = new Image(fileURL.toExternalForm());
                imageSelectionView.setImage(image);
                double scaledWidth = 100;
                double perc = scaledWidth / image.getWidth();
                double scaledHeight = image.getHeight() * perc;
                imageSelectionView.setFitWidth(scaledWidth);
                imageSelectionView.setFitHeight(scaledHeight);
            }
            catch (Exception e1) {
                
            }
        });
        bannerImg.getChildren().addAll(selectImg, imageSelectionView);
        bannerImg.setStyle("-fx-spacing: 10px;");
        return bannerImg;
    }
    
    public void reloadComponents() {
        Screen screen = Screen.getPrimary();
	Rectangle2D bounds = screen.getVisualBounds();
        ArrayList<TextComponent> textComponents = page.getTextComponents();
        ArrayList<ImageComponent> imageComponents = page.getImageComponents();
        ArrayList<VideoComponent> videoComponents = page.getVideoComponents();
        ArrayList<SlideShowComponent> slideShowComponents = page.getSlideShowComponents();
        
        for (TextComponent component : textComponents) {
            String textType = component.getTextType();
            if (textType.equalsIgnoreCase("paragraph")){
                Label paragraphLabel = new Label("Paragraph: ");
                TextFlow paragraphField;
                if (component.getHyperlinks().size() == 0)
                    paragraphField = new TextFlow(new Text(component.getData()));
                else
                    paragraphField = new TextFlow();
                
                ArrayList<Hyperlink> links = new ArrayList<Hyperlink>();
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for (HyperlinkComponent link : component.getHyperlinks()) {
                    String hyperlink = link.getUrl();
                    int position = link.getCompleteText().indexOf(link.getSelectedText());
                    Hyperlink now = new Hyperlink();
                    now.setText(link.getSelectedText());
                    now.setStyle("-fx-text-fill: #1919ff");
                    now.setTooltip(new Tooltip(hyperlink));
                    links.add(now);
                    positions.add((Integer)position);
                }
                positions.add(component.getData().length());
                for (int i = 0; i < positions.size()-1; i++) {
                    int index = i;
                    for (int j = i + 1; j < positions.size(); j++)
                        if (positions.get(j) < positions.get(index))
                            index = j;

                    int smallerNumber = positions.get(index); 
                    positions.set(index, positions.get(i));
                    positions.set(i, smallerNumber);
                    Hyperlink temp = links.get(index); 
                    links.set(index, links.get(i));
                    links.set(i, temp);
                }
                
                for (int i = 0; i < positions.size()-1; i++) {
                    String beforeHyperlink = component.getData().substring(0, positions.get(i));
                    String afterHyperlink = component.getData().substring(positions.get(i) + links.get(i).getText().length(), positions.get(i+1));
                    Text before = new Text(beforeHyperlink);
                    Text after = new Text(afterHyperlink);
                    if (i == 0)
                        paragraphField.getChildren().addAll(before, links.get(i), after);
                    else
                        paragraphField.getChildren().addAll(links.get(i), after);
                }
                    
                Text data = new Text(component.getData());
                HBox paragraphComponent = new HBox();
                paragraphComponent.getChildren().addAll(paragraphLabel, paragraphField);
                paragraphComponent.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = paragraphComponent;
                    selectedTextComponent = component;
                    paragraphComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
                });
                paragraphComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                if (!isNoneSelected()) {
                    if (component.equals(selectedTextComponent))
                        paragraphComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                }
                paragraphField.setMaxWidth(bounds.getWidth()/(1.5));
                getChildren().add(paragraphComponent);
            }
            else if (textType.equalsIgnoreCase("list")) {
                Label listLabel = new Label("List: ");
                ArrayList<String> data = component.getList();
                ListView<String> list = new ListView<String>();
                for (String s : data) {
                    list.getItems().add(s);
                }  
                
                for (HyperlinkComponent links : component.getHyperlinks()) {
                    list.getItems().set(links.getIndex(), "Hyperlink(" + links.getUrl() + ") - " + list.getItems().get(links.getIndex()));
                }
                
                HBox listComponent = new HBox();
                listComponent.getChildren().addAll(listLabel, list);
                listComponent.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = listComponent;
                    selectedTextComponent = component;
                    listComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
                });
                list.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = listComponent;
                    selectedTextComponent = component;
                    listComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
                });
                listComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                if (!isNoneSelected()) {
                    if (component.equals(selectedTextComponent))
                        listComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                }
                getChildren().add(listComponent);
            }
            else {
                Label headerLabel = new Label("Header: ");
                Text headerField = new Text(component.getData());
                HBox headerComponent = new HBox();
                headerComponent.getChildren().addAll(headerLabel, headerField);
                headerComponent.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = headerComponent;
                    selectedTextComponent = component;
                    headerComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
                });
                headerComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                if (!isNoneSelected()) {
                    if (component.equals(selectedTextComponent))
                        headerComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                }
                headerField.setWrappingWidth(bounds.getWidth()-500);
                getChildren().add(headerComponent);
            }
                
        };
        
        for (ImageComponent component : imageComponents) {
            Label imageLabel = new Label("Image: ");
            ImageView imageView = component.getImageView();
            Text caption = new Text(component.getCaption());
            HBox imageComponent = new HBox();
            VBox imageAndCaption = new VBox();
            Text width = new Text("Width: " + component.getWidth() + "px");
            Text height = new Text("Height: " + component.getHeight() + "px");
            Text position = new Text("Floating position: " + component.getPosition());
            VBox widthHeight = new VBox();
            widthHeight.getChildren().addAll(width, height, position);
            widthHeight.setStyle("-fx-padding: 5px 5px 5px 5px;");
            imageAndCaption.getChildren().addAll(imageView, caption);
            imageComponent.getChildren().addAll(imageLabel, imageAndCaption, widthHeight);
            imageComponent.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = imageComponent;
                    selectedImageComponent = component;
                    imageComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
            });
            imageComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            if (!isNoneSelected()) {
                if (component.equals(selectedImageComponent))
                    imageComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            }
            getChildren().add(imageComponent);
        };
        
        for (VideoComponent component : videoComponents) {
            Label videoLabel = new Label("Video: ");
            MediaView mediaView = component.getMediaView();
            Text caption = new Text(component.getCaption());
            HBox videoComponent = new HBox();
            Text width = new Text("Width: " + component.getWidth() + "px");
            Text height = new Text("Height: " + component.getHeight() + "px");
            VBox widthHeight = new VBox();
            VBox video = new VBox();
            Button play = new Button("Play Video");
            Button stop = new Button("Stop Video");
            play.setOnMouseClicked( e-> {
                mediaView.getMediaPlayer().play();
            });
            stop.setOnMouseClicked( e-> {
                mediaView.getMediaPlayer().stop();
            });
            video.getChildren().addAll(mediaView, caption);
            widthHeight.getChildren().addAll(width, height, play, stop);
            widthHeight.setStyle("-fx-padding: 5px 5px 5px 5px; -fx-spacing: 5px;");
            videoComponent.getChildren().addAll(videoLabel, video, widthHeight);
            videoComponent.setOnMouseClicked(e-> {
                    if (!isNoneSelected()) {
                        selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                        resetSelectedComponents();
                    }
                    selectedHBox = videoComponent;
                    selectedVideoComponent = component;
                    videoComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    page.setPageEditView(this);
            });
            videoComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            if (!isNoneSelected()) {
                if (component.equals(selectedVideoComponent))
                    videoComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            }
            getChildren().addAll(videoComponent);
        };
        
        for (SlideShowComponent component : slideShowComponents) {
            Label slideShowLabel = new Label("Slide Show: ");
            
            Button previous = new Button();
            String previousPath = "file:images/icons/Previous.png";
            Image previousImage = new Image(previousPath);
            previous.setGraphic(new ImageView(previousImage));
            previous.setTooltip(new Tooltip("Previous Slide"));
            
            Button next = new Button();
            String nextPath = "file:images/icons/Next.png";
            Image nextImage = new Image(nextPath);
            next.setGraphic(new ImageView(nextImage));
            next.setTooltip(new Tooltip("Next Slide"));
            
            ArrayList<ImageView> imageViews = component.createImages();
            ArrayList<String> captions = component.getCaptions();
            
            HBox buttons = new HBox();
            buttons.getChildren().addAll(previous, next);
            
            Text currentCaption;
            BorderPane bp = new BorderPane();
            
            currentCaption = new Text(captions.get(component.getPosition()));
            buttons.setStyle("-fx-spacing: 15px; -fx-alignment: CENTER;");
            bp.setTop(slideShowLabel);
            VBox current = new VBox();
            current.getChildren().addAll(imageViews.get(component.getPosition()), currentCaption);
            bp.setCenter(current);
            bp.setBottom(buttons);
            
            previous.setOnMouseClicked(e-> {
                component.decreasePosition();
                VBox currently = new VBox();
                currently.getChildren().addAll(imageViews.get(component.getPosition()), new Text(captions.get(component.getPosition())));
                bp.setCenter(currently);
            });
            
            next.setOnMouseClicked(e-> {
                component.increasePosition();
                VBox currently = new VBox();
                currently.getChildren().addAll(imageViews.get(component.getPosition()), new Text(captions.get(component.getPosition())));
                bp.setCenter(currently);
            });
            
            HBox slideShowComponent = new HBox();
            slideShowComponent.getChildren().addAll(bp);
            slideShowComponent.setOnMouseClicked(e-> {
                if (!isNoneSelected()) {
                    selectedHBox.setStyle("-fx-background-color: #ffffb2; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                    resetSelectedComponents();
                }
                selectedHBox = slideShowComponent;
                selectedSlideShowComponent = component;
                slideShowComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
                page.setPageEditView(this);
            });
            getChildren().add(slideShowComponent);
            slideShowComponent.setStyle("-fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            if (!isNoneSelected()) {
                if (component.equals(selectedSlideShowComponent))
                    slideShowComponent.setStyle("-fx-background-color: #ffa500; -fx-border-color: rgb(0,0,0); -fx-padding: 5px 5px 5px 5px;");
            }
        };
    }
    
    public boolean isParagraphSelected() {
        if (isTextSelected()) {
            return (selectedTextComponent.getTextType().equalsIgnoreCase("paragraph"));
        }
        return false;
    }
    
    public boolean isListSelected() {
        if (isTextSelected()) {
            return (selectedTextComponent.getTextType().equalsIgnoreCase("list"));
        }
        return false;
    }
    
    public TextComponent getTextComponent() {
        return selectedTextComponent;
    }
    
    public boolean isTextSelected() {
        return (selectedTextComponent != null);
    }
    
    public boolean isVideoSelected() {
        return (selectedVideoComponent != null);
    }
    
    public boolean isImageSelected() {
        return (selectedImageComponent != null);
    }
    
    public boolean isSlideShowSelected() {
        return (selectedSlideShowComponent != null);
    }
    
    public boolean isNoneSelected() {
        return (!isTextSelected() && !isVideoSelected() && !isImageSelected() && !isSlideShowSelected());
    }
    
    public void resetSelectedComponents() {
        selectedTextComponent = null;
        selectedVideoComponent = null;
        selectedImageComponent = null;
        selectedSlideShowComponent = null;
        selectedHBox = null;
    }
    
    public void displayEditBox() {
        if (!isNoneSelected()) {
            if (isTextSelected()) {
                String type = selectedTextComponent.getTextType();
                if (type.equalsIgnoreCase("list")) {
                    TextComponentDialogue dialogue = new TextComponentDialogue(type, selectedTextComponent.getList(), selectedTextComponent);
                    dialogue.showAndWait();
                }
                else {
                    TextComponentDialogue dialogue = new TextComponentDialogue(type, selectedTextComponent.getData(), selectedTextComponent);
                    dialogue.showAndWait();
                }    
            }
            else if (isVideoSelected()) {
                String url = selectedVideoComponent.getUrl();
                String caption = selectedVideoComponent.getCaption();
                double height = selectedVideoComponent.getHeight();
                double width = selectedVideoComponent.getWidth();
                VideoComponentDialogue dialogue = new VideoComponentDialogue(url, caption, width, height, selectedVideoComponent);
                dialogue.showAndWait();
            }
            else if (isImageSelected()) {
                String url = selectedImageComponent.getUrl();
                String position = selectedImageComponent.getPosition();
                double height = selectedImageComponent.getHeight();
                double width = selectedImageComponent.getWidth();
                ImageComponentDialogue dialogue = new ImageComponentDialogue(url, position, width, height, selectedImageComponent);
                dialogue.showAndWait();
            }
            else {
                SlideShowMaker slideShow = selectedSlideShowComponent.getSlideShow();
                slideShow.showAndWait();
            }
        }
    }
}
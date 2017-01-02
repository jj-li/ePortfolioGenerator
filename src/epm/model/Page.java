package epm.model;

import epm.view.PageEditView;
import java.util.ArrayList;

/**
 * This class represents a single page in the ePortfolio.
 * 
 * @author Jia Li
 */
public class Page {
    PageEditView selectedPageEditView;
    String title;
    String studentName;
    ArrayList<TextComponent> textComponents;
    ArrayList<ImageComponent> imageComponents;
    ArrayList<VideoComponent> videoComponents;
    ArrayList<SlideShowComponent> slideShowComponents;
    String layout = "Top-Left Nagivation";
    String color = "Blue/Yellow";
    String font = "PT Sans";
    String footer = "";
    String bannerImgName = "";
    String bannerImgPath = "";
     
    /**
     * Constructor, it initializes all slide data.
     * @param initImageFileName File name of the image.
     * 
     * @param initImagePath File path for the image.
     * 
     */
    public Page() {
        title = "New Page";
        studentName = "";
        textComponents = new ArrayList<TextComponent>();
        imageComponents = new ArrayList<ImageComponent>();
        videoComponents = new ArrayList<VideoComponent>();
        slideShowComponents = new ArrayList<SlideShowComponent>();
       
    }
    
    public Page(String title, String studentName) {
        this.title = title;
        this.studentName = studentName;
        textComponents = new ArrayList<TextComponent>();
        imageComponents = new ArrayList<ImageComponent>();
        videoComponents = new ArrayList<VideoComponent>();
        slideShowComponents = new ArrayList<SlideShowComponent>();
    }
    
    // ACCESSOR METHODS
    public String getTitle() { return title; }
    public String getStudentName() { return studentName; }
    
    // MUTATOR METHODS
    public void setTitle(String newTitle) {
	title = newTitle;
    }
    
    public void setStudentName(String newStudentName) {
	studentName = newStudentName;
    }
    
    public void addTextComponent(TextComponent textComponent) {
        textComponents.add(textComponent);
    }
    
    public ArrayList<TextComponent> getTextComponents() {
        return textComponents;
    }
    
    public ArrayList<ImageComponent> getImageComponents() {
        return imageComponents;
    }
    
    public void addImageComponent(ImageComponent imageComponent) {
        imageComponents.add(imageComponent);
    }
    
    public ArrayList<VideoComponent> getVideoComponents() {
        return videoComponents;
    }
    
    public void addVideoComponent(VideoComponent videoComponent) {
        videoComponents.add(videoComponent);
    }
    
    public ArrayList<SlideShowComponent> getSlideShowComponents() {
        return slideShowComponents;
    }
    
    public void addSlideShowComponent(SlideShowComponent slideShowComponent) {
        slideShowComponents.add(slideShowComponent);
    }
    
    public void setPageEditView(PageEditView editView) {
        selectedPageEditView = editView;
    }
    
    public PageEditView getSelectedPageEditView() {
        return selectedPageEditView;
    }
    
    public void removeTextComponent(TextComponent textComponent) {
        for (int i = textComponents.size()-1; i >= 0; i--) {
            if (textComponents.get(i).equals(textComponent)) {
                textComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeImageComponent(ImageComponent imageComponent) {
        for (int i = imageComponents.size()-1; i >= 0; i--) {
            if (imageComponents.get(i).equals(imageComponent)) {
                imageComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeVideoComponent(VideoComponent videoComponent) {
        for (int i = videoComponents.size()-1; i >= 0; i--) {
            if (videoComponents.get(i).equals(videoComponent)) {
                videoComponent.getMediaView().getMediaPlayer().stop();
                videoComponents.remove(i);
                break;
            }  
        }
    }
    
    public void removeSlideShowComponent(SlideShowComponent slideShowComponent) {
        for (int i = slideShowComponents.size()-1; i >= 0; i--) {
            if (slideShowComponents.get(i).equals(slideShowComponent)) {
                slideShowComponents.remove(i);
                break;
            }  
        }
    }
    
    public String getLayout() {
        return layout;
    }
    
    public String getFont() {
        return font;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setLayout(String layout) {
        this.layout = layout;
    }
    
    public void setFont(String font) {
        this.font = font;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public boolean hasBannerImage() {
        if (layout.equalsIgnoreCase("Middle-Left Navigation") || layout.equalsIgnoreCase("Middle Navigation"))
            return true;
        else
            return false;
    }
    
    public String getFooter() {
        return footer;
    }
    
    public void setFooter(String text) {
        footer = text;
    }
    
    public void setBannerImgName(String img) {
        bannerImgName = img;
    }
    
    public String getBannerImg() {
        return bannerImgName;
    }
    
    public void setBannerImgPath(String path) {
        bannerImgPath = path;
    }
    
    public String getBannerImgPath() {
        return bannerImgPath;
    }
}

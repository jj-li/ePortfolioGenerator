/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.model;

import epm.ssm.SlideShowMaker;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author icysniper
 */
public class SlideShowComponent {
    private ArrayList<String> imagePaths;
    private ArrayList<String> captions;
    private SlideShowMaker slideShow;
    private int position;
    private ArrayList<ImageView> imageViews;
    private Page page;
    
    public SlideShowComponent(ArrayList<String> paths, ArrayList<String> caps, Page page) {
        imagePaths = paths;
        captions = caps;
        this.page = page;
        createSlideShow(page);
        
    }
    
    public SlideShowMaker getSlideShow() {
        return slideShow;
    }
    
    public void createSlideShow(Page page) {
        slideShow = new SlideShowMaker(imagePaths, captions, page);
    }
    
    public ArrayList<String> getImagePaths() {
        return imagePaths;
    }
    
    public ArrayList<String> getCaptions() {
        return captions;
    }
    
    public void setImagePaths(ArrayList<String> newImagePaths) {
        imagePaths = newImagePaths;
    }
    
    public void setCaptions(ArrayList<String> newCaptions) {
        captions = newCaptions;
    }
    
    public void increasePosition() {
        position++;
        if (position >= imagePaths.size())
            position = 0;
    }
    
    public void decreasePosition() {
        position--;
        if (position < 0)
            position = imagePaths.size()-1;
    }
    
    public int getPosition() {
        return position;
    }
    
    public ArrayList<ImageView> createImages() {
        imageViews = new ArrayList<ImageView>();
        for (String s : imagePaths) {
            File file = new File(s);
            try {
                // GET AND SET THE IMAGE
                URL fileURL = file.toURI().toURL();
                Image image = new Image(fileURL.toExternalForm());
                ImageView imageView = new ImageView();
                imageView.setImage(image);
                
                double scaledWidth = 300;
                double perc = scaledWidth / image.getWidth();
                double scaledHeight = image.getHeight() * perc;
                imageView.setFitWidth(scaledWidth);
                imageView.setFitHeight(scaledHeight);
                
                imageViews.add(imageView);
                
            }
            catch (Exception e1) {

            }
        }
        return imageViews;
    }
}

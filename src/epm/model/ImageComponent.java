/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.model;

import java.io.File;
import java.net.URL;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author icysniper
 */
public class ImageComponent {
    private String imageUrl;
    private String imagePosition;
    private double width;
    private double height;
    private ImageView imageView;
    private String caption;
    private String imageName;
    
    public ImageComponent(String url, String position, double width, double height, String caption) {
        imageUrl = url;
        imagePosition = position;
        this.width = width;
        this.height = height;
        imageView = new ImageView();
        setImageView();
        this.caption = caption;
        File file = new File(url);
        imageName = file.getName();
    }
    
    public String getUrl(){
        return imageUrl;
    }
    
    public String getPosition() {
        return imagePosition;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setUrl(String url) {
        imageUrl = url;
        File file = new File(url);
        imageName = file.getName();
    }
    
    public void setPosition(String position) {
        imagePosition = position;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public ImageView getImageView() {
        return imageView;
    }
    
    public String getCaption() {
        return caption;
    }
    
    public void setCaption(String newCaption) {
        caption = newCaption;
    }
    
    public void setImageView() {
        File file = new File(imageUrl);
            try {
                // GET AND SET THE IMAGE
                URL fileURL = file.toURI().toURL();
                Image image = new Image(fileURL.toExternalForm());
                imageView.setImage(image);
                if (width > 0 && height > 0) {
                    imageView.setFitWidth(width);
                    imageView.setFitHeight(height);
                }
            }
            catch (Exception e1) {
                
            }
    }
    
    public String getImageName() {
        return imageName;
    }
}

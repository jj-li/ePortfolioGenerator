/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.model;

import java.io.File;
import java.net.URL;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

/**
 *
 * @author icysniper
 */
public class VideoComponent {
    String videoUrl;
    String videoCaption;
    double width;
    double height;
    MediaView videoView;
    
    public VideoComponent(String url, String caption, double width, double height) {
        videoUrl = url;
        videoCaption = caption;
        this.width = width;
        this.height = height;
        setMediaView();
    }
    
    public String getUrl(){
        return videoUrl;
    }
    
    public String getCaption() {
        return videoCaption;
    }
    
    public double getWidth() {
        return width;
    }
    
    public double getHeight() {
        return height;
    }
    
    public void setUrl(String url) {
        videoUrl = url;
    }
    
    public void setCaption(String caption) {
        videoCaption = caption;
    }
    
    public void setWidth(double width) {
        this.width = width;
    }
    
    public void setHeight(double height) {
        this.height = height;
    }
    
    public MediaView getMediaView() {
        return videoView;
    }
    
    public void setMediaView() {
        File file = new File(videoUrl);
            try {
                // GET AND SET THE IMAGE
                URL fileURL = file.toURI().toURL();
                Media video = new Media(fileURL.toExternalForm());
                MediaPlayer videoPlayer = new MediaPlayer(video);
                videoView = new MediaView(videoPlayer);
                if (width > 0 && height > 0) {
                    videoView.setFitWidth(width);
                    videoView.setFitHeight(height);
                }
            }
            catch (Exception e1) {
                
            }
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epm.model;

import java.util.ArrayList;

/**
 *
 * @author icysniper
 */
public class TextComponent {
    private String textType;
    private ArrayList<String> list;
    private String data;
    
    public TextComponent(String type, String text) {
        textType = type;
        data = text;
    }
    
    public TextComponent(String type, ArrayList<String> datas) {
        textType = type;
        list = datas;
    }
    
    public void setTextType(String newType) {
        textType = newType;
    }
    
    public void setList(ArrayList<String> newData) {
        list = newData;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getTextType() {
        return textType;
    }
    
    public ArrayList<String> getList() {
        return list;
    }
    
    public String getData() {
        return data;
    }
}

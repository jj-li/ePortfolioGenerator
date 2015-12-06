/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * @author Jia Li
 */
var url = "./Testing.json";
var pageTitles = [""];
var studentNames = [""];
var imagePaths = [""];
var videoPaths = [""];
var slideShowPaths = [""];
var links = [""];
var list = [""];
var paragraphHyperlinks = [""];
var paragraphHyperlinksContentLength = [""];
var paragraphHyperlinksIndex = [""];
var paragraphHyperlinksContent = [""];
var listHyperlinks = [""];
var listHyperlinksIndex = [""];
var bannerImage = "";
var myArr;
var videoTagStart = '<video width="'; 
var videoTagMiddle1 = '" height = "';
var videoTagMiddle2 = '" controls><source src =';
var videoTagEnd = 'type="video/mp4"></video>';
var imageTagStart = '<img src=';
var imageTagEnd = 'alt="Missing Icon" width = "';
var imageTagEnd1 = '" height = "';
var imageTagEnd2 = '" class="verticalImages"/><br />';
var iframeTagStart = '<iframe src=';
var iframeTagEnd = '></iframe>';
var headerTagStart = '<div class = "header">';
var headerTagEnd = '</div>';
var paragraphTagStart = '<p>';
var paragraphTagEnd = '</p>';
var linkStart = '<a href = "';
var linkMiddle = '" class = "navigation">';
var linkEnd = '</a>';
var pageName = "";
var hyperlinkStart = '<a href = "';
var hyperlinkEnd = '" class = "inlineHyperlinks">';

function load() {
    $.getJSON(url, function( json ) {
        $.each(json.pages, function(index, d){  
                var pagePathName= window.location.pathname;
                pageName = pagePathName.substring(pagePathName.lastIndexOf("/") + 1);
                pageName = pageName.substring(0, pageName.indexOf(".html"));
                loadLink(d.title);
                var noSpace = (d.title).split(" ");
                var newName = "";
                for (i = 0; i < noSpace.length; i++) {
                    newName += noSpace[i];
                }
                if (pageName === newName) {
                    loadBannerContent(d.title, d.name);
                    $("div#footer").append(d.footer);
                    $("body").css("font-family", d.font);
                    if (d.banner_image_path !== "")
                        $("#bannerImg").attr("src", "./img/" + d.banner_image_name);
                    $.each(d.video_paths, function(index, e){
                        loadVideoContent(e.name, e.caption, e.width, e.height);
                    });
                    $.each(d.image_paths, function(index, e){
                        loadImageContent(e.name, e.caption);
                    });
                    /*$.each(d.slideshow_paths, function(index, e){
                        slideShowPaths.push(e.path);
                    });*/
                    $.each(d.text_components, function(index, e){
                        if (e.type === "List")
                        {
                            $.each(e.hyperlink, function(index, f) {
                                listHyperlinks.push(f.url);
                                listHyperlinksIndex.push(f.selected_text);
                            });
                            $.each(e.text, function(index, f){
                                list.push(f.data);
                                
                            }); 
                            for (i = 1; i < list.length; i++) {
                                var isLink = false;
                                var url = "";
                                for (j = 1; j < listHyperlinksIndex.length; j++) {
                                    if (i == (parseInt(listHyperlinksIndex[j])+1)) {
                                        isLink = true;
                                        url = listHyperlinks[j];
                                    }
                                }
                                if (isLink === false)
                                    $("div#textComponentContainers").append('<li style = "font: ' + e.style + " " + e.size + "px " + e.font + '">' + list[i] + '</li>');
                                else 
                                    $("div#textComponentContainers").append('<li style = "font: ' + e.style + " " + e.size + "px " + e.font + '">' + hyperlinkStart + url + hyperlinkEnd + list[i] + '</a></li>');                                
                            }
                            listHyperlinks = [""];
                            listHyperlinksIndex = [""];
                        }
                        else if (e.type === "Header")
                            loadHeaderContent(e.text);
                        else {
                            $.each(e.hyperlink, function(index, f) {
                                paragraphHyperlinks.push(f.url);
                                paragraphHyperlinksIndex.push((e.text).indexOf(f.selected_text));
                                paragraphHyperlinksContent.push(f.selected_text);
                                paragraphHyperlinksContentLength.push((f.selected_text).length);
                            });
                            loadParagraphContent(e.text, e.font, e.style, e.size);
                            paragraphHyperlinks = [""];
                            paragraphHyperlinksContentLength = [""];
                            paragraphHyperlinksIndex = [""];
                            paragraphHyperlinksContent = [""];
                        } 
                    });
                }
            });
            //loadSlideShowContent();
        });
            
            
}

function loadLink(name) {
    var noSpace = name.split(" ");
    var newName = "";
    for (i = 0; i < noSpace.length; i++) {
        newName += noSpace[i];
    }
    var temp = linkStart + newName + ".html" + linkMiddle + name + linkEnd;
    $("#topLeftNavigationBar").append(temp);
} 

function loadBannerContent(title, author) {
    $("div#title").html(title);
    $("div#studentName").html(author);
}

function loadVideoContent(path, caption, width, height) {
    var temp = videoTagStart + width + videoTagMiddle1 + height + videoTagMiddle2 + '"./Videos/' + path + '"' + videoTagEnd;
    $("div#videoContainers").append(temp);
    var caption = "<div>" + caption + "</div>";
    $("div#videoContainers").append(caption);
}

function loadImageContent(path, caption, width, height) {
    var temp = imageTagStart + '"./img/' + path + '"' + imageTagEnd + width + imageTagEnd1 + height + imageTagEnd2;
    var image = new Image();
            image.src = path
            image.onload = function() {
                image.width = width;
                image.height = height;
            };
    $("div#imageContainers").append(temp);
    var caption = "<div>" + caption + "</div>";
    $("div#imageContainers").append(caption);
}

function loadSlideShowContent() {
    for (var i = 1; i < slideShowPaths.length; i++){
        var temp = iframeTagStart + '"' + slideShowPaths[i] + '"' + iframeTagEnd;
        $("div#slideShowContainers").append(temp);
    }
}

function loadHeaderContent(data) {
    var temp = headerTagStart + data + headerTagEnd;
    $("div#textComponentContainers").append(temp);
}

function loadParagraphContent(data, font, style, size) {
    for (i = 0; i < paragraphHyperlinksIndex.length - 1; i++)
    {
        var index = i;
        for (j = i + 1; j < paragraphHyperlinksIndex.length; j++)
                if (paragraphHyperlinksIndex[j] < paragraphHyperlinksIndex[index])
                    index = j;
      
        var smallerNumber = paragraphHyperlinksIndex[index]; 
        paragraphHyperlinksIndex[index] = paragraphHyperlinksIndex[i];
        paragraphHyperlinksIndex[i] = smallerNumber;
        
        var temp = paragraphHyperlinks[index]; 
        paragraphHyperlinks[index] = paragraphHyperlinks[i];
        paragraphHyperlinks[i] = temp;
        
        var temp1 = paragraphHyperlinksContentLength[index]; 
        paragraphHyperlinksContentLength[index] = paragraphHyperlinksContentLength[i];
        paragraphHyperlinksContentLength[i] = temp1;
        
        var temp2 = paragraphHyperlinksContent[index]; 
        paragraphHyperlinksContent[index] = paragraphHyperlinksContent[i];
        paragraphHyperlinksContent[i] = temp2;
    }
    var datas = [""];
    for (i = 1; i < paragraphHyperlinksIndex.length; i++) {
        if (i === 1)
            datas.push(data.substring(0, paragraphHyperlinksIndex[i]));
        else
            datas.push(data.substring(paragraphHyperlinksIndex[i-1]+paragraphHyperlinksContentLength[i-1], paragraphHyperlinksIndex[i]));
    }
    data = "";
    for (i = 1; i < datas.length; i++) {
        data += datas[i] + hyperlinkStart + paragraphHyperlinks[i] + hyperlinkEnd + paragraphHyperlinksContent[i] + "</a>";
    }
    var temp = '<p style = "font: ' + style + " " + size + "px " + font + '">' + data + paragraphTagEnd;
    
    $("div#textComponentContainers").append(temp);
}



load();
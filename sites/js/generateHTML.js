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
var bannerImage = "";
var myArr;
var videoTagStart = '<video width="400" controls><source src =';
var videoTagEnd = 'type="video/mp4"></video>';
var imageTagStart = '<img src=';
var imageTagEnd = 'alt="Missing Icon" class="verticalImages"/><br />';
var iframeTagStart = '<iframe src=';
var iframeTagEnd = '></iframe>';
var headerTagStart = '<div class = "header">';
var headerTagEnd = '</div>';
var paragraphTagStart = '<p>';
var paragraphTagEnd = '</p>';

function load() {
    $.getJSON(url, function( json ) {
        $.each(json.pages, function(index, d){  
                loadBannerContent(d.page_title, d.student_name);
                $("div#footer").append(d.footer);
                $("#bannerImg").attr("src", d.bannerimage_path);
                $.each(d.video_paths, function(index, e){
                    videoPaths.push(e.path);
                });
                $.each(d.image_paths, function(index, e){
                    imagePaths.push(e.path);
                });
                $.each(d.slideshow_paths, function(index, e){
                    slideShowPaths.push(e.path);
                });
                $.each(d.text_components, function(index, e){
                    if (e.type === "list")
                    {
                        $.each(e.text, function(index, f){
                            $("div#textComponentContainers").append('<li>' + f.data + '</li>');
                        }); 
                    }
                    else if (e.type === "header")
                        loadHeaderContent(e.text);
                    else
                        loadParagraphContent(e.text);
                });
            });
            loadVideoContent();
            loadImageContent();
            loadSlideShowContent();
        });
            
            
}

function loadBannerContent(title, author) {
    $("div#title").html(title);
    $("div#studentName").html(author);
}

function loadVideoContent() {
    for (var i = 1; i < videoPaths.length; i++){
        var temp = videoTagStart + '"' + videoPaths[i] + '"' + videoTagEnd;
        $("div#videoContainers").append(temp);
    }
}

function loadImageContent() {
    for (var i = 1; i < imagePaths.length; i++){
        var temp = imageTagStart + '"' + imagePaths[i] + '"' + imageTagEnd;
        $("div#imageContainers").append(temp);
    }
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

function loadParagraphContent(data) {
    var temp = paragraphTagStart + data + paragraphTagEnd;
    $("div#textComponentContainers").append(temp);
}

load();

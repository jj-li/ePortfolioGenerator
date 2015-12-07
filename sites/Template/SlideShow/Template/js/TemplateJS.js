/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * 
 * @author Jia Li
 */
var url = "./Missing Image.json";
var slideShow = [""];
var captions = [""];
var slideShowTitle = "";
var myArr;
var slidePosition = 1;
var captionPosition = 1;
var playSlide;

function load() {
    $.getJSON(url, function( json ) {
        $.each(json.slides, function(index, d){         
                pathOfImage = d.image_file_name;
                caption = d.image_caption;
                slideShow.push("./img/" + pathOfImage);
                if (caption == "")
                    caption = "<br>";
                captions.push(caption);
            });
            $("#slideShowCaption").html(captions[captionPosition]);
            $("#slideShow").attr("src", slideShow[slidePosition]);
            
            var image = new Image();
            image.src = slideShow[slidePosition];
            image.onload = function() {
                var imgWidth = image.naturalWidth;
                var imgHeight = image.naturalHeight;
                var newImgHeight = 251;
                $("#slideShow").attr("height", newImgHeight);
                var newImgWidth = (newImgHeight/imgHeight)* imgWidth;
                $("#slideShow").attr("width", newImgWidth);
                image.width = newImgWidth;
                image.height = newImgHeight;
            };
        });
            
            
}

function play() {
    var img = document.getElementById("playAndPause");
    if (img.src.indexOf("play") !== -1)
    {
        img.src = "./img/pause.png";
        playSlide = setInterval(nextSlide, 3000);
    }
    else if (img.src.indexOf("pause") !== -1)
    {
        img.src = "./img/play.png";
        clearInterval(playSlide); 
    }
}

function nextSlide() {
    slidePosition++;
    captionPosition++;
    if (slidePosition >= (slideShow.length)) {
        slidePosition = 1;
        captionPosition = 1;
    }
    $("#slideShowCaption").html(captions[captionPosition]);
            $("#slideShow").attr("src", slideShow[slidePosition]);
            var image = new Image();
            image.src = slideShow[slidePosition];
            image.onload = function() {
                var imgWidth = image.naturalWidth;
                var imgHeight = image.naturalHeight;
                var newImgHeight = 251;
                var newImgWidth = (newImgHeight/imgHeight)* imgWidth;
                $("#slideShow").attr("width", newImgWidth);
            };
}
            
function previousSlide() {
    slidePosition--;
    captionPosition--;
    if (slidePosition < 1) {
        slidePosition = (slideShow.length);
        captionPosition = (captions.length);
    }
    $("#slideShowCaption").html(captions[captionPosition]);
            $("#slideShow").attr("src", slideShow[slidePosition]);
             var image = new Image();
            image.src = slideShow[slidePosition];
            image.onload = function() {
                var imgWidth = image.naturalWidth;
                var imgHeight = image.naturalHeight;
                var newImgHeight = 251;
                var newImgWidth = (newImgHeight/imgHeight)* imgWidth;
                $("#slideShow").attr("width", newImgWidth);
            };
}

load();

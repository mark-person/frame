var util = {};
var cropbox = function(options) {
	var obj = {state:{}, ratio:1, options: options, image:new Image()}
    obj.image.onload = function() {util.setBackground()}
    obj.image.src = options.imgSrc;
    return obj;
}

var imageBoxVue = new Vue({
	el:'#imageBox',
	data: {currentBgX:0, currentBgY:0, thumbWidth:'200', thumbHeight:'200', boxWidth:'400', boxHeight:'400', textArray:[{}]},
	methods: {
		setCurrentBgXY:function() {
			var bg = document.getElementById("imageBox").style.backgroundPosition.split(' '); 
            this.currentBgX = parseInt(bg[0]);
            this.currentBgY = parseInt(bg[1]);
		},
		mousedown:function() {
			cropper.state.dragable = true;
            event.preventDefault();
            event.stopImmediatePropagation();
            if (event.targetTouches) {
                if (event.targetTouches.length == 2) {
                    var len = util.getDistance(event.targetTouches[0], event.targetTouches[1]);
                    cropper.state.downLen = len;
                }
                else if (event.targetTouches.length == 1) {
                    var touch = event.targetTouches[0];
                    cropper.state.mouseX = touch.pageX;
                    cropper.state.mouseY = touch.pageY;
                }
            }
            else {
            	cropper.state.mouseX = event.clientX;
            	cropper.state.mouseY = event.clientY;
            }
		},
		mousemove:function() {
			if (!cropper.state.dragable) return;
			
			event.stopImmediatePropagation();
            event.preventDefault();
           
            if (event.targetTouches) {
                if (event.targetTouches.length == 2) {
                    var len = util.getDistance(event.targetTouches[0], event.targetTouches[1]);
                    cropper.ratio *= len / cropper.state.downLen;
                    cropper.state.downLen = len;
                    util.setBackground();
                    return;
                }
                else if (event.targetTouches.length == 1) {
                    var touch = event.targetTouches[0];
                    event.clientX = touch.pageX;
                    event.clientY = touch.pageY;
                }
            }
            
            var x = event.clientX - cropper.state.mouseX;
            var y = event.clientY - cropper.state.mouseY;
            
            var posi = document.getElementById("imageBox").style.backgroundPosition;
            var bg = posi.split(' ');
            document.getElementById("imageBox").style.backgroundPosition =  (x + parseInt(bg[0])) +'px ' + (y + parseInt(bg[1])) + 'px';
            
            cropper.state.mouseX = event.clientX;
            cropper.state.mouseY = event.clientY;
		}
	}
})

var actionVue = new Vue({
	el:'#actionDiv',
	data: {circleColor:'blue', text:{value:'text', color:'white', bgColor:'black', x:0, y:0},  textArray:[{value:'text', color:'white', bgColor:'black', x:0, y:0}]},
	methods:{
		circleColorChange:function(value) {
			document.getElementById("cropCircle").src = util.getCropCircle(value);
		},
		uploadChange:function() {
			uploadFile().then(val => {
				document.getElementById("cropCircle").src = util.getCropCircle(actionVue.circleColor);
	            document.getElementById("cropCircle").style.display = "block";
	            document.getElementById("cropCircleImg").style.display = "none";
				
        		cropper = cropbox({imgSrc:val});
        	})
        	
        	/*
			var reader = new FileReader();
	        reader.onload = function(e) {
	            
	            
	        }
	        reader.readAsDataURL(event.currentTarget.files[0]);*/
		},
		crop:function() {
			document.getElementById("cropCircle").style.display = "none";
			document.getElementById("cropCircleImg").style.display = "block";
	        document.getElementById("cropCircleImg").src = util.getDataURL();
	       
	        var bg = document.getElementById("imageBox").style.backgroundPosition.split(' ');
	        var bgX = -imageBoxVue.currentBgX + parseInt(bg[0]);
	        var bgY = -imageBoxVue.currentBgY + parseInt(bg[1]); 
	        imageBoxVue.setCurrentBgXY();
	        
	        var x = cropper.image.width / 2 - bgX / cropper.ratio;
	        var y = cropper.image.height / 2 - bgY / cropper.ratio;
	        var canvas = util.getCircleCanvas(cropper.image.width, cropper.image.height, x, y, actionVue.circleColor, cropper.ratio);
	        
	        // canvas的toDataURL是只能压缩jpg的，上传的图片是png的话，转成jpg， 统一用canvas.toDataURL('image/jpeg', 0.1)
	        cropper.image.onload = function() {
	        	document.getElementById("imageBox").style.backgroundPosition = imageBoxVue.currentBgX + " px" + imageBoxVue.currentBgY + "px";
	        }
	        cropper.image.src = canvas.toDataURL('image/jpeg', 1);
	        document.getElementById("imageBox").style.backgroundImage = 'url(' + cropper.image.src + ')';
		},
		autoResize:function() {
			var x = 0;
		    var y = 0;
		    var w = imageBoxVue.boxWidth;
		    var h = imageBoxVue.boxHeight;
		    
		    var imgW = cropper.image.width;
		    var imgH = cropper.image.height;
		    if (imgW > imgH) {
		        h = w * imgH / imgW;
		        y = (imageBoxVue.boxHeight - h) / 2;
		    }
		    else if (imgW < imgH) {
		        w = h * imgW / imgH;
		        x = (imageBoxVue.boxWidth - w) / 2;
		    }
		    util.setImageBoxBg(cropper.image.src, x, y, w, h);
		},
		zoomIn:function() {
			cropbox.ratio *= 1.1;
			util.setBackground();
		},
		zoomOut:function() {
			cropbox.ratio *= 0.9;
			util.setBackground();
		},
		textOk:function() {
			imageBoxVue.textArray = this.textArray;
		},
		addText:function() {
			 this.textArray.push({value:'text', color:'white', bgColor:'black', x:0, y:0});
		}
	}
})


util.getCircleCanvas = function(w, h, x, y, color, ratio) {
	var canvas = document.createElement("canvas");
    canvas.width = w;
    canvas.height = h;
    var context = canvas.getContext("2d");
    
    if (ratio) {
    	context.drawImage(cropper.image, 0, 0, cropper.image.width, cropper.image.height, 0, 0, cropper.image.width, cropper.image.height);
    }
    else {
    	ratio = 1;
    }
    
    context.lineWidth = 3;
    context.beginPath();
    context.setLineDash([4,2]);
    
    context.arc(x, y, 100 / ratio, 0, Math.PI * 2, true);
    context.strokeStyle = color;
    context.stroke();
    context.closePath();
    return canvas;
}

util.getCropCircle = function(color) {
	var w = imageBoxVue.boxWidth;
	var h = imageBoxVue.boxHeight;
    var canvas = util.getCircleCanvas(w, h, w/2, h/2, color);
    return canvas.toDataURL('image/png');
}

util.setBackground = function() {
	var w =  parseInt(cropper.image.width) * cropper.ratio;
    var h =  parseInt(cropper.image.height) * cropper.ratio;
    var x = (imageBoxVue.boxWidth - w) / 2;
    var y = (imageBoxVue.boxHeight - h) / 2;
    util.setImageBoxBg(cropper.image.src, x, y, w, h);
    imageBoxVue.setCurrentBgXY();
}

util.getDataURL = function() {
    var canvas = document.createElement("canvas");
    canvas.width = imageBoxVue.thumbWidth;
    canvas.height = imageBoxVue.thumbHeight;
    
    var bgStyle = document.getElementById("imageBox").style;
    var dim = bgStyle.backgroundPosition.split(' ');
    var size = bgStyle.backgroundSize.split(' ');
    
    var dx = parseInt(dim[0]) - imageBoxVue.boxWidth / 2 + canvas.width / 2;
    var dy = parseInt(dim[1]) - imageBoxVue.boxHeight / 2 + canvas.height / 2;
    var dw = parseInt(size[0]);
    var dh = parseInt(size[1]);
    var sh = parseInt(cropper.image.height);
    var sw = parseInt(cropper.image.width);
    
    var context = canvas.getContext("2d");
    context.drawImage(cropper.image, 0, 0, sw, sh, dx, dy, dw, dh);
    return canvas.toDataURL('image/jpeg');
}

util.setImageBoxBg = function(url, x, y, w, h) {
	var s = document.getElementById("imageBox").style;
    s.backgroundImage = 'url(' + url + ')';
    s.backgroundSize = w + 'px ' + h + 'px';
    s.backgroundPosition = x + 'px ' + y + 'px';
}

util.getDistance = function(p1, p2) {
    var x = p2.pageX - p1.pageX,
    y = p2.pageY - p1.pageY;
    return Math.sqrt((x * x) + (y * y));
}












const uploadFile = function() {
    return new Promise((resolve, reject) => {
    	var reader = new FileReader();
        reader.onload = function(e) {
            // alert(e.target.result);
            //EXIF js 可以读取图片的元信息  https://github.com/exif-js/exif-js
            var tmpImg = new Image();
            
            var canvas = document.createElement("canvas");
    		canvas.width = this.width;
    	    canvas.height = this.height;
    	    var context = canvas.getContext("2d");
            
            tmpImg.onload = function() {
            	EXIF.getData(this, function() {
        	        var orient = EXIF.getTag(this, 'Orientation');
    		        var drawWidth = tmpImg.width;
    		        var drawHeight = tmpImg.height;
    		        // iphone不同方向拍摄
    		        if (orient == 3 || orient == 6 || orient == 8) {
    		        	var o = util.changeOrient(tmpImg, canvas, context, orient)
    		        	drawWidth = o.drawWidth;
    		        	drawHeight = o.drawHeight;
    		        	context.drawImage(this, 0, 0, drawWidth, drawHeight);
    		        }
    		        else {
    		        	canvas.width = tmpImg.width;
    		            canvas.height = tmpImg.height;
    		            context.drawImage(this, 0, 0, drawWidth, drawHeight, 0, 0, drawWidth, drawHeight);
    		        }
    			    var newSrc = canvas.toDataURL('image/jpeg', 1);
    			    resolve(newSrc);
        		})
            }
            tmpImg.src = this.result; 
        }
        reader.readAsDataURL(event.currentTarget.files[0]);
    })
};

util.changeOrient = function(tmpImg, canvas, context, orientation) {
	 var degree = 0;
	 var drawWidth = tmpImg.width;
   var drawHeight = tmpImg.height;
   switch(orientation){
  	//iphone横屏拍摄，此时home键在左侧
   	 case 3:
        degree=180;
        drawWidth=-tmpImg.width;
        drawHeight=-tmpImg.height;
        break;
    //iphone竖屏拍摄，此时home键在下方(正常拿手机的方向)
    case 6:
        canvas.width=tmpImg.height;
        canvas.height=tmpImg.width; 
        degree=90;
        drawWidth=tmpImg.width;
        drawHeight=-tmpImg.height;
        break;
    //iphone竖屏拍摄，此时home键在上方
    case 8:
        canvas.width=tmpImg.height;
        canvas.height=tmpImg.width; 
        degree=270;
        drawWidth=-tmpImg.width;
        drawHeight=tmpImg.height;
        break;
	}
  context.rotate(degree*Math.PI/180);
  return {drawWidth:drawWidth, drawHeight:drawHeight}
}



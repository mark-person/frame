
var cropbox = function(options) {
	var obj = {
            state : {},
            ratio : 1,
            options : options,
            image : new Image()
        }
        
        obj.image.onload = function() {
            setBackground();
        };
    
    obj.image.src = options.imgSrc;
    return obj;
}





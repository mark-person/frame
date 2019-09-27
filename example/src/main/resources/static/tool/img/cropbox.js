
"use strict";
(function (factory) {
    if (typeof define === 'function' && define.amd) {
        define(['jquery'], factory);
    } else {
        factory(jQuery);
    }
}(function ($) {
    var cropbox = function(options, el){
        var el = el || $(options.imageBox);
        var obj = {
                state : {},
                ratio : 1,
                options : options,
                imageBox : el,
                image : new Image()
            }
            
	        obj.image.onload = function() {
	            setBackground();
	        };
        
        obj.image.src = options.imgSrc;
        return obj;
    };

    jQuery.fn.cropbox = function(options){
        return new cropbox(options, this);
    };
}));





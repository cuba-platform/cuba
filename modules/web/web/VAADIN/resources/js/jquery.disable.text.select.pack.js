/**
 * .disableTextSelect - Disable Text Select Plugin
 *
 * Version: 1.1
 * Updated: 2007-11-28
 * Requirements: jQuery (John Resig, http://www.jquery.com/)
 *
 * Used to stop users from selecting text
 *
 * Copyright (c) 2007 James Dempster (letssurf@gmail.com, http://www.jdempster.com/category/jquery/disabletextselect/)
 *
 * Dual licensed under the MIT (MIT-LICENSE.txt)
 * and GPL (GPL-LICENSE.txt) licenses.
 **/
(function ($) {
    // minimal optimization
    var falseFunction = function () {
        return false
    };
    if ($.browser.mozilla) {
        $.fn.disableTextSelect = function () {
            return this.each(function () {
                $(this).css({
                    "MozUserSelect": "none"
                })
            })
        };
        $.fn.enableTextSelect = function () {
            return this.each(function () {
                $(this).css({
                    "MozUserSelect": ""
                })
            })
        }
    } else {
        if ($.browser.msie) {
            $.fn.disableTextSelect = function () {
                return this.each(function () {
                    $(this).bind("selectstart.disableTextSelect", falseFunction)
                })
            };
            $.fn.enableTextSelect = function () {
                return this.each(function () {
                    $(this).unbind("selectstart.disableTextSelect")
                })
            }
        } else {
            $.fn.disableTextSelect = function () {
                return this.each(function () {
                    $(this).bind("selectstart", falseFunction)
                })
            };
            $.fn.enableTextSelect = function () {
                return this.each(function () {
                    $(this).unbind("selectstart");
                })
            }
        }
    }
})(jQuery);
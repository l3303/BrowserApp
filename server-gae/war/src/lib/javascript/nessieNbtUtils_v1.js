

var ScreenSizeMonitor = function(isSmallScreen, cssFileSmallScreen, cssFileBigScreen) {
	this.isSmallScreen = isSmallScreen;
	window.setInterval($.proxy(this.checkScreenSizeSwitch, this), 1000);
	this.cssTag = $('#css_nbt');
	this.cssSmall = $('<style id="small" type="text/css" />');
	this.cssBig = $('<style id="big" type="text/css" />');
	var self = this;
	
	$.ajax({
		url : cssFileSmallScreen,
		type : 'GET',
		dataType : 'text',
		success : function(cssFile, textStatus, jqXHR) {
			self.cssSmall.html(cssFile);
		}
	});
	$.ajax({
		url : cssFileBigScreen,
		type : 'GET',
		dataType : 'text',
		success : function(cssFile, textStatus, jqXHR) {
			self.cssBig.html(cssFile);
		}
	});
	
	$('head').append(isSmallScreen? this.cssSmall: this.cssBig);

};

ScreenSizeMonitor.prototype.checkScreenSizeSwitch = function() {
	'use strict';
	var self = this;
    var newSetting = window.innerWidth < 800;
    var title = $('.visible.detailPanel .header .title, .visible.panel > .header .title');
    if (window.innerWidth < 800) {
		$('.only10').hide();			      	
		$('.only7').show();
	}
	else {
		$('.only10').show();			      	
		$('.only7').hide();
	}
    if (newSetting !== this.isSmallScreen) {
    	this.isSmallScreen = newSetting;
    	if (this.isSmallScreen) {
    		$('.only10').hide();			      	
    		$('.only7').show();
    		this.cssBig.detach();
    		$('head').append(this.cssSmall);
//    		title.html(title.attr("origText"));
//    		PM.cutTitleText();
    	}
    	else {
    		$('.only10').show();			      	
    		$('.only7').hide();
    		this.cssSmall.detach();
    		$('head').append(this.cssBig);
//    		title.html(title.attr("origText"));
//    		PM.cutTitleText();
    	}
    }
};

function runtimePopup(message, popupafterclose) {
  var template = "<div data-role='popup' class='ui-content messagePopup' style='max-width:280px'>" 
      + "<a href='#' data-role='button' data-theme='g' data-icon='check' data-iconpos='notext' " 
      + " class='ui-btn-right closePopup'>Close</a> <span> " 
      + message + " </span> </div>";
  
  popupafterclose = popupafterclose ? popupafterclose : function () {};

  $.mobile.activePage.append(template).trigger("create");

  $.mobile.activePage.find(".closePopup").bind("tap", function (e) {
    $.mobile.activePage.find(".messagePopup").popup("close");
  });

  $.mobile.activePage.find(".messagePopup").popup().popup("open").bind({
    popupafterclose: function () {
      $(this).unbind("popupafterclose").remove();
      popupafterclose();
    }
  });
}
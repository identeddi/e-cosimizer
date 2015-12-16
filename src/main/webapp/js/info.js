var BookIt = BookIt || {};
var usr=null;
$( document ).on( "pagebeforeshow" , function(e, data) {
 if(usr ==null)
   {
      callurl="http://" + window.location.host + BookIt.Settings.signInUrl;
   	    $.ajax({
        type: 'POST',
        url: callurl,
        success: function (resp) {
        	  usr=resp;
        },
        error: function (e) {
        	usr=null;
        }
    });
   }
   
});


$(document).on("pagebeforeshow","#info-main",function(event){
		resp = $.ajax({
	    type: 'GET',
	    url: BookIt.Settings.getLastPowerMeasureURL,
	            success: function (resp) {
	            	console.log(resp);
	            				ko.mapping.fromJS(resp, lastMeasures);
	            		    		},
        error: function (e) {
        }
		});
	});



resp = $.ajax({
    type: 'GET',
    url: BookIt.Settings.getAllPowerMeasureURL,
    async: false
    
}).responseText;

app.allPowerMeasure = JSON.parse(resp);

//$(document).delegate("#page-signup", "pagebeforeshow", function () {
//    // Reset the signup form.
//    app.signupController.resetSignUpForm();
//});


$(document).on("pagebeforeshow","#page_power_list",function(event){
   	if(app.allPowerMeasure != null)
		{
   	// Add a new listview element
   		powerlist = $('#power_list')
   		powerlist.empty();
   		for(var i in app.allPowerMeasure)
   		{
   			powerlist.append('<li>' + toNiceDate(app.allPowerMeasure[i].measureDate) + "  " +app.allPowerMeasure[i].measureValue + " kWh" +   '</li>');
		}
   	    // Enhance new listview element
   		powerlist.listview('refresh');
   	    // Hide first listview element
   	    }
	});


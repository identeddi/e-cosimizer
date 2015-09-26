resp = $.ajax({
	type : 'GET',
	url : BookIt.Settings.getAllPowerMeasureURL,
	async : false

}).responseText;

app.allPowerMeasure = JSON.parse(resp);

//$(document).delegate("#page-signup", "pagebeforeshow", function () {
//    // Reset the signup form.
//    app.signupController.resetSignUpForm();
//});

$(document).on(
		"pagebeforeshow",
		"#page_power_verlauf",
		function(event) {
			if (app.allPowerMeasure != null) {
				// Add a new listview element
				powerlist = $('#power_list')
				powerlist.empty();
				for ( var i in app.allPowerMeasure) {
					powerlist.append('<li>'
							+ toNiceDate(app.allPowerMeasure[i].measureDate)
							+ "  " + app.allPowerMeasure[i].measureValue
							+ " kWh" + '</li>');
				}
				// Enhance new listview element
				powerlist.listview('refresh');
				// Hide first listview element
			}
		});
$(document).on("pagebeforeshow","#page_power_aktuell",function(event){
   	if(app.lastPowerMeasure != null && app.lastPowerMeasure.measureDate != null &&
			app.lastPowerMeasure.measureValue != null)
		{
			$("#power-last").text("Strom zuletzt abgelesen am " + toNiceDate(app.lastPowerMeasure.measureDate) + "  mit " +  app.lastPowerMeasure.measureValue + "kWh");
		}
	});

$(document).on("pagebeforeshow","#power_zaehler_erfassen",function(event){
    app.powerController.resetStromErfassenForm();
 		});

$(document).delegate("#power_zaehler_erfassen", "pagebeforecreate", function () {

    app.powerController.init();

    app.powerController.$btnSubmit.off("tap").on("tap", function () {
        app.powerController.onErfassenCommand();
        window.location.href = "power.html";
    });

});

resp = $.ajax({
    type: 'GET',
    url: BookIt.Settings.getLastPowerMeasureURL,
    async: false
});
app.lastPowerMeasure = JSON.parse(resp.responseText);

app.powerController = new BookIt.PowerController();


$(document).on("pagebeforeshow","#main",function(event){
   	if(app.lastPowerMeasure != null && app.lastPowerMeasure.measureDate != null &&
			app.lastPowerMeasure.measureValue != null)
		{
			$("#power-last").text("Strom zuletzt abgelesen am " + toNiceDate(app.lastPowerMeasure.measureDate) + "  mit " +  app.lastPowerMeasure.measureValue + "kWh");
		}
	});


$(document).on("pagebeforeshow","#dialog-zaehler_erfassen",function(event){
    app.powerController.resetStromErfassenForm();
 		});

$(document).delegate("#dialog-zaehler_erfassen", "pagebeforecreate", function () {

    app.powerController.init();

    app.powerController.$btnSubmit.off("tap").on("tap", function () {
        app.powerController.onErfassenCommand();
    });

});


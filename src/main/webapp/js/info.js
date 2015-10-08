var BookIt = BookIt || {};

$(document).on("pagebeforeshow","#info-main",function(event){
	app.powerController.updateLastPowerMeasure();
	if(app.lastPowerMeasure != null && app.lastPowerMeasure.measureDate != null &&
			app.lastPowerMeasure.measureValue != null)
		{
			$("#power-last").text("Strom zuletzt abgelesen am " + toNiceDate(app.lastPowerMeasure.measureDate) + "  mit " +  app.lastPowerMeasure.measureValue + "kWh");
		}
	});





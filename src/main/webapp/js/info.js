resp = $.ajax({
    type: 'GET',
    url: BookIt.Settings.getLastPowerMeasureURL,
    async: false
});
if(resp.status==200)
{
	app.lastPowerMeasure = JSON.parse(resp.responseText);
}
app.powerController = new BookIt.PowerController();


$(document).on("pagebeforeshow","#main",function(event){
   	if(app.lastPowerMeasure != null && app.lastPowerMeasure.measureDate != null &&
			app.lastPowerMeasure.measureValue != null)
		{
			$("#power-last").text("Strom zuletzt abgelesen am " + toNiceDate(app.lastPowerMeasure.measureDate) + "  mit " +  app.lastPowerMeasure.measureValue + "kWh");
		}
	});





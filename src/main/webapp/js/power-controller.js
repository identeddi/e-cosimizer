var BookIt = BookIt || {};

BookIt.PowerController = function () {
	
};

BookIt.PowerController.prototype.init = function () {
    this.$dialogzaehlerErfassen = $("#power_zaehler_erfassen");
    this.$btnSubmit = $("#power_zaehler_erfassen_btn-submit", this.$dialogzaehlerErfassen);
    this.$zaehlerStand = $("#zaehler_stand", this.$dialogzaehlerErfassen);
    this.$zaehlerDatum = $("#zaehler_datum", this.$dialogzaehlerErfassen);
    this.afterErfassenMsg=null;

    
};

BookIt.PowerController.prototype.resetStromErfassenForm = function () {

	this.afterErfassenMsg=null;
};


BookIt.PowerController.prototype.onErfassenCommand = function () {

	    var me = this, zaehlerStand = me.$zaehlerStand.val().trim(), zaehlerDatum = me.$zaehlerDatum
			.val().trim(), invalidInput = false, invisibleStyle = "bi-invisible", invalidInputStyle = "bi-invalid-input";

	callurl = "http://" + window.location.host
			+ BookIt.Settings.measurePowerURL + "?" + "measureValue="
			+ zaehlerStand + "&measureDate=" + zaehlerDatum

	$.ajax({
		type : 'POST',
		url : callurl,
		success : function() {
			runtimePopup("Daten erfolgreich verbucht",function(){$.mobile.changePage("#info-main")});
			return;
		},
		error : function(e) {
			afterErfassenMsg = e.responseText;
			runtimePopup(afterErfassenMsg);
			console.log(e.message);
			return;
		}
	});
  
};

$(document).on(
		"pagebeforeshow",
		"#page_power_verlauf",
		function(event) {

resp = $.ajax({
	type : 'GET',
	url : BookIt.Settings.getAllPowerMeasureURL,
	success: function (resp) {
				// Add a new listview element
				powerlist = $('#power_list')
				powerlist.empty();
				for ( var i in resp) {
					powerlist.append('<li>'
						+ "<p>Ablesedatum: " + toNiceDate(resp[i].measureDate) + "</p>"
						+"<p>Zählerstand: <strong>" + resp[i].measureValue + " kWh</strong></p>"
							+ '</li>');
				}
				// Enhance new listview element
				powerlist.listview('refresh');
				// Hide first listview element
	}

})

		});


$(document).on("pagebeforeshow","#page_power_aktuell",function(event){

	resp = $.ajax({
	    type: 'GET',
	    url: BookIt.Settings.getLastPowerMeasureURL,
	            success: function (resp) {
	            	console.log(resp);
if(resp != null && resp.measureDate != null &&
			resp.measureValue != null)
		{
			$("#page_power_aktuell-last").text("Strom zuletzt abgelesen am " + toNiceDate(resp.measureDate) + "  mit " +  resp.measureValue + "kWh");
		}
	            		    		},
        error: function (e) {
        }
	});

	});

$(document).on("pagebeforeshow","#power_zaehler_erfassen",function(event){
    app.powerController.resetStromErfassenForm();
 		});

$(document).delegate("#power_zaehler_erfassen", "pagebeforecreate", function () {

    app.powerController.init();

    app.powerController.$btnSubmit.off("tap").on("tap", function () {
        app.powerController.onErfassenCommand();
    });

});

$(document).on('click', '#power_supply_update', function(e){
	$.mobile.loading( "show", {
	    text: "Angebote werden abgerufen",
	    textVisible: true
	});

	var powerSupply = $("#page_power_vertrag");
	var powerSupplyZipcode = $("#zipcode_supply", powerSupply);
	var powerSupplyConsumption = $("#consumption_supply", powerSupply);

    var powerSupplyVal = powerSupplyConsumption.val().trim();
    var powerSupplyZipcode = powerSupplyZipcode.val().trim();
 $.ajax({
        type: 'GET',
        url: BookIt.Settings.getAllPowerSuppliesURL,
        data: {zipcode: powerSupplyZipcode, consumption:powerSupplyVal},
        success: function (resp) {
			// Add a new listview element
			powerSupplylist = $('#power_supply_list')
			powerSupplylist.empty();
			if(resp.length == 0)
			{
							runtimePopup("Es wurden keine Angebote zu den eingebenen Daten gefunden.");

			}
			else
			{

			
				powerSupplylist.append('<li>'
				 + "<a href='" + resp[resp.length-1].url + " ' target='_blank'>"
					+ "<p><strong>" + resp[resp.length-1].supply +"</strong></p>"
					+ "<p>" + resp[resp.length-1].provider +"</p>"
					+ "<p>Bewertungen: " + resp[resp.length-1].ratingCount+"</p>"
					+ "<p>Weiterempfehlung: " + resp[resp.length-1].rating + "% </p>"
					+ "<strong>" + resp[resp.length-1].price+ '€ </strong>'
					+ "<a/>"
					+ '</li>');

				for ( var i in resp) {
					if(i<resp.length-1)
						{
					powerSupplylist.append('<li>'
						 + "<a href='" + resp[i].url + " ' target='_blank'>"
							+ "<strong>" + resp[i].supply +"</strong>"
							+ "<p>" + resp[i].provider +"</p>"
							+ "<p>Bewertungen: " + resp[i].ratingCount+"</p>"
							+ "<p>Weiterempfehlung: " + resp[i].rating + "% </p>"
							+ "<strong>" + resp[i].price+ '€ </strong>'
							+ "<a/>"
							+ '</li>');
						}
				}
			}
			// Enhance new listview element
			powerSupplylist.listview('refresh');
			$.mobile.loading( "hide" );
		},
        error: function (e) {
			$.mobile.loading( "hide" );
        }
    });
});
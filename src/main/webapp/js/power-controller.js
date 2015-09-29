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
	$('#measureDate').datepicker({
        dateFormat: 'dd.mm.yyyy',
        altField: '#thealtdate',
        altFormat: 'yy-mm-dd'
    });
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
			runtimePopup("Daten erfolgreich verbucht",function(){$.mobile.changePage("power.html")});
			return;
		},
		error : function(e) {
			afterErfassenMsg = e.responseText;
			runtimePopup(afterErfassenMsg)
			console.log(e.message);
			return;
		}
	});
  
};
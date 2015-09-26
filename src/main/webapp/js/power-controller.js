var BookIt = BookIt || {};

BookIt.PowerController = function () {
};

BookIt.PowerController.prototype.init = function () {
    this.$dialogzaehlerErfassen = $("#dialog-zaehler_erfassen");
    this.$btnSubmit = $("#btn-submit", this.$dialogzaehlerErfassen);
    this.$zaehlerStand = $("#zaehler_stand", this.$dialogzaehlerErfassen);
    this.$zaehlerDatum = $("#zaehler_datum", this.$dialogzaehlerErfassen);
};

BookIt.PowerController.prototype.resetStromErfassenForm = function () {
	$('#measureDate').datepicker({
        dateFormat: 'dd.mm.yyyy',
        altField: '#thealtdate',
        altFormat: 'yy-mm-dd'
    });

/*    callurl="http://" + window.location.host + BookIt.Settings.deleteSessionUrl
    
    $.ajax({
        type: 'DELETE',
        url: callurl,
        success: function (resp) {
            return;
        },
        error: function (e) {
        	return;
        }
    });
*/};


BookIt.PowerController.prototype.onErfassenCommand = function () {

    var me = this,
    zaehlerStand = me.$zaehlerStand.val().trim(),
    zaehlerDatum = me.$zaehlerDatum.val().trim(),
    invalidInput = false,
    invisibleStyle = "bi-invisible",
    invalidInputStyle = "bi-invalid-input";

    
    callurl="http://" + window.location.host + BookIt.Settings.measurePowerURL + "?" + "measureValue=" + zaehlerStand + "&measureDate=" + zaehlerDatum
    
    $.ajax({
        type: 'POST',
        url: callurl,
        success: function (resp) {
       	$.mobile.changePange("info.html")
           return;
        },
        error: function (e) {
            console.log(e.message);
            return;
        }
    });
  
};
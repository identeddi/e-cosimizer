var BookIt = BookIt || {};

BookIt.SettingsController = function() {

};

BookIt.SettingsController.prototype.init = function() {

};

BookIt.SettingsController.prototype.resetStromErfassenForm = function() {

	this.afterErfassenMsg = null;
};

BookIt.SettingsController.prototype.onErfassenCommand = function() {

	var me = this, zaehlerStand = me.$zaehlerStand.val().trim(), zaehlerDatum = me.$zaehlerDatum
			.val().trim(), invalidInput = false, invisibleStyle = "bi-invisible", invalidInputStyle = "bi-invalid-input";

	var tep = $.datepicker.parseDate('dd.mm.yy', zaehlerDatum);
	zaehlerDatum = tep.toJSON();
	callurl = "http://" + window.location.host
			+ BookIt.Settings.measurePowerURL.replace("%TYPEID%", powerType)
			+ "?" + "measureValue=" + zaehlerStand + "&measureDate="
			+ zaehlerDatum

	$.ajax({
		type : 'POST',
		url : callurl,
		success : function() {
			runtimePopup("Daten erfolgreich verbucht", function() {
				$.mobile.changePage("#info-main")
			});
			return;
		},
		error : function(e) {
			var afterErfassenMsg = e.responseText;
			if (afterErfassenMsg.length == 0) {
				afterErfassenMsg = "Ein unerwarteter Fehler ist aufgetreten."
			}
			runtimePopup(afterErfassenMsg);
			console.log(afterErfassenMsg);
			return;
		}
	});

};

$(document).on('click', '#save_general_settings', function(e) {

	jsobj = ko.mapping.toJS(userModel);
	var jsonString = JSON.stringify(jsobj);
	$.ajax({
		type : 'PUT',
		url : BookIt.Settings.getUser,
		data : jsonString,
		contentType : "application/json",
		success : function(resp) {
			runtimePopup("Daten erfolgreich verbucht");
		},
		error : function(e) {
			runtimePopup("Daten konnten nicht gespeichert werden");
		}
	});

});

$(document).on("pagecreate", "#settings_general", function(event) {

});

$(document).delegate("#settings_general", "pagebeforeshow", function() {
	$.ajax({
		type : 'GET',
		url : BookIt.Settings.getUser,
		success : function(resp) {
			ko.mapping.fromJS(resp, userModel);

		},
		error : function(e) {
			$.mobile.loading("hide");
		}
	});

});

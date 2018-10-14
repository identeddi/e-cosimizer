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
	callurl = "https://" + window.location.host
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
			runtimePopup("Daten erfolgreich verbucht", function() {
				$.mobile.changePage("#settings_powertype");
			});
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

$(document).delegate("#settings_powertype", "pagebeforeshow", function() {
	$.ajax({
		type : 'GET',
		url : BookIt.Settings.powerMeasureTypes,
		success : function(resp) {
			ko.mapping.fromJS(resp, powerMeasureTypesModel);
		},
		error : function(e) {
			$.mobile.loading("hide");
		}
	});

});
$(document).on('click', '.delete_powertype', function(e) {
	var id = this.id;
	jsobj = ko.mapping.toJS(powerMeasureModel);
	var jsonString = JSON.stringify(jsobj);
	$.ajax({
		type : 'DELETE',
		url : BookIt.Settings.powerMeasureType + "/" + id,
		data : jsonString,
		contentType : "application/json",
		success : function() {
			runtimePopup("Daten erfolgreich gelöscht", function() {
				$.mobile.changePage("#settings_powertype");
			});
			return;
		},
		error : function(e) {
			var afterErfassenMsg = e.responseText;
			if (afterErfassenMsg.length == 0) {
				afterErfassenMsg = "Ein unerwarteter Fehler ist aufgetreten.";
			}
			runtimePopup(afterErfassenMsg);
			console.log(afterErfassenMsg);
			return;
		}
	});

});

$(document).on('click', '.edit_powertype', function(e) {
	var id = this.id;
	resp = $.ajax({
		type : 'GET',
		url : BookIt.Settings.powerMeasureType + "/" + id,
		success : function(resp) {
			ko.mapping.fromJS(resp, powerTypeModel);
			console.log("enabled2: " + powerTypeModel.enabled())
		},
		error : function(e) {
		}
	});
	$.mobile.changePage("#power_typ_erfassen");
});

$(document).on('click', '#neuen_typ_erfassen', function(e) {

	$.mobile.changePage("#power_typ_erfassen");
});

$(document).on('click', '#power_type_erfassen_btn-submit', function(e) {

	jsobj = ko.mapping.toJS(powerTypeModel);
	// delete jsobj.entryNotificationObject;
	delete jsobj.periodocNotificationChoices;
	console.log("enabled3: " + powerTypeModel.enabled())
	var jsonString = JSON.stringify(jsobj);
	resp = $.ajax({
		type : 'PUT',
		url : BookIt.Settings.powerMeasureType,
		data : jsonString,
		contentType : "application/json",
		success : function(resp) {
//			runtimePopup("Daten erfolgreich verbucht", function() {
				$.mobile.changePage("#settings_powertype");
//			});
		},
		error : function(e) {
			runtimePopup("Daten konnten nicht gespeichert werden");
		}
	});
});

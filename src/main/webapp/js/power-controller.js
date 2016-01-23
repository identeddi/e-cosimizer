var BookIt = BookIt || {};

BookIt.PowerController = function() {

};

BookIt.PowerController.prototype.onErfassenCommand = function() {

	var me = this, zaehlerStand = me.$zaehlerStand.val().trim(), zaehlerDatum = me.$zaehlerDatum
			.val().trim(), invalidInput = false, invisibleStyle = "bi-invisible", invalidInputStyle = "bi-invalid-input";

	var tep = $.datepicker.parseDate('dd.mm.yy', zaehlerDatum);
	zaehlerDatum = tep.toJSON();
	callurl = "http://"
			+ window.location.host
			+ BookIt.Settings.measurePowerURL.replace("%TYPEID%",
					localStorage.powerType) + "?" + "measureValue="
			+ zaehlerStand + "&measureDate=" + zaehlerDatum

	$.ajax({
		type : 'POST',
		url : callurl,
		success : function() {
			runtimePopup("Daten erfolgreich verbucht", function() {
				$.mobile.changePage("#page_power_verlauf");
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

};

function drawChart(jsonData) {
	if (typeof google != 'undefined') {
		var data = new google.visualization.DataTable(jsonData);

		var options = {
			title : '5-Jahresverbrauch',
			legend : {
				position : 'bottom'
			}
		};

		var chart = new google.visualization.LineChart(document
				.getElementById('line'));

		chart.draw(data, options);
	}
}

$(document).on(
		"pagebeforeshow",
		"#page_power_verlauf",
		function(event) {
			resp = $.ajax({
				type : 'GET',
				url : BookIt.Settings.getAllPowerMeasureGraph.replace(
						"%TYPEID%", localStorage.powerType),
				success : function(resp) {
					drawChart(resp);
				}
			}).responseText;

			resp = $.ajax({
				type : 'GET',
				url : BookIt.Settings.measurePowerHistoryURL.replace(
						"%TYPEID%", localStorage.powerType),
				success : function(resp) {
					// drawChart();
					ko.mapping.fromJS(resp, historyMeasures);

					// Add a new listview element
					// powerlist = $('#power_list');
					// // Enhance new listview element
					// powerlist.listview('refresh');
					// Hide first listview element
				}

			})

		});

$(document).on(
		"pagebeforeshow",
		"#page_power_aktuell",
		function(event) {

			resp = $.ajax({
				type : 'GET',
				url : BookIt.Settings.measurePowerURL.replace("%TYPEID%",
						localStorage.powerType),
				success : function(resp) {
					console.log(resp);
					ko.mapping.fromJS(resp, lastMeasureModel);
					console.log("Reading for page_power_aktuell: "
							+ lastMeasureModel.measureValue());

				},
				error : function(e) {
				}
			});

		});

$(document).on("pagebeforeshow", "#power_zaehler_erfassen", function(event) {

});

$(document)
		.on(
				'click',
				'#power_zaehler_erfassen_btn-submit',
				function(e) {
					jsobj = ko.mapping.toJS(powerMeasureModel);
					var jsonString = JSON.stringify(jsobj);
					$
							.ajax({
								type : 'POST',
								url : BookIt.Settings.measurePowerURL.replace(
										"%TYPEID%", localStorage.powerType),
								data : jsonString,
								contentType : "application/json",
								success : function() {
									runtimePopup(
											"Daten erfolgreich verbucht",
											function() {
												$.mobile
														.changePage("#page_power_verlauf");
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
$(document)
		.on(
				'click',
				'.delete_measure',
				function(e) {
					var id = this.id;
					jsobj = ko.mapping.toJS(powerMeasureModel);
					var jsonString = JSON.stringify(jsobj);
					$
							.ajax({
								type : 'DELETE',
								url : BookIt.Settings.measurePowerURL.replace(
										"%TYPEID%", localStorage.powerType)
										+ "/" + id,
								data : jsonString,
								contentType : "application/json",
								success : function() {
									runtimePopup(
											"Daten erfolgreich gelöscht",
											function() {
												$.mobile
														.changePage("#page_power_verlauf");
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

$(document).on('click', '.edit_measure', function(e) {
	var id = this.id;
	resp = $.ajax({
		type : 'GET',
		url : BookIt.Settings.getMeasureByIDURL + id,
		success : function(resp) {
			ko.mapping.fromJS(resp, powerMeasureModel);
		},
		error : function(e) {
		}
	});
	$.mobile.changePage("#power_zaehler_erfassen");
});
$(document).on('click', '#neue_zaehler_erfassen', function(e) {
	PowerMeasureModel
	newPowerMeasureModel = new PowerMeasureModel();
	newPowerMeasureModel.measureDate = new Date();
	newPowerMeasureModel.measureDate.setHours(0, 0, 0, 0);
	ko.mapping.fromJS(newPowerMeasureModel, powerMeasureModel);

	$.mobile.changePage("#power_zaehler_erfassen");
});

$(document).delegate(
		"#page_power_supply",
		"pagebeforeshow",
		function() {
			$.ajax({
				type : 'GET',
				url : BookIt.Settings.getSupplySettings.replace("%TYPEID%",
						localStorage.powerType),
				success : function(resp) {
					ko.mapping.fromJS(resp, powerSupplyModel);

				},
				error : function(e) {
					$.mobile.loading("hide");
				}
			});

		});

$(document)
		.on(
				'click',
				'#power_supply_update',
				function(e) {
					$.mobile.loading("show", {
						text : "Angebote werden abgerufen",
						textVisible : true
					});
					var powerSupplyVal = powerSupplyModel
							.estimatedConsumption();
					var powerSupplyZipcode = powerSupplyModel.zipcode();
					$
							.ajax({
								type : 'GET',
								url : BookIt.Settings.getAllPowerSuppliesURL
										.replace("%TYPEID%",
												localStorage.powerType),
								data : {
									zipcode : powerSupplyZipcode,
									consumption : powerSupplyVal
								},
								success : function(resp) {
									// Add a new listview element
									powerSupplylist = $('#power_supply_list');
									powerSupplylist.empty();
									if (resp.length == 0) {
										runtimePopup("Es wurden keine Angebote zu den eingebenen Daten gefunden.");

									} else {

										powerSupplylist
												.append('<li>'
														+ "<a href='"
														+ resp[resp.length - 1].url
														+ " ' target='_blank'>"
														+ "<p><strong>"
														+ resp[resp.length - 1].supply
														+ "</strong></p>"
														+ "<p>"
														+ resp[resp.length - 1].provider
														+ "</p>"
														+ "<p>Bewertungen: "
														+ resp[resp.length - 1].ratingCount
														+ "</p>"
														+ "<p>Weiterempfehlung: "
														+ resp[resp.length - 1].rating
														+ "% </p>"
														+ "<strong>"
														+ resp[resp.length - 1].price
														+ '€ </strong>'
														+ "<a/>" + '</li>');

										for ( var i in resp) {
											if (i < resp.length - 1) {
												powerSupplylist
														.append('<li>'
																+ "<a href='"
																+ resp[i].url
																+ " ' target='_blank'>"
																+ "<strong>"
																+ resp[i].supply
																+ "</strong>"
																+ "<p>"
																+ resp[i].provider
																+ "</p>"
																+ "<p>Bewertungen: "
																+ resp[i].ratingCount
																+ "</p>"
																+ "<p>Weiterempfehlung: "
																+ resp[i].rating
																+ "% </p>"
																+ "<strong>"
																+ resp[i].price
																+ '€ </strong>'
																+ "<a/>"
																+ '</li>');
											}
										}
									}
									// Enhance new listview element
									powerSupplylist.listview('refresh');
									$.mobile.loading("hide");
								},
								error : function(e) {
									$.mobile.loading("hide");
								}
							});
				});

$(document).delegate(
		"#page_power_contract",
		"pagebeforeshow",
		function() {
			$.ajax({
				type : 'GET',
				url : BookIt.Settings.powerContract.replace("%TYPEID%",
						localStorage.powerType),
				success : function(resp) {
					if (resp.dueDate != null) {
						resp.dueDate = new Date(resp.dueDate);
					}
					ko.mapping.fromJS(resp, contractModel);
				},
				error : function(e) {
					$.mobile.loading("hide");
				}
			});

		});

$(document).on(
		'click',
		'#save_contract_settings',
		function(e) {

			jsobj = ko.mapping.toJS(contractModel);
			var jsonString = JSON.stringify(jsobj);
			$.ajax({
				type : 'PUT',
				url : BookIt.Settings.powerContract.replace("%TYPEID%",
						localStorage.powerType),
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

var BookIt = BookIt || {};

BookIt.PowerController = function() {

};

BookIt.PowerController.prototype.onErfassenCommand = function() {

	var me = this, zaehlerStand = me.$zaehlerStand.val().trim(), zaehlerDatum = me.$zaehlerDatum
			.val().trim(), invalidInput = false, invisibleStyle = "bi-invisible", invalidInputStyle = "bi-invalid-input";

	var tep = $.datepicker.parseDate('dd.mm.yy', zaehlerDatum);
	zaehlerDatum = tep.toJSON();
	callurl = "https://"
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
	// Get context with jQuery - using jQuery's .get() method.
	var ctx = $("#myChart").get(0).getContext("2d");
	// var data = {
	// labels : [ "January", "February", "March", "April", "May", "June",
	// "July", "August", "September", "October", "November",
	// "December" ],
	// datasets : [ {
	// label : "My First dataset",
	// fillColor : "rgba(220,220,220,0.5)",
	// strokeColor : "rgba(220,220,220,0.8)",
	// highlightFill : "rgba(220,220,220,0.75)",
	// highlightStroke : "rgba(220,220,220,1)",
	// data : [ 65, 59, 80, 81, 56, 55, 40, 59, 80, 81, 56, 55, 40 ]
	// }, {
	// label : "My Second dataset",
	// fillColor : "rgba(151,187,205,0.5)",
	// strokeColor : "rgba(151,187,205,0.8)",
	// highlightFill : "rgba(151,187,205,0.75)",
	// highlightStroke : "rgba(151,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// }, {
	// label : "My Third dataset",
	// fillColor : "rgba(251,187,205,0.5)",
	// strokeColor : "rgba(251,187,205,0.8)",
	// highlightFill : "rgba(251,187,205,0.75)",
	// highlightStroke : "rgba(251,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// }, {
	// label : "My Fourth dataset",
	// fillColor : "rgba(51,187,205,0.5)",
	// strokeColor : "rgba(51,187,205,0.8)",
	// highlightFill : "rgba(51,187,205,0.75)",
	// highlightStroke : "rgba(51,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// } ]
	// };
	var options = {
		// Boolean - Whether the scale should start at zero, or an order of
		// magnitude down from the lowest value
		scaleBeginAtZero : true,

		// Boolean - Whether grid lines are shown across the chart
		scaleShowGridLines : true,

		// String - Colour of the grid lines
		scaleGridLineColor : "rgba(0,0,0,.05)",

		// Number - Width of the grid lines
		scaleGridLineWidth : 1,

		// Boolean - Whether to show horizontal lines (except X axis)
		scaleShowHorizontalLines : true,

		// Boolean - Whether to show vertical lines (except Y axis)
		scaleShowVerticalLines : true,

		// Boolean - If there is a stroke on each bar
		barShowStroke : true,

		// Number - Pixel width of the bar stroke
		barStrokeWidth : 1,

		// Number - Spacing between each of the X value sets
		barValueSpacing : 2,

		// Number - Spacing between data sets within X values
		barDatasetSpacing : 1,
		// Boolean - whether or not the chart should be responsive and resize
		// when the browser does.
		responsive : true,

		// String - A legend template
		legendTemplate : "<ul class=\"<%=name.toLowerCase()%>-legend\"><% for (var i=0; i<datasets.length; i++){%><li><span style=\"background-color:<%=datasets[i].strokeColor%>\"><%if(datasets[i].label){%><%=datasets[i].label%><%}%></span></li><%}%></ul>"

	};
	// This will get the first returned node in the jQuery collection.
	var myBarChart = new Chart(ctx).Bar(jsonData, options);
	// then you just need to generate the legend
	var legend = myBarChart.generateLegend();

	// and append it to your page somewhere
	$('#myChartLegend').html(legend);
	/*
	 * if (typeof google != 'undefined') { var data = new
	 * google.visualization.DataTable(jsonData);
	 * 
	 * var options = { title : '5-Jahresverbrauch', legend : { position :
	 * 'bottom' } };
	 * 
	 * var chart = new google.visualization.LineChart(document
	 * .getElementById('line'));
	 * 
	 * chart.draw(data, options); }
	 */
}

$(document).on(
		"pagebeforeshow",
		"#page_power_verlauf",
		function(event) {
			resp = $.ajax({
				type : 'GET',
				url : BookIt.Settings.getAllPowerMeasureGraph.replace(
						"%TYPEID%", localStorage.powerType),
				contentType : "application/json",
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

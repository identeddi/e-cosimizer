var BookIt = BookIt || {};

// Begin boilerplate code generated with Cordova project.
$.ajaxSetup({
	xhrFields : {
		withCredentials : true
	}
})

var app = {
	// Application Constructor
	initialize : function() {
		this.bindEvents();
	},
	// Bind Event Listeners
	//
	// Bind any events that are required on startup. Common events are:
	// 'load', 'deviceready', 'offline', and 'online'.
	bindEvents : function() {
		document.addEventListener('deviceready', this.onDeviceReady, false);
	},
	// deviceready Event Handler
	//
	// The scope of 'this' is the event. In order to call the 'receivedEvent'
	// function, we must explicitly call 'app.receivedEvent(...);'
	onDeviceReady : function() {
		app.receivedEvent('deviceready');
	},
	// Update DOM on a Received Event
	receivedEvent : function(id) {

	}
};

function UserModel() {
	self = this;
	self.firstName = '';
	self.lastName = '';
	self.email = '';
	self.zipcode = '';
	self.username = '';
}

function PanelModel() {
	self = this;
	self.fullUserName = '';
}

function MenuModel() {
	self = this;
	self.caption = '';
	self.url = '';
}

function PowerSupplyModel() {
	self = this;
	self.zipcode = '';
	self.estimatedConsumption = '';
	self.passedConsumption = '';
}
function ContractModel() {
	self = this;
	self.providerName = '';
	self.contractName = '';
	self.dueDate = '';
	self.cancellationPeriod = 0;
}
function LastMeasureModel() {
	self = this;
	self.measureDate = '';

	self.nextMeasureDate = '';

	self.measureValue = '';
	self.dailyConsumption = '';

	self.dataType = '';
}

var userModel;
var contractModel;
var powerSupplyModel;
var powerType = 0;
var lastMeasureModel;
var panelitems;

ko.bindingHandlers.datepicker = {
	init : function(element, valueAccessor, allBindingsAccessor) {
		// initialize datepicker with some optional options
		var options = allBindingsAccessor().datepickerOptions || {};
		$(element).datepicker(options);

		// handle the field changing
		ko.utils.registerEventHandler(element, "change", function() {
			var observable = valueAccessor();
			observable($(element).datepicker("getDate"));
		});

		// handle disposal (if KO removes by the template binding)
		ko.utils.domNodeDisposal.addDisposeCallback(element, function() {
			$(element).datepicker("destroy");
		});

	},
	// update the control when the view model changes
	update : function(element, valueAccessor) {
		var value = ko.utils.unwrapObservable(valueAccessor()), current = $(
				element).datepicker("getDate");

		if (value - current !== 0) {
			$(element).datepicker("setDate", value);
		}
	}
};
if (typeof google != 'undefined') {
	google.load('visualization', '1.1', {
		'packages' : [ 'corechart' ]
	});
}

app.initialize();
jQuery(function($) {

	if (typeof $.datepicker != 'undefined') {
		$.datepicker.regional['de'] = {
			clearText : 'löschen',
			clearStatus : 'aktuelles Datum löschen',
			closeText : 'schließen',
			closeStatus : 'ohne Änderungen schließen',
			prevText : '<zurück',
			prevStatus : 'letzten Monat zeigen',
			nextText : 'Vor>',
			nextStatus : 'nächsten Monat zeigen',
			changeMonth : true,
			changeYear : true,
			currentText : 'heute',
			currentStatus : '',
			monthNames : [ 'Januar', 'Februar', 'März', 'April', 'Mai', 'Juni',
					'Juli', 'August', 'September', 'Oktober', 'November',
					'Dezember' ],
			monthNamesShort : [ 'Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun',
					'Jul', 'Aug', 'Sep', 'Okt', 'Nov', 'Dez' ],
			monthStatus : 'anderen Monat anzeigen',
			yearStatus : 'anderes Jahr anzeigen',
			weekHeader : 'Wo',
			weekStatus : 'Woche des Monats',
			dayNames : [ 'Sonntag', 'Montag', 'Dienstag', 'Mittwoch',
					'Donnerstag', 'Freitag', 'Samstag' ],
			dayNamesShort : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
			dayNamesMin : [ 'So', 'Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa' ],
			dayStatus : 'Setze DD als ersten Wochentag',
			dateStatus : 'Wähle D, M d',
			dateFormat : 'dd.mm.yy',
			firstDay : 1,
			initStatus : 'Wähle ein Datum',
			isRTL : false
		};

		$.datepicker.setDefaults($.datepicker.regional['de']);
	}
	$.mobile.changePage.defaults.allowSamePageTransition = true;
	$.mobile.defaultPageTransition = "slide";

	userModel = ko.mapping.fromJS(new UserModel());
	contractModel = ko.mapping.fromJS(new ContractModel());
	powerSupplyModel = ko.mapping.fromJS(new PowerSupplyModel());
	panelModel = ko.mapping.fromJS(new PanelModel());
	ko.applyBindings(userModel, $('#settings_general')[0]);
	panelitems = ko.mapping.fromJS([]);
	lastMeasures = ko.mapping.fromJS([]);
	historyMeasures = ko.mapping.fromJS([]);
	lastMeasureModel = ko.mapping.fromJS(new LastMeasureModel());

	ko.applyBindings(contractModel, $('#page_power_contract')[0]);
	ko.applyBindings(powerSupplyModel, $('#page_power_supply')[0]);
	ko.applyBindings(panelModel, $('#nav-panel-profile')[0]);
	ko.applyBindings(panelitems, $('#nav-panel-list')[0]);
	ko.applyBindings(lastMeasureModel, $('#page_power_aktuell')[0]);
	ko.applyBindings(lastMeasures, $('#info-main')[0]);
	ko.applyBindings(historyMeasures, $('#page_power_verlauf')[0]);

	app.signupController = new BookIt.SignUpController();
	app.signinController = new BookIt.SignInController();
	app.powerController = new BookIt.PowerController();
	$.ajax({
		type : 'GET',
		url : BookIt.Settings.getUser,
		success : function(resp) {
			panelModel.fullUserName(resp.firstName + ' ' + resp.lastName);

		},
		error : function(e) {
			$.mobile.loading("hide");
		}
	});

});

$(document).on('click', '#button-page-signin', function(e) {
	callurl = "http://" + window.location.host + BookIt.Settings.signInUrl

	// If the return is 401, refresh the page to request new details.

	callurl = "http://" + window.location.host + BookIt.Settings.signInUrl
	resp = $.ajax({
		type : 'POST',
		url : callurl,
		username : 'reset',
		password : 'reset',
		async : false
	});
	$.mobile.changePage("#page-signin");
	//	
	// resp = $.ajax({
	// type : 'POST',
	// url : callurl,
	// success : function(resp) {
	// $.mobile.changePage("#page-signin");
	// },
	// error : function(e) {
	// runtimePopup(e)
	// }
	// });

});

$(document).on("click", "#nav-panel-list li a", function() {
	var powerMeasureType = $(this).attr('id');
	if (powerMeasureType > 0) {
		powerType = powerMeasureType;
	}
	else if(powerMeasureType = -3)
	{
			callurl = "http://" + window.location.host + "/rest/login/logout";
	resp = $.ajax({
		type : 'POST',
		url : callurl,
		async : false
	});

	callurl = "http://" + window.location.host + "/rest/login/login";
	resp = $.ajax({
		type : 'POST',
		url : callurl,
		headers : {
			"Authorization" : "Basic " + btoa("reset:reset")
		},
		async : false
	});

	}
	var href = $(this).attr('href');
	var e = 2;
	// your code
});

$(document).on("panelbeforeopen", "#nav-panel", function(event) {
	callurl = "http://" + window.location.host + BookIt.Settings.getMenuList;
	$.ajax({
		type : 'GET',
		url : callurl,
		success : function(resp) {
			ko.mapping.fromJS(resp, panelitems);
			$('#nav-panel-list').listview('refresh');
		},
		error : function(e) {
			usr = null;
		}
	});
});

$(document).ajaxError(function(event, jqxhr, settings, thrownError) {
	if (jqxhr.status == 500) {
		$.mobile.changePage("#info-main");
	} else if (jqxhr.status == 403) {
		console.log($.mobile.activePage);
		if($.mobile.activePage != undefined)
		{
			pageName = $.mobile.activePage[0].id;
			console.log(pageName);
			if(pageName != 'page-signin')
			{
//				runtimePopup("Sie sind nicht eingeloggt und werden deshalb automatisch zur Login-Seite navigiert.");
				$.mobile.changePage("#page-signin");
			}
		}
	}
	console.log("Triggered ajaxError handler.");
});

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
	self.firstName = 'Test';
	self.lastName = '';
	self.email = '';
	self.zipcode = '';
	self.username = '';
}
function ContractModel() {
	self = this;
	self.providerName = '';
	self.contractName = '';
	self.dueDate = '';
	self.cancellationPeriod = 14;
}

var userModel;
var contractModel;
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
app.initialize();
jQuery(function($) {

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
		monthNamesShort : [ 'Jan', 'Feb', 'Mär', 'Apr', 'Mai', 'Jun', 'Jul',
				'Aug', 'Sep', 'Okt', 'Nov', 'Dez' ],
		monthStatus : 'anderen Monat anzeigen',
		yearStatus : 'anderes Jahr anzeigen',
		weekHeader : 'Wo',
		weekStatus : 'Woche des Monats',
		dayNames : [ 'Sonntag', 'Montag', 'Dienstag', 'Mittwoch', 'Donnerstag',
				'Freitag', 'Samstag' ],
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
	$.mobile.changePage.defaults.allowSamePageTransition = true;
	$.mobile.defaultPageTransition = "slide";

	userModel = ko.mapping.fromJS(new UserModel());
	contractModel = ko.mapping.fromJS(new ContractModel());
	ko.applyBindings(userModel, $('#settings_general')[0]);
	ko.applyBindings(contractModel, $('#page_power_contract')[0]);
	app.signupController = new BookIt.SignUpController();
	app.signinController = new BookIt.SignInController();
	app.powerController = new BookIt.PowerController();

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

	resp = $.ajax({
		type : 'POST',
		url : callurl,
		success : function(resp) {
			$.mobile.changePage("#info-main");
		},
		error : function(e) {
			runtimePopup(e)
		}
	});

});

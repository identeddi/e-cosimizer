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

});

// End boilerplate code.

$(document).on("mobileinit", function(event, ui) {
	$.mobile.defaultPageTransition = "slide";
	$.mobile.changePage.defaults.allowSamePageTransition = true;
	$.datepicker.setDefaults($.datepicker.regional["de"]);
});

app.signupController = new BookIt.SignUpController();
app.signinController = new BookIt.SignInController();
app.powerController = new BookIt.PowerController();

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

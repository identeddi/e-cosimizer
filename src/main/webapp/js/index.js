var BookIt = BookIt || {};

// Begin boilerplate code generated with Cordova project.
$.ajaxSetup({
    xhrFields: {
        withCredentials : true
    }
})

var app = {
    // Application Constructor
    initialize: function () {
        this.bindEvents();
    },
    // Bind Event Listeners
    //
    // Bind any events that are required on startup. Common events are:
    // 'load', 'deviceready', 'offline', and 'online'.
    bindEvents: function () {
        document.addEventListener('deviceready', this.onDeviceReady, false);
    },
    // deviceready Event Handler
    //
    // The scope of 'this' is the event. In order to call the 'receivedEvent'
    // function, we must explicitly call 'app.receivedEvent(...);'
    onDeviceReady: function () {
        app.receivedEvent('deviceready');
    },
    // Update DOM on a Received Event
    receivedEvent: function (id) {

    }
};

app.initialize();

// End boilerplate code.

$(document).on("mobileinit", function (event, ui) {
    $.mobile.defaultPageTransition = "slide";
    $.mobile.changePage.defaults.allowSamePageTransition = true;
    $.datepicker.setDefaults( $.datepicker.regional[ "de" ] );
});

app.signupController = new BookIt.SignUpController();
app.signinController = new BookIt.SignInController();
app.powerController = new BookIt.PowerController();

$(document).on('click', '#button-page-signin', function(e){
	   callurl="http://" + window.location.host + BookIt.Settings.signInUrl

// If the return is 401, refresh the page to request new details.


   callurl="http://" + window.location.host + BookIt.Settings.signInUrl
        resp =$.ajax({
        type: 'POST',
        url: callurl,
                username: 'reset',
                password: 'reset',
        async:false
    });

                        	   
resp =$.ajax({
type: 'POST',
url: callurl,
        success: function (resp) {
			$.mobile.changePage("#info-main");
		},
        error: function (e) {
			runtimePopup(e)
        }
});

});
	
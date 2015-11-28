var BookIt = BookIt || {};

BookIt.SignInController = function() {

	this.$txtUsername = null;
	this.$txtPassword = null;
};

BookIt.SignInController.prototype.init = function() {
	this.$signInPage = $("#page-signin");
	this.$btnSubmit = $("#btn-submit", this.$signInPage);
	this.$ctnErr = $("#ctn-err", this.$signInPage);
	this.$txtUsername = $("#txt-username", this.$signInPage);
	this.$txtPassword = $("#txt-password", this.$signInPage);
};

BookIt.SignInController.prototype.resetSessionForm = function() {

};

BookIt.SignInController.prototype.onSigninCommand = function() {

	var me = this, username = me.$txtUsername.val().trim(), password = me.$txtPassword
			.val().trim(), invalidInput = false, invisibleStyle = "bi-invisible", invalidInputStyle = "bi-invalid-input";

	// Reset styles.
	me.$ctnErr.removeClass().addClass(invisibleStyle);
	me.$txtUsername.removeClass(invalidInputStyle);
	me.$txtPassword.removeClass(invalidInputStyle);

	callurl = "http://" + window.location.host + BookIt.Settings.signInUrl;
	resp = $.ajax({
		type : 'POST',
		url : callurl,
		username : 'reset',
		password : 'reset',
		async : false
	});

	auth = "Basic " + make_base_auth(username, password);
	resp = $.ajax({
		type : 'POST',
		url : callurl,
		username : username,
		password : password,
		xhr : function() {
			// Get new xhr object using default factory
			var xhr = jQuery.ajaxSettings.xhr();
			// Copy the browser's native setRequestHeader method
			var setRequestHeader = xhr.setRequestHeader;
			// Replace with a wrapper
			xhr.setRequestHeader = function(name, value) {
				// Ignore the X-Requested-With header
				if (name == 'X-Requested-With')
					return;
				// Otherwise call the native setRequestHeader method
				// Note: setRequestHeader requires its 'this' to be the xhr
				// object,
				// which is what 'this' is here when executed.
				setRequestHeader.call(this, name, value);
			}
			// pass it on to jQuery
			return xhr;
		},
		async : false
	});

	panelModel.fullUserName(resp.firstName + ' ' + resp.lastName);

};
function make_base_auth(user, password) {
	var tok = user + ':' + password;
	var hash = btoa(tok);
	return "Basic " + hash;
}
$(document).delegate("#page-signin", "pagebeforecreate", function() {

	app.signinController.init();

	app.signinController.$btnSubmit.off("tap").on("tap", function() {
		app.signinController.onSigninCommand();
	});

});

$(document).on("pagebeforeshow", "#page-signin", function(event) {
	app.signinController.resetSessionForm();

});
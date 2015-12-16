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
	callurl = "http://" + window.location.host + "/rest/login/login";

	resp = $.ajax({
		type : 'POST',
		url : callurl,
		headers : {
			"Authorization" : "Basic " + btoa(username + ":" + password)
		},
		success : function(resp) {
			panelModel.fullUserName(resp.firstName + ' ' + resp.lastName);
			$.mobile.changePage("#info-main");

		},
		error : function(e) {
		}
	});

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
//	app.signinController.resetSessionForm();

});
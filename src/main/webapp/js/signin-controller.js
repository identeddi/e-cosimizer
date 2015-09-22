﻿var BookIt = BookIt || {};

BookIt.SignInController = function () {

    this.$txtEmailAddress = null;
    this.$txtPassword = null;
};

BookIt.SignInController.prototype.init = function () {
    this.$signInPage = $("#page-signin");
    this.$btnSubmit = $("#btn-submit", this.$signInPage);
    this.$ctnErr = $("#ctn-err", this.$signInPage);
    this.$txtEmailAddress = $("#txt-email", this.$signInPage);
    this.$txtPassword = $("#txt-password", this.$signInPage);
};

BookIt.SignInController.prototype.resetSessionForm = function () {

    callurl="http://" + window.location.host + BookIt.Settings.deleteSessionUrl
    
    $.ajax({
        type: 'DELETE',
        url: callurl,
        success: function (resp) {
            return;
        },
        error: function (e) {
        	return;
        }
    });
};

BookIt.SignInController.prototype.onSigninCommand = function () {

    var me = this,
        emailAddress = me.$txtEmailAddress.val().trim(),
        password = me.$txtPassword.val().trim(),
        invalidInput = false,
        invisibleStyle = "bi-invisible",
        invalidInputStyle = "bi-invalid-input";

    // Reset styles.
    me.$ctnErr.removeClass().addClass(invisibleStyle);
    me.$txtEmailAddress.removeClass(invalidInputStyle);
    me.$txtPassword.removeClass(invalidInputStyle);

    callurl="http://" + window.location.host + BookIt.Settings.signInUrl
    
    $.ajax({
        type: 'POST',
        url: callurl,
        headers: {
            "Authorization": "Basic " + btoa(emailAddress + ":" + password)
          },
        data: "email=" + emailAddress + "&password=" + password,
        success: function (resp) {
            $.mobile.navigate("info.html");
            return;
        },
        error: function (e) {
        	
        	$( "#dlg-invalid-credentials" ).popup( "open", options );
            console.log(e.message);
            // TODO: Use a friendlier error message below.
            me.$ctnErr.html("<p>Oops! BookIt had a problem and could not register you.  Please try again in a few minutes.</p>");
            me.$ctnErr.addClass("bi-ctn-err").slideDown();
        }
    });
};
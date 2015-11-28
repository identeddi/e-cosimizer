ko.bindingHandlers.dateString = {
	update : function(element, valueAccessor, allBindingsAccessor, viewModel) {
		var value = valueAccessor(), allBindings = allBindingsAccessor();
		var valueUnwrapped = ko.utils.unwrapObservable(value);
		var pattern = allBindings.datePattern || 'mmmm d, yyyy';
		if (valueUnwrapped == undefined || valueUnwrapped == null) {
			$(element).text("");
		} else {
			var date = moment(valueUnwrapped, "YYYY-MM-DDTHH:mm:ss"); // new
																		// Date(Date.fromISO(valueUnwrapped));
			$(element).text(moment(date).format(pattern));
		}
	}
}
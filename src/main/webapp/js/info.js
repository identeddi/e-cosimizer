var BookIt = BookIt || {};

$(document).on("pagebeforeshow","#info-main",function(event){
		resp = $.ajax({
	    type: 'GET',
	    url: BookIt.Settings.getLastPowerMeasureURL,
	            success: function (resp) {
	            	console.log(resp);
if(resp != null && resp.measureDate != null &&
			resp.measureValue != null)
		{
$("#power-last").text("");
			$("#power-last").append("<div class='nd2-card'>"
			+ "<div class='card-title has-avatar'>"
			+ "<img class='card-avatar' src='//lorempixel.com/200/200/people/9/'>"
			+ "<h3 class='card-primary-title'>Stromzähler</h3>"
			+ "<h5 class='card-subtitle'>4711</h5>"
			+ "</div>"
			+ "<div class='card-supporting-text has-action'>"
			+ "<p>Zuletzt abgelesen: " + toNiceDate(resp.measureDate)  + "<p>"
			+ "<p>Aktueller Verbrauch: " + 320  + " kWh<p>"
			+ "<p><strong>Zählerstand: " +  resp.measureValue + " kWh</strong></p>"
			+ "<p><strong>Nächste Ablesung: " +  "01.01.2028" + "</strong></p>"
			+ "</div>"
			+ "</div>");			
			
			$("#power-last").append("<div class='nd2-card'>"
			+ "<div class='card-title has-avatar'>"
			+ "<img class='card-avatar' src='//lorempixel.com/200/200/people/9/'>"
			+ "<h3 class='card-primary-title'>Gaszähler</h3>"
			+ "<h5 class='card-subtitle'>4712</h5>"
			+ "</div>"
			+ "<div class='card-supporting-text has-action'>"
			+ "<p>Zuletzt abgelesen: " + toNiceDate(resp.measureDate)  + "<p>"
			+ "<p>Aktueller Verbrauch: " + 320  + " kWh<p>"
			+ "<p><strong>Zählerstand: " +  resp.measureValue + " kWh</strong></p>"
			+ "<p><strong>Nächste Ablesung: " +  "01.01.2028" + "</strong></p>"
			+ "</div>"
			+ "</div>");
					}
	            		    		},
        error: function (e) {
        }
		});
	});






package de.milke.ecost.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class DateParam {
    private final Date date;

    public DateParam(String dateStr) throws WebApplicationException {
	if (dateStr.isEmpty()) {
	    this.date = null;
	    return;
	}
	TimeZone tz = TimeZone.getTimeZone("UTC");
	final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	dateFormat.setTimeZone(tz);

	try {
	    this.date = dateFormat.parse(dateStr);
	} catch (ParseException e) {
	    throw new WebApplicationException(Response.status(Status.BAD_REQUEST)
		    .entity("Couldn't parse date string: " + e.getMessage()).build());
	}
    }

    public Date getDate() {
	return date;
    }
}

package de.milke.ecost.rest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.joda.JodaModule;

@Provider
public class JacksonConfig implements ContextResolver<ObjectMapper> {
    private final ObjectMapper objectMapper;

    public JacksonConfig() {
	super();
	TimeZone tz = TimeZone.getTimeZone("UTC");
	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
	df.setTimeZone(tz);
	objectMapper = new ObjectMapper();

	objectMapper.registerModule(new JodaModule());
	objectMapper.setDateFormat(df);
	objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @Override
    public ObjectMapper getContext(Class<?> objectType) {
	return objectMapper;
    }
}

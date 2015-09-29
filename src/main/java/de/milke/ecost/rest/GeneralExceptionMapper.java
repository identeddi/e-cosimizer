package de.milke.ecost.rest;

import java.util.logging.Logger;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import de.milke.ecost.model.GeneralException;

@Provider
public class GeneralExceptionMapper implements ExceptionMapper<GeneralException> {
    static Logger LOG = Logger.getLogger(GeneralExceptionMapper.class.getName());

    @Override
    public Response toResponse(GeneralException ge) {
	LOG.info("Mapping GeneralExceptionMapper with  message: \"" + ge.getMessage() + "\"");
	ResponseBuilder rb = Response.status(Status.BAD_REQUEST).entity(ge.getMessage());

	return rb.build();
    }

}

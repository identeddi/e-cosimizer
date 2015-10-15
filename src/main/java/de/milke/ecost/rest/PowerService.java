/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.milke.ecost.rest;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.ws.WebServiceContext;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.dao.PowerMeasureDao;
import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.PowerSupply;
import de.milke.ecost.model.User;
import de.milke.ecost.service.Check24SupplyResolver;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/power")
@Stateless
@RolesAllowed("admin")
public class PowerService {

    static Logger LOG = Logger.getLogger(PowerService.class.getName());

    @Resource
    WebServiceContext webServiceContext;

    @EJB
    AccountDao accountDao;

    @EJB
    PowerMeasureDao powerMeasureDao;

    @EJB
    Check24SupplyResolver check24SupplyResolver;

    @POST
    @Path("/measure")
    public void measure(@QueryParam("measureValue") Double measureValue,
	    @QueryParam("measureDate") Date measureDate) throws GeneralException {

	if (measureValue == null) {
	    String msg = getUser().getUsername() + ": measureValue ist: " + measureValue;
	    LOG.info(msg);
	    throw new WebApplicationException(msg);

	}
	if (measureDate == null) {
	    String msg = getUser().getUsername() + ": measureDate ist: " + measureDate;
	    LOG.info(msg);
	    throw new WebApplicationException(msg);

	}
	LOG.info(getUser().getUsername() + ": measureValue: " + measureValue + " measureDate"
		+ measureDate);

	// check measure valid

	PowerMeasure powerMeasure = new PowerMeasure();
	powerMeasure.setUser(getUser());
	powerMeasure.setMeasureDate(measureDate);
	powerMeasure.setMeasureValue(measureValue);
	powerMeasureDao.save(powerMeasure);
    }

    @GET
    @Path("/measure")
    @Produces("application/json")
    public List<PowerMeasure> getMeasure() {

	LOG.info(getUser().getUsername() + ": getMeasure");
	return powerMeasureDao.getByUser(getUser());
    }

    @GET
    @Path("/measure/last")
    @Produces("application/json")
    public PowerMeasure getLastMeasure(@Context HttpServletRequest request)
	    throws GeneralException {

	LOG.info(request.toString());
	LOG.info(getUser().getUsername() + ": getMeasure last ");
	LOG.info(getUser().getUsername() + ": session: " + request.getSession().getId());
	return powerMeasureDao.getLastByUser(getUser());
    }

    @GET
    @Path("/supplies")
    @Produces("application/json")
    public List<PowerSupply> getPowerSupplies(@QueryParam("zipcode") int zipCode,
	    @QueryParam("consumption") int consumption) throws GeneralException, IOException {
	return check24SupplyResolver.getPowerSupplies(zipCode, consumption);
    }

    private Principal principal;

    @Context
    public void setSecurityContext(SecurityContext context) {
	LOG.info(context.getUserPrincipal().getName() + ": getMeasure last ");
	principal = context.getUserPrincipal();
    }

    protected User getUser() {
	return accountDao.getByUsername(principal.getName());
    }
}

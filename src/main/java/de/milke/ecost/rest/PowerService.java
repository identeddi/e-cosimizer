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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.SecurityContext;
import javax.xml.ws.WebServiceContext;

import org.picketlink.authorization.annotations.LoggedIn;

import com.google.visualization.datasource.base.TypeMismatchException;
import com.google.visualization.datasource.datatable.ColumnDescription;
import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.datatable.value.ValueType;
import com.google.visualization.datasource.render.JsonRenderer;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.dao.ContractDao;
import de.milke.ecost.dao.PowerMeasureDao;
import de.milke.ecost.dao.PowerMeasureTypeDao;
import de.milke.ecost.model.Contract;
import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.MenuItemDTO;
import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.PowerMeasureHistoryDTO;
import de.milke.ecost.model.PowerMeasureType;
import de.milke.ecost.model.PowerSupply;
import de.milke.ecost.model.SupplySettingsDTO;
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
// @RolesAllowed("admin")
@LoggedIn
public class PowerService {

    static Logger LOG = Logger.getLogger(PowerService.class.getName());

    @Resource
    WebServiceContext webServiceContext;

    @EJB
    AccountDao accountDao;

    @EJB
    PowerMeasureDao powerMeasureDao;

    @EJB
    PowerMeasureTypeDao powerMeasureTypeDao;

    @EJB
    ContractDao contractDao;

    @EJB
    Check24SupplyResolver check24SupplyResolver;

    @POST
    @Path("/type/{powerMeasureType}/measure")
    public void measure(@PathParam("powerMeasureType") Long powerMeasureTypeId,
	    @QueryParam("measureValue") Double measureValue,
	    @QueryParam("measureDate") DateParam measureDate) throws GeneralException {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);
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
		+ measureDate.getDate());

	// check measure valid

	PowerMeasure powerMeasure = new PowerMeasure();
	powerMeasure.setPowerMeasureType(powerMeasureType);
	powerMeasure.setMeasureDate(measureDate.getDate());
	powerMeasure.setMeasureValue(measureValue);
	powerMeasureDao.save(powerMeasure);
    }

    @GET
    @Path("/type/{powerMeasureType}/measure/history")
    @Produces("application/json")
    public List<PowerMeasureHistoryDTO> getMeasure(
	    @PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	return powerMeasureDao.getMeasureHistory(powerMeasureType);
    }

    @GET
    @Path("/type/{powerMeasureType}/measure")
    @Produces("application/json")
    public PowerMeasureHistoryDTO getLastMeasure(
	    @PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	List<PowerMeasureHistoryDTO> historyList = powerMeasureDao
		.getMeasureHistory(powerMeasureType);

	if (historyList.size() > 0) {
	    return historyList.get(0);
	} else {
	    return null;
	}

    }

    @GET
    @Path("/menulist")
    @Produces("application/json")
    public List<MenuItemDTO> getMenuItems() {

	List<MenuItemDTO> listMenuItems = new ArrayList<>();
	listMenuItems.add(new MenuItemDTO("Übersicht", "#info-main", -1));
	for (PowerMeasureType powerMeasureType : powerMeasureTypeDao.getByUser(getUser())) {
	    listMenuItems.add(new MenuItemDTO(powerMeasureType.getTypeName(), "#page_power_aktuell",
		    powerMeasureType.getId()));
	}
	listMenuItems.add(new MenuItemDTO("Einstellungen", "#settings_general", -2));
	listMenuItems.add(new MenuItemDTO("Ausloggen", "#page-index", -3));
	return listMenuItems;
    }

    @GET
    @Path("/type/{powerMeasureType}/measuregraph")
    public String getMeasureGraphData(@PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	// Create a data table,
	DataTable data = new DataTable();
	ArrayList cd = new ArrayList();

	Date now = new Date();
	int currentYear = now.getYear() + 1900;
	int numYears = 3;

	cd.add(new ColumnDescription("Monat", ValueType.TEXT, "Jahr"));
	for (int i = 0; i < numYears; i++) {
	    cd.add(new ColumnDescription((currentYear - i) + "", ValueType.NUMBER,
		    (currentYear - i) + ""));

	}

	data.addColumns(cd);

	// Fill the data table.
	try {
	    List<PowerMeasureHistoryDTO> histList = powerMeasureDao
		    .getMeasureHistory(powerMeasureType);

	    String[] months = { "Jan", "Feb", "Mär", "Apr", "Mai", "Jun", "Jul", "Aug", "Sep",
		    "Okt", "Nov", "Dez" };

	    for (int i = 0; i < months.length; i++) {
		Integer[] yearMeasure = new Integer[numYears];
		for (int j = 0; j < numYears; j++) {
		    Date searchDate = new Date(currentYear - 1900 - j, i, 1);
		    yearMeasure[j] = powerMeasureDao.getEstimationForDate(searchDate,
			    powerMeasureType);
		}
		data.addRowFromValues(months[i], yearMeasure[1], yearMeasure[2]);
	    }

	} catch (TypeMismatchException e) {
	    System.out.println("Invalid type!");
	}
	String json = JsonRenderer.renderDataTable(data, true, false).toString();

	System.out.println(json);
	return json;
    }

    @GET
    @Path("/type/{powerMeasureType}/contract")
    @Produces("application/json")
    public Contract getContract(@PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	LOG.info(getUser().getUsername() + ": getMeasure");

	return contractDao.getByType(powerMeasureType);
    }

    @PUT
    @Path("/type/{powerMeasureType}/contract")
    @Consumes("application/json")
    @Produces("application/json")
    public Contract setContract(Contract contract,
	    @PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	contract.setPowerMeasureType(powerMeasureType);
	LOG.info(getUser().getUsername() + ": getContract");

	return contractDao.save(contract);
    }

    @GET
    @Path("/measure/last")
    @Produces("application/json")
    public List<PowerMeasureHistoryDTO> getLastMeasures(@Context HttpServletRequest request)
	    throws GeneralException {
	List<PowerMeasureType> powerMeasureTypes = powerMeasureTypeDao.getByUser(getUser());

	List<PowerMeasureHistoryDTO> lastMeasures = new ArrayList<>();
	for (PowerMeasureType powerMeasureType : powerMeasureTypes) {
	    List<PowerMeasureHistoryDTO> history = powerMeasureDao
		    .getMeasureHistory(powerMeasureType);
	    if (history.size() > 0) {
		lastMeasures.add(history.get(0));
	    }
	}
	LOG.info(request.toString());
	LOG.info(getUser().getUsername() + ": getMeasure last ");
	LOG.info(getUser().getUsername() + ": session: " + request.getSession().getId());
	return lastMeasures;
    }

    @GET
    @Path("/type/{powerMeasureType}/supplies")
    @Produces("application/json")
    public List<PowerSupply> getPowerSupplies(
	    @PathParam("powerMeasureType") Long powerMeasureTypeId,
	    @QueryParam("zipcode") int zipCode, @QueryParam("consumption") int consumption)
		    throws GeneralException, IOException {
	return check24SupplyResolver.getPowerSupplies(zipCode, consumption);
    }

    @GET
    @Path("/type/{powerMeasureType}/supply/settings")
    @Produces("application/json")
    public SupplySettingsDTO getSupplySettings(
	    @PathParam("powerMeasureType") Long powerMeasureTypeId)
		    throws GeneralException, IOException {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	String zipcode = null;
	Integer estimatedConsumtion = null;
	Integer passedConsumtion = null;

	zipcode = getUser().getZipcode();
	for (PowerMeasureHistoryDTO hist : powerMeasureDao.getMeasureHistory(powerMeasureType)) {
	    passedConsumtion = (int) (hist.getDailyConsumption() * 365);
	    estimatedConsumtion = (int) (passedConsumtion * 1.1);
	    break;
	}

	return new SupplySettingsDTO(zipcode, estimatedConsumtion, passedConsumtion);
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

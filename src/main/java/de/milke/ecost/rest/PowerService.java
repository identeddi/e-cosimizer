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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.xml.ws.WebServiceContext;

import org.picketlink.Identity;
import org.picketlink.authorization.annotations.LoggedIn;

import com.google.visualization.datasource.datatable.DataTable;
import com.google.visualization.datasource.render.JsonRenderer;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.dao.ContractDao;
import de.milke.ecost.dao.PowerMeasureDao;
import de.milke.ecost.dao.PowerMeasureTypeDao;
import de.milke.ecost.model.Contract;
import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.MenuItemDTO;
import de.milke.ecost.model.MyUser;
import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.PowerMeasureHistoryDTO;
import de.milke.ecost.model.PowerMeasureType;
import de.milke.ecost.model.PowerSupply;
import de.milke.ecost.model.SupplySettingsDTO;
import de.milke.ecost.model.User;
import de.milke.ecost.model.chart.ChartModel;
import de.milke.ecost.model.chart.Dataset;
import de.milke.ecost.service.Check24SupplyResolver;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/power")
@Stateless
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

    @Inject
    Identity identity;

    @DELETE
    @Path("/type/{powerMeasureType}/measure/{id}")
    public void deleteMeasure(@PathParam("powerMeasureType") Long powerMeasureTypeId,
	    @PathParam("id") Long id) throws GeneralException {
	powerMeasureDao.deleteMeasure(id);
    }

    @POST
    @Path("/type/{powerMeasureType}/measure")
    public void measure(@PathParam("powerMeasureType") Long powerMeasureTypeId,
	    PowerMeasure powerMeasure) throws GeneralException {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);
	if (powerMeasure.getMeasureValue() == null) {
	    String msg = getUser().getFirstName() + ": measureValue ist: "
		    + powerMeasure.getMeasureValue();
	    LOG.info(msg);
	    throw new WebApplicationException(msg);

	}
	if (powerMeasure.getMeasureDate() == null) {
	    String msg = getUser().getFirstName() + ": measureDate ist: "
		    + powerMeasure.getMeasureDate();
	    LOG.info(msg);
	    throw new WebApplicationException(msg);

	}
	LOG.info(getUser().getFirstName() + ": measureValue: " + powerMeasure.getMeasureValue()
		+ " measureDate" + powerMeasure.getMeasureDate().getDate());

	// check measure valid
	powerMeasure.setPowerMeasureType(powerMeasureType);
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
    @Path("/measure/{id}")
    @Produces("application/json")
    public PowerMeasure getMeasureById(@PathParam("id") Long id) {
	return powerMeasureDao.get(id);
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
	for (PowerMeasureType powerMeasureType : powerMeasureTypeDao.getEnabledByUser(getUser())) {
	    listMenuItems.add(new MenuItemDTO(powerMeasureType.getTypeName(), "#page_power_aktuell",
		    powerMeasureType.getId()));
	}
	listMenuItems.add(new MenuItemDTO("Einstellungen", "#settings_general", -2));
	listMenuItems.add(new MenuItemDTO("Ausloggen", "#page-index", -3));
	return listMenuItems;
    }

    @GET
    @Path("/powermeasuretypes")
    @Produces("application/json")
    public List<PowerMeasureType> getPowerMeasureTypes() {

	return powerMeasureTypeDao.getAllByUser(getUser());
    }

    @GET
    @Path("/powermeasuretype/{id}")
    @Produces("application/json")
    public PowerMeasureType getPowerMeasureType(@PathParam("id") Long id) {

	return powerMeasureTypeDao.findById(id);
    }

    @DELETE
    @Path("/powermeasuretype/{id}")
    public void deletePowerMeasureType(@PathParam("id") Long id) throws GeneralException {
	powerMeasureTypeDao.remove(id);
    }

    @PUT
    @Path("/powermeasuretype")
    @Produces("application/json")
    public PowerMeasureType updatePowerMeasureType(PowerMeasureType powerMeasureType) {
	powerMeasureType.setUser(getUser());
	return powerMeasureTypeDao.save(powerMeasureType);
    }

    @GET
    @Path("/type/{powerMeasureType}/measuregraph")
    @Produces("application/json")
    public ChartModel getMeasureGraphData(@PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	// Create a data table,
	DataTable data = new DataTable();

	Date now = new Date();
	int currentYear = now.getYear() + 1900;

	// var data = {
	// labels : [ "January", "February", "March", "April", "May", "June",
	// "July", "August", "September", "October", "November",
	// "December" ],
	// datasets : [ {

	// data : [ 65, 59, 80, 81, 56, 55, 40, 59, 80, 81, 56, 55, 40 ]
	// }, {
	// label : "My Second dataset",
	// fillColor : "rgba(151,187,205,0.5)",
	// strokeColor : "rgba(151,187,205,0.8)",
	// highlightFill : "rgba(151,187,205,0.75)",
	// highlightStroke : "rgba(151,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// }, {
	// label : "My Third dataset",
	// fillColor : "rgba(251,187,205,0.5)",
	// strokeColor : "rgba(251,187,205,0.8)",
	// highlightFill : "rgba(251,187,205,0.75)",
	// highlightStroke : "rgba(251,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// }, {
	// label : "My Fourth dataset",
	// fillColor : "rgba(51,187,205,0.5)",
	// strokeColor : "rgba(51,187,205,0.8)",
	// highlightFill : "rgba(51,187,205,0.75)",
	// highlightStroke : "rgba(51,187,205,1)",
	// data : [ 28, 48, 40, 19, 86, 27, 90, 48, 40, 19, 86, 27, 90 ]
	// } ]
	// };

	ChartModel historyChart = new ChartModel();
	List<String> labels = new ArrayList<>();
	List<Dataset> datasets = new ArrayList<>();
	labels.add("Jan");
	labels.add("Feb");
	labels.add("Mär");
	labels.add("Apr");
	labels.add("Mai");
	labels.add("Jun");
	labels.add("Jul");
	labels.add("Aug");
	labels.add("Sep");
	labels.add("Okt");
	labels.add("Nov");
	labels.add("Dez");

	historyChart.setLabels(labels);

	List<PowerMeasureHistoryDTO> histList = powerMeasureDao.getMeasureHistory(powerMeasureType);

	Dataset yearData = new Dataset();
	yearData.setFillColor("rgba(220,220,20,0.5)");
	yearData.setStrokeColor("rgba(220,220,20,0.8)");
	yearData.setHighlightFill("rgba(220,220,20,0.75)");
	yearData.setHighlightStroke("rgba(220,220,20,1)");
	yearData.setLabel("Jahr " + currentYear + "");
	historyChart.getDatasets().add(yearData);

	yearData = new Dataset();
	yearData.setFillColor("rgba(220,220,220,0.5)");
	yearData.setStrokeColor("rgba(220,220,220,0.8)");
	yearData.setHighlightFill("rgba(220,220,220,0.75)");
	yearData.setHighlightStroke("rgba(220,220,220,1)");
	yearData.setLabel("Jahr " + (currentYear - 1) + "");
	historyChart.getDatasets().add(yearData);

	yearData = new Dataset();
	yearData.setFillColor("rgba(20,220,220,0.5)");
	yearData.setStrokeColor("rgba(20,220,220,0.8)");
	yearData.setHighlightFill("rgba(20,220,220,0.75)");
	yearData.setHighlightStroke("rgba(20,220,220,1)");
	yearData.setLabel("Jahr " + (currentYear - 2) + "");
	historyChart.getDatasets().add(yearData);

	yearData = new Dataset();
	yearData.setFillColor("rgba(20,120,220,0.5)");
	yearData.setStrokeColor("rgba(20,120,220,0.8)");
	yearData.setHighlightFill("rgba(20,120,220,0.75)");
	yearData.setHighlightStroke("rgba(20,120,220,1)");
	yearData.setLabel("Jahr " + (currentYear - 3) + "");
	historyChart.getDatasets().add(yearData);

	for (int j = 0; j < historyChart.getDatasets().size(); j++) {
	    List<Integer> yearMeasure = new ArrayList<>();
	    for (int i = 0; i < 12; i++) {

		Date searchDate = new Date(currentYear - 1900 - j, i, 1);
		yearMeasure.add(powerMeasureDao.getEstimationForDate(searchDate, powerMeasureType));
	    }
	    historyChart.getDatasets().get(j).setData(yearMeasure);
	}

	String json = JsonRenderer.renderDataTable(data, true, false).toString();

	System.out.println(json);
	return historyChart;
    }

    @GET
    @Path("/type/{powerMeasureType}/contract")
    @Produces("application/json")
    public Contract getContract(@PathParam("powerMeasureType") Long powerMeasureTypeId) {
	PowerMeasureType powerMeasureType = powerMeasureTypeDao.findById(powerMeasureTypeId);

	LOG.info(getUser().getFirstName() + ": getMeasure");

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
	LOG.info(getUser().getFirstName() + ": getContract");

	return contractDao.save(contract);
    }

    @GET
    @Path("/measure/last")
    @Produces("application/json")
    public List<PowerMeasureHistoryDTO> getLastMeasures(@Context HttpServletRequest request)
	    throws GeneralException {
	List<PowerMeasureType> powerMeasureTypes = powerMeasureTypeDao.getEnabledByUser(getUser());

	List<PowerMeasureHistoryDTO> lastMeasures = new ArrayList<>();
	for (PowerMeasureType powerMeasureType : powerMeasureTypes) {
	    List<PowerMeasureHistoryDTO> history = powerMeasureDao
		    .getMeasureHistory(powerMeasureType);
	    if (history.size() > 0) {
		lastMeasures.add(history.get(0));
	    }
	}
	LOG.info(request.toString());
	LOG.info(getUser().getFirstName() + ": getMeasure last ");
	LOG.info(getUser().getFirstName() + ": session: " + request.getSession().getId());
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

    protected User getUser() {
	MyUser user = (MyUser) identity.getAccount();
	LOG.info("userinfo: " + user.getUser().getId() + " firstname: "
		+ user.getUser().getFirstName());
	return user.getUser();
    }
}

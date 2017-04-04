/*
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 * JBoss, Home of Professional Open Source
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
package de.milke.ecost.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.PowerMeasureHistoryDTO;
import de.milke.ecost.model.PowerMeasureType;

@Stateless
public class PowerMeasureDao {
    static Logger LOG = Logger.getLogger(PowerMeasureDao.class.getName());

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public PowerMeasure findById(Long id) {
	return em.find(PowerMeasure.class, id);
    }

    public PowerMeasure persist(PowerMeasure Power) {
	em.persist(Power);
	return Power;
    }

    public List<PowerMeasure> getByType(PowerMeasureType powerMeasureType) {
	TypedQuery<PowerMeasure> lQuery = em.createQuery(
		"from PowerMeasure where powerMeasureType=:powerMeasureType order by measureDate desc",
		PowerMeasure.class);
	lQuery.setParameter("powerMeasureType", powerMeasureType);
	return lQuery.getResultList();

    }

    public PowerMeasure deleteLastMeasure(PowerMeasureType powerMeasureType) {
	TypedQuery<PowerMeasure> lQuery = em.createQuery(
		"from PowerMeasure where powerMeasureType=:powerMeasureType order by measureDate desc",
		PowerMeasure.class);
	lQuery.setParameter("powerMeasureType", powerMeasureType);
	List<PowerMeasure> measureList = lQuery.getResultList();

	if (measureList.size() > 0) {
	    Query query = em.createQuery("delete from PowerMeasure where id=:id");
	    query.setParameter("id", measureList.get(0).getId());
	    query.executeUpdate();
	    return measureList.get(0);

	}
	return null;
    }

    public PowerMeasure save(PowerMeasure power) throws GeneralException {

	List<PowerMeasure> listPM = getByType(power.getPowerMeasureType());

	if (power.getMeasureDate().after(new Date())) {
	    // Ablesung höher aber Datum jünger, Fehler
	    throw new GeneralException("Ablesung in der Zukunft nicht möglich");
	}

	for (PowerMeasure pm : listPM) {

	    if (power.getMeasureDate().before(pm.getMeasureDate())
		    && power.getMeasureValue() > pm.getMeasureValue()
		    && pm.getId() != power.getId()) {
		// Ablesung höher aber Datum jünger, Fehler
		throw new GeneralException("Ablesung höher aber Datum jünger, Fehler");
	    }

	    if (power.getMeasureDate().after(pm.getMeasureDate())
		    && power.getMeasureValue() < pm.getMeasureValue()
		    && pm.getId() != power.getId()) {
		// Ablesung höher aber Datum jünger, Fehler
		throw new GeneralException("Ablesung niedriger aber Datum später, Fehler");
	    }
	}
	for (PowerMeasure pm : listPM) {
	    System.out.println("pm: " + pm.getMeasureDate().getTime() + " power: "
		    + power.getMeasureDate().getTime() + " cmp: "
		    + (pm.getMeasureDate().compareTo(power.getMeasureDate()) == 0));
	    if (pm.getMeasureDate().compareTo(power.getMeasureDate()) == 0) {
		// update measure value
		pm.setMeasureValue(power.getMeasureValue());
		em.merge(pm);
		return pm;

	    }
	}
	if (power.getId() != null) {
	    em.merge(power);
	} else {

	    em.persist(power);
	}
	return power;

    }

    public PowerMeasure getLastByType(PowerMeasureType powerMeasureType) throws GeneralException {

	try {
	    TypedQuery<PowerMeasure> lQuery = em.createQuery(
		    "from PowerMeasure where powerMeasureType=:powerMeasureType order by measureDate desc",
		    PowerMeasure.class);
	    lQuery.setParameter("powerMeasureType", powerMeasureType);
	    lQuery.setMaxResults(1);
	    return lQuery.getSingleResult();
	} catch (NoResultException e) {
	    throw new GeneralException("Keine Zählerdaten für den Typ "
		    + powerMeasureType.getTypeName() + " gefunden");
	}

    }

    public List<PowerMeasure> getAllLastMeasures() throws GeneralException {

	TypedQuery<PowerMeasure> lQuery = em.createQuery(
		"FROM PowerMeasure where (powerMeasureType, measureDate) in (SELECT powerMeasureType.id, max(measureDate)  FROM PowerMeasure group by powerMeasureType) ",
		PowerMeasure.class);
	return lQuery.getResultList();
    }

    public List<PowerMeasureHistoryDTO> getMeasureHistory(PowerMeasureType powerMeasureType) {
	LOG.info(powerMeasureType.getUser().getFirstName() + ": getMeasure");
	List<PowerMeasure> listMeasures = getByType(powerMeasureType);
	List<PowerMeasureHistoryDTO> listHistory = new ArrayList<>();
	for (int i = 0; i < listMeasures.size() - 1; i++) {
	    PowerMeasure current = listMeasures.get(i + 1);
	    PowerMeasure next = listMeasures.get(i);
	    int currentYear = current.getMeasureDate().getYear();
	    int nextYear = next.getMeasureDate().getYear();
	    int days = Days.daysBetween(new DateTime(current.getMeasureDate()),
		    new DateTime(next.getMeasureDate())).getDays();
	    double consumptionPerDay = (next.getMeasureValue() - current.getMeasureValue()) / days;
	    consumptionPerDay = ((int) (consumptionPerDay * 100)) / 100.;
	    Date nextMeasure = new Date(
		    current.getMeasureDate().getTime() + 1000 * 60 * 60 * 24 * 31);

	    listHistory.add(new PowerMeasureHistoryDTO(next.getId(), next.getMeasureDate(),
		    next.getMeasureValue(), "gemessen", powerMeasureType, consumptionPerDay,
		    nextMeasure));

	    if (currentYear < nextYear) {

		// estimated date
		DateTime estimatedDate = new DateTime(1900 + next.getMeasureDate().getYear(), 1, 1,
			0, 0);
		if (!estimatedDate.equals(current.getMeasureDate())
			&& estimatedDate.equals(next.getMeasureDate())) {
		    int daysToEstimated = Days
			    .daysBetween(new DateTime(current.getMeasureDate()), estimatedDate)
			    .getDays();

		    int estimatedValue = (int) (consumptionPerDay * daysToEstimated);

		    listHistory.add(new PowerMeasureHistoryDTO(null, estimatedDate.toDate(),
			    (current.getMeasureValue() + estimatedValue), "geschätzt",
			    powerMeasureType, consumptionPerDay));
		}
	    }

	}

	if (listMeasures.size() > 0) {
	    PowerMeasure current = listMeasures.get(listMeasures.size() - 1);
	    Date nextMeasure = new Date(
		    current.getMeasureDate().getTime() + 1000 * 60 * 60 * 24 * 31);
	    listHistory.add(new PowerMeasureHistoryDTO(current.getId(), current.getMeasureDate(),
		    current.getMeasureValue(), "gemessen", powerMeasureType, 0, nextMeasure));

	}
	return listHistory;
    }

    public Integer getEstimationForDate(Date searchDate, PowerMeasureType powerMeasureType) {
	// TODO Auto-generated method stub
	System.out.println("searchDate: " + searchDate);
	List<PowerMeasure> listMeasures = getByType(powerMeasureType);
	DateTime estimatedDate = new DateTime(searchDate.getYear() + 1900,
		searchDate.getMonth() + 1, searchDate.getDate(), 0, 0);
	System.out.println("estimatedDate: " + estimatedDate);

	for (int i = 0; i < listMeasures.size() - 1; i++) {

	    if (listMeasures.get(i).getMeasureDate().before(searchDate)) {
		// LOG.info(listMeasures.get(i).getMeasureDate() + " before " +
		// searchDate);

		PowerMeasure current = listMeasures.get(i + 1);
		PowerMeasure next = listMeasures.get(i);
		int days = Days.daysBetween(new DateTime(current.getMeasureDate()),
			new DateTime(next.getMeasureDate())).getDays();
		double consumptionPerDay = (next.getMeasureValue() - current.getMeasureValue())
			/ days;

		// estimated date
		// int daysToEstimated = Days
		// .daysBetween(new DateTime(current.getMeasureDate()),
		// estimatedDate)
		// .getDays();

		int estimatedValue = (int) (consumptionPerDay * 30);
		LOG.info(powerMeasureType.getUser().getFirstName() + ": estimation for "
			+ searchDate + " Value: " + estimatedValue);

		return estimatedValue;
	    }

	}
	LOG.info(powerMeasureType.getUser().getFirstName() + ": no estimation found for "
		+ searchDate);
	return null;
    }

    public void deleteMeasure(Long id) {
	Query query = em.createQuery("delete from PowerMeasure where id=:id");
	query.setParameter("id", id);
	query.executeUpdate();

    }

    public PowerMeasure get(Long id) {
	TypedQuery<PowerMeasure> lQuery = em.createQuery("from PowerMeasure where id=:id",
		PowerMeasure.class);
	lQuery.setParameter("id", id);
	lQuery.setMaxResults(1);
	return lQuery.getSingleResult();

    }
}

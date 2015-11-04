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
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import org.joda.time.DateTime;
import org.joda.time.Days;

import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.PowerMeasureHistoryDTO;
import de.milke.ecost.model.User;

@Stateless
public class PowerMeasureDao {
    static Logger LOG = Logger.getLogger(PowerMeasureDao.class.getName());

    @Inject
    private EntityManager em;

    public PowerMeasure findById(Long id) {
	return em.find(PowerMeasure.class, id);
    }

    public PowerMeasure persist(PowerMeasure Power) {
	em.persist(Power);
	return Power;
    }

    public List<PowerMeasure> getByUser(User user) {
	TypedQuery<PowerMeasure> lQuery = em.createQuery(
		"from PowerMeasure where user=:user order by measureDate desc", PowerMeasure.class);
	lQuery.setParameter("user", user);
	return lQuery.getResultList();

    }

    public PowerMeasure save(PowerMeasure power) throws GeneralException {

	List<PowerMeasure> listPM = getByUser(power.getUser());

	if (power.getMeasureDate().after(new Date())) {
	    // Ablesung höher aber Datum jünger, Fehler
	    throw new GeneralException("Ablesung in der Zukunft nicht möglich");
	}

	for (PowerMeasure pm : listPM) {
	    if (pm.getMeasureDate().compareTo(power.getMeasureDate()) == 0) {
		// update measure value
		pm.setMeasureValue(power.getMeasureValue());
		em.merge(pm);
		return pm;

	    }

	    if (power.getMeasureDate().before(pm.getMeasureDate())
		    && power.getMeasureValue() > pm.getMeasureValue()) {
		// Ablesung höher aber Datum jünger, Fehler
		throw new GeneralException("Ablesung höher aber Datum jünger, Fehler");
	    }

	    if (power.getMeasureDate().after(pm.getMeasureDate())
		    && power.getMeasureValue() < pm.getMeasureValue()) {
		// Ablesung höher aber Datum jünger, Fehler
		throw new GeneralException("Ablesung niedriger aber Datum später, Fehler");
	    }
	}

	em.persist(power);
	return power;

    }

    public PowerMeasure getLastByUser(User user) throws GeneralException {

	try {
	    TypedQuery<PowerMeasure> lQuery = em.createQuery(
		    "from PowerMeasure where user=:user order by measureDate desc",
		    PowerMeasure.class);
	    lQuery.setParameter("user", user);
	    lQuery.setMaxResults(1);
	    return lQuery.getSingleResult();
	} catch (NoResultException e) {
	    throw new GeneralException(
		    "Keine Zählerdaten für den User " + user.getUsername() + " gefunden");
	}

    }

    public List<PowerMeasureHistoryDTO> getMeasureHistory(User user) {
	LOG.info(user.getUsername() + ": getMeasure");
	List<PowerMeasure> listMeasures = getByUser(user);
	List<PowerMeasureHistoryDTO> listHistory = new ArrayList<>();
	for (int i = 0; i < listMeasures.size() - 1; i++) {
	    PowerMeasure current = listMeasures.get(i + 1);
	    PowerMeasure next = listMeasures.get(i);
	    int currentYear = current.getMeasureDate().getYear();
	    int nextYear = next.getMeasureDate().getYear();
	    if (currentYear < nextYear) {
		int days = Days.daysBetween(new DateTime(current.getMeasureDate()),
			new DateTime(next.getMeasureDate())).getDays();
		double consumptionPerDay = (next.getMeasureValue() - current.getMeasureValue())
			/ days;

		// estimated date
		DateTime estimatedDate = new DateTime(1900 + next.getMeasureDate().getYear(), 1, 1,
			0, 0);
		int daysToEstimated = Days
			.daysBetween(new DateTime(current.getMeasureDate()), estimatedDate)
			.getDays();

		int estimatedValue = (int) (consumptionPerDay * daysToEstimated);

		listHistory.add(new PowerMeasureHistoryDTO(next.getId(), next.getMeasureDate(),
			next.getMeasureValue(), "gemessen", user, consumptionPerDay));
		listHistory.add(new PowerMeasureHistoryDTO(null, estimatedDate.toDate(),
			(current.getMeasureValue() + estimatedValue), "geschätzt", user,
			consumptionPerDay));
	    }

	}

	if (listMeasures.size() > 0) {
	    PowerMeasure current = listMeasures.get(listMeasures.size() - 1);
	    listHistory.add(new PowerMeasureHistoryDTO(current.getId(), current.getMeasureDate(),
		    current.getMeasureValue(), "gemessen", user, 0));

	}
	return listHistory;
    }

    public Integer getEstimationForDate(Date searchDate, User user) {
	// TODO Auto-generated method stub
	System.out.println("searchDate: " + searchDate);
	List<PowerMeasure> listMeasures = getByUser(user);
	DateTime estimatedDate = new DateTime(searchDate.getYear() + 1900,
		searchDate.getMonth() + 1, searchDate.getDate(), 0, 0);
	System.out.println("estimatedDate: " + estimatedDate);

	for (int i = 0; i < listMeasures.size() - 1; i++) {

	    if (listMeasures.get(i).getMeasureDate().after(searchDate)) {
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
		LOG.info(user.getUsername() + ": estimation for " + searchDate + " Value: "
			+ estimatedValue);

		return estimatedValue;
	    }

	}
	LOG.info(user.getUsername() + ": no estimation found for " + searchDate);
	return null;
    }
}

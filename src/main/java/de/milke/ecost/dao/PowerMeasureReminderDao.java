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

import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.PowerMeasureReminder;
import de.milke.ecost.model.PowerMeasureType;

@Stateless
public class PowerMeasureReminderDao {
    static Logger LOG = Logger.getLogger(PowerMeasureReminderDao.class.getName());

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public PowerMeasureReminder findById(Long id) {
	return em.find(PowerMeasureReminder.class, id);
    }

    public PowerMeasureReminder persist(PowerMeasureReminder Power) {
	em.persist(Power);
	return Power;
    }

    public List<PowerMeasureReminder> getByType(PowerMeasureType powerMeasureType) {
	TypedQuery<PowerMeasureReminder> lQuery = em.createQuery(
		"from PowerMeasureReminder where powerMeasureType=:powerMeasureType order by measureDate desc",
		PowerMeasureReminder.class);
	lQuery.setParameter("powerMeasureType", powerMeasureType);
	return lQuery.getResultList();

    }

    public int deletePowerMeasureReminder(PowerMeasureType powerMeasureType) {

	Query query = em.createQuery(
		"delete from PowerMeasureReminder where powerMeasureType=:powerMeasureType");
	query.setParameter("powerMeasureType", powerMeasureType);
	return query.executeUpdate();
    }

    public PowerMeasureReminder save(PowerMeasureReminder reminder) throws GeneralException {

	if (reminder.getId() != null) {
	    em.merge(reminder);
	} else {
	    em.persist(reminder);
	}
	return reminder;

    }

    public PowerMeasureReminder get(Long id) {
	TypedQuery<PowerMeasureReminder> lQuery = em
		.createQuery("from PowerMeasureReminder where id=:id", PowerMeasureReminder.class);
	lQuery.setParameter("id", id);
	lQuery.setMaxResults(1);
	return lQuery.getSingleResult();

    }
}

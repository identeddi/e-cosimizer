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
import javax.persistence.TypedQuery;

import de.milke.ecost.model.PowerMeasureType;
import de.milke.ecost.model.User;

@Stateless
public class PowerMeasureTypeDao {
    static Logger LOG = Logger.getLogger(PowerMeasureTypeDao.class.getName());

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public PowerMeasureType findById(Long id) {
	return em.find(PowerMeasureType.class, id);
    }

    public List<PowerMeasureType> getByUser(User user) {
	TypedQuery<PowerMeasureType> lQuery = em.createQuery(
		"from PowerMeasureType where user=:user order by typeName", PowerMeasureType.class);
	lQuery.setParameter("user", user);
	return lQuery.getResultList();

    }

    public PowerMeasureType save(PowerMeasureType powerMeasureType) {

	if (powerMeasureType.getId() == null || powerMeasureType.getId() == 0) {
	    em.persist(powerMeasureType);

	} else {
	    em.merge(powerMeasureType);
	}
	return powerMeasureType;

    }
}

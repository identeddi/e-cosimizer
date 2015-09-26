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
package de.milke.ecost.dao;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.milke.ecost.model.PowerMeasure;
import de.milke.ecost.model.User;

@Stateless
public class PowerMeasureDao {

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
		TypedQuery<PowerMeasure> lQuery = em.createQuery("from PowerMeasure where user=:user order by measureDate desc",
				PowerMeasure.class);
		lQuery.setParameter("user", user);
		return lQuery.getResultList();

	}

	public PowerMeasure save(PowerMeasure Power) {

		em.persist(Power);
		return Power;

	}

	public PowerMeasure getLastByUser(User user) {

		try {
			TypedQuery<PowerMeasure> lQuery = em
					.createQuery("from PowerMeasure where user=:user order by measureDate desc", PowerMeasure.class);
			lQuery.setParameter("user", user);
			lQuery.setMaxResults(1);
			return lQuery.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}
}

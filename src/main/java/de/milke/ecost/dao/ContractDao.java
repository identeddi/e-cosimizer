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

import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

import de.milke.ecost.model.Contract;
import de.milke.ecost.model.User;

@Stateless
public class ContractDao {
    static Logger LOG = Logger.getLogger(ContractDao.class.getName());

    @Inject
    private EntityManager em;

    public Contract findById(Long id) {
	return em.find(Contract.class, id);
    }

    public Contract persist(Contract user) {
	em.persist(user);
	return user;
    }

    public Contract getByUser(User user) {
	TypedQuery<Contract> lQuery = em.createQuery("from Contract where user=:user",
		Contract.class);
	lQuery.setParameter("user", user);
	try {
	    Contract contract = lQuery.getSingleResult();
	    LOG.info("Contract found " + contract.getContractName());
	    return contract;
	} catch (NoResultException e) {
	    LOG.info("Contract not found " + user.getUsername());

	    return null;

	}

    }

    public Contract save(Contract contract) {

	if (contract.getId() == null || contract.getId() == 0) {
	    em.persist(contract);

	} else {
	    Contract attachedContract = findById(contract.getId());
	    attachedContract.setCancellationPeriod(contract.getCancellationPeriod());
	    attachedContract.setContractName(contract.getContractName());
	    attachedContract.setContractType(contract.getContractType());
	    attachedContract.setDueDate(contract.getDueDate());
	    attachedContract.setNotification(contract.getNotification());
	    attachedContract.setProviderName(contract.getProviderName());
	    em.merge(contract);
	}
	return contract;

    }
}

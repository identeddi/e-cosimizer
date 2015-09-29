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

import de.milke.ecost.model.Role;
import de.milke.ecost.model.User;

@Stateless
public class AccountDao {
    static Logger LOG = Logger.getLogger(AccountDao.class.getName());

    @Inject
    private EntityManager em;

    public User findById(Long id) {
	return em.find(User.class, id);
    }

    public User persist(User user) {
	em.persist(user);
	return user;
    }

    public Role getOrCreateRole(String roleName) {
	TypedQuery<Role> lQuery = em.createQuery("from Role where rolename=:rolename", Role.class);
	lQuery.setParameter("rolename", roleName);
	try {
	    Role foundRole = lQuery.getSingleResult();
	    return foundRole;
	} catch (NoResultException e) {
	    Role role = new Role(roleName);

	    em.persist(role);
	    return role;

	}

    }

    public User getByUsername(String userName) {
	TypedQuery<User> lQuery = em.createQuery("from User where username=:username", User.class);
	lQuery.setParameter("username", userName);
	try {
	    User usr = lQuery.getSingleResult();
	    LOG.info("User found " + usr.getUsername());
	    return usr;
	} catch (NoResultException e) {
	    LOG.info("User not found " + userName);

	    return null;

	}

    }

    public User save(User user) {

	em.persist(user);
	return user;

    }
}

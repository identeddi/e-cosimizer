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
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import de.milke.ecost.model.UserEntity;

@Stateless
public class AccountDao {
    static Logger LOG = Logger.getLogger(AccountDao.class.getName());

    @PersistenceContext(name = "primary")
    private EntityManager em;

    public UserEntity findById(Long id) {
	return em.find(UserEntity.class, id);
    }

    public UserEntity persist(UserEntity user) {
	em.persist(user);
	return user;
    }

    /*
     * public Role getOrCreateRole(String roleName) { TypedQuery<Role> lQuery =
     * em.createQuery("from Role where rolename=:rolename", Role.class);
     * lQuery.setParameter("rolename", roleName); try { Role foundRole =
     * lQuery.getSingleResult(); return foundRole; } catch (NoResultException e)
     * { Role role = new Role(roleName);
     * 
     * em.persist(role); return role;
     * 
     * }
     * 
     * }
     */
    public UserEntity getByUsername(String userName) {
	TypedQuery<UserEntity> lQuery = em.createQuery("from User where username=:username", UserEntity.class);
	lQuery.setParameter("username", userName);
	try {
	    UserEntity usr = lQuery.getSingleResult();
	    LOG.info("User found " + usr.getFirstName());
	    return usr;
	} catch (NoResultException e) {
	    LOG.info("User not found " + userName);

	    return null;

	}

    }

    public UserEntity save(UserEntity user) {

	if (user.getId() == null || user.getId() == 0) {
	    em.persist(user);

	} else {
	    em.merge(user);
	}
	return user;

    }
}

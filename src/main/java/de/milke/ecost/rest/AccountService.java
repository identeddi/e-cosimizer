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

import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.xml.ws.WebServiceContext;

import org.jboss.security.auth.spi.Util;
import org.picketlink.authorization.annotations.LoggedIn;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.model.Role;
import de.milke.ecost.model.User;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/account")
@Stateless
@LoggedIn
public class AccountService {

    static Logger LOG = Logger.getLogger(AccountService.class.getName());

    @Inject
    private AccountDao accountDao;

    @Resource
    WebServiceContext webServiceContext;

    @POST
    @Path("/register")
    public String register(@QueryParam("email") String email,
	    @QueryParam("firstName") String firstName, @QueryParam("username") String username,
	    @QueryParam("lastName") String lastName, @QueryParam("password") String password,
	    @QueryParam("passwordConfirm") String passwordConfirm) {

	Role adminRole = accountDao.getOrCreateRole("admin");

	// check user exists
	if (null != accountDao.getByUsername(email)) {
	    throw new WebApplicationException(email + " bereits registriert.");
	}

	User user = new User();
	user.setPassword(Util.createPasswordHash("SHA-256", "BASE64", null, null, password));
	user.setEmail(email);
	user.setUsername(username);
	user.setFirstName(firstName);
	user.setLastName(lastName);
	user.getRoles().add(adminRole);
	accountDao.save(user);
	LOG.info("username: " + username + "email: " + email + " firstname" + firstName
		+ " lastName: " + lastName + "password: " + password + " passwortconfirmed: "
		+ passwordConfirm);
	return "Successfully registered " + email;
    }
}

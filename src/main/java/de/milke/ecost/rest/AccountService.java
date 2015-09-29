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
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.xml.ws.WebServiceContext;

import org.jboss.security.auth.spi.Util;

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
@RolesAllowed("admin")
public class AccountService {

    static Logger LOG = Logger.getLogger(AccountService.class.getName());

    @Inject
    private AccountDao accountDao;

    @Resource
    WebServiceContext webServiceContext;

    @DELETE
    @Path("/session")
    public void deleteSession(@Context HttpServletRequest request) {
	LOG.info("Delete session: " + request.getSession().getId());
	request.getSession().invalidate();
	return;
    }

    @POST
    @Path("/register")
    public String register(@QueryParam("email") String email,
	    @QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName,
	    @QueryParam("password") String password,
	    @QueryParam("passwordConfirm") String passwordConfirm) {

	Role adminRole = accountDao.getOrCreateRole("admin");

	User user = new User();
	user.setPassword(Util.createPasswordHash("SHA-256", "BASE64", null, null, password));
	user.setEmail(email);
	user.setUsername(email);
	user.setFirstName(firstName);
	user.setLastName(lastName);
	user.getRoles().add(adminRole);
	accountDao.save(user);
	LOG.info("email: " + email + " firstname" + firstName + " lastName: " + lastName
		+ "password: " + password + " passwortconfirmed: " + passwordConfirm);
	return "super";
    }

    @POST
    @Path("/login")
    public String registerget(@QueryParam("email") String email,
	    @QueryParam("password") String password) {
	LOG.info("logged in - email: " + email + "password: " + password);
	return "successfully logged in";
    }
}

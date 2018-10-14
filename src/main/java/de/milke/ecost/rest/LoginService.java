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

import java.io.IOException;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.xml.ws.WebServiceContext;

import org.picketlink.Identity;
import org.picketlink.authorization.annotations.LoggedIn;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.model.Email;
import de.milke.ecost.model.MyUser;
import de.milke.ecost.model.UserEntity;
import de.milke.ecost.model.UserDTO;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */

@Path("/login")
@Stateless
public class LoginService {

    static Logger LOG = Logger.getLogger(LoginService.class.getName());

    @Inject
    private AccountDao accountDao;

    @Resource
    WebServiceContext webServiceContext;

    @Inject
    private Identity identity;

    @PersistenceContext(name = "primary")
    private EntityManager em;

    @EJB
    MailService mailService;

    @POST
    @Path("/logout")
    public void logout(@Context HttpServletRequest request, @Context HttpServletResponse response)
	    throws ServletException, IOException {
	int xx = 3;
	this.identity.logout();
    }

    @DELETE
    @Path("/session")
    @LoggedIn
    public void deleteSession(@Context HttpServletRequest request,
	    @Context HttpServletResponse response) throws ServletException, IOException {
	LOG.info("Delete session: " + request.getSession().getId());
	response.setHeader("Cache-Control", "no-cache, no-store");

	response.setHeader("Pragma", "no-cache");

	response.setHeader("Expires", new java.util.Date().toString());
	if (request.getSession(false) != null) {

	    request.getSession(false).invalidate();// remove session.

	}

	// if (request.getSession() != null) {
	//
	// request.getSession().invalidate();// remove session.
	//
	// }
	request.logout();
	response.sendRedirect(request.getContextPath());
	return;
    }

    @POST
    @Path("/login")
    @Produces("application/json")
    @LoggedIn
    public UserEntity registerget() {
	UserEntity user = getUser();
	LOG.info("logged in - firstname: " + user.getFirstName() + "lastname: "
		+ user.getLastName());
	if (!this.identity.isLoggedIn()) {
	    this.identity.login();
	}
	MyUser myUser = (MyUser) identity.getAccount();
	if (!myUser.isEnabled()) {
	    // send email for activatio
	    int dd = 2;
	}

	Email email = new Email("Loged in" + user.getFirstName(), "Jupiieh", user.getEmail());
	mailService.send(email);
	return user;
    }

    @PUT
    @Path("/login")
    @Produces("application/json")
    @Consumes("application/json")
    @LoggedIn
    public UserEntity updateUser(UserDTO usr) {
	LOG.info("start update user - username: " + usr.getFirstName() + "password: "
		+ usr.getLastName());
	UserEntity user = getUser();
	user.setFirstName(usr.getFirstName());
	user.setLastName(usr.getLastName());
	user.setEmail(usr.getEmail());
	user.setZipcode(usr.getZipcode());
	user = accountDao.save(user);
	// MyUser myUser = (MyUser) identity.getAccount();
	// myUser.setLoginName(usr.getUsername());
	// em.merge(myUser);
	return user;
    }

    @GET
    @Path("/login")
    @Produces("application/json")
    @LoggedIn
    public UserDTO getLoggedInUser() {
	UserEntity user = getUser();
	MyUser myUser = (MyUser) identity.getAccount();
	UserDTO userDTO = new UserDTO();
	LOG.info(
		"getuser - firstname: " + user.getFirstName() + " lastname: " + user.getLastName());
	userDTO.setEmail(user.getEmail());
	userDTO.setFirstName(user.getFirstName());
	userDTO.setId(user.getId());
	userDTO.setLastName(user.getLastName());
	userDTO.setZipcode(user.getZipcode());
	userDTO.setUsername(myUser.getLoginName());
	return userDTO;
    }

    protected UserEntity getUser() {
	MyUser user = (MyUser) identity.getAccount();
	LOG.info(identity.getAccount().getId());
	LOG.info(identity.toString());
	return user.getUser();
    }
}

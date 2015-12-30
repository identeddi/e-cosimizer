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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.ws.WebServiceContext;

import org.picketlink.authorization.annotations.LoggedIn;
import org.picketlink.authorization.annotations.RolesAllowed;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.model.ApplicationRole;
import de.milke.ecost.model.IdentityModelManager;
import de.milke.ecost.model.MyUser;
import de.milke.ecost.model.User;
import de.milke.ecost.util.MessageBuilder;

/**
 * JAX-RS Example
 * <p/>
 * This class produces a RESTful service to read/write the contents of the
 * members table.
 */
@Path("/account")
@Stateless
@LoggedIn
@RolesAllowed(ApplicationRole.ADMINISTRATOR)
public class AccountService {

    static Logger LOG = Logger.getLogger(AccountService.class.getName());

    @Inject
    private AccountDao accountDao;

    @Resource
    WebServiceContext webServiceContext;

    @Inject
    private IdentityModelManager identityModelManager;

    @POST
    @Path("enableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response enable(User passedUser) {
	MessageBuilder message;

	MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

	if (user == null) {
	    return MessageBuilder.badRequest().message("Invalid account.").build();
	}

	if (user.isEnabled()) {
	    return MessageBuilder.badRequest().message("Account is already enabled.").build();
	}

	this.identityModelManager.enableAccount(user);

	return MessageBuilder.ok().message("Account is now enabled.").build();
    }

    @POST
    @Path("disableAccount")
    @Produces(MediaType.APPLICATION_JSON)
    public Response disable(User passedUser) {
	MessageBuilder message;

	MyUser user = this.identityModelManager.findByLoginName(passedUser.getEmail());

	if (user == null) {
	    return MessageBuilder.badRequest().message("Invalid account.").build();
	}

	if (!user.isEnabled()) {
	    return MessageBuilder.badRequest().message("Accound is already disabled.").build();
	}

	this.identityModelManager.disableAccount(user);

	return MessageBuilder.ok().message("Account is now disabled.").build();
    }
}

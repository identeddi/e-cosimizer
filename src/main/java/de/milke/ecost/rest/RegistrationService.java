/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package de.milke.ecost.rest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Any;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.picketlink.idm.credential.Token;

import de.milke.ecost.dao.AccountDao;
import de.milke.ecost.dao.PowerMeasureTypeDao;
import de.milke.ecost.model.ApplicationRole;
import de.milke.ecost.model.Email;
import de.milke.ecost.model.GeneralException;
import de.milke.ecost.model.IdentityModelManager;
import de.milke.ecost.model.MyUser;
import de.milke.ecost.model.UserRegistration;
import de.milke.ecost.util.MessageBuilder;

/**
 * <p>
 * RESTFul endpoint responsible for:
 * </p>
 *
 * <ul>
 * <li>Create a new user account and send a notification with the activation
 * code.</li>
 * <li>Activate a previously created account based on a activation code..</li>
 * </ul>
 *
 * <p>
 * After a successful registration, an account is always disabled. In order to
 * enable the account and be able to log in, the activation code must be used to
 * invoke the <code>enableAccount</code> resource.
 * </p>
 *
 * @author Pedro Igor
 */
@Stateless
@Path("/register")
public class RegistrationService {

	@Inject
	private IdentityModelManager identityModelManager;

	@Inject
	@Any
	private Event<Email> event;

	@EJB
	AccountDao accountDao;

	@EJB
	PowerMeasureTypeDao powerMeasureTypeDao;

	@POST
	@Consumes("application/json")
	public String register(UserRegistration userRegistration) throws GeneralException {

		if (!userRegistration.getPassword().equals(userRegistration.getPasswordConfirmation())) {
			throw new GeneralException("Passwörter sind nicht gleich.");
		}

		try {
			// if there is no user with the provided e-mail, perform
			// registration
			if (this.identityModelManager.findByLoginName(userRegistration.getUserName()) == null) {

				MyUser newUser = this.identityModelManager.createAccount(userRegistration);

				this.identityModelManager.grantRole(newUser, ApplicationRole.USER);
				powerMeasureTypeDao.createDefaults(newUser.getUser());
				String activationCode = newUser.getActivationCode();

				sendNotification(userRegistration, activationCode);

				return activationCode;
			} else {
				throw new GeneralException(
						userRegistration.getUserName() + " Benutzer bereits registriert, bitte einen anderen wählen");
			}
		} catch (Exception e) {
			throw new GeneralException(userRegistration.getUserName() + " Unbekannter Fehler: " + e.getMessage());
		}
	}

	@POST
	@Path("/activation/{activationcode}")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes("application/json")
	public Response activateAccount(@PathParam("activationcode") String activationCode) {
		MessageBuilder message;

		try {
			Token token = this.identityModelManager.activateAccount(activationCode);
			 message = MessageBuilder.ok().message("Account erfolgreich aktiviert. Sie können sich nun einloggen");
		} catch (Exception e) {
			 message = MessageBuilder.badRequest().message("Fehler beim Aktivieren des Accounts");
		}

		return message.build();
	}

	private void sendNotification(UserRegistration request, String activationCode) {
		Email email = new Email("Please complete the signup",
				"http://localhost:8100/?activationcode=" + activationCode, request.getEmail());

		event.fire(email);
	}
}

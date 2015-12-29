package de.milke.ecost.rest;

import java.util.List;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.picketlink.authorization.annotations.LoggedIn;

import de.milke.ecost.model.User;

@Path("/private/person")
@Stateless
@LoggedIn
public class PersonService {

    @Inject
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> getAll() {
	CriteriaBuilder cb = em.getCriteriaBuilder();
	CriteriaQuery<User> criteria = cb.createQuery(User.class);
	Root<User> person = criteria.from(User.class);

	criteria.select(person).orderBy(cb.asc(person.get("firstName")));

	return em.createQuery(criteria).getResultList();

    }
}

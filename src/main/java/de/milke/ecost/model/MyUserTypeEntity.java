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
package de.milke.ecost.model;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

import org.picketlink.idm.jpa.annotations.AttributeValue;
import org.picketlink.idm.jpa.annotations.entity.IdentityManaged;
import org.picketlink.idm.jpa.model.sample.simple.IdentityTypeEntity;

/**
 * <p>
 * This is a simple example about how to extend the entity classes provided by
 * the Basic Identity Model to map your own types. In this case, this entity is
 * responsible to map
 * {@link org.jboss.as.quickstarts.picketlink.angularjs.security.model.MyUser}.
 * </p>
 *
 * <p>
 * You are not forced to use the Basic Identity Model all the time. This is just
 * an example.
 * </p>
 *
 * @author Pedro Igor
 */
@Entity
@IdentityManaged(MyUser.class)
public class MyUserTypeEntity extends IdentityTypeEntity {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    @AttributeValue
    private String loginName;

    @AttributeValue
    private String activationCode;

    @AttributeValue
    @OneToOne(cascade = CascadeType.ALL)
    private UserEntity user;

    public String getLoginName() {
	return this.loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public String getActivationCode() {
	return this.activationCode;
    }

    public void setActivationCode(String activationCode) {
	this.activationCode = activationCode;
    }

    public UserEntity getUser() {
	return user;
    }

    public void setUser(UserEntity user) {
	this.user = user;
    }

}

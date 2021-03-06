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
package de.milke.ecost.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@XmlRootElement
@Table(name = "PowerMeasureType")
public class PowerMeasureType implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public enum PeriodicNotification {
	NEVER, WEEKLY, MONTHLY, YEARLY

    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private MeasureTypeEnum typeEnum;

    @Column(nullable = false)
    private String typeName;

    @Column(nullable = false)
    private String referenceId;

    @Column(nullable = false)
    private Boolean enabled;

    @Column(nullable = false)
    private PeriodicNotification entryNotification;

    @JsonIgnore
    @ManyToOne
    private UserEntity user;

    public PowerMeasureType() {
	super();
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public String getTypeName() {
	return typeName;
    }

    public void setTypeName(String typeName) {
	this.typeName = typeName;
    }

    public UserEntity getUser() {
	return user;
    }

    public void setUser(UserEntity user) {
	this.user = user;
    }

    public String getReferenceId() {
	return referenceId;
    }

    public void setReferenceId(String referenceId) {
	this.referenceId = referenceId;
    }

    public MeasureTypeEnum getTypeEnum() {
	return typeEnum;
    }

    public void setTypeEnum(MeasureTypeEnum typeEnum) {
	this.typeEnum = typeEnum;
    }

    public Boolean getEnabled() {
	return enabled;
    }

    public void setEnabled(Boolean enabled) {
	this.enabled = enabled;
    }

    public PeriodicNotification getEntryNotification() {
	return entryNotification;
    }

    public void setEntryNotification(PeriodicNotification entryNotification) {
	this.entryNotification = entryNotification;
    }

}

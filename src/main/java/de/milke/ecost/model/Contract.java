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
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

@Entity
@XmlRootElement
@Table(name = "Contract")
public class Contract implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private Date dueDate;

    @Column
    private Integer cancellationPeriod;

    @Column
    private String contractType;

    @Column
    private String providerName;

    @Column
    private String contractName;

    @Column
    private Boolean notification;

    @ManyToOne(optional = false)
    private PowerMeasureType powerMeasureType;

    public Contract() {
	super();
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getDueDate() {
	return dueDate;
    }

    public void setDueDate(Date dueDate) {
	this.dueDate = dueDate;
    }

    public Integer getCancellationPeriod() {
	return cancellationPeriod;
    }

    public void setCancellationPeriod(Integer cancellationPeriod) {
	this.cancellationPeriod = cancellationPeriod;
    }

    public String getContractType() {
	return contractType;
    }

    public void setContractType(String contractType) {
	this.contractType = contractType;
    }

    public String getProviderName() {
	return providerName;
    }

    public void setProviderName(String providerName) {
	this.providerName = providerName;
    }

    public String getContractName() {
	return contractName;
    }

    public void setContractName(String contractName) {
	this.contractName = contractName;
    }

    public Boolean getNotification() {
	return notification;
    }

    public void setNotification(Boolean notification) {
	this.notification = notification;
    }

    public PowerMeasureType getPowerMeasureType() {
	return powerMeasureType;
    }

    public void setPowerMeasureType(PowerMeasureType powerMeasureType) {
	this.powerMeasureType = powerMeasureType;
    }

}

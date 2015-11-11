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
@Table(name = "PowerMeasure")
public class PowerMeasure implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date measureDate;

    @Column(nullable = false)
    private Double measureValue;

    @ManyToOne(optional = false)
    private PowerMeasureType powerMeasureType;

    public PowerMeasure() {
	super();
    }

    public PowerMeasure(PowerMeasureType powerMeasureType, Date measureDate, Double measureValue) {
	this.powerMeasureType = powerMeasureType;
	this.measureDate = measureDate;
	this.measureValue = measureValue;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getMeasureDate() {
	return measureDate;
    }

    public void setMeasureDate(Date measureDate) {
	this.measureDate = measureDate;
    }

    public PowerMeasureType getPowerMeasureType() {
	return powerMeasureType;
    }

    public void setPowerMeasureType(PowerMeasureType powerMeasureType) {
	this.powerMeasureType = powerMeasureType;
    }

    public Double getMeasureValue() {
	return measureValue;
    }

    public void setMeasureValue(Double measureValue) {
	this.measureValue = measureValue;
    }

    @Override
    public String toString() {
	// TODO Auto-generated method stub
	return measureDate + " abgelesen, Wert: " + measureValue;
    }

}

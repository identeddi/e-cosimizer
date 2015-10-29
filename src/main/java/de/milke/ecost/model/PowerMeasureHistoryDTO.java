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

public class PowerMeasureHistoryDTO implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    private Long id;

    private Date measureDate;

    private Double measureValue;
    private Double dailyConsumption;

    private String dataType = "";

    private User user;

    public PowerMeasureHistoryDTO() {
	super();
    }

    public PowerMeasureHistoryDTO(Long id, Date measureDate, Double measureValue, String dataType,
	    User user, double dailyConsumption) {
	super();
	this.id = id;
	this.measureDate = measureDate;
	this.measureValue = measureValue;
	this.dataType = dataType;
	this.user = user;
	this.dailyConsumption = dailyConsumption;
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

    public User getUser() {
	return user;
    }

    public void setUser(User user) {
	this.user = user;
    }

    public Double getMeasureValue() {
	return measureValue;
    }

    public void setMeasureValue(Double measureValue) {
	this.measureValue = measureValue;
    }

    public String getDataType() {
	return dataType;
    }

    public void setDataType(String dataType) {
	this.dataType = dataType;
    }

    public Double getDailyConsumption() {
	return dailyConsumption;
    }

    public void setDailyConsumption(Double dailyConsumption) {
	this.dailyConsumption = dailyConsumption;
    }

}

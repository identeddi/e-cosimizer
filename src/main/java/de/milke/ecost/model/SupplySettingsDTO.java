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

public class SupplySettingsDTO implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    private String zipcode;
    private double estimatedConsumption;
    private double passedConsumption;

    public SupplySettingsDTO() {
	super();
    }

    public SupplySettingsDTO(String zipcode, double estimatedConsumption,
	    double passedConsumption) {
	super();
	this.zipcode = zipcode;
	this.estimatedConsumption = estimatedConsumption;
	this.passedConsumption = passedConsumption;
    }

    public String getZipcode() {
	return zipcode;
    }

    public void setZipcode(String zipcode) {
	this.zipcode = zipcode;
    }

    public double getEstimatedConsumption() {
	return estimatedConsumption;
    }

    public void setEstimatedConsumption(double estimatedConsumption) {
	this.estimatedConsumption = estimatedConsumption;
    }

    public double getPassedConsumption() {
	return passedConsumption;
    }

    public void setPassedConsumption(double passedConsumption) {
	this.passedConsumption = passedConsumption;
    }

}

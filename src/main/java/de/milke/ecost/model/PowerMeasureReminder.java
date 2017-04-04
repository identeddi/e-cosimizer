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
@Table(name = "PowerMeasureReminder")
public class PowerMeasureReminder implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    public enum ReminderType {
	CONTRACT, MEASURE

    }

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private Date remindDate;

    @Column(nullable = false)
    private ReminderType reminderType;

    @ManyToOne(optional = false)
    private PowerMeasureType powerMeasureType;

    @Column(nullable = false)
    private String remindTitle;

    @Column(nullable = false)
    private String remindDescription;

    @Column(nullable = true)
    private String remindCode;

    public PowerMeasureReminder() {
	super();
    }

    public PowerMeasureReminder(Date remindDate, ReminderType reminderType,
	    PowerMeasureType powerMeasureType, String remindTitle, String remindDescription,
	    String remindCode) {
	super();
	this.remindDate = remindDate;
	this.reminderType = reminderType;
	this.powerMeasureType = powerMeasureType;
	this.remindTitle = remindTitle;
	this.remindDescription = remindDescription;
	this.remindCode = remindCode;
    }

    public Long getId() {
	return id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    public Date getRemindDate() {
	return remindDate;
    }

    public void setRemindDate(Date remindDate) {
	this.remindDate = remindDate;
    }

    public ReminderType getReminderType() {
	return reminderType;
    }

    public void setReminderType(ReminderType reminderType) {
	this.reminderType = reminderType;
    }

    public PowerMeasureType getPowerMeasureType() {
	return powerMeasureType;
    }

    public void setPowerMeasureType(PowerMeasureType powerMeasureType) {
	this.powerMeasureType = powerMeasureType;
    }

    public String getRemindTitle() {
	return remindTitle;
    }

    public void setRemindTitle(String remindTitle) {
	this.remindTitle = remindTitle;
    }

    public String getRemindDescription() {
	return remindDescription;
    }

    public void setRemindDescription(String remindDescription) {
	this.remindDescription = remindDescription;
    }

    public String getRemindCode() {
	return remindCode;
    }

    public void setRemindCode(String remindCode) {
	this.remindCode = remindCode;
    }

}

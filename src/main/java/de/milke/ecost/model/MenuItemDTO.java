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

public class MenuItemDTO implements Serializable {
    /** Default value included to remove warning. Remove or modify at will. **/
    private static final long serialVersionUID = 1L;

    String caption;
    String url;

    long powerMeasureTypeid;

    public MenuItemDTO() {
	super();
    }

    public MenuItemDTO(String caption, String url, long powerMeasureTypeid) {
	super();
	this.caption = caption;
	this.url = url;
	this.powerMeasureTypeid = powerMeasureTypeid;
    }

    public String getCaption() {
	return caption;
    }

    public void setCaption(String caption) {
	this.caption = caption;
    }

    public String getUrl() {
	return url;
    }

    public void setUrl(String url) {
	this.url = url;
    }

    public long getPowerMeasureTypeid() {
	return powerMeasureTypeid;
    }

    public void setPowerMeasureTypeid(long powerMeasureTypeid) {
	this.powerMeasureTypeid = powerMeasureTypeid;
    }

}

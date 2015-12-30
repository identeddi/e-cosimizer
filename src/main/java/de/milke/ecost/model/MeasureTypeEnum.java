package de.milke.ecost.model;

public enum MeasureTypeEnum {

    GAS(1), STROM(2), AUTO(3), DSL(4), MOBILE(5), OTHER(99);

    private int value;

    private MeasureTypeEnum(int value) {
	this.value = value;
    }
}

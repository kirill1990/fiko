package ru.fiko.purchase.supports;

public class ComboItemStringValue {
    private String value;
    private String label;

    public ComboItemStringValue(String value, String label) {
	this.value = value;
	this.label = label;
    }

    public String getValue() {
	return this.value;
    }

    public String getLabel() {
	return this.label;
    }

    @Override
    public String toString() {
	return label;
    }
}

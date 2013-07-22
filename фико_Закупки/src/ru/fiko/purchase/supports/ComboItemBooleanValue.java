package ru.fiko.purchase.supports;

public class ComboItemBooleanValue {
    private boolean value;
    private String label;

    public ComboItemBooleanValue(boolean value, String label) {
	this.value = value;
	this.label = label;
    }

    public boolean getValue() {
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

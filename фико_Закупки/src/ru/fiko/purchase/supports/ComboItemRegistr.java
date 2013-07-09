package ru.fiko.purchase.supports;

public class ComboItemRegistr {
    private boolean value;
    private String label;

    public ComboItemRegistr(boolean value, String label) {
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

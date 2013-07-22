package ru.fiko.purchase.supports;

public class ComboItemIntValue {
    private int value;
    private String label;

    public ComboItemIntValue(int value, String label) {
	this.value = value;
	this.label = label;
    }

    public int getValue() {
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

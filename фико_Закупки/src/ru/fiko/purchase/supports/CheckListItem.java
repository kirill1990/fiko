package ru.fiko.purchase.supports;

public class CheckListItem {
    private String label;
    private boolean isSelected = false;
    private int value;

    public CheckListItem(String label, int value) {
	this.label = label;
	this.value = value;
    }

    public boolean isSelected() {
	return isSelected;
    }

    public void setSelected(boolean isSelected) {
	this.isSelected = isSelected;
    }

    public String toString() {
	return label;
    }

    public int getValue() {
	return this.value;
    }
}

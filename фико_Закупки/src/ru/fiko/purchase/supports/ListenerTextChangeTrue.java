package ru.fiko.purchase.supports;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JFormattedTextField;

public class ListenerTextChangeTrue implements FocusListener {

    @Override
    public void focusGained(FocusEvent arg0) {

    }

    @Override
    public void focusLost(FocusEvent arg0) {
        JFormattedTextField t = (JFormattedTextField) arg0.getSource();

        String str = t.getText();

        int lastIndex = str.lastIndexOf(" руб.");

        if (lastIndex >= 0)
    	str = str.substring(0, str.lastIndexOf(" руб."));
        
        str = str.replaceAll(" ", "");
        
        t.setText(str + " руб.");
    }

}

package com.maqautocognita.scene2d.ui;

/**
 * @author sc.chi csc19840914@gmail.com
 */

public class NumberCell extends TextCell {

    public NumberCell() {
        super();
    }
    public void setNumber(int number) {
        setText(String.valueOf(number));
    }

}

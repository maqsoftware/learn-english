package com.maqautocognita.constant;


import com.badlogic.gdx.graphics.Color;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public enum HairColorEnum {

    BLACK("black", Color.valueOf("181818")), BLONDE("blonde", Color.valueOf("f1c565")),
    BROWN("brown", Color.valueOf("5f3c28")),
    GREY("grey", Color.valueOf("756c5a")), RED("red", Color.valueOf("5e2322"));

    public final String color;
    public final Color colorCode;

    HairColorEnum(String color, Color colorCode) {
        this.color = color;
        this.colorCode = colorCode;
    }

    public static HairColorEnum getHairColorEnumByColor(Color colorCode) {
        for (HairColorEnum hairColorEnum : HairColorEnum.values()) {
            if (hairColorEnum.colorCode.equals(colorCode)) {
                return hairColorEnum;
            }
        }

        return null;
    }

}

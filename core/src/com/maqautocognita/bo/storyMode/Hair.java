package com.maqautocognita.bo.storyMode;

import com.maqautocognita.constant.HairColorEnum;

/**
 * This is mainly store the hair information for the character in the story mode
 *
 * @author sc.chi csc19840914@gmail.com
 */
public class Hair {

    public int id;

    public boolean isGirl;

    public boolean isBoy;
    public String imageName;
    public String frontImageName;
    public String rearImageName;
    public int frontHairXPositionToHead;
    public int frontHairYPositionToHead;
    public Integer rearHairXPositionToHead;
    public Integer rearHairYPositionToHead;
    public int frontImageHeight;
    public int frontImageWidth;
    public Integer rearImageWidth;
    public Integer rearImageHeight;
    public int groupId;
    private HairColorEnum color;

    public HairColorEnum getColor() {
        return color;
    }

    public void setColor(String color) {
        for (HairColorEnum hairColorEnum : HairColorEnum.values()) {
            if (hairColorEnum.color.equalsIgnoreCase(color)) {
                this.color = hairColorEnum;
            }
        }
    }
}

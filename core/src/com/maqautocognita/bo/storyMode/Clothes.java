package com.maqautocognita.bo.storyMode;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class Clothes {
    public int id;
    public String imageName;
    public String frontImageName;
    public String rearImageName;
    public String word;
    public boolean isGirl;
    public boolean isBoy;
    public int selectionLevel;
    public int anotherSelectionLevel;
    public int imageWidth;
    public int imageHeight;
    public int xPositionToBody;
    public int yPositionToBody;
    public int level;

    @Override
    public String toString() {
        return "Clothes{" +
                "id=" + id +
                ", imageName='" + imageName + '\'' +
                ", frontImageName='" + frontImageName + '\'' +
                ", rearImageName='" + rearImageName + '\'' +
                ", word='" + word + '\'' +
                ", isGirl=" + isGirl +
                ", isBoy=" + isBoy +
                ", selectionLevel=" + selectionLevel +
                ", anotherSelectionLevel=" + anotherSelectionLevel +
                ", imageWidth=" + imageWidth +
                ", imageHeight=" + imageHeight +
                ", xPositionToBody=" + xPositionToBody +
                ", yPositionToBody=" + yPositionToBody +
                ", level=" + level +
                '}';
    }
}

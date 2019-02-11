package com.maqautocognita.utils;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.Arrays;
import java.util.Random;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public final class RandomUtils {

    private static Random RANDOM = new Random();

    public static Vector2[] getRandomPositions(int numberOfRandomPosition, Rectangle withinArea, Vector2 objectSize) {

        if (null != withinArea && numberOfRandomPosition > 0) {
            Vector2[] randomPositions = new Vector2[numberOfRandomPosition];

            for (int i = 0; i < numberOfRandomPosition; i++) {
                generateRandomPosition(withinArea, randomPositions, i, objectSize);
            }
            return randomPositions;
        }

        return null;
    }

    private static void generateRandomPosition(Rectangle withinArea, Vector2[] randomPositions, int randomPositionIndex, Vector2 objectSize) {
        boolean isOverlay = true;

        while (isOverlay) {
            //TODO the random logic is not efficiency, reduce the random range after a object is generated
            double randomXPosition = withinArea.x + (Math.random() * (withinArea.width - objectSize.x)) + 1;
            double randomYPosition = withinArea.y + (Math.random() * (withinArea.height - objectSize.y)) + 1;
            if (!isOverlapped(randomXPosition, randomYPosition, objectSize, randomPositions)) {
                randomPositions[randomPositionIndex] = new Vector2((float) randomXPosition, (float) randomYPosition);
                isOverlay = false;
            }
        }
    }

    private static boolean isOverlapped(double randomXPosition, double randomYPosition, Vector2 objectSize, Vector2[] randomPositions) {
        if (ArrayUtils.isNotEmpty(randomPositions) && null != objectSize) {
            for (Vector2 randomPosition : randomPositions) {
                if (null != randomPosition) {
                    Rectangle existsObject = new Rectangle(randomPosition.x, randomPosition.y, objectSize.x, objectSize.y);
                    if (null != randomPosition && isOverLapped(randomXPosition, randomYPosition, objectSize.x, objectSize.y, existsObject)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private static boolean isOverLapped(double x, double y, float width, float height, Rectangle existsObject) {
        return existsObject.x < x + width
                && existsObject.x + existsObject.width > x &&
                existsObject.y < y + height
                && existsObject.y + existsObject.height > y;
    }

    public static boolean isOverlapped(double randomXPosition, double randomYPosition, float width, float height, Rectangle[] existsObjects) {
        if (ArrayUtils.isNotEmpty(existsObjects)) {
            for (Rectangle randomPosition : existsObjects) {
                if (null != randomPosition && isOverLapped(randomXPosition, randomYPosition, width, height, randomPosition)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static int getRandomWithExclusion(int start, int end, int... excludeNumbers) {
        int randomNumber = start + RANDOM.nextInt(end + 1 - start);
        randomNumber = doCheckingOnRandomNumber(randomNumber, excludeNumbers);

        if (randomNumber > end) {
            randomNumber = doCheckingOnRandomNumber(start, excludeNumbers);
        }

        return randomNumber;
    }

    private static int doCheckingOnRandomNumber(int randomNumber, int... excludeNumbers) {
        if (null != excludeNumbers && excludeNumbers.length > 0) {
            Arrays.sort(excludeNumbers);
            for (int excludeNumber : excludeNumbers) {
                if (randomNumber < excludeNumber) {
                    break;
                }
                randomNumber++;

            }
        }
        return randomNumber;
    }

}

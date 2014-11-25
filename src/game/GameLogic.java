package game;

import solver.Direction;
import solver.Grid;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hallvard on 24.11.2014.
 */
public class GameLogic {


    public static Grid simulateMove(Grid grid, Direction direction) {

        int[] array = rotate(grid.grid, direction.degrees);

        boolean changesMade = false;
        for(int i=0; i<4; i++){
            int[] original = getLine(grid.grid, i);
            int[] merged = mergeLine(moveLine(original));
            array = setLine(i, merged, array);

            for(int j=0; j<original.length; j++)
                if(original[j]!=merged[j])
                    changesMade = true;
        }
        if(!changesMade)
            return null;

        return new Grid(rotate(array,360 - direction.degrees));
    }

    public static int[] rotate(int[] array, int angle) {

        if(angle==0 || angle==360)
            return array;

        int[] rotated = new int[4 * 4];
        int offsetX = 3, offsetY = 3;
        if (angle == 90) {
            offsetY = 0;
        } else if (angle == 270) {
            offsetX = 0;
        }

        double rad = Math.toRadians(angle);
        int cos = (int) Math.cos(rad);
        int sin = (int) Math.sin(rad);
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int newX = (x * cos) - (y * sin) + offsetX;
                int newY = (x * sin) + (y * cos) + offsetY;
                rotated[(newX) + (newY) * 4] = array[x + y*4];
            }
        }
        return rotated;
    }

    private static int[] moveLine(int[] original) {
        LinkedList<Integer> l = new LinkedList<Integer>();
        for (int i = 0; i < 4; i++) {
            if (original[i]!=0)
                l.addLast(original[i]);
        }
        if (l.size() == 0) {
            return original;
        } else {
            int[] newLine = new int[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private static int[] mergeLine(int[] oldLine) {
        LinkedList<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < 4 && oldLine[i]!=0; i++) {
            int num = oldLine[i];
            if (i < 3 && oldLine[i] == oldLine[i + 1]) {
                num *= 2;
                i++;
            }
            list.add(num);
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, 4);
            int[] array = new int[4];
            Integer[] a2 = list.toArray(new Integer[4]);
            for(int j=0; j<4; j++)
                array[j] = a2[j];
            return array;
        }
    }
    private static void ensureSize(java.util.List<Integer> l, int s) {
        while (l.size() != s) {
            l.add(0);
        }
    }
    private static int[] getLine(int[] array,int index) {
        int[] result = new int[4];
        for (int i = 0; i < 4; i++) {
            result[i] = array[i+index*4];
        }
        return result;
    }

    public static int[] setLine(int index, int[] re, int[] array) {

        for(int i=0; i<4; i++) {
            array[(index*4)+i] = re[i];
        }
        return array;
    }

    public static int[] copyArray(int[] array) {
        int[] copy = new int[array.length];
        for(int i=0; i<array.length; i++) {
            copy[i] = array[i];
        }
        return copy;
    }












    public static boolean moveLeft(int[][] grid){
        boolean hasChanged = false;
        for (int y = 0; y < grid.length; y++){
            int current = 0;
            int currentPos = 0;
            //add up
            for (int x = 0; x < grid[0].length; x++){
                if (current == 0){
                    current = grid[x][y];
                    currentPos = x;
                    continue;
                }
                if (grid[x][y] == current){
                    grid[currentPos][y] = current*2;
                    grid[x][y] = 0;
                    hasChanged = true;
                    current = 0;
                    currentPos = x;
                    continue;
                }
                if (grid[x][y] != 0){
                    current = grid[x][y];
                    currentPos = x;
                }

            }
            //move
            int oldZero = -1;
            for (int x = 0; x < grid[0].length; x++){
                if (grid[x][y] == 0 && oldZero == -1){
                    oldZero = x;
                    continue;
                }
                if (oldZero != -1 && grid[x][y] != 0){
                    grid[oldZero][y] = grid[x][y];
                    grid[x][y] = 0;
                    hasChanged = true;
                    oldZero++;

                }
            }
        }
        return hasChanged;
    }

    public static boolean moveRight(int[][] grid){
        boolean hasChanged = false;
        for (int y = 0; y < grid.length; y++){
            int current = 0;
            int currentPos = 3;
            //add up
            for (int x = 3; x >= 0; x--){
                if (current == 0){
                    current = grid[x][y];
                    currentPos = x;
                    continue;
                }
                if (grid[x][y] == current){
                    grid[currentPos][y] = current*2;
                    grid[x][y] = 0;
                    hasChanged = true;
                    current = 0;
                    currentPos = x;
                    continue;
                }
                if (grid[x][y] != 0){
                    current = grid[x][y];
                    currentPos = x;
                }

            }
            //move
            int oldZero = -1;
            for (int x = 3; x >= 0; x--){
                if (grid[x][y] == 0 && oldZero == -1){
                    oldZero = x;
                    continue;
                }
                if (oldZero != -1 && grid[x][y] != 0){
                    grid[oldZero][y] = grid[x][y];
                    grid[x][y] = 0;
                    hasChanged = true;
                    oldZero--;
                }
            }
        }
        return hasChanged;
    }

    public static boolean moveDown(int[][] grid){
        boolean hasChanged = false;
        for (int x = 0; x < grid.length; x++){
            int current = 0;
            int currentPos = 3;
            //add up
            for (int y = 3; y >= 0; y--){
                if (current == 0){
                    current = grid[x][y];
                    currentPos = y;
                    continue;
                }
                if (grid[x][y] == current){
                    grid[x][currentPos] = current*2;
                    grid[x][y] = 0;
                    hasChanged = true;
                    current = 0;
                    currentPos = y;
                    continue;
                }
                if (grid[x][y] != 0){
                    current = grid[x][y];
                    currentPos = y;
                }

            }
            //move
            int oldZero = -1;
            for (int y = 3; y >= 0; y--){
                if (grid[x][y] == 0 && oldZero == -1){
                    oldZero = y;
                    continue;
                }
                if (oldZero != -1 && grid[x][y] != 0){
                    grid[x][oldZero] = grid[x][y];
                    grid[x][y] = 0;
                    hasChanged = true;
                    oldZero--;
                }
            }
        }
        return hasChanged;
    }

    public static boolean moveUp(int[][] grid){
        boolean hasChanged = false;
        for (int x = 0; x < grid.length; x++){
            int current = 0;
            int currentPos = 0;
            //add up
            for (int y = 0; y < grid[0].length; y++){
                if (current == 0){
                    current = grid[x][y];
                    currentPos = y;
                    continue;
                }
                if (grid[x][y] == current){
                    grid[x][currentPos] = current*2;
                    grid[x][y] = 0;
                    hasChanged = true;
                    current = 0;
                    currentPos = y;
                    continue;
                }
                if (grid[x][y] != 0){
                    current = grid[x][y];
                    currentPos = y;
                }

            }
            //move
            int oldZero = -1;
            for (int y = 0; y < grid[0].length; y++){
                if (grid[x][y] == 0 && oldZero == -1){
                    oldZero = y;
                    continue;
                }
                if (oldZero != -1 && grid[x][y] != 0){
                    grid[x][oldZero] = grid[x][y];
                    grid[x][y] = 0;
                    hasChanged = true;
                    oldZero++;

                }
            }
        }
        return hasChanged;
    }
}

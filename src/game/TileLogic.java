package game;

import solver.Direction;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Hallvard on 24.11.2014.
 */
public class TileLogic {

    public static java.util.List<Tile> availableSpace(Tile[] tiles) {
        final List<Tile> list = new ArrayList<Tile>(16);
        for (Tile tile : tiles)
            if (tile.isEmpty())
                list.add(tile);
        return list;
    }


    public static Tile[] simulateMove(Tile[] tils, Direction direction) {
        Tile[] tiles = deepCopy(tils);
        tiles = rotate(tiles,direction.degrees);

        boolean changesMade = false;
        for(int i=0; i<4; i++){
            Tile[] original = getLine(tiles,i);
            Tile[] merged = mergeLine(moveLine(original));
            setLine(tiles, i, merged);
            if(!compare(original, merged))
                changesMade = true;
        }
        if(!changesMade)
            return null;

        tiles = rotate(tiles, 360 - direction.degrees);

        return tiles;
    }

    public static Tile[] deepCopy(Tile[] tils) {
        Tile[] copy = new Tile[tils.length];
        for(int i=0; i<tils.length; i++)
            copy[i] = new Tile(tils[i].value);
        return copy;
    }

    public static boolean isFull(Tile[] tiles) {
        return availableSpace(tiles).size() == 0;
    }

    public static boolean canMove(Tile[] tiles) {
        if (!isFull(tiles)) {
            return true;
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile t = tileAt(tiles,x, y);
                if ((x < 3 && t.value == tileAt(tiles, x + 1, y).value)
                        || ((y < 3) && t.value == tileAt(tiles, x, y + 1).value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean compare(Tile[] focal, Tile[] nonFocal){
        if(focal == nonFocal)
            return true;
        else if(focal.length != nonFocal.length)
            return false;

        for(int i=0; i<focal.length; i++) {
            if(focal[i].value != nonFocal[i].value)
                return false;
        }
        return true;
    }

    private static Tile[] rotate(Tile[] tiles, int angle) {

        if(angle==0 || angle==360)
            return tiles;

        Tile[] rotated = new Tile[4 * 4];
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
                rotated[(newX) + (newY) * 4] = tileAt(tiles, x, y);
            }
        }
        return rotated;
    }

    private static Tile[] moveLine(Tile[] oldLine) {
        LinkedList<Tile> l = new LinkedList<Tile>();
        for (int i = 0; i < 4; i++) {
            if (!oldLine[i].isEmpty())
                l.addLast(oldLine[i]);
        }
        if (l.size() == 0) {
            return oldLine;
        } else {
            Tile[] newLine = new Tile[4];
            ensureSize(l, 4);
            for (int i = 0; i < 4; i++) {
                newLine[i] = l.removeFirst();
            }
            return newLine;
        }
    }

    private static Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].value;
            if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
                num *= 2;
                i++;
            }
            list.add(new Tile(num));
        }
        if (list.size() == 0) {
            return oldLine;
        } else {
            ensureSize(list, 4);
            return list.toArray(new Tile[4]);
        }
    }

    private static void ensureSize(java.util.List<Tile> l, int s) {
        while (l.size() != s) {
            l.add(new Tile());
        }
    }
    private static Tile[] getLine(Tile[] tiles, int index) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++) {
            result[i] = tileAt(tiles,i, index);
        }
        return result;
    }

    private static void setLine(Tile[] tiles, int index, Tile[] re) {
        System.arraycopy(re, 0, tiles, index * 4, 4);
    }

    private static Tile tileAt(Tile[] tiles, int x, int y) {
        return tiles[x + y * 4];
    }

}

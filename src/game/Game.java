package game;

import solver.Direction;

import java.awt.*;
import java.util.List;
import java.util.*;

/**
 * Created by Hallvard on 21.11.2014.
 */
public class Game {

    public final int ROW = 4;
    public  final double PROBABILITY_2 = 0.9;

    private Tile[] tiles;

    public boolean won = false;
    public boolean lost = false;

    int score = 0;

    public Game() {}

    public void resetGame() {
        score = 0;
        won = false;
        lost = false;
        tiles = new Tile[ROW*ROW];
        for(int i=0; i<tiles.length; i++)
            tiles[i] = new Tile();
        addTile();  addTile();  //Add two tiles
    }

    private void addTile() {
        List<Tile> list = availableSpace();
        if(!availableSpace().isEmpty()) {
            int index = (int) (Math.random() * list.size()) % list.size();
            Tile selected = list.get(index);
            selected.value = Math.random() < PROBABILITY_2 ? 2 : 4;
        }
    }
    private java.util.List<Tile> availableSpace() {
        final List<Tile> list = new ArrayList<Tile>(16);
        for (Tile tile : tiles)
            if (tile.isEmpty())
                list.add(tile);
        return list;
    }

    public void move(Direction direction) {

        if(direction==null){
            lost = true;
            return;
        }

        tiles = rotate(direction.degrees);

        boolean addTile = false;
        for(int i=0; i<4; i++){
            Tile[] original = getLine(i);
            Tile[] merged = mergeLine(moveLine(original));
            setLine(i, merged);
            if(!addTile && !compare(original, merged))
                addTile = true;
        }
        if(addTile)
            addTile();

        tiles = rotate(360 - direction.degrees);
    }

    private boolean isFull() {
        return availableSpace().size() == 0;
    }

    public boolean canMove() {
        if (!isFull()) {
            return true;
        }
        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Tile t = tileAt(x, y);
                if ((x < 3 && t.value == tileAt(x + 1, y).value)
                        || ((y < 3) && t.value == tileAt(x, y + 1).value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean compare(Tile[] focal, Tile[] nonFocal){
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

    private Tile[] rotate(int angle) {

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
                rotated[(newX) + (newY) * 4] = tileAt(x, y);
            }
        }
        return rotated;
    }

    private Tile[] moveLine(Tile[] oldLine) {
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

    private Tile[] mergeLine(Tile[] oldLine) {
        LinkedList<Tile> list = new LinkedList<Tile>();
        for (int i = 0; i < 4 && !oldLine[i].isEmpty(); i++) {
            int num = oldLine[i].value;
            if (i < 3 && oldLine[i].value == oldLine[i + 1].value) {
                num *= 2;
                score += num;
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
    private Tile[] getLine(int index) {
        Tile[] result = new Tile[4];
        for (int i = 0; i < 4; i++) {
            result[i] = tileAt(i, index);
        }
        return result;
    }

    private void setLine(int index, Tile[] re) {
        System.arraycopy(re, 0, tiles, index * 4, 4);
    }

    private Tile tileAt(int x, int y) {
        return tiles[x + y * 4];
    }

    public int getScore() {
        return score;
    }

    public Tile[] getTiles() {
        return tiles;
    }

}
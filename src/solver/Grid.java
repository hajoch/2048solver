package solver;

import game.GameLogic;
import game.Tile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hallvard on 18.11.2014.
 */
public class Grid {
    public int[] grid;

    private double probability = 1;

    public Grid(int[] grid) {
        this.grid = grid;
    }

    public Grid(int[] grid, double probability) {
        this.grid = grid;
        this.probability = probability;
    }

    public Direction[] getPossibleMoves() {

        ArrayList<Direction> dirsa = new ArrayList<Direction>();
        for(Direction dir : Direction.values()){
            if(GameLogic.simulateMove(new Grid(grid.clone(), probability), dir)!= null)
                dirsa.add(dir);
        }
        return dirsa.toArray(new Direction[dirsa.size()]);


/*        Direction[] dirs = new Direction[4];
        dirs[0] = Direction.DOWN;
        dirs[1] = Direction.LEFT;
        dirs[2] = Direction.RIGHT;
        dirs[3] = Direction.UP;
        return dirs;//Direction.values(); //TODO remove unnceccesary directions for the grid.*/
    }

    public List<Grid> getTilePossibilities() {
        ArrayList<Grid> possibilities = new ArrayList<Grid>();
        for(int i=0; i<16;i++) {
            if(grid[i]==0) {
                int[] copy = new int[4*4];
                int[] copy2= new int[4*4];
                System.arraycopy(grid, 0, copy, 0, 4*4);
                System.arraycopy(grid, 0, copy2, 0, 4*4);
                copy[i] = 2;
                copy2[i] = 4;
                possibilities.add(new Grid(copy, 0.9));
                possibilities.add(new Grid(copy2, 0.1));
            }
        }
        return possibilities;
    }

    public double getProbability() {
        return probability;
    }
}

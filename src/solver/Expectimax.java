package solver;

import game.GameLogic;
import game.Tile;

/**
 * Created by Hallvard on 18.11.2014.
 */
public class Expectimax {

    //  The weights in the gradient system. Which will make the AI favor big numbers
    //int the bottom right corner.
    public final int[] weigth1 = new int[]{
            -3,-2,-1, 0,
            -2,-1, 0, 1,
            -1, 0, 1, 2,
            0,  1, 2, 3
    };

    public final int[] weigth2 = new int[]{
            3, 2, 1, 0,
            2, 1, 0,-1,
            1, 0,-1,-2,
            0,-1,-2,-3
    };
    public final int[] weigth3 = new int[]{
            0,1,2,3,
            -1,0,1,2,
            -2,-1,0,1,
            -3,-2,-1,0
    };
    public final int[] weigth4 = new int[]{
            0,-1,-2,-3,
            1,0,-1,-2,
            2,1,0,-1,
            3,2,1,0
    };
    public final int[] superGradient = new int[] {
            -3,-4,-5,-6,
            -2,-1, 0, 1,
             5, 4, 3, 2,
             6, 8, 10, 20
    };


    public final int[] special1 = new int[]{
            -3,-4, -5,-6,
            -2,-1, 0, 0,
            4,3,2, 1,
            5,6,8, 12
    };
    public final int[] special2 = new int[]{
             0,-3,-3, 0,
             1,-1,-1, 1,
             2, 1, 1, 2,
             4, 6, 8, 12
    };

    public int MAXDEPTH = 6;


    public Direction nextMove(Tile[] tiles){

        int[] array = new int[4*4];

        int maxVal = 0;
        int emptyTiles = 0;
        int tilesOverThousand = 0;

        for(int i=0; i<tiles.length; i++) {
            array[i] = tiles[i].value;
            if(array[i] > maxVal)
                maxVal = array[i];
            if(array[i]==0){
                emptyTiles++;
            }
            if(array[i] > 1000)
                tilesOverThousand++;
        }
        MAXDEPTH = 6;

        if(maxVal > 2048) {
            if(emptyTiles < 5)
                MAXDEPTH = 8;
            if ( maxVal > 8000) {
                if(emptyTiles < 2 && tilesOverThousand > 1) {
                        MAXDEPTH = 10;
                } else {
                    MAXDEPTH = 8;
                }
            }
        }
        if(tilesOverThousand > 3 && emptyTiles < 2)
            MAXDEPTH = 12;


        Score score = bestScore(new Grid(array), MAXDEPTH);
        System.out.println(score.direction+": "+score.score);

        return score.direction;
    }
    private Score bestScore(Grid grid, int depth) {
        if(depth == 0){
            if(hasMove(grid))
                return heurastics(grid);
            else
                return new Score(-1, null);
        }
        int bestScore = -1;
        Direction bestDirection = null;

        for(Direction dir : Direction.values()){

            int[][] temp = new int[4][4];
            for(int i=0; i<4*4; i++) {
                temp[i%4][(int)Math.floor(i/4)] = grid.grid[i];
            }

            boolean moved;

            if(dir == Direction.DOWN){
                moved =GameLogic.moveDown(temp);
            } else if(dir == Direction.UP)
                moved = GameLogic.moveUp(temp);
            else if(dir == Direction.LEFT)
                moved = GameLogic.moveLeft(temp);
            else
                moved = GameLogic.moveRight(temp);


            if (!moved) {
                continue;
            }
            int[] aa = new int[4*4];
            for(int x=0; x<4; x++) {
                for(int y=0; y<4; y++) {
                    aa[(y*4)+x] = temp[x][y];
                }
            }
            Grid g = new Grid(aa);

            int score = averageScore(g, depth-1);

            if(score >= bestScore) {
                bestScore = score;
                bestDirection = dir;
            }
        }
        return new Score(bestScore, bestDirection);
    }

    private int averageScore(Grid grid, int depth) {
        double totalScore = 0;
        double totalWeight = 0;
        for(Grid next : grid.getTilePossibilities()) {
            int score = bestScore(next, depth-1).score;

            totalScore += (score * next.getProbability());
            totalWeight += next.getProbability();
        }
        return (int)(totalScore/totalWeight);
    }


    private Score heurastics(Grid grid) {
        int score1=0, score2=0,   score3=0,   score4=0, score=0;
        for(int i=0; i<grid.grid.length;i++) {
            score1 += (grid.grid[i]*grid.grid[i]) * weigth1[i];
//            score2 += (grid.grid[i]*grid.grid[i]) * weigth2[i];
//            score3 += (grid.grid[i]*grid.grid[i]) * weigth3[i];
  //          score4 += (grid.grid[i]*grid.grid[i]) * weigth4[i];
    //        score  += (grid.grid[i]*grid.grid[i]) * superGradient[i];
//            score1 += (grid.grid[i]*grid.grid[i]) * special1[i];
  //          score2 += (grid.grid[i]*grid.grid[i]) * special2[i];
        }
        return new Score(score1, null);
//        return new Score(Math.max(Math.max(Math.max(score1, score2),score3),score4), null);
    }

    private boolean hasMove(Grid grid) {
        for(int i : grid.grid)
            if(i==0)
                return true;

        int[][] temp = new int[4][4];
        for(int i=0; i<4*4; i++) {
            temp[i%4][(int)Math.floor(i/4)] = grid.grid[i];
        }
        if(GameLogic.moveDown(temp))
            return true;
        if(GameLogic.moveUp(temp))
            return true;
        if(GameLogic.moveLeft(temp))
            return true;
        if(GameLogic.moveRight(temp))
            return true;
        return false;
    }

    public static class Score {
        public final int score;
        public final Direction direction;
        public Score(int score, Direction direction) {
            this.score = score;
            this.direction = direction;
        }
    }
}


/*
        if(counter<30) {
            counter++;
            if(counter%10==0)
                return Direction.UP;
            if(counter%2==0)
                return Direction.DOWN;
            else
                return Direction.RIGHT;
        }*/

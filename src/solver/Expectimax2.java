package game2048.expectimax;


import javafx.application.Platform;

import java.util.Map;

/**
 * Created by Kyrre on 15.11.2014.
 */

/*
public class Expectimax {
    private GameManager gameManager;

    public Expectimax(GameManager gameManager) {
        this.gameManager = gameManager;
    }
    public void play() {
        while (true){
            expectiMax(gameManager.getGameGrid());
        }
    }

    void expectiMax(Map<Location, Tile> gameGrid){
        int[][] grid = new int[4][4];
        for (Location l: gameGrid.keySet()){
            if (gameGrid.get(l) == null){
                grid[l.getX()][l.getY()] = 0;
            }else {
                grid[l.getX()][l.getY()] = gameGrid.get(l).getValue();
            }
        }
        Direction bestMove = playerBestScore(grid,6).direction;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                gameManager.move(bestMove);
            }
        });
    }

    private ScoreDirection playerBestScore(int[][] grid, int depth) {
        if (depth == 0){
            if (!moveDown(deepCopyGrid(grid)) && !moveUp(deepCopyGrid(grid)) && !moveLeft(deepCopyGrid(grid)) && !moveRight(deepCopyGrid(grid))){
                return new ScoreDirection(null,0);
            }
            return gradient(grid);
        }

        double bestScore = 0;
        Direction bestDir = null;

        int[][] up = deepCopyGrid(grid);
        if (moveUp(up)){
            double upScore = computerAverageScore(up, depth-1);
            if (upScore > bestScore){
                bestDir = Direction.UP;
                bestScore = upScore;
            }

        }

        int[][] down = deepCopyGrid(grid);
        if (moveDown(down)){
            double downScore = computerAverageScore(down, depth-1);
            if(downScore > bestScore){
                bestDir = Direction.DOWN;
                bestScore = downScore;
            }
        }

        int[][] left = deepCopyGrid(grid);
        if (moveLeft(left)){
            double leftScore = computerAverageScore(left, depth-1);
            if (leftScore > bestScore){
                bestDir = Direction.LEFT;
                bestScore = leftScore;
            }
        }

        int[][] right = deepCopyGrid(grid);
        if(moveRight(right)){
            double rightScore = computerAverageScore(right, depth-1);
            if (rightScore > bestScore){
                bestDir = Direction.RIGHT;
                bestScore = rightScore;
            }
        }
        return new ScoreDirection(bestDir, bestScore);
    }

    private double computerAverageScore(int[][] grid, int depth) {
        double totalScore = 0;
        double totalWeight = 0;
        for (int y = 0; y < grid.length; y++){
            for (int x = 0; x < grid.length; x++){
                if (grid[y][x] == 0){
                    int[][] two = deepCopyGrid(grid);
                    int[][] four = deepCopyGrid(grid);

                    two[y][x] = 2;
                    four[y][x] = 4;

                    double score = playerBestScore(two, depth-1).score;
                    totalScore += score * 0.9;
                    totalWeight += 0.9;

                    score = playerBestScore(four, depth-1).score;
                    totalScore += score * 0.1;
                    totalWeight += 0.1;
                }
            }
        }
        return totalScore / totalWeight;
    }

    private boolean moveLeft(int[][] grid){
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

    private boolean moveRight(int[][] grid){
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

    private boolean moveDown(int[][] grid){
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

    private boolean moveUp(int[][] grid){
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

    private ScoreDirection gradient(int[][] grid) {
        int grad0 = 0, grad1 = 0, grad2 = 0, grad3 =0;
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid.length; x++) {
                int value = grid[x][y];
                grad0 += (3-(x+y))*value;
                grad1 += ((3-(x+y))*-1)*value;
                grad2 += (3-(3-x+y))*value;
                grad3 += ((3-(3-x+y))*-1)*value;
            }
        }
        int grad = Math.max(Math.max(grad0,grad1),Math.max(grad2,grad3));
        return new ScoreDirection(null,grad);
    }

    private void printGrid(int[][] grid) {
        for (int y = 0; y < grid.length; y++){
            System.out.println();
            for (int x = 0; x < grid.length; x++){
                System.out.print(grid[x][y] + " ");
            }
        }
        System.out.println("\n---------------------------------------");
    }

    private int[][] deepCopyGrid(int[][] grid) {
        int[][] copy = new int[grid.length][grid.length];
        for (int y = 0; y < copy.length; y++){
            for (int x = 0; x < copy.length; x++){
                copy[y][x] = grid[y][x];
            }
        }
        return copy;
    }
}


*/
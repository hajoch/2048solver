package gui;

import game.Game;
import game.GameLogic;
import game.Tile;
import solver.Direction;
import solver.Expectimax;
import solver.Grid;


import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.*;
import javax.swing.JOptionPane;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Hallvard on 18.11.2014.
 */
public class GUI extends JPanel implements Runnable{

    private final int DELAY = 1000; //milliseconds

    private final Color BG_COLOR = new Color(0xbbada0);
    private final String FONT = "Helvetica";
    private final int TILE_SIZE = 125;
    private final int TILE_MARGIN = 16;


    private LinkedList<Tile[]> viewQueue = new LinkedList<Tile[]>();

    public final Game game2048 = new Game();

    public GUI() {
        setFocusable(true);
        Object[] options = {"AI", "Play"};
        int choice = JOptionPane.showOptionDialog(null,
                "A Message",
                "A Title",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.DEFAULT_OPTION,
                null,
                options,
                options[1]);

        if(choice == 0) {
            (new Thread(this)).start();
        } else {
            playGame();
        }
    }

    public static void main(String[]args) {

        JFrame game = new JFrame();
        game.setTitle("2048");
        game.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game.setSize(600, 700);

        game.add(new GUI());

        game.setLocationRelativeTo(null);
        game.setVisible(true);

    }

    public void run() {
        Expectimax algorithm = new Expectimax();

        game2048.resetGame();

        Timer timer = new Timer();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                repaint();
            }
        }, DELAY);


        while(!game2048.lost) {
//            viewQueue.add(game2048.getTiles());
            try {
                Thread.sleep(0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            game2048.move(algorithm.nextMove(game2048.getTiles()));//viewQueue.getFirst()));
            repaint();
        }
    }

    private void playGame() {
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {


                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    game2048.resetGame();
                }
                if (!game2048.canMove()) {
                    game2048.lost = true;
                }
                if (!game2048.lost) {
                    Direction dir = null;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_LEFT:
                            dir = Direction.LEFT;
                            break;
                        case KeyEvent.VK_RIGHT:
                            dir = Direction.RIGHT;
                            break;
                        case KeyEvent.VK_UP:
                            dir = Direction.UP;
                            break;
                        case KeyEvent.VK_DOWN:
                            dir = Direction.DOWN;
                            break;
                        default:
                            return;
                    }
                    game2048.move(dir);
                }
                if (!game2048.canMove()) {
                    game2048.lost = true;
                }

                addToQueue(game2048.getTiles());
                repaint();
            }
        });
        game2048.resetGame();
        addToQueue(game2048.getTiles());
    }

    public void addToQueue(Tile[] tiles) {
        viewQueue.add(tiles);
    }

    private int offsetCoors(int arg) {
        return arg * (TILE_MARGIN + TILE_SIZE) + TILE_MARGIN;
    }


    //GUI
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        g.setColor(BG_COLOR);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);
        Tile[] tiles = game2048.getTiles();//viewQueue.poll();
        if(tiles==null)
            return;
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 4; x++) {
                drawTile(g, tiles[x + y * 4], x, y);
            }
        }
    }

    private void drawTile(Graphics g2, Tile tile, int x, int y) {
        Graphics2D g = ((Graphics2D) g2);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_NORMALIZE);
        int value = tile.value;
        int xOffset = offsetCoors(x);
        int yOffset = offsetCoors(y);
        g.setColor(tile.getBackground());
        g.fillRoundRect(xOffset, yOffset, TILE_SIZE, TILE_SIZE, 14, 14);
        g.setColor(tile.getForeground());
        final int size = value < 100 ? 36 : value < 1000 ? 32 : 24;
        final Font font = new Font(FONT, Font.BOLD, size);
        g.setFont(font);

        String s = String.valueOf(value);
        final FontMetrics fm = getFontMetrics(font);

        final int w = fm.stringWidth(s);
        final int h = -(int) fm.getLineMetrics(s, g).getBaselineOffsets()[2];

        if (value != 0)
            g.drawString(s, xOffset + (TILE_SIZE - w) / 2, yOffset + TILE_SIZE - (TILE_SIZE - h) / 2 - 2);

            if (game2048.lost) {
                g.drawString("Game over!", 250, 300);
                g.setFont(new Font(FONT, Font.PLAIN, 16));

            }
        g.setFont(new Font(FONT, Font.PLAIN, 18));
        g.drawString("Score: " + game2048.getScore(), 250, 620);

    }

}

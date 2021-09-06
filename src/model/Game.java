package model;

import controller.Handler;
import view.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serial;
import java.util.Random;

public class Game extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = 533351226053614578L;

    public static final int WIDTH = 1000, HEIGHT = WIDTH;

    private Thread thread; //single threaded game not recommended!
    private boolean running = false;

    private Random r;
    private Handler handler;

    public Game() {
        handler = new Handler();

        Window window = new view.Window(WIDTH, HEIGHT, "Let´s build a Game!", this);
        window.getQuit().addActionListener(e -> {
            System.exit(0);
            stop();
        });

        r = new Random();

        /*
        for(int i = 0; i<50; i++) {
            handler.addObject(new Player(r.nextInt(WIDTH),r.nextInt(HEIGHT), ID.PLAYER));
            //handler.addObject(new Player(0,0, ID.PLAYER));
        }
        */
        /*
        for(int i = 1; i<WIDTH/3; i++) {
            for(int j = 1; j<HEIGHT/3; j++) {
                handler.addObject(new Block(i*3,j*3, ID.BLOCK));
            }
        }
        */
        //generateBlockMatrix(100,100);
        calculateRule(100,100);

    }

    private Block[][] generateBlockMatrix(int x, int y) {
        Block[][] gameMatrix = new Block[x][y];
        for(int i = 0; i < x; i++) {
            for(int j = 0; j < y; j++) {
                Block block = new Block(j+(9*i),i+(9*j), ID.BLOCK);
                gameMatrix[i][j] = block;
                handler.addObject(block);
            }
        }
        return gameMatrix;
    }

    private Block[][] calculateRule(int x, int y) {
        Block[][] matrix = generateBlockMatrix(x,y);
        matrix[x/2][0].setActive(true);
        for(int i = 1; i<x-1; i++) {
            for(int j = 1; j<y-1; j++) {
                if((matrix[i-1][j-1].isActive() && !matrix[i][j-1].isActive() && !matrix[i+1][j-1].isActive()) ||
                        (!matrix[i-1][j-1].isActive() && matrix[i][j-1].isActive() && matrix[i+1][j-1].isActive()) ||
                        (!matrix[i-1][j-1].isActive() && matrix[i][j-1].isActive() && !matrix[i+1][j-1].isActive()) ||
                        (!matrix[i-1][j-1].isActive() && !matrix[i][j-1].isActive() && matrix[i+1][j-1].isActive())
                ) {
                    matrix[i][j].setActive(true);
                }
            }
        }
        return matrix;
    }

    public static void main(String[] args) {
        new Game();
    }

    public synchronized void start() {
        thread = new Thread(this);
        thread.start();
        running = true;
    }

    public synchronized void stop() {
        try {
            thread.join();
            running = false;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;
        while(running) {
            long now = System.nanoTime();
            delta += (now-lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                delta--;
            }
            if(running) {
                render();
            }
            frames++;

            if(System.currentTimeMillis()-timer > 1000) {
                timer += 1000;
                System.out.println("FPS: " + frames);
                frames = 0;
            }
        }
        stop();
    }

    private void tick() {
        handler.tick();
    }

    private void render() {
        BufferStrategy bs = this.getBufferStrategy();
        if(bs == null) {
            this.createBufferStrategy(3);
            return;
        }

        Graphics g = bs.getDrawGraphics();

        //avoids flickering
        g.setColor(Color.BLACK);
        g.fillRect(0,0,WIDTH,HEIGHT);

        handler.render(g);

        g.dispose();
        bs.show();
    }
}

package model;

import controller.Handler;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serial;
import java.util.Random;

public class Game extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = 533351226053614578L;

    public static final int WIDTH = 1280, HEIGHT = WIDTH / 12 * 9;

    private Thread thread; //single threaded game not recommended!
    private boolean running = false;

    private Random r;
    private Handler handler;

    public Game() {
        new view.Window(WIDTH, HEIGHT, "Let´s build a Game!", this);

        handler = new Handler();
        r = new Random();
/*
        for(int i = 0; i<50; i++) {
            handler.addObject(new Player(r.nextInt(WIDTH),r.nextInt(HEIGHT), ID.PLAYER));
            //handler.addObject(new Player(0,0, ID.PLAYER));
        }
        */
        for(int i = 1; i<11; i++) {
            for(int j = 1; j<11; j++) {
                handler.addObject(new Block(i*35,j*35, ID.BLOCK));
            }
        }


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

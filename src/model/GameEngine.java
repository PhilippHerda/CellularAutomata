package model;

import controller.Handler;
import view.Window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.Serial;
import java.util.Random;

public class GameEngine extends Canvas implements Runnable{

    @Serial
    private static final long serialVersionUID = 533351226053614578L;

    public static final int WIDTH = 1280, HEIGHT = WIDTH;

    private Thread thread; //single threaded game not recommended!
    private boolean running = false;

    private Random r;
    private Handler handler;

    public GameEngine() {

        handler = new Handler();

        CellularAutomata ca = new CellularAutomata(30, WIDTH, HEIGHT, handler);

        Window window = new view.Window(WIDTH, HEIGHT, "Cellular Automata v. 0.1", this);
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
    }


    public static void main(String[] args) {
        new GameEngine();
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
                try {
                    tick();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
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

    private void tick() throws InterruptedException {
        handler.tick();
        Thread.sleep(10);
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

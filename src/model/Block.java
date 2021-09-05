package model;

import java.awt.*;
import java.util.Random;

public class Block extends GameObject{

    private boolean active;
    private boolean lastActive;

    private Random r;

    private Color defaultColor = Color.WHITE;
    private Color activatedColor = Color.GREEN;
    private Color doubleActivated = Color.RED;

    public Block(int x, int y, ID id) {
        super(x, y, id);

        r = new Random();

        active = false;
        lastActive = false;
    }

    @Override
    public void tick() {
        active = r.nextBoolean();
    }

    @Override
    public void render(Graphics g) {

        if(active && lastActive) {
            g.setColor(doubleActivated);
            lastActive = false;
        } else if(active) {
            g.setColor(activatedColor);
            lastActive = active;
        } else {
            g.setColor(defaultColor);
            lastActive = active;
        }
        g.fillRect(x, y, 32,32);
    }
}

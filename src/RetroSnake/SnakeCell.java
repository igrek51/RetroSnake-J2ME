package RetroSnake;

import javax.microedition.lcdui.Graphics;

public class SnakeCell {
    public SnakeCell() {
        x = Config.map_w/2;
        y = Config.map_h/2;
    }
    public SnakeCell(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g, int color){
        g.setColor(color);
        g.fillRoundRect(Map.map_to_screen_x(x), Map.map_to_screen_y(y), Config.view_cell, Config.view_cell, Config.rectArc, Config.rectArc);
    }
    int x;
    int y;
}

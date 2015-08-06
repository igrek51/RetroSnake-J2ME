package RetroSnake;

import javax.microedition.lcdui.Graphics;

public class FoodCell {
    public FoodCell(){
        x = 0;
        y = 0;
    }
    public FoodCell(int x, int y){
        this.x = x;
        this.y = y;
    }
    public void draw(Graphics g){
        int rect_x = Map.map_to_screen_x(x);
        int rect_y = Map.map_to_screen_y(y);
        int food_cell = Config.view_cell - Config.view_cell*2/3;
        g.setColor(Config.Color.food);
        //4 kwadraciki
        g.fillRoundRect(rect_x + Config.view_cell/3, rect_y, food_cell, food_cell, Config.foodArc, Config.foodArc);
        g.fillRoundRect(rect_x, rect_y + Config.view_cell/3, food_cell, food_cell, Config.foodArc, Config.foodArc);
        g.fillRoundRect(rect_x + Config.view_cell*2/3, rect_y + Config.view_cell/3, food_cell, food_cell, Config.foodArc, Config.foodArc);
        g.fillRoundRect(rect_x + Config.view_cell/3, rect_y + Config.view_cell*2/3, food_cell, food_cell, Config.foodArc, Config.foodArc);
    }
    int x;
    int y;
}

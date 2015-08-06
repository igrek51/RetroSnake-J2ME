package RetroSnake;

import java.util.Vector;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;

public class Paint {
    static Font font_1 = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    static Font font_bold = Font.getFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    static Font font_mono = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    
    static void draw_background(Graphics g){
        g.setColor(Config.Color.background);
        g.fillRect(0, 0, Config.screen_w, Config.screen_h);
        //podpis
        g.setFont(font_mono);
        g.setColor(Config.Color.signature);
        g.drawString("Igrek", Config.screen_w-2, 0, Graphics.TOP|Graphics.RIGHT);
    }
    static void draw_map(Graphics g){
        int border_x = - Map.view_pos_x * Config.screen_w / Config.view_w;
        int border_y = - Map.view_pos_y * Config.screen_h / Config.view_h;
        int border_w = Config.map_w * Config.screen_w / Config.view_w;
        int border_h = Config.map_h * Config.screen_h / Config.view_h;
        if(!Config.Menu.map_open){ //zamknięta mapa
            g.setColor(Config.Color.border);
            for(int i=0; i<2; i++){ //grubość = 2
                g.drawRect(border_x+i, border_y+i, border_w-i*2-1, border_h-i*2-1);
            }
        }
    }
    static void draw_snake(Graphics g, Vector snake){
        int grayscale;
        for(int i=0; i<snake.size(); i++){
            grayscale = Config.Color.snake_gray_level*i/snake.size();
            ((SnakeCell)snake.elementAt(i)).draw(g, (grayscale<<16) + (grayscale<<8) + grayscale);
        }
    }
    static void draw_snake_head(Graphics g, int direction){
        int x_c = Map.map_to_screen_x(Map.snake_head_x) + Config.view_cell/2 - Config.snake_eyes_size/2;
        int y_c = Map.map_to_screen_y(Map.snake_head_y) + Config.view_cell/2 - Config.snake_eyes_size/2;
        int x_e1 = 0, x_e2 = 0, y_e1 = 0, y_e2 = 0;
        if(direction==Direction.RIGHT){
            x_e1 = x_c + Config.snake_eyes_size;
            x_e2 = x_e1;
            y_e1 = y_c - Config.snake_eyes_size;
            y_e2 = y_c + Config.snake_eyes_size;
        }else if(direction==Direction.LEFT){
            x_e1 = x_c - Config.snake_eyes_size;
            x_e2 = x_e1;
            y_e1 = y_c - Config.snake_eyes_size;
            y_e2 = y_c + Config.snake_eyes_size;
        }else if(direction==Direction.UP){
            x_e1 = x_c - Config.snake_eyes_size;
            x_e2 = x_c + Config.snake_eyes_size;
            y_e1 = y_c - Config.snake_eyes_size;
            y_e2 = y_e1;
        }else if(direction==Direction.DOWN){
            x_e1 = x_c - Config.snake_eyes_size;
            x_e2 = x_c + Config.snake_eyes_size;
            y_e1 = y_c + Config.snake_eyes_size;
            y_e2 = y_e1;
        }
        g.setColor(Config.Color.snake_eyes);
        g.fillRoundRect(x_e1, y_e1, Config.snake_eyes_size, Config.snake_eyes_size, Config.snake_eyes_size, Config.snake_eyes_size);
        g.fillRoundRect(x_e2, y_e2, Config.snake_eyes_size, Config.snake_eyes_size, Config.snake_eyes_size, Config.snake_eyes_size);
    }
    static void draw_food(Graphics g, Vector food, int logic_cycles){
        for(int i=0; i<food.size(); i++){
            ((FoodCell)food.elementAt(i)).draw(g);
        }
    }
    static void quick_menu(Graphics g, String l, String m, String r){
        g.setColor(Config.Color.quick_menu);
        g.setFont(font_bold);
        if(l.length()>0) g.drawString(l, 0, Config.screen_h, Graphics.BOTTOM|Graphics.LEFT);
        if(m.length()>0) g.drawString(m, Config.screen_w/2, Config.screen_h, Graphics.BOTTOM|Graphics.HCENTER);
        if(r.length()>0) g.drawString(r, Config.screen_w, Config.screen_h, Graphics.BOTTOM|Graphics.RIGHT);
    }
    static void draw_score(Graphics g, int score){
        g.setColor(Config.Color.stats);
        g.setFont(font_mono);
        g.drawString("Punkty: "+score+"/"+(Config.map_w*Config.map_h), 0, 0, Graphics.TOP|Graphics.LEFT);
    }
    static void start_arrows(Graphics g){
        //strzałki do rozpoczęcia gry
        g.setColor(Config.Color.menu_text);
        int cx = Config.screen_w * (Config.view_w/2) / Config.view_w + Config.view_cell/2;
        int cy = Config.screen_h * (Config.view_h/2) / Config.view_h + Config.view_cell/2;
        //góra
        g.drawLine(cx, cy-Config.arrowsD, cx-Config.arrowsL, cy-Config.arrowsD+Config.arrowsL);
        g.drawLine(cx, cy-Config.arrowsD, cx+Config.arrowsL, cy-Config.arrowsD+Config.arrowsL);
        //dół
        g.drawLine(cx, cy+Config.arrowsD, cx-Config.arrowsL, cy+Config.arrowsD-Config.arrowsL);
        g.drawLine(cx, cy+Config.arrowsD, cx+Config.arrowsL, cy+Config.arrowsD-Config.arrowsL);
        //lewo
        g.drawLine(cx-Config.arrowsD, cy, cx-Config.arrowsD+Config.arrowsL, cy-Config.arrowsL);
        g.drawLine(cx-Config.arrowsD, cy, cx-Config.arrowsD+Config.arrowsL, cy+Config.arrowsL);
        //prawo
        g.drawLine(cx+Config.arrowsD, cy, cx+Config.arrowsD-Config.arrowsL, cy-Config.arrowsL);
        g.drawLine(cx+Config.arrowsD, cy, cx+Config.arrowsD-Config.arrowsL, cy+Config.arrowsL);
    }
    static void draw_menu(Graphics g, int menu){
        g.setFont(font_mono);
        g.setColor(Config.Color.menu_text);
        g.drawString("R e t r o S n a k e",Config.screen_w/2,Config.menu_0/2,Graphics.TOP|Graphics.HCENTER);
        //wybrany kursor
        g.drawString(">", 10, Config.menu_0+Config.menu_step*menu, Graphics.TOP|Graphics.HCENTER);
        g.drawString("<", Config.screen_w-10, Config.menu_0+Config.menu_step*menu, Graphics.TOP|Graphics.HCENTER);
        g.setFont(font_bold);
        int draw_y = Config.menu_0;
        g.drawString("Nowa gra",Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Prędkość: "+RetroSnake.speed_to_ms(Config.Menu.speed)+" ms",Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Przyspieszanie: "+(Config.Menu.acc>0?"-"+Config.Menu.acc+" ms":"wyłączone"),Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Pokarm: "+(Config.Menu.food==0?"progresywny":""+Config.Menu.food), Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Mapa: "+(Config.Menu.map_open?"otwarta":"zamknięta"),Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Widok: "+(Config.Menu.view_static?"statyczny":"dynamiczny"),Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Rozmiar w.: "+Config.view_w+" x "+Config.view_h+" ("+Config.Menu.view_cell+" px)",Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Szerokość mapy: "+Config.Menu.map_w,Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Wysokość mapy: "+Config.Menu.map_h,Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        draw_y += Config.menu_step;
        g.drawString("Wyjście",Config.screen_w/2,draw_y,Graphics.TOP|Graphics.HCENTER);
        Paint.quick_menu(g, "", "OK", "Wyjdź");
    }
}

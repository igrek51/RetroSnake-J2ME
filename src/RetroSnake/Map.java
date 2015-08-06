package RetroSnake;

import java.util.Vector;

public class Map {
    static int snake_head_x = 0, snake_head_y = 0; //położenie głowy snake'a na mapie
    static int view_pos_x = 0; //pozycja ekranu od początku układu (w kratkach)
    static int view_pos_y = 0;
    
    static void calc_coord(Vector snake){
        SnakeCell snake_head = (SnakeCell)snake.firstElement();
        snake_head_x = snake_head.x;
        snake_head_y = snake_head.y;
        if(Config.Menu.view_static){
            view_pos_x = 0;
            view_pos_y = 0;
        }else{
            view_pos_x = snake_head_x - Config.view_w/2;
            view_pos_y = snake_head_y - Config.view_h/2;
        }
    }
    //transformacja na współrzędne ekranowe
    static int map_to_screen_x(int map_x){
        //jeśli jest zawijanie mapy
        if(Config.Menu.map_open){
            if(map_x-view_pos_x >= Config.view_w){ //jeśli byłby wyświetlany poza widokiem
                map_x -= Config.map_w;
            }
            if(map_x-view_pos_x < 0){
                map_x += Config.map_w;
            }
        }
        return (map_x - view_pos_x) * Config.screen_w / Config.view_w;
    }
    static int map_to_screen_y(int map_y){
        //jeśli jest zawijanie mapy
        if(Config.Menu.map_open){
            if(map_y-view_pos_y >= Config.view_h){ //jeśli byłby wyświetlany poza widokiem
                map_y -= Config.map_h;
            }
            if(map_y-view_pos_y < 0){
                map_y += Config.map_h;
            }
        }
        return (map_y - view_pos_y) * Config.screen_h / Config.view_h;
    }
}

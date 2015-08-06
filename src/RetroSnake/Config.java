package RetroSnake;

import javax.microedition.lcdui.Canvas;

public class Config {
    static int timer_period = 130; //[ms]
    static final int timer_period_menu = 130; //[ms]
    //ustawienia w menu
    static public class Menu {
        static int speed = 12;
        static int acc = 0;
        static int food = 2;
        static boolean map_open = true;
        static int map_w = 50;
        static int map_h = 50;
        static boolean view_static = true;
        //NWD(240,320) = 5 * 2^4 = 80
        static int view_cell = 16;
        static final int speed_max = 25;
    }
    static int screen_w = 0;
    static int screen_h = 0;
    //wartości wynikowe
    static void refresh(){
        view_cell = Menu.view_cell; //rozmiar pola w pikselach na ekranie
        view_w = screen_w / view_cell; //ilość pól w poziomie na ekranie
        view_h = screen_h / view_cell; //ilość pól w pionie na ekranie
        //walidacja rozmiaru mapy
        if(Config.Menu.view_static){
            Menu.map_w = view_w;
            Menu.map_h = view_h;
        }else{
            if(Config.Menu.map_open){
                if(Menu.map_w<view_w) Menu.map_w = view_w;
                if(Menu.map_h<view_h) Menu.map_h = view_h;
            }
        }
        map_w = Menu.map_w; //ilość pól w poziomie na mapie
        map_h = Menu.map_h; //ilość pól w pionie na mapie
        rectArc = view_cell*5/8; //[px] - średnica zaokrąglenia prostokątów snake'a
        foodArc = view_cell/4; //[px] - średnica zaokrąglenia prostokątów fooda
        snake_eyes_size = view_cell/5; //[px]
        arrowsD = view_cell*2;
        arrowsL = view_cell/2;
    }
    static int map_w = 0;
    static int map_h = 0;
    static int view_cell = 0;
    static int view_w = 0;
    static int view_h = 0;
    static int rectArc = 0; //[px] - średnica zaokrąglenia prostokątów snake'a
    static int foodArc = 0; //[px] - średnica zaokrąglenia prostokątów fooda
    static int snake_eyes_size = 0; //[px]
    //strzałki
    static int arrowsD = 0;
    static int arrowsL = 0;
    //menu
    static final int menu_max = 10; //liczba elementów menu
    static final int menu_0 = 100; //[px]
    static final int menu_step = 18; //[px]
    //kolory
    public class Color {
        static final int background = 0x9cc283;
        static final int signature = 0x97b08b;
        static final int snake_gray_level = 80;
        static final int snake_eyes = 0x454545;
        static final int quick_menu = 0x758575;
        static final int stats = 0x909090;
        static final int food = 0xe0e0e0;
        static final int menu_text = 0x000000;
        static final int game_over = 0xf0f0f0;
        static final int border = 0x202020;
    }
    //klawisze
    static final int KEY_LEFT = -3;
    static final int KEY_RIGHT = -4;
    static final int KEY_UP = -1;
    static final int KEY_DOWN = -2;
    static final int KEY_L = -6;
    static final int KEY_M = -5;
    static final int KEY_R = -7;
    static final int KEY_BACKSPACE = -8;
    static final int KEY_STAR = Canvas.KEY_STAR;
    static final int KEY_POUND = Canvas.KEY_POUND;
    static final int KEY_0 = Canvas.KEY_NUM0;
    static final int KEY_1 = Canvas.KEY_NUM1;
    static final int KEY_2 = Canvas.KEY_NUM2;
    static final int KEY_3 = Canvas.KEY_NUM3;
    static final int KEY_4 = Canvas.KEY_NUM4;
    static final int KEY_5 = Canvas.KEY_NUM5;
    static final int KEY_6 = Canvas.KEY_NUM6;
    static final int KEY_7 = Canvas.KEY_NUM7;
    static final int KEY_8 = Canvas.KEY_NUM8;
    static final int KEY_9 = Canvas.KEY_NUM9;
}

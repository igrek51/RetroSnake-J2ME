package RetroSnake;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;
import java.util.Random;
public class RetroSnake extends MIDlet implements CommandListener {
    public RetroSnake() { }
    //ekran
    private Display display;
    MyCanvas canvas = null;
    //zmienne
    int stan = 0;
    int score = 0;
    int direction = 0; //0 - prawo, 1 - góra, 2 - lewo, 3 - dół
    String info = "";
    Vector food = new Vector();
    Vector snake = new Vector();
    Vector kierunki_bufor = new Vector();
    boolean pause;
    int menu = 0;
    long start_time = 0;
    long end_time = 0;
    int max_food = 0, max_food_target = 0;
    AI ai_engine = null;
    //timery
    class MyTimerTask extends TimerTask {
        public void run(){
            if(stan==2) game_logic();
            canvas.repaint();
        }
    }
    Timer timer1 = null;
    MyTimerTask timer1_task = null;
    Random r;
    int logic_cycles = 0;
    //czcionki
    Font font_mono = Font.getFont(Font.FACE_MONOSPACE, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    
    protected void startApp() {
        display = Display.getDisplay(this);
        canvas = new MyCanvas();
        canvas.setFullScreenMode(true);
        Config.screen_w = canvas.getWidth();
        Config.screen_h = canvas.getHeight();
        Config.refresh();
        timer1 = new Timer();
        timer1_task = new MyTimerTask();
        timer1.scheduleAtFixedRate(timer1_task, 0, Config.timer_period_menu);
        r = new Random();
        display.setCurrent(canvas);
    }
    
    public class MyCanvas extends Canvas {
        public void paint(Graphics g) {
            if(stan==0){ //menu
                paint_menu(g);
            }else if(stan==1){ //oczekiwanie na rozpoczęcie
                paint_game_start(g);
            }else if(stan==2){ //gra
                paint_game(g);
            }else if(stan==3){ //game_over
                paint_game_over(g);
            }
        }
        public void keyPressed(int keyCode){
            if(stan==0){ //menu
                if(keyCode==Config.KEY_R) exit();
                else if(keyCode==Config.KEY_M||keyCode==Config.KEY_5) menu_exec(menu, 0);
                else if(keyCode==Config.KEY_LEFT||keyCode==Config.KEY_4) menu_exec(menu, -1);
                else if(keyCode==Config.KEY_RIGHT||keyCode==Config.KEY_6) menu_exec(menu, +1);
                else if(keyCode==Config.KEY_UP||keyCode==Config.KEY_2){
                    menu--;
                    if(menu<0) menu = Config.menu_max-1;
                }else if(keyCode==Config.KEY_DOWN||keyCode==Config.KEY_8){
                    menu++;
                    if(menu>=Config.menu_max) menu=0;
                }
            }else if(stan==1){ //oczekiwanie na rozpoczęcie
                if(keyCode==Config.KEY_R) back_to_menu();
                else if(keyCode==Config.KEY_LEFT||keyCode==Config.KEY_RIGHT
                        ||keyCode==Config.KEY_UP||keyCode==Config.KEY_DOWN
                        ||keyCode==Config.KEY_4||keyCode==Config.KEY_6
                        ||keyCode==Config.KEY_2||keyCode==Config.KEY_8){
                    if(keyCode==Config.KEY_LEFT||keyCode==Config.KEY_4){
                        direction = Direction.LEFT;
                    }else if(keyCode==Config.KEY_RIGHT||keyCode==Config.KEY_6){
                        direction = Direction.RIGHT;
                    }else if(keyCode==Config.KEY_UP||keyCode==Config.KEY_2){
                        direction = Direction.UP;
                    }else if(keyCode==Config.KEY_DOWN||keyCode==Config.KEY_8){
                        direction = Direction.DOWN;
                    }
                    stan = 2;
                }
            }else if(stan==2){ //gra
                if(keyCode==Config.KEY_LEFT||keyCode==Config.KEY_RIGHT
                        ||keyCode==Config.KEY_UP||keyCode==Config.KEY_DOWN
                        ||keyCode==Config.KEY_4||keyCode==Config.KEY_6
                        ||keyCode==Config.KEY_2||keyCode==Config.KEY_8){
                    if(keyCode==Config.KEY_LEFT||keyCode==Config.KEY_4){
                        change_direction(Direction.LEFT);
                    }else if(keyCode==Config.KEY_RIGHT||keyCode==Config.KEY_6){
                        change_direction(Direction.RIGHT);
                    }else if(keyCode==Config.KEY_UP||keyCode==Config.KEY_2){
                        change_direction(Direction.UP);
                    }else if(keyCode==Config.KEY_DOWN||keyCode==Config.KEY_8){
                        change_direction(Direction.DOWN);
                    }
                }else if(keyCode==Config.KEY_R) back_to_menu();
                else if(keyCode==Config.KEY_L || keyCode==Config.KEY_BACKSPACE) kierunki_bufor.removeAllElements();
                else if(keyCode==Config.KEY_M||keyCode==Config.KEY_5) pause = !pause;
                else if(keyCode==Config.KEY_POUND) Config.ai_enabled = !Config.ai_enabled;
            }else if(stan==3){ //game_over
                if(keyCode==Config.KEY_M||keyCode==Config.KEY_5) new_game();
                else if(keyCode==Config.KEY_R) back_to_menu();
            }
        }
        public void keyReleased(int keyCode){ }
        public void keyRepeated(int keyCode){
            keyPressed(keyCode);
        }
    }
    void paint_menu(Graphics g){
        Paint.draw_background(g);
        Paint.draw_menu(g, menu);
    }
    void paint_game_start(Graphics g){
        Paint.draw_background(g);
        Map.calc_coord(snake);
        Paint.draw_map(g);
        Paint.draw_snake(g, snake);
        Paint.start_arrows(g);
        Paint.draw_score(g, score);
        Paint.quick_menu(g, "", "", "Wróć");
    }
    void paint_game(Graphics g){
        Paint.draw_background(g);
        Map.calc_coord(snake);
        Paint.draw_map(g);
        Paint.draw_snake(g, snake);
        Paint.draw_snake_head(g, direction);
        Paint.draw_score(g, score);
        g.drawString("Prędkość: "+Config.timer_period+" ms", 0, 16, Graphics.TOP|Graphics.LEFT);
        g.drawString("Czas: "+((float)(System.currentTimeMillis()-start_time))/1000+" s", 0, 32, Graphics.TOP|Graphics.LEFT);
        if(Config.ai_enabled){
            g.drawString("Sztuczna inteligencja: włączona", 0, 48, Graphics.TOP|Graphics.LEFT);
        }
        String kolejka = "";
        if(pause&&kierunki_bufor.size()>0){
            kolejka = "Czyść ("+kierunki_bufor.size()+")";
        }
        Paint.quick_menu(g, kolejka, pause?"Kontunuuj":"Pauza", "Wróć");
        Paint.draw_food(g, food, logic_cycles);
    }
    void paint_game_over(Graphics g){
        Paint.draw_background(g);
        Map.calc_coord(snake);
        Paint.draw_map(g);
        Paint.draw_snake(g, snake);
        Paint.draw_snake_head(g, direction);
        Paint.draw_score(g, score);
        g.drawString("Prędkość: "+Config.timer_period+" ms", 0, 16, Graphics.TOP|Graphics.LEFT);
        g.drawString("Czas: "+((float)(end_time-start_time))/1000+" s", 0, 32, Graphics.TOP|Graphics.LEFT);
        if(Config.ai_enabled){
            g.drawString("Sztuczna inteligencja: włączona", 0, 48, Graphics.TOP|Graphics.LEFT);
        }
        g.setFont(font_mono);
        g.setColor(Config.Color.game_over);
        g.drawString("Koniec gry",Config.screen_w/2,Config.screen_h/2,Graphics.TOP|Graphics.HCENTER);
        Paint.quick_menu(g, "", "Restart", "Wróć");
        Paint.draw_food(g, food, logic_cycles);
    }
    
    void new_game(){
        //wykasowanie starych danych
        snake.removeAllElements();
        food.removeAllElements();
        kierunki_bufor.removeAllElements();
        Config.refresh();
        //reset timera
        Config.timer_period = speed_to_ms(Config.Menu.speed);
        timer1.cancel();
        timer1 = new Timer();
        timer1_task = new MyTimerTask();
        timer1.scheduleAtFixedRate(timer1_task, 0, Config.timer_period);
        //inicjalizacja
        snake.addElement(new SnakeCell());
        pause = false;
        score = 1;
        logic_cycles = 0;
        start_time = System.currentTimeMillis();
        if(Config.Menu.food>0) max_food = Config.Menu.food;
        else max_food = 1;
        max_food_target = 2;
        stan = 1;
        ai_engine = new AI(Config.map_w, Config.map_h, Config.Menu.map_open);
    }
    
    void change_direction(int pressed){
        int last_direction = direction;
        if(kierunki_bufor.size()>0){
            last_direction = ((Integer)kierunki_bufor.lastElement()).intValue();
        }
        //if(pressed == last_direction) return; //ten sam kierunek
        if(pressed != last_direction){ //różne kierunki
            if(pressed+last_direction == 2) return; //lewo i prawo
            if(pressed+last_direction == 4) return; //góra i dół
        }
        //dodanie do bufora
        kierunki_bufor.addElement(new Integer(pressed));
    }
    
    void game_logic(){
        if(pause) return;
        logic_cycles++;
        //sztuczna inteligencja
        if(Config.ai_enabled){
            ai_engine.refresh_map(snake);
            if(food.size()>0){
                SnakeCell snake_head = (SnakeCell)snake.firstElement();
                ai_engine.last_path = ai_engine.find_path(snake_head.x, snake_head.y, food);
                if(ai_engine.last_path!=null){
                    if(ai_engine.last_path.length() >= 1){
                        int[] point2 = (int[])ai_engine.last_path.points.elementAt(1);
                        kierunki_bufor.removeAllElements();
                        int dir = Direction.points_to_direction(snake_head.x, snake_head.y, point2[0], point2[1], Config.map_w, Config.map_h, Config.Menu.map_open);
                        kierunki_bufor.addElement(new Integer(dir));
                    }
                }
            }
        }
        //zmiana kierunku
        if(kierunki_bufor.size()>0){
            //odczytanie kierunku z bufora
            int pressed = ((Integer)kierunki_bufor.elementAt(0)).intValue();
            //usunięcie odczytanego kierunku
            kierunki_bufor.removeElementAt(0);
            direction = pressed;
        }
        //zapamiętanie położenia ogona
        SnakeCell ogon = (SnakeCell)snake.lastElement();
        int ogon_x = ogon.x;
        int ogon_y = ogon.y;
        //przesuń poprzednie komórki (od ogona do głowy)
        for(int i=snake.size()-1; i>0; i--){
            SnakeCell sc = (SnakeCell)snake.elementAt(i);
            SnakeCell nastepny = (SnakeCell)snake.elementAt(i-1);
            sc.x = nastepny.x;
            sc.y = nastepny.y;
        }
        //przesuwanie
        SnakeCell snake_head = (SnakeCell)snake.firstElement();
        if(direction==Direction.RIGHT){
            snake_head.x++;
        }else if(direction==Direction.LEFT){
            snake_head.x--;
        }else if(direction==Direction.DOWN){
            snake_head.y++;
        }else if(direction==Direction.UP){
            snake_head.y--;
        }
        //zawijanie ekranu
        if(Config.Menu.map_open){
            if(snake_head.x<0) snake_head.x = Config.map_w - 1;
            if(snake_head.x>=Config.map_w) snake_head.x = 0;
            if(snake_head.y<0) snake_head.y = Config.map_h - 1;
            if(snake_head.y>=Config.map_h) snake_head.y = 0;
        }
        //kolizja z samym sobą lub z mapą
        if(is_collision_with_snake(snake_head) || is_collision_with_map(snake_head)){
            //cofnięcie o 1
            snake.addElement(new SnakeCell(ogon_x, ogon_y)); //ogon na koniec
            //usunięcie głowy
            snake.removeElementAt(0);
            end_time = System.currentTimeMillis(); //zapisanie czasu gry
            stan = 3; //koniec gry
            return;
        }
        //wzięcie nowych komórek
        for(int i=0; i<food.size(); i++){
            FoodCell f = (FoodCell)food.elementAt(i);
            if(f.x == snake_head.x && f.y == snake_head.y){
                //dodanie nowej komórki do ogona
                snake.addElement(new SnakeCell(ogon_x, ogon_y));
                //zjedzenie fooda
                food.removeElementAt(i);
                score++;
                if(Config.Menu.food==0){ //przy progresywnym pokarmie
                    //zwiększenie następnego celu
                    if(snake.size()>=max_food_target){
                        max_food_target *= 2;
                        max_food++;
                    }
                }
                //przyspieszanie timera
                if(Config.Menu.acc>0){
                    Config.timer_period -= Config.Menu.acc;
                    if(Config.timer_period < 20) Config.timer_period = 20;
                    timer1.cancel();
                    timer1 = new Timer();
                    timer1_task = new MyTimerTask();
                    timer1.scheduleAtFixedRate(timer1_task, Config.timer_period, Config.timer_period);
                }
                break;
            }
        }
        //dodawanie nowych foodów
        if(food.size() < max_food){
            //jeśli są jeszcze wolne miejsca
            if(is_empty_space()){
                FoodCell nowy_food = new FoodCell();
                food_random(nowy_food);
                food.addElement(nowy_food);
            }
        }
    }
    
    boolean is_collision_with_snake(SnakeCell snake_head){
        for(int i=1; i<snake.size(); i++){
            SnakeCell sc = (SnakeCell)snake.elementAt(i);
            if(sc.x == snake_head.x && sc.y == snake_head.y){
                return true;
            }
        }
        return false;
    }
    boolean is_collision_with_map(SnakeCell snake_head){
        if(Config.Menu.map_open) return false;
        return snake_head.x<0 || snake_head.y<0 || snake_head.x>=Config.map_w || snake_head.y>=Config.map_h;
    }
    
    void food_random(FoodCell foodcell){
        do{
            foodcell.x = r.nextInt(Config.map_w);
            foodcell.y = r.nextInt(Config.map_h);
        }while(!food_correct(foodcell));
    }
    boolean food_correct(FoodCell foodcell){
        for(int i=0; i<snake.size(); i++){ //jeśli jest tam snake
            SnakeCell sc = (SnakeCell)snake.elementAt(i);
            if(sc.x == foodcell.x && sc.y == foodcell.y){ 
                return false;
            }
        }
        for(int i=0; i<food.size(); i++){ //jeśli jest tam inny food
            FoodCell f = (FoodCell)food.elementAt(i);
            if(f.x == foodcell.x && f.y == foodcell.y){ 
                return false;
            }
        }
        return true;
    }
    boolean is_empty_space(){
        return snake.size() + food.size() < Config.map_w * Config.map_h;
    }
    
    void menu_exec(int menu_nr, int action){
        if(menu_nr==0){
            if(action==0) new_game();
        }else if(menu_nr==1){ //prędkość początkowa
            if(action!=0){
                Config.Menu.speed += action;
                if(Config.Menu.speed < 1) Config.Menu.speed = 1;
                if(Config.Menu.speed > Config.Menu.speed_max) Config.Menu.speed = Config.Menu.speed_max;
            }
        }else if(menu_nr==2){ //przyspieszanie
            if(action==0){
                Config.Menu.acc = 0;
            }else{
                Config.Menu.acc += action;
                if(Config.Menu.acc < 0) Config.Menu.acc = 0;
            }
        }else if(menu_nr==3){ //ilość pokarmu
            if(action==0){
                Config.Menu.food = 0;
            }else{
                Config.Menu.food += action;
                if(Config.Menu.food<0) Config.Menu.food = 0;
            }
        }else if(menu_nr==4){ //mapa otwarta / zamknięta
            Config.Menu.map_open = !Config.Menu.map_open;
        }else if(menu_nr==5){ //widok statyczny / dynamiczny
            Config.Menu.view_static = !Config.Menu.view_static;
            Config.refresh();
        }else if(menu_nr==6){ //rozmiar widoku
            if(action!=0){
                Config.Menu.view_cell += -action;
                if(Config.Menu.view_cell<1) Config.Menu.view_cell = 1;
                if(Config.Menu.view_cell>Config.screen_w/2) Config.Menu.view_cell = Config.screen_w/2;
                Config.refresh();
            }
        }else if(menu_nr==7){ //szerokość mapy
            if(action==0){
                Config.Menu.map_w = Config.view_w;
            }else{
                Config.Menu.map_w += action;
                if(Config.Menu.map_w<2) Config.Menu.map_w = 2;
                Config.refresh();
            }
        }else if(menu_nr==8){ //wysokość mapy
            if(action==0){
                Config.Menu.map_h = Config.view_h;
            }else{
                Config.Menu.map_h += action;
                if(Config.Menu.map_h<2) Config.Menu.map_h = 2;
                Config.refresh();
            }
        }else if(menu_nr==9){ //sztuczna inteligencja
            Config.ai_enabled = !Config.ai_enabled;
        }else if(menu_nr==10){
            if(action==0) exit();
        }
    }
    
    static int speed_to_ms(int speed){
        if(speed<=0) return 1000;
        final double a = -2;
        final double b = -102.5;
        final double c = 3307.5;
        //y = c / (x-a) + b
        return (int)(c / (speed-a) + b);
    }
    
    void back_to_menu(){
        stan = 0;
        //reset timera
        Config.timer_period = speed_to_ms(Config.Menu.speed);
        timer1.cancel();
        timer1 = new Timer();
        timer1_task = new MyTimerTask();
        timer1.scheduleAtFixedRate(timer1_task, 0, Config.timer_period_menu);
    }
    
    void error_out(Exception ex){
        error_out(ex.toString());
    }
    void error_out(String ex){
        info = ex;
    }
    
    void minimize_app(){
        display.setCurrent(null);
    }
    void exit(){
        destroyApp(false);
    }
    
    public void pauseApp() { }
    public void resumeApp() {
        display.setCurrent(canvas);
    }
    public void destroyApp(boolean unconditional) { notifyDestroyed(); }

    public void commandAction(Command c, Displayable d) { }
}
package RetroSnake;

import java.util.Vector;

public class AI {
    public AI(int map_w, int map_h, boolean map_open){
        this.map_w = map_w;
        this.map_h = map_h;
        this.map_open = map_open;
        map = new boolean[map_w][map_h];
        map_clear();
    }
    public int map_w;
    public int map_h;
    public boolean[][] map; //[x][y], true - puste pole, false - zajęte
    public boolean map_open;
    public void map_clear(){
        for(int i=0; i<map_w; i++){
            for(int j=0; j<map_h; j++){
                map[i][j] = true;
            }
        }
    }
    public void refresh_map(Vector snake){
        map_clear();
        //zajęte miejsce przez snake'a
        for(int i=0; i<snake.size(); i++){
            SnakeCell snake_cell = ((SnakeCell)snake.elementAt(i));
            map[snake_cell.x][snake_cell.y] = false;
        }
    }
    public int start_x = 0, start_y = 0;
    public Path last_path = null;
    
    public class Node {
        public Node(int x, int y){
            this.x = x;
            this.y = y;
            g = 0;
            parent = null;
        }
        int x;
        int y;
        int g; /// odległość od startu po wyznaczonej drodze
        Node parent; /// wskaźnik na węzeł będący rodzicem (lub NULL)
    }

    public class Path {
        public Path() {}
        /// lista kolejnych punktów od startu do mety (włącznie) jako tablica int[2]: 0 - x, 1 - y
        Vector points = new Vector();
        int length(){
            if(points.size()<=1) return 0;
            return points.size()-1;
        }
    };
    
    public Path find_path(int start_x, int start_y, Vector food){
        this.start_x = start_x;
        this.start_y = start_y;
        //  ALGORYTM Dijkstry (zmodyfikowany A* - H = 0)
        if(food.isEmpty()) return null;
        //zmienne pomocnicze
        Vector o_list = new Vector(); //lista otwartych
        Vector c_list = new Vector(); //lista zamkniętych
        Node Q; //aktualne pole (o minimalnym F)
        Node sasiad;
        int min_f_i; //indeks minium f na liście otwartych
        int s_x, s_y; //położenie sąsiada
        int nowe_g;
        //Dodajemy pole startowe (lub węzeł) do Listy Otwartych.
        o_list.addElement(new Node(start_x, start_y));
        //dopóki lista otwartych nie jest pusta
        while(o_list.size()>0){
            //Szukamy pola o najniższej wartości F na Liście Otwartych. Czynimy je aktualnym polem - Q.
            min_f_i = 0; //indeks minimum
            for(int i=1; i<o_list.size(); i++){
                if(((Node)o_list.elementAt(i)).g < ((Node)o_list.elementAt(min_f_i)).g)
                    min_f_i = i; //nowe minimum
            }
            Q = ((Node)o_list.elementAt(min_f_i));
            //jeśli Q jest węzłem docelowym
            if(is_destination(Q, food)){
                //znaleziono najkrótszą ścieżkę
                Path sciezka;
                //jeśli punkt docelowy jest punktem startowym - brak ścieżki
                if(start_x==Q.x && start_y==Q.y){
                    sciezka = null;
                }else{
                    //Zapisujemy ścieżkę. Krocząc w kierunku od pola docelowego do startowego, przeskakujemy z kolejnych pól na im przypisane pola rodziców, aż do osiągnięcia pola startowego.
                    sciezka = new Path();
                    while(Q!=null){
                        int[] point = new int [2];
                        point[0] = Q.x;
                        point[1] = Q.y;
                        sciezka.points.insertElementAt(point, 0); //dopisanie na początek (odwrócenie listy)
                        Q = Q.parent;
                    }
                }
                return sciezka;
            }
            //Aktualne pole przesuwamy do Listy Zamkniętych.
            o_list.removeElementAt(min_f_i);
            c_list.addElement(Q);
            //Dla każdego z wybranych przyległych pól (sasiad) do pola aktualnego (Q) sprawdzamy:
            for(int s=0; s<4; s++){ //dla każdego sąsiada
                if(s==0){
                    s_x = Q.x - 1;
                    s_y = Q.y;
                }else if(s==1){
                    s_x = Q.x + 1;
                    s_y = Q.y;
                }else if(s==2){
                    s_x = Q.x;
                    s_y = Q.y - 1;
                }else{
                    s_x = Q.x;
                    s_y = Q.y + 1;
                }
                //jeśli jest poza mapą
                if(s_x<0 || s_y<0 || s_x>=map_w || s_y>=map_h){
                    //w zamkniętej mapie ignorujemy je
                    if(!map_open) continue;
                    //zawijanie w otwartej mapie
                    if(s_x<0)
                        s_x = map_w-1;
                    if(s_y<0)
                        s_y = map_h-1;
                    if(s_x>=map_w)
                        s_x = 0;
                    if(s_y>=map_h)
                        s_y = 0;
                }
                //jeśli NIE-MOŻNA go przejść, ignorujemy je.
                if(map[s_x][s_y]==false)
                    continue;
                //jeśli pole sąsiada jest już na Liście Zamkniętych
                if(find_in_list(c_list,s_x,s_y)!=null)
                    continue;
                //Jeśli pole sąsiada nie jest jeszcze na Liście Otwartych.
                sasiad = find_in_list(o_list,s_x,s_y);
                if(sasiad == null){
                    //dodajemy je do niej
                    sasiad = new Node(s_x, s_y);
                    o_list.addElement(sasiad);
                    //Aktualne pole (Q) przypisujemy sasiadowi jako "pole rodzica"
                    sasiad.parent = Q;
                    //i zapisujemy sasiada wartości F, G i H. (F = G + H)
                    sasiad.g = policz_g(sasiad);
                }else{
                    //jeśli pole było na liście otwartych
                    //sprawdzamy czy aktualna ścieżka do tego pola (sasiad) (prowadząca przez Q) jest krótsza, poprzez porównanie sasiada wartości G dla starej i aktualnej ścieżki. Mniejsza wartość G oznacza, że ścieżka jest krótsza.
                    nowe_g = 1 + Q.g;
                    if(nowe_g < sasiad.g){
                        //Jeśli tak, zmieniamy przypisanie "pole rodzica" na aktualne pole (Q) i przeliczamy wartości G i F dla pola (sasiad). Jeśli wasza Lista Otwartych jest posortowana pod kątem wartości F, trzeba ją ponownie przesortować po wprowadzonej zmianie.
                        sasiad.parent = Q;
                        sasiad.g = nowe_g;
                    }
                }
            }
        }
        //Lista Otwartych jest pusta. nie znaleziono pola docelowego, a ścieżka nie istnieje.
        return null;
    }
    
    public boolean is_destination(Node Q, Vector food){
        for(int i=0; i<food.size(); i++){
            FoodCell compare = ((FoodCell)food.elementAt(i));
            if(compare.x==Q.x && compare.y==Q.y)
                return true;
        }
        return false;
    }

    Node find_in_list(Vector list, int x, int y){
        for(int i=0; i<list.size(); i++){
            Node compare = ((Node)list.elementAt(i));
            if(compare.x==x && compare.y==y)
                return compare;
        }
        return null;
    }

    int policz_g(Node item){
        return 1 + item.parent.g;
    }
    
}

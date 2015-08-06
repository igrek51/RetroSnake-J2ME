package RetroSnake;

public class Direction {
    static final int RIGHT = 0;
    static final int UP = 1;
    static final int LEFT = 2;
    static final int DOWN = 3;
    
    public static int points_to_direction(int p1x, int p1y, int p2x, int p2y, int map_w, int map_h, boolean map_open){
        if(p2x == p1x + 1){
            return Direction.RIGHT;
        }else if(p2x == p1x - 1){
            return Direction.LEFT;
        }else if(p2y == p1y + 1){
            return Direction.DOWN;
        }else if(p2y == p1y - 1){
            return Direction.UP;
        }
        //ruch poza mapę (zawijanie)
        if(map_open){
            if(p2x + map_w == p1x + 1){
                return Direction.RIGHT;
            }else if(p1x + map_w == p2x + 1){
                return Direction.LEFT;
            }else if(p2y + map_h == p1y + 1){
                return Direction.DOWN;
            }else if(p1y + map_h == p2y + 1){
                return Direction.UP;
            }
        }
        System.out.println("points_to_direction: brak rozpoznanego kierunku.");
        return 0;
    }
}
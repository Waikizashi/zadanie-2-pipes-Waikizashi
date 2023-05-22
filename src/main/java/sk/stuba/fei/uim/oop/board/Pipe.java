package sk.stuba.fei.uim.oop.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pipe {

    private Type type;
    private Orientation orientation;
    private int[] connections;
    private int x;
    private int y;
    private boolean highlighted;

    public Pipe(Type type, Orientation orientation, int x, int y) {
        this.type = type;
        this.orientation = orientation;
        this.x = x;
        this.y = y;
        this.highlighted = false;
        connections = setConnections();
    }

    public Type getType() {
        return type;
    }

    public int[] setConnections(){
        if(type == Type.I){
            if(orientation == Orientation.UP || orientation == Orientation.DOWN){
                return new int[]{1,0,1,0};
            }else {
                return new int[]{0,1,0,1};
            }

        }else if(type == Type.L){
            if(orientation == Orientation.UP){
                return new int[]{1,1,0,0};
            }else if(orientation == Orientation.RIGHT){
                return new int[]{0,1,1,0};
            }else if(orientation == Orientation.DOWN){
                return new int[]{0,0,1,1};
            }else{
                return new int[]{1,0,0,1};
            }

        }else if(type == Type.START){
            return new int[]{1,1,1,0};
        }else {
            return new int[]{1,0,1,1};
        }
    }

    public Orientation getOrientation() {
        return orientation;
    }
    public void rotateClockwise() {
        if(type != Type.START && type != Type.FINISH){
            orientation = Orientation.values()[(orientation.ordinal() + 1) % Orientation.values().length];
            connections = setConnections();
        }
    }
}

package org.kakara.engine.utils;

public class Pair<X, Y> {
    private X x;
    private Y y;
    public Pair(X x, Y y){
        this.x = x;
         this.y = y;
    }

    public Pair(){}

    public void setX(X x){
        this.x = x;
    }

    public X getX(){
        return x;
    }

    public void setY(Y y){
        this.y = y;
    }

    public Y getY(){
        return y;
    }
}

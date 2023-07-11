package de.amg.chess.model;

public class Piece {
    private Pieces type;
    private Color color;
    public Piece(Pieces type, Color color){
        this.type = type;
        this.color = color;
    }
    public Pieces getType(){
        return type;
    }
    public Color getColor(){
        return color;
    }

    public String toString(){
        String s = Pieces.ps.get(type);
        if (color == Color.BLACK){
            s = s.toLowerCase();
        }
        return s;
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Piece){
            Piece p = (Piece)o;
            return this.type == p.type && this.color == p.color;
        }
        else{
            return false;
        }
    }

    public Piece copy(){
        return new Piece(type, color);
    }
}

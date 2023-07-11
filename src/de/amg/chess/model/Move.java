package de.amg.chess.model;

import java.lang.Math;
import java.util.Arrays;

public class Move {
    private Position position;
    private Piece piece;
    private String origin;
    private String dest;
    private boolean takes;
    private Pieces promote;
    private boolean check;
    private boolean mate;

    public Move(Position position, Piece piece, String origin, String dest, boolean takes, Pieces promote, boolean check, boolean mate){
        this.position = position;
        this.piece = piece;
        this.origin = origin;
        this.dest = dest;
        this.takes = takes;
        this.promote = promote;
        this.check = check;
        this.mate = mate;
    }

    public static Move fromString(Position position, String s){
        char[] c = s.toCharArray();
        Piece piece;
        String origin;
        String dest;
        boolean takes = false;
        Pieces promote = null;
        boolean check = false;
        boolean mate = false;
        int index;
        if (Pieces.sp.containsKey(String.valueOf(c[0]))){
            piece = position.getPiece(origin = s.substring(1, 3));
            index = 3;
        }
        else{
            piece = position.getPiece(origin = s.substring(0, 2));
            index = 2;
        }
        if (c[index] == 'x'){
            takes = true;
            index++;
        }
        dest = s.substring(index, index+2);
        index += 2;
        if (index<s.length()){
            if (c[index] == '='){
                promote = Pieces.sp.get(String.valueOf(c[index+1]));
                index += 2;
            }
            if (index<s.length()){
                if (c[index] == '+'){
                    check = true;
                }
                else if (c[index] == '#'){
                    mate = true;
                }
            }

        }
        return new Move(position, piece, origin, dest, takes, promote, check, mate);
    }
    public String toString(){
        StringBuilder b = new StringBuilder();
        if (piece.getType() == Pieces.KING && origin.startsWith("e") && dest.startsWith("g")){
            b.append("O-O");
        }
        else if (piece.getType() == Pieces.KING && origin.startsWith("e") && dest.startsWith("c")){
            b.append("O-O-O");
        }
        else {
            if (piece.getType() != Pieces.PAWN) {
                b.append(Pieces.ps.get(piece.getType()));
            }
            b.append(origin);
            if (takes) {
                b.append("x");
            }
            b.append(dest);
            if (promote != null) {
                b.append("=").append(Pieces.ps.get(promote));
            }
        }
        if (mate){
            b.append("#");
        }
        else if (check){
            b.append("+");
        }
        return b.toString();
    }
    public void apply(){
        position.deletePiece(origin);
        if (promote == null) {
            position.setPiece(dest, piece);
        }
        else{
            position.setPiece(dest, new Piece(promote, piece.getColor()));
        }
        if (position.getEnPassant() != null && position.getEnPassant().equals(dest)){
            char[] c = dest.toCharArray();
            position.deletePiece(c[0], Character.getNumericValue(c[1]) + ((piece.getColor() == Color.WHITE) ? -1 : 1));
        }
        if (piece.getType() == Pieces.PAWN && Math.abs(Character.getNumericValue(origin.charAt(1)) - Character.getNumericValue(dest.charAt(1))) > 1){
            char[] c = dest.toCharArray();
            position.setEnPassant(String.valueOf(c[0]) + String.valueOf(Character.getNumericValue(c[1]) + ((piece.getColor() == Color.WHITE) ? -1 : 1)));
        }
        else{
            position.setEnPassant(null);
        }
        if (position.getPiece("a1") == null || position.getPiece("a1").getType() != Pieces.ROOK || position.getPiece("a1").getColor() != Color.WHITE){
            position.setQ(false);
        }
        if (position.getPiece("h1") == null || position.getPiece("h1").getType() != Pieces.ROOK || position.getPiece("h1").getColor() != Color.WHITE){
            position.setK(false);
        }
        if (position.getPiece("a8") == null || position.getPiece("a8").getType() != Pieces.ROOK || position.getPiece("a8").getColor() != Color.BLACK){
            position.setq(false);
        }
        if (position.getPiece("h8") == null || position.getPiece("h8").getType() != Pieces.ROOK || position.getPiece("h8").getColor() != Color.BLACK){
            position.setk(false);
        }
        int a = "abcdefgh".indexOf(origin.charAt(0));
        int b = "abcdefgh".indexOf(dest.charAt(0));
        char[] c = origin.toCharArray();
        if (piece.getType() == Pieces.KING){
            String d = String.valueOf(c[1]);
            if (b == a + 2){
                position.setPiece("abcdefgh".charAt(b-1)+d, position.getPiece("abcdefgh".charAt(b+1)+d));
                position.deletePiece("abcdefgh".charAt(b+1)+d);
                if (piece.getColor() == Color.WHITE){
                    position.setK(false);
                }
                else{
                    position.setk(false);
                }
            }
            else if (b == a - 2){
                position.setPiece("abcdefgh".charAt(b+1)+d, position.getPiece("abcdefgh".charAt(b-2)+d));
                position.deletePiece("abcdefgh".charAt(b-2)+d);
                if (piece.getColor() == Color.WHITE){
                    position.setQ(false);
                }
                else{
                    position.setq(false);
                }
            }
            else{
                if (piece.getColor() == Color.WHITE) {
                    position.setK(false);
                    position.setQ(false);
                }
                else{
                    position.setk(false);
                    position.setq(false);
                }
            }
        }
        if (position.getToPlay() == Color.BLACK){
            position.setMoveNumber(position.getMoveNumber()+1);
        }
        position.setToPlay((position.getToPlay() == Color.WHITE ? Color.BLACK : Color.WHITE));
        if (piece.getType() != Pieces.PAWN){
            position.setDrawMoves(position.getDrawMoves() + 1);
        }
        else{
            position.setDrawMoves(0);
        }
        for (Piece[][] p:position.getPast()){
            if (Arrays.deepEquals(p, position.getArray())){
                position.setRepetitions(position.getRepetitions() + 1);
                break;
            }
        }
        position.addPast();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public boolean isTakes() {
        return takes;
    }

    public void setTakes(boolean takes) {
        this.takes = takes;
    }

    public Pieces getPromote() {
        return promote;
    }

    public void setPromote(Pieces promote) {
        this.promote = promote;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isMate() {
        return mate;
    }

    public void setMate(boolean mate) {
        this.mate = mate;
    }

    public boolean isLegal(){
        return piece != null && position.getPlayableMoves(origin).contains(this);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Move){
            Move m = (Move)o;
            return this.position.equals(m.position) && this.piece.equals(m.piece) && this.origin.equals(m.origin) && this.dest.equals(m.dest) && this.takes == m.takes && this.promote == m.promote && this.check == m.check && this.mate == m.mate;
        }
        else{
            return false;
        }
    }

    public Move copy(){
        return new Move(position.copy(), piece.copy(), origin, dest, takes, promote, check, mate);
    }
}

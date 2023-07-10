package de.amg.chess.main;

import de.amg.chess.pieces.Piece;

public class Field {

    private final boolean white;
    private Piece piece;

    public Field(boolean white) {
        this.white = white;
    }

    public Field(boolean white, Piece piece) {
        this.white = white;
        this.piece = piece;
    }

    public boolean isWhite() {
        return white;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }
}

package de.amg.chess.util;

import de.amg.chess.pieces.*;

public abstract class FieldDefaultPieces {

    public static final Piece[][] FIELD_DEFAULT_PIECES = new Piece[8][8];

    private FieldDefaultPieces(){

    }

    static {
        //Black pieces
        FIELD_DEFAULT_PIECES[0][0] = new Rook(false);
        FIELD_DEFAULT_PIECES[1][0] = new Knight(false);
        FIELD_DEFAULT_PIECES[2][0] = new Bishop(false);
        FIELD_DEFAULT_PIECES[3][0] = new Queen(false);
        FIELD_DEFAULT_PIECES[4][0] = new King(false);
        FIELD_DEFAULT_PIECES[5][0] = new Bishop(false);
        FIELD_DEFAULT_PIECES[6][0] = new Knight(false);
        FIELD_DEFAULT_PIECES[7][0] = new Rook(false);
        //Fill second row with pawns
        for(int i = 0; i < 8; i++){
            FIELD_DEFAULT_PIECES[i][1] = new Pawn(false);
        }

        //White pieces
        FIELD_DEFAULT_PIECES[0][7] = new Rook(true);
        FIELD_DEFAULT_PIECES[1][7] = new Knight(true);
        FIELD_DEFAULT_PIECES[2][7] = new Bishop(true);
        FIELD_DEFAULT_PIECES[3][7] = new Queen(true);
        FIELD_DEFAULT_PIECES[4][7] = new King(true);
        FIELD_DEFAULT_PIECES[5][7] = new Bishop(true);
        FIELD_DEFAULT_PIECES[6][7] = new Knight(true);
        FIELD_DEFAULT_PIECES[7][7] = new Rook(true);
        //Fill second row with pawns
        for(int i = 0; i < 8; i++){
            FIELD_DEFAULT_PIECES[i][6] = new Pawn(true);
        }
    }

}

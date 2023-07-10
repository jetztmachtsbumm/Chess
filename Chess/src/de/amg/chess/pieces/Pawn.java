package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class Pawn extends Piece{

    public Pawn(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_PAWN;
        }else{
            return Images.B_PAWN;
        }
    }

}

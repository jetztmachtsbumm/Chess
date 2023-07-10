package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class Bishop extends Piece{

    public Bishop(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_BISHOP;
        }else{
            return Images.B_BISHOP;
        }
    }

}

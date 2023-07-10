package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class Knight extends Piece{

    public Knight(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_KNIGHT;
        }else{
            return Images.B_KNIGHT;
        }
    }

}

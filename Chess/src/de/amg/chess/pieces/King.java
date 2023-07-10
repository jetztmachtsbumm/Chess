package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class King extends Piece{

    public King(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_KING;
        }else{
            return Images.B_KING;
        }
    }

}

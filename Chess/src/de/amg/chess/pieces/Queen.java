package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class Queen extends Piece{

    public Queen(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_QUEEN;
        }else{
            return Images.B_QUEEN;
        }
    }

}

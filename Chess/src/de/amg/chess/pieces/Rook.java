package de.amg.chess.pieces;

import de.amg.chess.util.Images;

import java.awt.*;

public class Rook extends Piece{

    public Rook(boolean isWhite) {
        super(isWhite);
    }

    @Override
    protected Image setImage() {
        if(isWhite){
            return Images.W_ROOK;
        }else{
            return Images.B_ROOK;
        }
    }

}

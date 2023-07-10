package de.amg.chess.pieces;

import java.awt.*;

public abstract class Piece {

    protected boolean isWhite;
    protected Image image;

    public Piece(boolean isWhite) {
        this.isWhite = isWhite;
        image = setImage();
    }

    protected abstract Image setImage();

    public boolean isWhite(){
        return isWhite;
    }

    public Image getImage() {
        return image;
    }
}

package de.amg.chess.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Images {

    public static final Image B_KING = loadImage("C:\\Chess\\Images\\bKing.png");
    public static final Image B_QUEEN = loadImage("C:\\Chess\\Images\\bQueen.png");
    public static final Image B_ROOK = loadImage("C:\\Chess\\Images\\bRook.png");
    public static final Image B_KNIGHT = loadImage("C:\\Chess\\Images\\bKnight.png");
    public static final Image B_BISHOP = loadImage("C:\\Chess\\Images\\bBishop.png");
    public static final Image B_PAWN = loadImage("C:\\Chess\\Images\\bPawn.png");
    public static final Image W_KING = loadImage("C:\\Chess\\Images\\wKing.png");
    public static final Image W_QUEEN = loadImage("C:\\Chess\\Images\\wQueen.png");
    public static final Image W_ROOK = loadImage("C:\\Chess\\Images\\wRook.png");
    public static final Image W_KNIGHT = loadImage("C:\\Chess\\Images\\wKnight.png");
    public static final Image W_BISHOP = loadImage("C:\\Chess\\Images\\wBishop.png");
    public static final Image W_PAWN = loadImage("C:\\Chess\\Images\\wPawn.png");

    private Images(){}

    private static Image loadImage(String path){
        try {
            return ImageIO.read(new File(path)).getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        } catch (IOException e){
            e.printStackTrace();
            return null;
        }
    }

}

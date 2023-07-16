package de.amg.chess.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public abstract class Images {

    public static final String path = "./src/Images";

    public static final Image B_KING = loadImage(path + "/bKing.png");
    public static final Image B_QUEEN = loadImage(path + "/bQueen.png");
    public static final Image B_ROOK = loadImage(path + "/bRook.png");
    public static final Image B_KNIGHT = loadImage(path + "/bKnight.png");
    public static final Image B_BISHOP = loadImage(path + "/bBishop.png");
    public static final Image B_PAWN = loadImage(path + "/bPawn.png");
    public static final Image W_KING = loadImage(path + "/wKing.png");
    public static final Image W_QUEEN = loadImage(path + "/wQueen.png");
    public static final Image W_ROOK = loadImage(path + "/wRook.png");
    public static final Image W_KNIGHT = loadImage(path + "/wKnight.png");
    public static final Image W_BISHOP = loadImage(path + "/wBishop.png");
    public static final Image W_PAWN = loadImage(path + "/wPawn.png");

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

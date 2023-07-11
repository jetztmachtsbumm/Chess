package de.amg.chess.model;

import de.amg.chess.util.Images;

import java.awt.*;
import java.util.HashMap;
public enum Pieces {
    ROOK, KNIGHT, BISHOP, QUEEN, KING, PAWN;
    public static HashMap<String, Pieces> sp;
    public static HashMap<Pieces, String> ps;
    public static HashMap<Pieces, Double> values;
    public static HashMap<Pieces, Image> white_images;
    public static HashMap<Pieces, Image> black_images;
    static {
        sp = new HashMap<>();
        ps = new HashMap<>();
        values = new HashMap<>();
        white_images = new HashMap<>();
        black_images = new HashMap<>();
        sp.put("R", ROOK);
        ps.put(ROOK, "R");
        sp.put("N", KNIGHT);
        ps.put(KNIGHT, "N");
        sp.put("B", BISHOP);
        ps.put(BISHOP, "B");
        sp.put("Q", QUEEN);
        ps.put(QUEEN, "Q");
        sp.put("K", KING);
        ps.put(KING, "K");
        sp.put("P", PAWN);
        ps.put(PAWN, "P");
        values.put(ROOK, 5d);
        values.put(KNIGHT, 3d);
        values.put(BISHOP, 3.15d);
        values.put(QUEEN, 9d);
        values.put(KING, 1000d);
        values.put(PAWN, 1d);
        white_images.put(ROOK, Images.W_ROOK);
        white_images.put(KNIGHT, Images.W_KNIGHT);
        white_images.put(BISHOP, Images.W_BISHOP);
        white_images.put(QUEEN, Images.W_QUEEN);
        white_images.put(KING, Images.W_KING);
        white_images.put(PAWN, Images.W_PAWN);
        black_images.put(ROOK, Images.B_ROOK);
        black_images.put(KNIGHT, Images.B_KNIGHT);
        black_images.put(BISHOP, Images.B_BISHOP);
        black_images.put(QUEEN, Images.B_QUEEN);
        black_images.put(KING, Images.B_KING);
        black_images.put(PAWN, Images.B_PAWN);
    }

}

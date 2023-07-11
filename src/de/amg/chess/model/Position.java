package de.amg.chess.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class Position {
    private Piece[][] array;

    private List<Piece[][]> past;

    private Color toPlay;

    public Color getToPlay() {
        return toPlay;
    }

    public void setToPlay(Color toPlay) {
        this.toPlay = toPlay;
    }

    public boolean isK() {
        return K;
    }

    public void setK(boolean K) {
        this.K = K;
    }

    public boolean isQ() {
        return Q;
    }

    public void setQ(boolean Q) {
        this.Q = Q;
    }

    public boolean isk() {
        return k;
    }

    public void setk(boolean k) {
        this.k = k;
    }

    public boolean isq() {
        return q;
    }

    public void setq(boolean q) {
        this.q = q;
    }

    public String getEnPassant() {
        return enPassant;
    }

    public void setEnPassant(String enPassant) {
        this.enPassant = enPassant;
    }

    public int getDrawMoves() {
        return drawMoves;
    }

    public void setDrawMoves(int drawMoves) {
        this.drawMoves = drawMoves;
    }

    public int getMoveNumber() {
        return moveNumber;
    }

    public void setMoveNumber(int moveNumber) {
        this.moveNumber = moveNumber;
    }

    public int getRepetitions(){ return repetitions; }

    public void setRepetitions(int repetitions){ this.repetitions = repetitions; }

    private boolean K;
    private boolean Q;
    private boolean k;
    private boolean q;
    private String enPassant;

    private int repetitions;
    private int drawMoves;
    private int moveNumber;

    private Position(Piece[][] array, List<Piece[][]> past, Color toPlay, boolean K, boolean Q, boolean k, boolean q, String enPassant, int repetitions, int drawMoves, int moveNumber) {
        this.array = array;
        this.past = past;
        this.toPlay = toPlay;
        this.K = K;
        this.Q = Q;
        this.k = k;
        this.q = q;
        this.enPassant = enPassant;
        this.repetitions = repetitions;
        this.drawMoves = drawMoves;
        this.moveNumber = moveNumber;
    }

    public Piece[][] getArray() {
        return array;
    }

    public void setArray(Piece[][] array) {
        this.array = array;
    }

    public Piece getPiece(int a, int b){
        return array[a][b];
    }

    public Piece getPiece(char a, int b){
        return array[8-b]["abcdefgh".indexOf(a)];
    }

    public Piece getPiece(String s){
        char[] c = s.toCharArray();
        return array[8-Character.getNumericValue(c[1])]["abcdefgh".indexOf(c[0])];
    }

    public void setPiece(int a, int b, Piece piece){
        array[a][b] = piece;
    }

    public void setPiece(char a, int b, Piece piece){
        array[8-b]["abcdefgh".indexOf(a)] = piece;
    }

    public void setPiece(String s, Piece piece){
        char[] c = s.toCharArray();
        array[8-Character.getNumericValue(c[1])]["abcdefgh".indexOf(c[0])] = piece;
    }

    public void deletePiece(int a, int b){
        array[a][b] = null;
    }

    public void deletePiece(char a, int b){
        array[8-b]["abcdefgh".indexOf(a)] = null;
    }

    public void deletePiece(String s){
        char[] c = s.toCharArray();
        array[8-Character.getNumericValue(c[1])]["abcdefgh".indexOf(c[0])] = null;
    }

    public int[] stringToInt(String s){
        char[] c = s.toCharArray();
        return new int [] {8-Character.getNumericValue(c[1]), "abcdefgh".indexOf(c[0])};
    }

    public String intToString(int[] i){
        return "abcdefgh".charAt(i[1])+String.valueOf(8 - i[0]);
    }

    public String intToString(int a, int b){
        return "abcdefgh".charAt(b)+String.valueOf(8 - a);
    }

    public void addPast(){
        past.add(this.copy().getArray());
    }

    public List<Piece[][]> getPast(){
        return past;
    }

    public static Position fromFEN(String s) {
        Piece[][] array = new Piece[8][8];
        String[] params = s.split(" ");
        for (int i = 1; i < 9; i++) {
            params[0] = params[0].replaceAll("" + i, "*".repeat(i));
        }
        String[] pos = params[0].split("/");
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                switch (pos[i].charAt(j)) {
                    case 'r' -> array[i][j] = new Piece(Pieces.ROOK, Color.BLACK);
                    case 'n' -> array[i][j] = new Piece(Pieces.KNIGHT, Color.BLACK);
                    case 'b' -> array[i][j] = new Piece(Pieces.BISHOP, Color.BLACK);
                    case 'q' -> array[i][j] = new Piece(Pieces.QUEEN, Color.BLACK);
                    case 'k' -> array[i][j] = new Piece(Pieces.KING, Color.BLACK);
                    case 'p' -> array[i][j] = new Piece(Pieces.PAWN, Color.BLACK);
                    case 'R' -> array[i][j] = new Piece(Pieces.ROOK, Color.WHITE);
                    case 'N' -> array[i][j] = new Piece(Pieces.KNIGHT, Color.WHITE);
                    case 'B' -> array[i][j] = new Piece(Pieces.BISHOP, Color.WHITE);
                    case 'Q' -> array[i][j] = new Piece(Pieces.QUEEN, Color.WHITE);
                    case 'K' -> array[i][j] = new Piece(Pieces.KING, Color.WHITE);
                    case 'P' -> array[i][j] = new Piece(Pieces.PAWN, Color.WHITE);
                    case '*' -> array[i][j] = null;
                }
            }
        }
        return new Position(array, new ArrayList<>(), (params[1].equals("w")) ? Color.WHITE : Color.BLACK, params[2].contains("K"), params[2].contains("Q"), params[2].contains("k"), params[2].contains("q"), (!params[3].equals("-")) ? params[3] : null, 0, Integer.valueOf(params[4]), Integer.valueOf(params[5]));
    }

    public String toFEN() {
        StringBuilder b = new StringBuilder();
        int nc = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (array[i][j] == null) {
                    nc++;
                } else if (nc > 0) {
                    b.append(String.valueOf(nc));
                    nc = 0;
                }

                if (array[i][j] != null) {
                    String st = switch (array[i][j].getType()) {
                        case ROOK -> "r";
                        case KNIGHT -> "n";
                        case BISHOP -> "b";
                        case QUEEN -> "q";
                        case KING -> "k";
                        case PAWN -> "p";
                    };
                    if (array[i][j].getColor() == Color.WHITE) {
                        st = st.toUpperCase();
                    }
                    b.append(st);
                }
            }
            if (nc > 0){
                b.append(String.valueOf(nc));
                nc = 0;
            }
            if (i < 7){
                b.append("/");
            }
        }
        boolean nothing = !K && !Q && !k && !q;
        b.append(" ").append((toPlay == Color.WHITE) ? "w" : "b").append(" ").append(K ? "K" : "").append(Q ? "Q" : "").append(k ? "k" : "").append(q ? "q" : "").append(nothing ? "-" : "").append(" ").append((enPassant != null) ? enPassant : "-").append(" ").append(String.valueOf(drawMoves)).append(" ").append(String.valueOf(moveNumber));
        return b.toString();
    }
    public void print(){
        System.out.print("\t");
        for (int i=0; i<8; i++){
            System.out.print("\t" + "abcdefgh".toCharArray()[i]);
        }
        System.out.println("\n");
        for (int i=0; i<8; i++){
            System.out.print(8-i);
            System.out.print("\t");
            for (int j=0; j<8; j++){
                String p;
                if (array[i][j] != null) {
                    p = Pieces.ps.get(array[i][j].getType());
                    if (array[i][j].getColor() == Color.BLACK) {
                        p = p.toLowerCase();
                    }
                }
                else{
                    p = " ";
                }
                System.out.print("\t" + p);
            }
            System.out.println();
        }
    }

    public Position copy(){
        Piece[][] arr = new Piece[8][8];
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                arr[i][j] = (array[i][j] != null) ? array[i][j].copy() : null;
            }
        }
        List<Piece[][]> pas = new ArrayList<>();
        for (Piece[][] p:past){
            Piece[][] arra = new Piece[8][8];
            for (int i=0; i<8; i++){
                for (int j=0; j<8; j++){
                    arra[i][j] = (p[i][j] != null) ? p[i][j].copy() : null;
                }
            }
            pas.add(arra);
        }
        return new Position(arr, pas, toPlay, K, Q, k, q, enPassant, 0, drawMoves, moveNumber);
    }

    @Override
    public boolean equals(Object o){
        if (o instanceof Position){
            Position p = (Position)o;
            return Arrays.deepEquals(this.array, p.array) && this.toPlay == p.toPlay && this.K == p.K && this.Q == p.Q && this.k == p.k && this.q == p.q && Objects.equals(this.enPassant, p.enPassant) && this.drawMoves == p.drawMoves && this.moveNumber == p.moveNumber;
        }
        else{
            return false;
        }
    }

    public Color getOpponent(Color color){
        return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
    }

    public String getKingLocation(Color color){
        return getPieces(Pieces.KING, color).get(0);
    }

    public List<String> getPieces(Pieces type, Color color){
        List<String> result = new ArrayList<>();
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (array[i][j] != null && array[i][j].getType() == type && array[i][j].getColor() == color){
                    result.add("abcdefgh".charAt(j)+String.valueOf(8-i));
                }
            }
        }
        return result;
    }

    public List<String> getAllPieces(Color color){
        List<String> result = new ArrayList<>();
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (array[i][j] != null && array[i][j].getColor() == color){
                    result.add("abcdefgh".charAt(j)+String.valueOf(8-i));
                }
            }
        }
        return result;
    }

    public List<String> getPieceList(){
        List<String> result = new ArrayList<>();
        for (int i=0; i<8; i++){
            for (int j=0; j<8; j++){
                if (array[i][j] != null) {
                    result.add("abcdefgh".charAt(j) + String.valueOf(8 - i));
                }
            }
        }
        return result;
    }

    public boolean isInvalid(int a, int b){
        return a < 0 || a >= array.length || b < 0 || b >= array.length;
    }

    public boolean isInCheck(Color color){
        String s = getKingLocation(color);
        char[] c = s.toCharArray();
        int a = 8-Character.getNumericValue(c[1]);
        int b = "abcdefgh".indexOf(c[0]);
        /**List<String> pieces = getAllPieces(getOpponent(color));
        pieces = pieces.stream().filter((p) -> getPiece(p).getType() != Pieces.KING).toList();
        for (String st:pieces){
            for (Move move:getAvailableMoves(st, true, false)){
                if (move.getDest().equals(s)){
                    return true;
                }
            }
        }
        return false;**/
        for (int i = a+1; i < 8; i++) {
            if (isInvalid(i, b)) {
                continue;
            }
            Piece p = array[i][b];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.ROOK || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                continue;
            }
            else{
                break;
            }
        }
        for (int i = a-1; i >= 0; i--) {
            if (isInvalid(i, b)) {
                continue;
            }
            Piece p = array[i][b];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.ROOK || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                continue;
            }
            else{
                break;
            }
        }
        for (int i = b+1; i < 8; i++) {
            if (isInvalid(a, i)) {
                continue;
            }
            Piece p = array[a][i];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.ROOK || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                continue;
            }
            else{
                break;
            }
        }
        for (int i = b-1; i >= 0; i--) {
            if (isInvalid(a, i)) {
                continue;
            }
            Piece p = array[a][i];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.ROOK || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                continue;
            }
            else{
                break;
            }
        }
        int j = b+1;
        for (int i = a+1; i < 8; i++) {
            if (isInvalid(i, j)) {
                continue;
            }
            Piece p = array[i][j];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.BISHOP || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                j++;
            }
            else{
                break;
            }
        }
        j = b+1;
        for (int i = a-1; i >= 0; i--) {
            if (isInvalid(i, j)) {
                continue;
            }
            Piece p = array[i][j];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.BISHOP || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                j++;
            }
            else{
                break;
            }
        }
        j = b-1;
        for (int i = a+1; i < 8; i++) {
            if (isInvalid(i, j)) {
                continue;
            }
            Piece p = array[i][j];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.BISHOP || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                j--;
            }
            else{
                break;
            }
        }
        j = b-1;
        for (int i = a-1; i >= 0; i--) {
            if (isInvalid(i, j)) {
                continue;
            }
            Piece p = array[i][j];
            if(p != null && p.getColor() == getOpponent(color) && (p.getType() == Pieces.BISHOP || p.getType() == Pieces.QUEEN)){
                return true;
            }
            else if (p == null){
                j--;
            }
            else{
                break;
            }
        }
        int[] arr1 = new int[] {-2, -2, 2, 2, -1, -1, 1, 1};
        int[] arr2 = new int[] {-1, 1, -1, 1, -2, 2, -2, 2};
        for (int co=0; co<8; co++){
            int i = a+arr1[co];
            j = b+arr2[co];
            if (isInvalid(i, j)) {
                continue;
            }
            Piece p = array[i][j];
            if(p != null && p.getColor() == getOpponent(color) && p.getType() == Pieces.KNIGHT){
                return true;
            }
        }
        if (color == Color.WHITE) {
            if (!isInvalid(a-1, b+1) && array[a-1][b+1] != null && array[a - 1][b + 1].getType() == Pieces.PAWN && array[a - 1][b + 1].getColor() == getOpponent(color)) {
                return true;
            }
            if (!isInvalid(a-1, b-1) &&  array[a-1][b-1] != null &&array[a - 1][b - 1].getType() == Pieces.PAWN && array[a - 1][b - 1].getColor() == getOpponent(color)) {
                return true;
            }
        }
        else{
            if (!isInvalid(a+1, b+1) &&  array[a+1][b+1] != null &&array[a + 1][b + 1].getType() == Pieces.PAWN && array[a + 1][b + 1].getColor() == getOpponent(color)) {
                return true;
            }
            if (!isInvalid(a+1, b-1) &&  array[a+1][b-1] != null &&array[a + 1][b - 1].getType() == Pieces.PAWN && array[a + 1][b - 1].getColor() == getOpponent(color)) {
                return true;
            }
        }
        return false;
    }

    public List<String> getCheckers(Color color){
        String s = getKingLocation(color);
        char[] c = s.toCharArray();
        int a = 8-Character.getNumericValue(c[1]);
        int b = "abcdefgh".indexOf(c[0]);
        List<String> pieces = getAllPieces(getOpponent(color));
        pieces = pieces.stream().filter((p) -> getPiece(p).getType() != Pieces.KING).toList();
        List<String> checkers = new ArrayList<>();
        for (String st:pieces){
            for (Move move:getAvailableMoves(st, false, true)){
                if (move.getDest().equals(s)){
                    checkers.add(move.getOrigin());
                }
            }
        }
        return checkers;
    }

    public double getMaterial(Color color){
        double sum = 0d;
        for (String s:getAllPieces(color)){
            sum += Pieces.values.get(getPiece(s).getType());
        }
        return sum;
    }

    public double getMaterialImbalance(){
        return getMaterial(Color.WHITE) - getMaterial(Color.BLACK);
    }

    public boolean isInStalemate(){
        return !isInCheck(toPlay) && getAllPlayableMoves().isEmpty();
    }

    public boolean isInDraw(){
        return drawMoves >= 100 || repetitions >= 3;
    }

    public boolean isInRemis(){
        return isInStalemate() || isInDraw();
    }

    public boolean isInMate(Color color){
        if (getAvailableMoves(getKingLocation(color), false, true).isEmpty()) {
            List<String> checkers = getCheckers(color);
            if (checkers.isEmpty()) {
                return false;
            } else if (checkers.size() > 1) {
                return true;
            } else {
                List<String> pieces = getAllPieces(color);
                //pieces = pieces.stream().filter((p) -> getPiece(p).getType() != de.amg.chess.model.Pieces.KING).toList();
                for (String st : pieces) {
                    for (Move move : getAvailableMoves(st, false, true)) {
                        Position p = this.copy();
                        move.setPosition(p);
                        move.apply();
                        if (!p.isInCheck(color)) {
                            return false;
                        }
                    }
                }
                return true;
            }
        }
        else{
            return false;
        }
    }
    private void considerMove(String s, List<Move> list, int a, int b, Pieces promote, boolean ignoreCheck){
        Piece piece = getPiece(s);
        Position p = this.copy();
        String dest = "abcdefgh".charAt(b)+String.valueOf(8 - a);
        Move move = new Move(p, piece, s,  dest, false, promote, false, false);
        if ((array[a][b] != null && array[a][b].getColor() == getOpponent(piece.getColor())) || dest.equals(enPassant)){
            move.setTakes(true);
        }
        move.apply();
        if (piece.getType() != Pieces.KING && p.isInCheck(piece.getColor())){
            return;
        }
        if (!ignoreCheck && p.isInMate(getOpponent(piece.getColor()))) {
            move.setMate(true);
        } else if (!ignoreCheck && p.isInCheck(getOpponent(piece.getColor()))) {
            move.setCheck(true);
        }
        move.setPosition(this);
        list.add(move);
    }

    public List<Move> getPlayableMoves(String s){
        return getAvailableMoves(s, true, false);
    }

    public List<Move> getAllPlayableMoves(){
        List<Move> l = new ArrayList<>();
        for (String piece:getPieceList()){
            l.addAll(getPlayableMoves(piece));
        }
        return l;
    }

    public List<Move> getAllAvailableMoves(boolean checkColor){
        List<Move> l = new ArrayList<>();
        for (String piece:getPieceList()){
            l.addAll(getAvailableMoves(piece, checkColor, false));
        }
        return l;
    }

    public List<Move> getAvailableMoves(String s, boolean checkColor, boolean ignoreCheck){
        Piece piece = getPiece(s);
        List<Move> result = new ArrayList<>();
        if (checkColor && piece.getColor() != toPlay){
            return result;
        }
        char[] c = s.toCharArray();
        int a = 8-Character.getNumericValue(c[1]);
        int b = "abcdefgh".indexOf(c[0]);
        switch (piece.getType()) {
            case ROOK:
                for (int i = a+1; i < 8; i++) {
                    if (array[i][b] != null && array[i][b].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][b] != null && array[i][b].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, b, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, b, null, ignoreCheck);
                    }
                }
                for (int i = a-1; i >= 0; i--) {
                    if (array[i][b] != null && array[i][b].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][b] != null && array[i][b].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, b, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, b, null, ignoreCheck);
                    }
                }
                for (int i = b+1; i < 8; i++) {
                    if (array[a][i] != null && array[a][i].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[a][i] != null && array[a][i].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, a, i, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, a, i, null, ignoreCheck);
                    }
                }
                for (int i = b-1; i >= 0; i--) {
                    if (array[a][i] != null && array[a][i].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[a][i] != null && array[a][i].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, a, i, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, a, i, null, ignoreCheck);
                    }
                }
                break;
            case KNIGHT:
                int[] arr1 = new int[] {-2, -2, 2, 2, -1, -1, 1, 1};
                int[] arr2 = new int[] {-1, 1, -1, 1, -2, 2, -2, 2};
                for (int co=0; co<8; co++){
                    int i = a+arr1[co];
                    int j = b+arr2[co];
                    if (i < 0 || i >= array.length || j < 0 || j >= array.length){
                        continue;
                    }
                    if (array[i][j] == null || array[i][j].getColor() == getOpponent(piece.getColor())) {
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                }
                break;
            case BISHOP:
                int j = b+1;
                for (int i = a+1; i < 8; i++) {
                    if (j >= array.length){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j++;
                }
                j = b+1;
                for (int i = a-1; i >= 0; i--) {
                    if (j >= array.length){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j++;
                }
                j = b-1;
                for (int i = a+1; i < 8; i++) {
                    if (j < 0){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j--;
                }
                j = b-1;
                for (int i = a-1; i >= 0; i--) {
                    if (j < 0){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j--;
                }
                break;
            case QUEEN:
                for (int i = a+1; i < 8; i++) {
                    if (array[i][b] != null && array[i][b].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][b] != null && array[i][b].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, b, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, b, null, ignoreCheck);
                    }
                }
                for (int i = a-1; i >= 0; i--) {
                    if (array[i][b] != null && array[i][b].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][b] != null && array[i][b].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, b, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, b, null, ignoreCheck);
                    }
                }
                for (int i = b+1; i < 8; i++) {
                    if (array[a][i] != null && array[a][i].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[a][i] != null && array[a][i].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, a, i, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, a, i, null, ignoreCheck);
                    }
                }
                for (int i = b-1; i >= 0; i--) {
                    if (array[a][i] != null && array[a][i].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[a][i] != null && array[a][i].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, a, i, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, a, i, null, ignoreCheck);
                    }
                }
                j = b+1;
                for (int i = a+1; i < 8; i++) {
                    if (j >= array.length){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j++;
                }
                j = b+1;
                for (int i = a-1; i >= 0; i--) {
                    if (j >= array.length){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j++;
                }
                j = b-1;
                for (int i = a+1; i < 8; i++) {
                    if (j < 0){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j--;
                }
                j = b-1;
                for (int i = a-1; i >= 0; i--) {
                    if (j < 0){
                        break;
                    }
                    if (array[i][j] != null && array[i][j].getColor() == piece.getColor()) {
                        break;
                    }
                    else if(array[i][j] != null && array[i][j].getColor() == getOpponent(piece.getColor())){
                        considerMove(s, result, i, j, null, ignoreCheck);
                        break;
                    }
                    else{
                        considerMove(s, result, i, j, null, ignoreCheck);
                    }
                    j--;
                }
                break;
            case KING:
                int[] arr3 = new int[] {-1, 0, 1, -1, 1, -1, 0, 1};
                int[] arr4 = new int[] {-1, -1, -1, 0, 0, 1, 1, 1};
                List<String> pieces = getAllPieces(getOpponent(piece.getColor()));
                pieces = pieces.stream().filter((p) -> getPiece(p).getType() != Pieces.KING).toList();
                for (int co=0; co<8; co++){
                    int i = a+arr3[co];
                    j = b+arr4[co];
                    if (i < 0 || i >= array.length || j < 0 || j >= array.length || (array[i][j] != null && array[i][j].getColor() == piece.getColor())){
                        continue;
                    }
                    /**boolean blocked = false;
                    for (String st : pieces) {
                        for (de.amg.chess.model.Move move : getAvailableMoves(st, true, false)) {
                            if (move.getDest().equals("abcdefgh".charAt(j)+String.valueOf(8 - i))) {
                                blocked = true;
                                break;
                            }
                        }
                    }**/
                    Position p = this.copy();
                    new Move(p, piece, s, "abcdefgh".charAt(j)+String.valueOf(8 - i), false, null, false, false).apply();
                    boolean blocked = p.isInCheck(piece.getColor());

                    String op = getKingLocation(getOpponent(piece.getColor()));
                    char[] ca = op.toCharArray();
                    int aa = 8-Character.getNumericValue(ca[1]);
                    int ba = "abcdefgh".indexOf(ca[0]);
                    if (blocked || Math.sqrt((i-aa)*(i-aa)+(j-ba)*(j-ba)) < 1.5f){
                        continue;
                    }
                    considerMove(s, result, i, j, null, true);
                }
                if ((piece.getColor() == Color.WHITE && K && !isInCheck(Color.WHITE)) || (piece.getColor() == Color.BLACK && k && !isInCheck(Color.BLACK))){
                    boolean bl = false;
                    List<String> tr = new ArrayList<>();
                    for (int i=b+1; i<7; i++){
                        if (array[a][i] != null){
                            bl = true;
                            break;
                        }
                        tr.add("abcdefgh".charAt(i)+String.valueOf(8 - a));
                    }
                    if (!bl){
                        boolean blocked = false;
                        for (String st : pieces) {
                            for (Move move : getAvailableMoves(st, false, true)) {
                                if (tr.contains(move.getDest())) {
                                    blocked = true;
                                    break;
                                }
                            }
                        }
                        if (!blocked){
                            considerMove(s, result, a, b+2, null, true);
                        }
                    }
                }
                if ((piece.getColor() == Color.WHITE && Q && !isInCheck(Color.WHITE)) || (piece.getColor() == Color.BLACK && q && !isInCheck(Color.BLACK))){
                    boolean bl = false;
                    List<String> tr = new ArrayList<>();
                    for (int i=b-1; i>0; i--){
                        if (array[a][i] != null){
                            bl = true;
                            break;
                        }
                        tr.add("abcdefgh".charAt(i)+String.valueOf(8 - a));
                    }
                    if (!bl){
                        boolean blocked = false;
                        for (String st : pieces) {
                            for (Move move : getAvailableMoves(st, false, true)) {
                                if (tr.contains(move.getDest())) {
                                    blocked = true;
                                    break;
                                }
                            }
                        }
                        if (!blocked){
                            considerMove(s, result, a, b-2, null, true);
                        }
                    }
                }
                break;
            case PAWN:
                if (piece.getColor() == Color.WHITE) {
                    if (a-1 >= 0 && array[a-1][b] == null){
                        if (a==1){
                            considerMove(s, result, a - 1, b, Pieces.QUEEN, false);
                            considerMove(s, result, a - 1, b, Pieces.ROOK, false);
                            considerMove(s, result, a - 1, b, Pieces.BISHOP, false);
                            considerMove(s, result, a - 1, b, Pieces.KNIGHT, false);
                        }
                        else {
                            considerMove(s, result, a - 1, b, null, ignoreCheck);
                        }
                        if (a == 6 && array[a-2][b] == null){
                            considerMove(s, result, a-2, b, null, ignoreCheck);
                        }
                    }
                    String ms1 = "";
                    String ms2 = "";
                    if (b+1 < array.length) {
                        ms1 = "abcdefgh".charAt(b + 1) + String.valueOf(9 - a);
                    }
                    if (b-1 >= 0) {
                        ms2 = "abcdefgh".charAt(b - 1) + String.valueOf(9 - a);
                    }
                    if (a-1 >= 0 && b+1 < array.length && array[a-1][b+1] != null && array[a-1][b+1].getColor() == getOpponent(piece.getColor()) || ms1.equals(enPassant)){
                        if (a==1){
                            considerMove(s, result, a - 1, b+1, Pieces.QUEEN, false);
                            considerMove(s, result, a - 1, b+1, Pieces.ROOK, false);
                            considerMove(s, result, a - 1, b+1, Pieces.BISHOP, false);
                            considerMove(s, result, a - 1, b+1, Pieces.KNIGHT, false);
                        }
                        else{
                            considerMove(s, result, a-1, b+1, null, ignoreCheck);
                        }
                    }
                    if (a-1 >= 0 && b-1 >= 0 && array[a-1][b-1] != null && array[a-1][b-1].getColor() == getOpponent(piece.getColor()) || ms2.equals(enPassant)){
                        if (a==1){
                            considerMove(s, result, a - 1, b-1, Pieces.QUEEN, false);
                            considerMove(s, result, a - 1, b-1, Pieces.ROOK, false);
                            considerMove(s, result, a - 1, b-1, Pieces.BISHOP, false);
                            considerMove(s, result, a - 1, b-1, Pieces.KNIGHT, false);
                        }
                        else{
                            considerMove(s, result, a-1, b-1, null, ignoreCheck);
                        }
                    }
                }
                else{
                    if (a+1 < array.length && array[a+1][b] == null){
                        if (a==6) {
                            considerMove(s, result, a + 1, b, Pieces.QUEEN, false);
                            considerMove(s, result, a + 1, b, Pieces.ROOK, false);
                            considerMove(s, result, a + 1, b, Pieces.BISHOP, false);
                            considerMove(s, result, a + 1, b, Pieces.KNIGHT, false);
                        }
                        else{
                            considerMove(s, result, a + 1, b, null, ignoreCheck);
                        }
                        if (a == 1 && array[a+2][b] == null){
                            considerMove(s, result, a+2, b, null, ignoreCheck);
                        }
                    }
                    String ms1 = "";
                    String ms2 = "";
                    if (b+1 < array.length) {
                        ms1 = "abcdefgh".charAt(b + 1) + String.valueOf(7 - a);
                    }
                    if (b-1 >= 0){
                        ms2 = "abcdefgh".charAt(b-1) + String.valueOf(7 - a);
                    }
                    if (a+1 < array.length && b+1 < array.length && array[a+1][b+1] != null && array[a+1][b+1].getColor() == getOpponent(piece.getColor()) || ms1.equals(enPassant)){
                        if (a==6){
                            considerMove(s, result, a + 1, b+1, Pieces.QUEEN, false);
                            considerMove(s, result, a + 1, b+1, Pieces.ROOK, false);
                            considerMove(s, result, a + 1, b+1, Pieces.BISHOP, false);
                            considerMove(s, result, a + 1, b+1, Pieces.KNIGHT, false);
                        }
                        else{
                            considerMove(s, result, a+1, b+1, null, ignoreCheck);
                        }
                    }
                    if (a+1 < array.length && b-1 >= 0 && array[a+1][b-1] != null && array[a+1][b-1].getColor() == getOpponent(piece.getColor()) || ms2.equals(enPassant)){
                        if (a==6){
                            considerMove(s, result, a + 1, b-1, Pieces.QUEEN, false);
                            considerMove(s, result, a + 1, b-1, Pieces.ROOK, false);
                            considerMove(s, result, a + 1, b-1, Pieces.BISHOP, false);
                            considerMove(s, result, a + 1, b-1, Pieces.KNIGHT, false);
                        }
                        else{
                            considerMove(s, result, a+1, b-1, null, ignoreCheck);
                        }
                    }
                }
                break;
        }
        return result;
    }
}

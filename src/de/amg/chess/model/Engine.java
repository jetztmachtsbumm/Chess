package de.amg.chess.model;

import java.util.*;

public class Engine {
    public static HashMap<Move, Double> evals = new HashMap<>();

    public static Move heuristic(Position position){
        Move best = null;
        double bestDist = 10000d;
        for (Move move:position.getAllPlayableMoves()){
            char[] c = move.getDest().toCharArray();
            int a = 8-Character.getNumericValue(c[1]);
            int b = "abcdefgh".indexOf(c[0]);
            Position p = move.getPosition();
            String k = p.getKingLocation(p.getOpponent(move.getPiece().getColor()));
            char[] ck = k.toCharArray();
            int ak = 8-Character.getNumericValue(ck[1]);
            int bk = "abcdefgh".indexOf(ck[0]);
            double cd = Math.sqrt(Math.pow(3.5d-a, 2)+Math.pow(3.5d-b, 2))/2;
            double kd = Math.sqrt(Math.pow(ak-a, 2)+Math.pow(bk-b, 2));
            Position np = position.copy();
            move.setPosition(np);
            move.apply();
            int danger = 0;
            for (Move m:np.getAllPlayableMoves()){
                //m.apply();
                //double mat = np.getMaterial(move.getPiece().getColor()) - position.getMaterial(move.getPiece().getColor());
                if (m.getDest().equals(move.getDest())){// && de.amg.chess.model.Pieces.values.get(m.getPiece().getType()) < de.amg.chess.model.Pieces.values.get(move.getPiece().getType())){
                    danger = 5000;
                    break;
                }
            }
            move.setPosition(position);
            if (cd+kd+danger < bestDist){
                bestDist = cd+kd+danger;
                best = move;
            }
        }
        return best;
    }
    public static List<Move> evaluate(Position position){
        for (Move move:position.getAllPlayableMoves()){
            evals.put(move, 0d);
            recurse(move, null, 0, 0);
        }
        double maxEval = -10000d;
        List<Move> output = new ArrayList<>();
        for (Move m:evals.keySet()){
            if (evals.get(m) > maxEval){
                output.clear();
                output.add(m);
                maxEval = evals.get(m);
            }
            else if (evals.get(m) == maxEval){
                output.add(m);
            }
        }
        return output;
    }
    public static void recurse(Move start, Move move, int depth, int maxdepth){
        if (move == null){
            move = start.copy();
        }
        Position orig = move.getPosition();
        Position p = orig.copy();
        move.setPosition(p);
        move.apply();
        for (Move opmove:p.getAllPlayableMoves()){
            Position o = p.copy();
            opmove.setPosition(o);
            opmove.apply();
            evals.put(start, evals.get(start)+o.getMaterial(move.getPiece().getColor()) - orig.getMaterial(move.getPiece().getColor()));
            if (depth <= maxdepth) {
                try {
                    for (Move nextmove : o.getAllPlayableMoves()) {
                        recurse(start, nextmove, depth + 1, maxdepth);
                    }
                }
                catch(Exception e){continue;}
            }
        }
    }
    public static void run(){
        Random rand = new Random();
        //de.amg.chess.model.Position pos = de.amg.chess.model.Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        Position pos = Position.fromFEN("rn1qkb1r/ppp1p2p/5p2/3N1Pp1/3P1B2/5Q2/PPP2PPP/R3KBNR w KQkq - 0 9");
        System.out.println(pos.getAllPlayableMoves());
        while (true){
            String r = "";
            Move move;
            Scanner sc = new Scanner(System.in);
            while (true){
                r = sc.nextLine();
                //try{
                    move = Move.fromString(pos, r);
                    if (move.isLegal()){
                        break;
                    }
                //}
                //catch(Exception e){}
            }
            move.apply();
            List<Move> moves = pos.getAllPlayableMoves();
            //List<de.amg.chess.model.Move> moves = evaluate(pos);
            /**de.amg.chess.model.Move m = heuristic(pos);
            if (m == null){
                break;
            }**/
            if (moves.isEmpty() || pos.isInDraw() || pos.isInStalemate()) {
                break;
            }
            System.out.println(moves);
            /**for (de.amg.chess.model.Move mo:moves){
                if (mo.getPiece().getType() == de.amg.chess.model.Pieces.KNIGHT && (mo.getDest().equals("f6") || mo.getDest().equals("g8"))){
                    mo.apply();
                    System.out.println(mo.toString());
                    break;
                }
            }**/
            Move m = moves.get(rand.nextInt(moves.size()));
            m.apply();
            System.out.println(m.toString());
        }
    }
}

package de.amg.chess.main;

import de.amg.chess.model.Move;
import de.amg.chess.model.Piece;
import de.amg.chess.model.Position;
import de.amg.chess.model.Pieces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main {

    public static Main instance;

    private JFrame menuFrame;

    private JFrame gameFrame;

    private JPanel gamePanel;
    private Position position;

    private Piece[][] array;

    private java.util.List<Move> moves;

    private Piece currentSelectedPiece;

    private String curSelPiecePos;
    private boolean mirrored;

    private boolean promote;

    private int px;
    private int py;

    private final int fieldSize = 90;

    private Main() {
        instance = this;

        loadMainMenuWindow();

        position = Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        array = position.getArray();
        moves = position.getAllPlayableMoves();
    }

    private void loadMainMenuWindow(){
        menuFrame = new JFrame();
        menuFrame.setBounds(1920 / 2, 1080 / 2, 420, 420);
        menuFrame.setLayout(null);
        menuFrame.setResizable(false);
        menuFrame.setTitle("Chess");
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JButton singleplayerButton = new JButton("Singleplayer");
        JButton multiplayerButton = new JButton("Multiplayer");
        JButton quitButton = new JButton("Quit");

        singleplayerButton.setBounds(130, 50, 150, 50);
        multiplayerButton.setBounds(130, 150, 150, 50);
        quitButton.setBounds(130, 250, 150, 50);

        singleplayerButton.addActionListener(null);
        multiplayerButton.addActionListener(e -> loadGameWindow());
        quitButton.addActionListener(e -> menuFrame.dispose());

        singleplayerButton.setFocusPainted(false);
        multiplayerButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);

        menuFrame.add(singleplayerButton);
        menuFrame.add(multiplayerButton);
        menuFrame.add(quitButton);

        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    private void loadGameWindow(){
        menuFrame.dispose();

        gameFrame = new JFrame();
        gameFrame.setBounds(0, 0, 1920, 1080);
        gameFrame.setUndecorated(true);

        JButton mirrorButton = new JButton("Mirror");
        JButton mainMenuButton = new JButton("MainMenu");

        mirrorButton.setBounds(900, 20, 150, 50);
        mainMenuButton.setBounds(1100, 20, 150, 50);

        mirrorButton.addActionListener(e -> {
            mirrored = !mirrored;
            gamePanel.repaint();
        });
        mainMenuButton.addActionListener(e -> {
            loadMainMenuWindow();
            gameFrame.dispose();
        });

        mirrorButton.setFocusPainted(false);
        mainMenuButton.setFocusPainted(false);

        gamePanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                super.paint(g);
                boolean white = true;
                for(int x = 0; x < 8; x++){
                    for(int y = 0; y < 8; y++){
                        Piece piece;
                        if(mirrored){
                            piece = array[7-x][7-y];
                        }else{
                            piece = array[x][y];
                        }
                        g.setColor(white ? Color.LIGHT_GRAY : Color.DARK_GRAY);

                        g.fillRect(fieldSize * y, fieldSize * x, fieldSize, fieldSize);

                        if(piece != null){
                            g.drawImage(piece.getColor() == de.amg.chess.model.Color.WHITE ? Pieces.white_images.get(piece.getType()) : Pieces.black_images.get(piece.getType()), y * fieldSize, x * fieldSize, this); // x and y switch is intentional
                        }
                        white = !white;
                    }
                    white = !white;
                }
                if (currentSelectedPiece != null){
                    for (Move move:moves){
                        if (curSelPiecePos.equals(move.getOrigin())){
                            int[] dest = position.stringToInt(move.getDest());
                            if (mirrored){
                                dest[0] = 7 - dest[0];
                                dest[1] = 7 - dest[1];
                            }
                            g.setColor(Color.BLUE);
                            g.fillOval(fieldSize*dest[1]+fieldSize/2-10, fieldSize*dest[0]+fieldSize/2-10, 20, 20);
                        }
                    }
                }
                else if(promote){
                    g.setColor(Color.WHITE);
                    int xval = fieldSize*py;
                    int yval = fieldSize*px;
                    int factor = px == 7 ? -1 : 1;
                    for (int i = 0; i < 4; i++){
                        g.fillOval(xval, yval+factor*i*fieldSize, fieldSize, fieldSize);
                    }
                    if (px == 0){
                        g.drawImage(Pieces.white_images.get(Pieces.QUEEN), xval, yval, this);
                        g.drawImage(Pieces.white_images.get(Pieces.KNIGHT), xval, yval+factor*fieldSize, this);
                        g.drawImage(Pieces.white_images.get(Pieces.ROOK), xval, yval+factor*fieldSize*2, this);
                        g.drawImage(Pieces.white_images.get(Pieces.BISHOP), xval, yval+factor*fieldSize*3, this);
                    }
                    else if (px == 7){
                        g.drawImage(Pieces.black_images.get(Pieces.QUEEN), xval, yval, this);
                        g.drawImage(Pieces.black_images.get(Pieces.KNIGHT), xval, yval+factor*fieldSize, this);
                        g.drawImage(Pieces.black_images.get(Pieces.ROOK), xval, yval+factor*fieldSize*2, this);
                        g.drawImage(Pieces.black_images.get(Pieces.BISHOP), xval, yval+factor*fieldSize*3, this);
                    }
                }
                add(mirrorButton);
                add(mainMenuButton);
            }
        };

        gamePanel.setLayout(null);

        gameFrame.add(gamePanel);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gameFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (promote){
                    int xval = fieldSize*py;
                    int yval = fieldSize*px;
                    if (xval <= e.getX() && e.getX() <= xval+fieldSize && ((px == 0 && 0 <= e.getY() && fieldSize*4 > e.getY()) || (px == 7 && 1080 > e.getY() && fieldSize*4 <= e.getY()))){
                        int val = (Math.abs(e.getY() - ((px==0) ? yval : yval+fieldSize))) / fieldSize;
                        Pieces type = switch (val){
                            case 0 -> Pieces.QUEEN;
                            case 1 -> Pieces.KNIGHT;
                            case 2 -> Pieces.ROOK;
                            case 3 -> Pieces.BISHOP;
                            default -> null;
                        };
                        for (Move move:moves){
                            if (move.getDest().equals(position.intToString(px, py)) && move.getPromote() == type){
                                move.apply();
                                break;
                            }
                        }
                        promote = false;
                        moves = position.getAllPlayableMoves();
                        //System.out.println(moves);
                        gamePanel.repaint();
                    }
                    else{
                        return;
                    }
                }
                int y = e.getX() / fieldSize; // this is intentional
                int x = e.getY() / fieldSize;
                if (x < 0 || x > 7 || y < 0 || y > 7){
                    return;
                }

                if(mirrored){
                    x = 7 - x;
                    y = 7 - y;
                }

                if(currentSelectedPiece == null){
                    currentSelectedPiece = array[x][y];
                    curSelPiecePos = position.intToString(x, y);
                    gamePanel.repaint();
                }else{
                    boolean legal = false;
                    for (Move move:moves){
                        if (move.getOrigin().equals(curSelPiecePos) && move.getDest().equals(position.intToString(x, y))){
                            if (move.getPromote() != null){
                                promote = true;
                                legal = true;
                                px = x;
                                py = y;
                                break;
                            }
                            legal = true;
                            move.apply();
                            break;
                        }
                    }
                    if (!legal){
                        int[] ar = position.stringToInt(curSelPiecePos);
                        if (x == ar[0] && y == ar[1]){
                            currentSelectedPiece = null;
                            curSelPiecePos = "";
                            gamePanel.repaint();
                        }
                        else if (array[x][y] != null){
                            currentSelectedPiece = array[x][y];
                            curSelPiecePos = position.intToString(x, y);
                            gamePanel.repaint();
                        }
                        else{
                            currentSelectedPiece = null;
                            curSelPiecePos = "";
                            gamePanel.repaint();
                        }
                        return;
                    }
                    moves = position.getAllPlayableMoves();
                    //System.out.println(moves);
                    currentSelectedPiece = null;
                    curSelPiecePos = "";
                    gamePanel.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseClicked(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    public static void main(String[] args) {
        new Main();
    }



    public JFrame getGameFrame() {
        return gameFrame;
    }

    public JPanel getGamePanel(){
        return gamePanel;
    }

    public JPanel getPanel() {
        return gamePanel;
    }

    public Position getPosition(){ return position; }

    public Piece[][] getArray(){ return array; }
}

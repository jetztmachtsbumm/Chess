package de.amg.chess.main;

import de.amg.chess.model.Move;
import de.amg.chess.model.Piece;
import de.amg.chess.model.Position;
import de.amg.chess.model.Pieces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
                int fieldSize = 90;
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
                int y = e.getX() / 90; // this is intentional
                int x = e.getY() / 90;
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

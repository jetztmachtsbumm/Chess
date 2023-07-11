package de.amg.chess.main;

import de.amg.chess.pieces.Piece;
import de.amg.chess.util.FieldDefaultPieces;
import jdk.jshell.spi.ExecutionControl;

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
    private Field[][] fields;

    private Piece currentSelectedPiece;
    private boolean mirrored;

    private Main() {
        instance = this;

        menuFrame = new JFrame();
        menuFrame.setBounds(1920 / 2, 1080 / 2, 420, 420);
        menuFrame.setLayout(null);
        menuFrame.setResizable(false);
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

        menuFrame.add(singleplayerButton);
        menuFrame.add(multiplayerButton);
        menuFrame.add(quitButton);

        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }

    private void loadGameWindow(){
        menuFrame.dispose();

        gameFrame = new JFrame();
        gameFrame.setBounds(0, 0, 1920, 1080);
        gameFrame.setUndecorated(true);

        fields = new Field[8][8];
        createFields();

        gamePanel = new JPanel(){
            @Override
            public void paint(Graphics g) {
                int fieldSize = 90;
                for(int x = 0; x < 8; x++){
                    for(int y = 0; y < 8; y++){
                        Field field = null;
                        if(mirrored){
                            field = fields[7 - x][7 - y];
                        }else{
                            field = fields[x][y];
                        }

                        if(field.isWhite()){
                            g.setColor(Color.LIGHT_GRAY);
                        }else{
                            g.setColor(Color.DARK_GRAY);
                        }

                        g.fillRect(fieldSize * x, fieldSize * y, fieldSize, fieldSize);

                        if(field.getPiece() != null){
                            g.drawImage(field.getPiece().getImage(), x * fieldSize, y * fieldSize, this);
                        }
                    }
                }
            }
        };

        gameFrame.add(gamePanel);
        gameFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        gameFrame.setLocationRelativeTo(null);
        gameFrame.setVisible(true);

        gameFrame.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = e.getX() / 90;
                int y = e.getY() / 90;

                if(mirrored){
                    x = 7 - x;
                    y = 7 - y;
                }

                if(currentSelectedPiece == null){
                    currentSelectedPiece = fields[x][y].getPiece();
                    fields[x][y].setPiece(null);
                }else{
                    fields[x][y].setPiece(currentSelectedPiece);
                    currentSelectedPiece = null;
                    gamePanel.repaint();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });

        gameFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == 'r'){
                    mirrored = !mirrored;
                    gamePanel.repaint();
                }
            }
        });
    }

    private void createFields(){
        boolean white = true;
        for(int x = 0; x < 8; x++){
            for(int y = 0; y < 8; y++){
                fields[x][y] = new Field(white, FieldDefaultPieces.FIELD_DEFAULT_PIECES[x][y]);
                white = !white;
            }
            white = !white;
        }
    }

    public JFrame getGameFrame() {
        return gameFrame;
    }

    public JPanel getGamePanel() {
        return gamePanel;
    }

    public Field[][] getFields() {
        return fields;
    }
}

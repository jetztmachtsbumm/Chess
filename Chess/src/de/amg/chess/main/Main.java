package de.amg.chess.main;

import de.amg.chess.pieces.Piece;
import de.amg.chess.util.FieldDefaultPieces;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Main {

    public static Main instance;

    private final JFrame frame;
    private final JPanel panel;
    private final Field[][] fields;

    private Piece currentSelectedPiece;
    private boolean mirrored;

    private Main() {
        instance = this;

        frame = new JFrame();
        frame.setBounds(0, 0, 1920, 1080);
        frame.setUndecorated(true);

        fields = new Field[8][8];
        createFields();

        panel = new JPanel(){
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

        frame.add(panel);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        frame.addMouseListener(new MouseListener() {
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
                    panel.repaint();
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

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyChar() == 'r'){
                    mirrored = !mirrored;
                    panel.repaint();
                }
            }
        });

    }

    public static void main(String[] args) {
        new Main();
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

    public JFrame getFrame() {
        return frame;
    }

    public JPanel getPanel() {
        return panel;
    }

    public Field[][] getFields() {
        return fields;
    }
}

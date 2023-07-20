package de.amg.chess.main;

import de.amg.chess.model.Move;
import de.amg.chess.model.Piece;
import de.amg.chess.model.Position;
import de.amg.chess.model.Pieces;
import de.amg.chess.clock.Clock;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;

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

    private final Font font1;

    private JTextArea movesArea;
    private JLabel label_clock_white;
    private JLabel label_clock_black;

    private JCheckBox checkBox;

    private Clock whiteClock;
    private Clock blackClock;

    private de.amg.chess.model.Color resigned = null;

    private Main() {
        instance = this;

        loadMainMenuWindow();

        position = Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
        array = position.getArray();
        moves = position.getAllPlayableMoves();
        font1 = new Font("Arial", Font.PLAIN, 20);
    }

    public int[] invertLocation(int[] v){
        v[0] = 7 - v[0];
        v[1] = 7 - v[1];
        return v;
    }

    private void loadMainMenuWindow(){
        menuFrame = new JFrame();
        menuFrame.setBounds(1920 / 2, 1080 / 2, 420, 420);
        menuFrame.setLayout(null);
        menuFrame.setResizable(false);
        menuFrame.setTitle("Chess");
        menuFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel versionLabel = new JLabel("Version from 20/07/23");
        JButton multiplayerButton = new JButton("Multiplayer");
        JButton quitButton = new JButton("Quit");

        versionLabel.setBounds(140, 50, 150, 50);
        multiplayerButton.setBounds(130, 150, 150, 50);
        quitButton.setBounds(130, 250, 150, 50);

        multiplayerButton.addActionListener(e -> loadGameWindow());
        quitButton.addActionListener(e -> menuFrame.dispose());

        multiplayerButton.setFocusPainted(false);
        quitButton.setFocusPainted(false);

        menuFrame.add(versionLabel);
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

        JButton mainMenuButton = new JButton("Main Menu");
        JButton mirrorButton = new JButton("Mirror Position");

        mainMenuButton.setBounds(900, 285, 150, 50);
        mirrorButton.setBounds(1100, 285, 150, 50);

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

        label_clock_white = new JLabel("1:30:00");
        label_clock_white.setFont(font1);
        label_clock_black = new JLabel("1:30:00");
        label_clock_black.setFont(font1);
        whiteClock = new Clock(0, 30, 1, label_clock_white, gamePanel);
        blackClock = new Clock(0, 30, 1, label_clock_black, gamePanel);
        JTextField field_clock_white = new JTextField();
        field_clock_white.setFont(font1);
        field_clock_white.addActionListener(e -> {try{
            whiteClock.stop();
            blackClock.stop();
            String[] parts = field_clock_white.getText().split(":");
            whiteClock = new Clock(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), label_clock_white, gamePanel);
            label_clock_white.setText(field_clock_white.getText());
            gamePanel.repaint();
        } catch (Exception exception){}});
        JTextField field_clock_black = new JTextField();
        field_clock_black.setFont(font1);
        field_clock_black.addActionListener(e -> {try{
            blackClock.stop();
            whiteClock.stop();
            String[] parts = field_clock_black.getText().split(":");
            blackClock = new Clock(Integer.parseInt(parts[2]), Integer.parseInt(parts[1]), Integer.parseInt(parts[0]), label_clock_black, gamePanel);
            label_clock_black.setText(field_clock_black.getText());
            gamePanel.repaint();
        } catch (Exception exception){}});

        JButton positionResetButton = new JButton("Reset Position");
        positionResetButton.setBounds(1300, 285, 150, 50);
        positionResetButton.addActionListener(e -> {
            position = Position.fromFEN("rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR w KQkq - 0 1");
            array = position.getArray();
            moves = position.getAllPlayableMoves();
            currentSelectedPiece = null;
            promote = false;
            resigned = null;
            movesArea.setText("");
            whiteClock.stop();
            blackClock.stop();
            gamePanel.repaint();
        });
        positionResetButton.setFocusPainted(false);

        FileDialog fdl = new FileDialog(gameFrame, "Load a FEN file", FileDialog.LOAD);
        fdl.setFile("*.fen");

        JButton loadPositionButton = new JButton("Load Position");
        loadPositionButton.setBounds(900, 385, 150, 50);
        loadPositionButton.addActionListener(e -> {
            fdl.setVisible(true);
            String filename = fdl.getFile();
            if (filename == null){
                return;
            }
            try {
                BufferedReader reader = new BufferedReader(new FileReader(fdl.getDirectory() + "/" + filename));
                position = Position.fromFEN(reader.readLine());
                reader.close();
                array = position.getArray();
                moves = position.getAllPlayableMoves();
                currentSelectedPiece = null;
                promote = false;
                resigned = null;
                movesArea.setText("");
                whiteClock.stop();
                blackClock.stop();
                gamePanel.repaint();
            }
            catch (Exception exception){
                fdl.setVisible(true);
            }
        });
        loadPositionButton.setFocusPainted(false);

        FileDialog fds = new FileDialog(gameFrame, "Save to FEN file", FileDialog.SAVE);
        fds.setFile("*.fen");

        JButton savePositionButton = new JButton("Save Position");
        savePositionButton.setBounds(1100, 385, 150, 50);
        savePositionButton.addActionListener(e -> {
            fds.setVisible(true);
            String filename = fds.getFile();
            if (filename == null){
                return;
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(fds.getDirectory() + "/" + filename));
                writer.write(position.toFEN());
                writer.close();
            }
            catch (Exception exception){
                fds.setVisible(true);
            }
        });
        savePositionButton.setFocusPainted(false);

        JButton stopClocksButton = new JButton("Stop Clocks");
        stopClocksButton.setBounds(1300, 385, 150, 50);
        stopClocksButton.addActionListener(e -> {
            whiteClock.stop();
            blackClock.stop();
        });
        stopClocksButton.setFocusPainted(false);

        checkBox = new JCheckBox("Auto-Mirror");
        checkBox.setBounds(1500, 297, 100, 25);
        checkBox.setSelected(true);
        checkBox.setFocusPainted(false);

        JButton resignButtonWhite = new JButton("Resign");
        resignButtonWhite.addActionListener(e -> {
            if (resigned == null) {
                resigned = de.amg.chess.model.Color.WHITE;
                gamePanel.repaint();
            }});
        resignButtonWhite.setFocusPainted(false);

        JButton resignButtonBlack = new JButton("Resign");
        resignButtonBlack.addActionListener(e -> {
            if (resigned == null) {
                resigned = de.amg.chess.model.Color.BLACK;
                gamePanel.repaint();
            }});
        resignButtonBlack.setFocusPainted(false);

        movesArea = new JTextArea(){
            @Override
            public void append(String str){
                super.append(str);
                setCaretPosition(getDocument().getLength());
            }
        };
        movesArea.setEditable(false);
        movesArea.setFont(font1);
        movesArea.setLineWrap(true);
        movesArea.setWrapStyleWord(true);

        JScrollPane scrollPane = new JScrollPane(movesArea);
        scrollPane.setBounds(0, 720, 1920, 360);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        gamePanel = new JPanel(){
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (mirrored){
                    label_clock_white.setBounds(750, 110, 200, 50);
                    label_clock_black.setBounds(750, 560, 200, 50);
                    field_clock_white.setBounds(950, 110, 200, 50);
                    field_clock_black.setBounds(950, 560, 200, 50);
                    resignButtonWhite.setBounds(750, 20, 100, 50);
                    resignButtonBlack.setBounds(750, 650, 100, 50);
                }
                else{
                    label_clock_black.setBounds(750, 110, 200, 50);
                    label_clock_white.setBounds(750, 560, 200, 50);
                    field_clock_black.setBounds(950, 110, 200, 50);
                    field_clock_white.setBounds(950, 560, 200, 50);
                    resignButtonBlack.setBounds(750, 20, 100, 50);
                    resignButtonWhite.setBounds(750, 650, 100, 50);
                }
                int[] wk = null;
                int[] bk = null;
                int[] kloc = null;
                int[] oloc = null;
                boolean flagged = ((position.getToPlay() == de.amg.chess.model.Color.WHITE && whiteClock.hasRunOut()) || (position.getToPlay() == de.amg.chess.model.Color.BLACK && blackClock.hasRunOut()));
                if (position.isInRemis()){
                    wk = position.stringToInt(position.getKingLocation(de.amg.chess.model.Color.WHITE));
                    bk = position.stringToInt(position.getKingLocation(de.amg.chess.model.Color.BLACK));
                    if (mirrored){
                        wk = invertLocation(wk);
                        bk = invertLocation(bk);
                    }
                    whiteClock.stop();
                    blackClock.stop();
                    movesArea.append("½ - ½");
                }
                else if (position.isInCheck(position.getToPlay()) || flagged || resigned != null){
                    kloc = position.stringToInt(position.getKingLocation(resigned == null ? position.getToPlay() : resigned));
                    if (mirrored){
                        kloc = invertLocation(kloc);
                    }
                    if (position.isInMate(position.getToPlay()) || flagged || resigned != null){
                        oloc = position.stringToInt(position.getKingLocation(position.getOpponent(resigned == null ? position.getToPlay() : resigned)));
                        if (mirrored){
                            oloc = invertLocation(oloc);
                        }
                        whiteClock.stop();
                        blackClock.stop();
                        if (position.isInMate(de.amg.chess.model.Color.WHITE) || whiteClock.hasRunOut() || resigned == de.amg.chess.model.Color.WHITE){
                            movesArea.append("0 - 1");
                        }
                        else if (position.isInMate(de.amg.chess.model.Color.BLACK) || blackClock.hasRunOut() || resigned == de.amg.chess.model.Color.BLACK){
                            movesArea.append("1 - 0");
                        }
                    }
                }
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

                        if (wk != null && wk[0] == x && wk[1] == y){
                            g.setColor(Color.CYAN);
                            g.fillOval(wk[1]*fieldSize, wk[0]*fieldSize, fieldSize, fieldSize);
                        }

                        if (bk != null && bk[0] == x && bk[1] == y){
                            g.setColor(Color.CYAN);
                            g.fillOval(bk[1]*fieldSize, bk[0]*fieldSize, fieldSize, fieldSize);
                        }

                        if (kloc != null && kloc[0] == x && kloc[1] == y){
                            g.setColor(Color.RED);
                            g.fillOval(kloc[1]*fieldSize, kloc[0]*fieldSize, fieldSize, fieldSize);
                        }

                        if (oloc != null && oloc[0] == x && oloc[1] == y){
                            g.setColor(Color.GREEN);
                            g.fillOval(oloc[1]*fieldSize, oloc[0]*fieldSize, fieldSize, fieldSize);
                        }

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
                    if ((px == 0 && !mirrored) || (px == 7 && mirrored)){
                        g.drawImage(Pieces.white_images.get(Pieces.QUEEN), xval, yval, this);
                        g.drawImage(Pieces.white_images.get(Pieces.KNIGHT), xval, yval+factor*fieldSize, this);
                        g.drawImage(Pieces.white_images.get(Pieces.ROOK), xval, yval+factor*fieldSize*2, this);
                        g.drawImage(Pieces.white_images.get(Pieces.BISHOP), xval, yval+factor*fieldSize*3, this);
                    }
                    else if ((px == 7 && !mirrored) || (px == 0 && mirrored)){
                        g.drawImage(Pieces.black_images.get(Pieces.QUEEN), xval, yval, this);
                        g.drawImage(Pieces.black_images.get(Pieces.KNIGHT), xval, yval+factor*fieldSize, this);
                        g.drawImage(Pieces.black_images.get(Pieces.ROOK), xval, yval+factor*fieldSize*2, this);
                        g.drawImage(Pieces.black_images.get(Pieces.BISHOP), xval, yval+factor*fieldSize*3, this);
                    }
                }
                add(mirrorButton);
                add(mainMenuButton);
                add(label_clock_white);
                add(label_clock_black);
                add(field_clock_white);
                add(field_clock_black);
                add(positionResetButton);
                add(loadPositionButton);
                add(savePositionButton);
                add(stopClocksButton);
                add(checkBox);
                add(resignButtonWhite);
                add(resignButtonBlack);
                add(scrollPane);
                revalidate();
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
                if (position.isInMate(position.getToPlay()) || position.isInRemis() || whiteClock.hasRunOut() || blackClock.hasRunOut() || resigned != null){
                    return;
                }
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
                        if (mirrored){
                            px = 7 - px;
                            py = 7 - py;
                        }
                        for (Move move:moves){
                            if (move.getDest().equals(position.intToString(px, py)) && move.getPromote() == type){
                                move.apply();
                                movesArea.append(move.toString() + " ");
                                if (position.getToPlay() == de.amg.chess.model.Color.WHITE){
                                    blackClock.stop();
                                    whiteClock.start();
                                }
                                else{
                                    whiteClock.stop();
                                    blackClock.start();
                                }
                                if (checkBox.isSelected()){
                                    mirrored = !mirrored;
                                }
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
                                if (mirrored){
                                    px = 7 - px;
                                    py = 7 - py;
                                }
                                break;
                            }
                            legal = true;
                            move.apply();
                            movesArea.append(move.toString() + " ");
                            if (position.getToPlay() == de.amg.chess.model.Color.WHITE){
                                blackClock.stop();
                                whiteClock.start();
                            }
                            else{
                                whiteClock.stop();
                                blackClock.start();
                            }
                            if (checkBox.isSelected()){
                                mirrored = !mirrored;
                            }
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

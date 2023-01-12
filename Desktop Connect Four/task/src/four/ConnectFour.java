package four;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ConnectFour extends JFrame {
    private static final int ROWS = 6;
    private static final int COLUMNS = 7;
    private final JButton[][] board = new JButton[6][7];
    private boolean turnX = true;
    private boolean turnO = false;
    JButton resetButton = new JButton("Reset");
    private final JPanel gameBoard = new JPanel(new GridLayout(6,7,0,0));
    JPanel bottomPanel = new JPanel(new BorderLayout());

    public ConnectFour() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 500);
        setLocationRelativeTo(null);
        setTitle("Connect Four");
        setLayout(new BorderLayout());
        initializeGameBoard();
        initializeUtils();
    }

    private void initializeGameBoard() {
        add(gameBoard, BorderLayout.CENTER);
        initializeCells(gameBoard);
    }

    public void play() {
        setVisible(true);
//        JOptionPane.showMessageDialog(null, "WIN!!!");
    }

    private void initializeCells(JPanel gameBoard) {
        char letter='A';
        for (int i=0, k=ROWS; i<ROWS; i++, k--) {
            for (int j=0; j<COLUMNS; j++) {
                String cellName = letter+String.valueOf(k);
                board[i][j] = new JButton(" ");
                gameBoard.add(board[i][j]);
                board[i][j].setName("Button"+cellName);
                board[i][j].setFocusPainted(false);
                int finalI = i;
                int finalJ = j;
                board[i][j].addActionListener(e -> {
                    buttonClick(finalI, finalJ);
                    });
                letter += 1;
            }
            letter = 'A';
        }
    }

    private void buttonClick(int finalI, int finalJ) {
        int I = reachLowestEmptyCell(finalI, finalJ);
        if (I >= 0 && I <= 5 && finalJ >=0 && finalJ <= 6) {
            board[I][finalJ].setText(turnX ? "X" : "O");
            board[I][finalJ].setFont(new Font("Arial", Font.BOLD, 24));
            turnX = turnO;
            turnO = !turnX;
            detectWinner(I, finalJ);
        }
    }

    private void detectWinner(int i, int j) {
        boolean winner = false;
        winner = detectLeftRight(i,j) || detectUpDown(i,j) || detectDiagonal(i, j);
//        winner = detectLeftRight(i,j) || detectUpDown(i,j);
        if (winner) {
            resetButton.setEnabled(true);
//            JOptionPane.showMessageDialog(null, "WIN!!!");
            disableAllActionListeners();
        }
    }

    private void disableAllActionListeners() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                for (ActionListener al: board[i][j].getActionListeners()) {
                    board[i][j].removeActionListener(al);
                }
            }
        }
    }


    private boolean detectLeftRight(int i, int j) {
        int left = j;
        int right = j;
        while (left > 0 && board[i][j].getText().equals(board[i][left - 1].getText()))
            left -= 1;
        while (right < 6 && board[i][j].getText().equals(board[i][right + 1].getText()))
            right += 1;

        if (right-left+1 == 4) {
            for (int k = left; k <= right; k++)
                board[i][k].setBackground(Color.cyan);
            return true;
        }
        return false;

    }
    private boolean detectUpDown(int i, int j) {
        int up = i;
        int down = i;
        while (up > 0 && board[i][j].getText().equals(board[up - 1][j].getText()))
            up -= 1;
        while (down < 5&& board[i][j].getText().equals(board[down + 1][j].getText()))
            down += 1;

        if (down-up+1 == 4) {
            for (int k = down; k >= up; k--) {
                board[k][j].setBackground(Color.CYAN);
            }
            return true;
        }
        return false;
    }

    private boolean detectDiagonal (int i, int j) {
        return detectTopRightBottomLeft(i, j) || detectTopLeftBottomRight(i, j);
    }

    private boolean detectTopLeftBottomRight(int i, int j) {
        // checking from top left to bottom right
        int top = i;
        int left = j;
        int bottom = i;
        int right = j;
        while (top > 0 && left > 0 && board[i][j].getText().equals(board[top-1][left-1].getText())) {
            top -= 1;
            left -=1;
        }
        while (bottom < 5 && right < 6 && board[i][j].getText().equals(board[bottom+1][right+1].getText())) {
            bottom += 1;
            right += 1;
        }

        if(bottom-top+1 == 4 && right-left+1 == 4) {
            for (int k = 0; k < 4; k++) {
                board[bottom][right].setBackground(Color.CYAN);
                bottom -= 1;
                right -=1;
            }
            return true;
        }
        return false;
    }

    private boolean detectTopRightBottomLeft(int i, int j) {
        // checking from top right to bottom left
        int top = i;
        int right = j;
        int bottom = i;
        int left = j;
        while (top > 0 && right < 5 && board[i][j].getText().equals(board[top - 1][right + 1].getText())) {
            top -= 1;
            right +=1;
        }
        while (bottom < 5 && left > 0 && board[i][j].getText().equals(board[bottom+1][left-1].getText())) {
            bottom += 1;
            left -= 1;
        }

        if(bottom-top+1 == 4 && right-left+1 == 4) {
            for (int k = 0; k < 4; k++) {
                board[bottom][left].setBackground(Color.CYAN);
                bottom -= 1;
                left +=1;
            }
            return true;
        }
        return false;
    }


    private void initializeUtils() {
        resetButton.setName("ButtonReset");
        resetButton.addActionListener(e -> {
            resetCells();
            turnX = true;
            turnO = false;
        });
        resetButton.setEnabled(true);
        bottomPanel.add(resetButton, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private void resetCells() {
        disableAllActionListeners();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                board[i][j].setText(" ");
                board[i][j].setBackground(null);
                int finalI = i;
                int finalJ = j;
                board[i][j].addActionListener(e -> {
                    buttonClick(finalI, finalJ);
                });
            }
        }
    }


    private int reachLowestEmptyCell(int i, int j) {
        String text = board[i][j].getText();
        if (text.equals(" ")) {
            while (i < 5 && board[i + 1][j].getText().equals(" "))
                i = i + 1;
            return i;
        }
        else {
            while (i > 0 && !board[i-1][j].getText().equals(" "))
                i = i-1;
            return i-1;
        }
    }
}
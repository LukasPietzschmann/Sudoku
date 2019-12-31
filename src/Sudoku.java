import java.util.Random;

public class Sudoku {
  private static final int GEN_MODE = 1;
  private static final int SOLVE_MODE = 2;
  
  public static final int BOARD_SIZE = 9;
  public static final int EMPTY_CELL = -1;
  
  public static final int EASY = 1;
  public static final int MEDIUM = 2;
  public static final int HARD = 3;
  public static final int TROLL = 5;
  
  private Random rnd;
  
  private int[][] board;
  
  public Sudoku(int[][] board) throws IllegalArgumentException {
    //if (board.length != BOARD_SIZE) throw new IllegalArgumentException();
    for (int i = 0; i < BOARD_SIZE; i++) {
      //if (board[i].length != BOARD_SIZE) throw new IllegalArgumentException();
      for (int j = 0; j < BOARD_SIZE; j++) {
        if ((board[i][j] > 9 || board[i][j] < 1) && board[i][j] != EMPTY_CELL) throw new IllegalArgumentException();
      }
    }
    
    this.board = board;
  }
  
  public Sudoku(int difficulty) throws IllegalArgumentException {
    if (!(difficulty == EASY || difficulty == MEDIUM || difficulty == HARD || difficulty == TROLL))
      throw new IllegalArgumentException(String.format("Difficulty %d is not permitted. Difficulty has to be either EASY (%d), MEDIUM (%d), HARD (%d), or TROLL (%d)", difficulty, EASY, MEDIUM, HARD, TROLL));
    rnd = new Random();
    genSudoku(difficulty);
  }
  
  private void genSudoku(int difficulty) {
    clearBoard();
    canSolveSudokuFromCell(0, 0, GEN_MODE);
  
  
    int perc = -1;
    switch (difficulty) {
      case EASY:
        perc = 20;
        break;
      case MEDIUM:
        perc = 45;
        break;
      case HARD:
        perc = 60;
        break;
      case TROLL:
        perc = 100;
        break;
    }
  
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) if (rnd.nextInt(100) <= perc) board[j][i] = EMPTY_CELL;
    }
  }
  
  public boolean solve() {
    return canSolveSudokuFromCell(0, 0, SOLVE_MODE);
  }
  
  private boolean canSolveSudokuFromCell(int row, int col, int mode) {
    if (col == BOARD_SIZE) {
      col = 0;
      row++;
      
      if (row == BOARD_SIZE) return true;
    }
    
    if (board[row][col] != EMPTY_CELL) return canSolveSudokuFromCell(row, col + 1, mode);
    
    if (mode == SOLVE_MODE) {
      for (int i = 1; i <= BOARD_SIZE; i++) {
        if (isSafeToPlace(row, col, i)) {
          board[row][col] = i;
          if (canSolveSudokuFromCell(row, col + 1, mode)) return true;
          board[row][col] = EMPTY_CELL;
        }
      }
    }else if (mode == GEN_MODE) {
      for (int i = 1; i <= 18; i++) {
        int r = rnd.nextInt(9) + 1;
        if (isSafeToPlace(row, col, r)) {
          board[row][col] = r;
          if (canSolveSudokuFromCell(row, col + 1, mode)) return true;
          board[row][col] = EMPTY_CELL;
        }
      }
    }
    
    return false;
  }
  
  private boolean isSafeToPlace(int row, int col, int val) {
    for (int i = 0; i < 9; i++) {
      if (board[row][i] == val && i != col) return false;
      if (board[i][col] == val && i != row) return false;
    }
    
    int subGridX = col / 3;
    int subGridY = row / 3;
    
    for (int i = 0; i < 3; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[subGridY * 3 + i][subGridX * 3 + j] == val && subGridY * 3 + i != row && subGridX * 3 + j != col) return false;
      }
    }
    
    return true;
  }
  
  private void clearBoard() {
    board = new int[BOARD_SIZE][BOARD_SIZE];
    for (int i = 0; i < BOARD_SIZE; i++) {
      for (int j = 0; j < BOARD_SIZE; j++) board[j][i] = EMPTY_CELL;
    }
  }
  
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[i].length; j++) {
        char c = board[i][j] == EMPTY_CELL ? 'x' : (char) (board[i][j] + '0');
        if ((j + 1) % 3 == 0 && j != 8) builder.append(String.format("%c | ", c));
        else builder.append(String.format("%c ", c));
      }
      if ((i + 1) % 3 == 0 && i != 8) builder.append("\n------ ------- ------\n");
      else builder.append("\n");
    }
    
    return builder.toString();
  }
}

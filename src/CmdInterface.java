import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CmdInterface {
  public static void main(String[] args) throws IOException {
    Sudoku sudoku = null;
    
    BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
    String line;
    
    System.out.print(">>> ");
    loop:
    while ((line = bf.readLine()) != null) {
      if (line.startsWith("gen")) {
        try {
          int difficulty = Integer.parseInt(line.split(" ")[1]);
          sudoku = new Sudoku(difficulty);
        }catch (IllegalArgumentException e) {
          System.out.println(e.getMessage());
        }catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("'gen' needs an Argument representing the Difficulty");
        }
      }else if (line.equals("solve")) {
        if (sudoku != null) {
          if (!sudoku.solve()) System.out.println("Sudoku could not be solved");
        }else System.out.println("There's no Sudoku to solve. Try 'gen' or 'set'");
      }else if (line.equals("dump")) {
        if (sudoku != null) System.out.println(sudoku);
        else System.out.println("There's no Sudoku. Try 'gen' or 'set'");
      }else if (line.startsWith("set")) {
        String[] arr = new String[0];
        try {
          arr = line.split(" ")[1].split(",");
        }catch (ArrayIndexOutOfBoundsException e) {
          System.out.println("'set' is followed by all Cells of the Sudoku separated by a Comma.");
          System.out.print(">>> ");
          continue loop;
        }
        int[][] board = new int[Sudoku.BOARD_SIZE][Sudoku.BOARD_SIZE];
        try {
          for (int i = 0; i < Sudoku.BOARD_SIZE; i++) {
            for (int j = 0; j < Sudoku.BOARD_SIZE; j++) {
              board[j][i] = Integer.parseInt(arr[j * Sudoku.BOARD_SIZE + i]);
            }
          }
          
          sudoku = new Sudoku(board);
        }catch (Exception e) {
          String s = line.split(" ")[1];
          if (s.length() > 20) s = s.substring(0, 19).concat("...");
          
          System.out.println(String.format("%s is not a valid input. (Use %d for empty Cells)", s, Sudoku.EMPTY_CELL));
        }
      }else if (line.equals("exit")) System.exit(1);
      else if (line.equals("help")) System.out.println("Use either 'gen', 'set', 'solve', 'dump', or 'exit'.");
      else System.out.println(String.format("%s is not a valid Command. Use either 'gen', 'set', 'solve', 'dump', or 'exit'.", line));
      System.out.print(">>> ");
    }
  }
}

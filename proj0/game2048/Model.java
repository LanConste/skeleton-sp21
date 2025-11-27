package game2048;

import java.util.Formatter;
import java.util.Observable;


/** The state of a game of 2048.
 *  @author Lan
 */
public class Model extends Observable {
    /** Current contents of the board. */
    private Board board;
    /** Current score. */
    private int score;
    /** Maximum score so far.  Updated when game ends. */
    private int maxScore;
    /** True iff game is ended. */
    private boolean gameOver;

    /* Coordinate System: column C, row R of the board (where row 0,
     * column 0 is the lower-left corner of the board) will correspond
     * to board.tile(c, r).  Be careful! It works like (x, y) coordinates.
     */

    /** Largest piece value. */
    public static final int MAX_PIECE = 2048;

    /** A new 2048 game on a board of size SIZE with no pieces
     *  and score 0. */
    public Model(int size) {
        board = new Board(size);
        score = maxScore = 0;
        gameOver = false;
    }

    /** A new 2048 game where RAWVALUES contain the values of the tiles
     * (0 if null). VALUES is indexed by (row, col) with (0, 0) corresponding
     * to the bottom-left corner. Used for testing purposes. */
    public Model(int[][] rawValues, int score, int maxScore, boolean gameOver) {
        int size = rawValues.length;
        board = new Board(rawValues, score);
        this.score = score;
        this.maxScore = maxScore;
        this.gameOver = gameOver;
    }

    /** Return the current Tile at (COL, ROW), where 0 <= ROW < size(),
     *  0 <= COL < size(). Returns null if there is no tile there.
     *  Used for testing. Should be deprecated and removed.
     *  */
    public Tile tile(int col, int row) {
        return board.tile(col, row);
    }

    /** Return the number of squares on one side of the board.
     *  Used for testing. Should be deprecated and removed. */
    public int size() {
        return board.size();
    }

    /** Return true iff the game is over (there are no moves, or
     *  there is a tile with value 2048 on the board). */
    public boolean gameOver() {
        checkGameOver();
        if (gameOver) {
            maxScore = Math.max(score, maxScore);
        }
        return gameOver;
    }

    /** Return the current score. */
    public int score() {
        return score;
    }

    /** Return the current maximum game score (updated at end of game). */
    public int maxScore() {
        return maxScore;
    }

    /** Clear the board to empty and reset the score. */
    public void clear() {
        score = 0;
        gameOver = false;
        board.clear();
        setChanged();
    }

    /** Add TILE to the board. There must be no Tile currently at the
     *  same position. */
    public void addTile(Tile tile) {
        board.addTile(tile);
        checkGameOver();
        setChanged();
    }

    /** Tilt the board toward SIDE. Return true iff this changes the board.
     *
     * 1. If two Tile objects are adjacent in the direction of motion and have
     *    the same value, they are merged into one Tile of twice the original
     *    value and that new value is added to the score instance variable
     * 2. A tile that is the result of a merge will not merge again on that
     *    tilt. So each move, every tile will only ever be part of at most one
     *    merge (perhaps zero).
     * 3. When three adjacent tiles in the direction of motion have the same
     *    value, then the leading two tiles in the direction of motion merge,
     *    and the trailing tile does not.
     * */
    public boolean tilt(Side side) {
        boolean changed;
        changed = false;

        // TODO: Modify this.board (and perhaps this.score) to account
        // for the tilt to the Side SIDE. If the board changed, set the
        // changed local variable to true.

        boolean isChanged = false; 

        board.setViewingPerspective(side); 

        for (int col = 0; col < board.size(); col++) {
            isChanged = isChanged | processSingleCol(col);
        }

        if (isChanged) {
            changed = isChanged;
        }

        board.setViewingPerspective(Side.NORTH);

        checkGameOver();
        if (changed) {
            setChanged();
        }
        return changed;
    }

    /** Consider the single column col to move the tiles
     *  and return if current column is changed.
    */
    private boolean processSingleCol(int col) {
        // Record the status if current row is merged.
        boolean isChanged = false; 
        boolean[] isMerged = new boolean[board.size()]; 

        // Check each tile at position (col, row). 
        for (int row = board.size() - 1; row >= 0; row--) { // 3 -> 2 -> 1 -> 0 until reach the bound.
            if (board.tile(col, row) != null) { // Check if the tile exists.
                Tile t = board.tile(col, row);  // Lock the target tile.
                // Check if the parameter row is not spilled the boundary. 
                ////if (row < board.size()) {
                // Move to the destinationRow (if can), change the status array(if moved).
                // board.move(col, destinationRow(col, row, isMerged), t); 
                // Get the status.
                int desRow = destinationRow(col, row, isMerged); 
                boolean merged = board.move(col, desRow, t);
                // Update status 'isChanged'.
                isChanged = isChanged | (desRow != row);
                if (merged) {
                    // Add the score.
                    score += board.tile(col, desRow).value();
                } 
                //}
            }
        }
        return isChanged; 
    }

    /** Return the destination of the row of the CURRENT COL. */
    public int destinationRow(int col, int row, boolean[] isMerged) {
        int desRow = row; // temp flag.
        for (int targetRow = row + 1; targetRow < board.size(); targetRow++) { // Find
            // If is empty or can be merged.
            if (canMoveTo(col, row, targetRow)) {
                desRow = targetRow;
                continue;  // Continue to find deeper...
            } else if (canMergeTo(col, row, targetRow, isMerged)) {
                desRow = targetRow;
                isMerged[desRow] = true; 
                break;  // Get the final detinaiton.
            } else {
                break;
            }
        }
        return desRow; 
    }

    /** If tile at (col, row) can move to (col, targetRow) */ 
    public boolean canMoveTo(int col, int row, int targetRow) {
        return board.tile(col, targetRow) == null;
    }

    /** If a tile havenot been merged and their values are equal, 
     *  tile at (col, row) with (col, targetRow) can merge together at (col, targetRow).
     */
    public boolean canMergeTo(int col, int row, int targetRow, boolean[] isMerged){
        return (!isMerged[targetRow] && board.tile(col, row).value() == board.tile(col, targetRow).value()); 
    }

    /** Checks if the game is over and sets the gameOver variable
     *  appropriately.
     */
    private void checkGameOver() {
        gameOver = checkGameOver(board);
    }

    /** Determine whether game is over. */
    private static boolean checkGameOver(Board b) {
        return maxTileExists(b) || !atLeastOneMoveExists(b);
    }

    /** Returns true if at least one space on the Board is empty.
     *  Empty spaces are stored as null.
     * */
    public static boolean emptySpaceExists(Board b) {
        // TODO: Fill in this function.
        //Search, loop if all t.value() != null 
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (b.tile(col, row) == null) {
                    return true; 
                }
            }
        }
        return false;
    }

    /**
     * Returns true if any tile is equal to the maximum valid value.
     * Maximum valid value is given by MAX_PIECE. Note that
     * given a Tile object t, we get its value with t.value().
     */
    public static boolean maxTileExists(Board b) {
        // TODO: Fill in this function.
        // Search if maxTile exists. For, if, .value = MAX_PIECE
        for (int col = 0; col < b.size(); col++) {
            for (int row = 0; row < b.size(); row++) {
                if (b.tile(col, row) != null) {
                    if (b.tile(col, row).value() == MAX_PIECE) {
                        return true; 
                    }
                }
            }
        }
        return false;
    }

    /**
     * Returns true if there are any valid moves on the board.
     * There are two ways that there can be valid moves:
     * 1. There is at least one empty space on the board.
     * 2. There are two adjacent tiles with the same value.
     */
    public static boolean atLeastOneMoveExists(Board b) {
        // TODO: Fill in this function.
        if (emptySpaceExists(b)) {
            return true; 
        } else {
            // Compare with ALL SOUTHERN tile if exist.
            for (int col = 0; col < b.size(); col++) {
                // Check each col that if the FIRST EXIST came across tile can be merged.
                for (int row = 0; row < b.size() - 1; row++) { // Just check first 3 tiles.
                    //Find the first EXIST tile.
                    for (int k = row + 1; k < b.size(); k++) {
                        if (b.tile(col, k) != null) {
                            // Found. Check whether the values equal.
                            if (b.tile(col, row).value() == b.tile(col, k).value()) {
                                return true; 
                            } else {
                                // Current tile cannot be merged anymore.
                                break; 
                            }
                        }
                    }
                } 
            }
            // Compare with ALL EASTERN tile if exist.
            for (int row = 0;row < b.size(); row++) {
                // Check each row that if the FIRST EXIST came across tile can be merged.
                for (int col = 0; col < b.size() - 1; col++) { // Just check first 3 tiles.
                    //Find the first EXIST tile.
                    for (int k = col + 1; k < b.size(); k++) {
                        if (b.tile(k, row) != null) {
                            // Found. Check whether the values equal.
                            if (b.tile(col, row).value() == b.tile(k, row).value()) {
                                return true; 
                            } else {
                                // Current tile cannot be merged anymore.
                                break; 
                            }
                        }
                    }
                } 
            }
        return false;
        }
    }


    @Override
     /** Returns the model as a string, used for debugging. */
    public String toString() {
        Formatter out = new Formatter();
        out.format("%n[%n");
        for (int row = size() - 1; row >= 0; row -= 1) {
            for (int col = 0; col < size(); col += 1) {
                if (tile(col, row) == null) {
                    out.format("|    ");
                } else {
                    out.format("|%4d", tile(col, row).value());
                }
            }
            out.format("|%n");
        }
        String over = gameOver() ? "over" : "not over";
        out.format("] %d (max: %d) (game is %s) %n", score(), maxScore(), over);
        return out.toString();
    }

    @Override
    /** Returns whether two models are equal. */
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        } else if (getClass() != o.getClass()) {
            return false;
        } else {
            return toString().equals(o.toString());
        }
    }

    @Override
    /** Returns hash code of Model’s string. */
    public int hashCode() {
        return toString().hashCode();
    }
}

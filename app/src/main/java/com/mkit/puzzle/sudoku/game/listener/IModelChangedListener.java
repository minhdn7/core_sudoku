package com.mkit.puzzle.sudoku.game.listener;

import com.mkit.puzzle.sudoku.game.GameCell;

/**
 * Created by Chris on 19.11.2015.
 */
public interface IModelChangedListener {
    public void onModelChange(GameCell c);
}

package com.mkit.puzzle.sudoku.game.listener;

import com.mkit.puzzle.sudoku.game.CellConflict;

import java.util.List;

/**
 * Created by Chris on 02.02.2016.
 */
public interface IGameErrorListener {
    public void onGameFilledWithErrors(List<CellConflict> errorList);
}

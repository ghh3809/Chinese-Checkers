package ai;

import entity.ChessState;

public interface Evaluator {
	public double getValue(ChessState cs, int playerOrder);
}

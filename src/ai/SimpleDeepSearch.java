package ai;

import java.util.Random;

import entity.*;

public class SimpleDeepSearch {
	
	static int[] playerTypes = null;
	static double finishBonus = 2;
	static Evaluator evaluator = null;
	
	public static Action searchMaxAction(ChessBoard cb, int depth, Evaluator givenEvaluator) {
		
		SimpleDeepSearch.playerTypes = cb.getplayerTypes();
		SimpleDeepSearch.evaluator = givenEvaluator;
		int playerOrder = cb.getPresentPlayerOrder();
		int playerNumber = cb.getPlayerNum();
		
		Action maxAction = null;
		double maxValue = Double.NEGATIVE_INFINITY;
		Random rand = new Random();
		int equalCount = 1;
		
		for (Position position : cb.getMovableChess()) {
			for (Action action : cb.getLegalActions(position)) {
				
				ChessState cs = new ChessState(cb.getChessState());
				cs.move(action);
				
				double evaluation = 0;
				if (depth <= playerNumber) { // 搜索层数不足玩家人数时，无需搜索到对应层数
					evaluation = evaluator.getValue(cs, playerOrder);
				} else { // 否则按正常顺序搜索
					evaluation = findMaxValue(cs, getNextPlayer(playerOrder), playerNumber, depth - 1, cb.getRuleType())[playerOrder];
				}
				
				if (Rule.isWin(playerOrder, cs)) evaluation += finishBonus;
				
				if (evaluation > maxValue){ // 寻找最大值
					maxValue = evaluation;
					maxAction = action;
					equalCount = 1;
				} else if ((evaluation == maxValue) && (rand.nextDouble() < 1.0/(++ equalCount))){ // 使各种可能均等出现
					maxAction = action;
				}
				
			}
		}
		return maxAction;
	}
	
	private static double[] findMaxValue(ChessState cs, int playerOrder, int playerNumber, int depth, int ruleType) {
		
		if (depth <= playerNumber) { // 尾递归
			double[] maxValue = finalEvaluation(cs, playerOrder, depth, ruleType);
			if (Rule.isWin(playerOrder, cs)) maxValue[playerOrder] += finishBonus;
			return maxValue;
		} else {
			if (Rule.isWin(playerOrder, cs)) { // 奖励情况
				double[] maxValue = findMaxValue(cs, getNextPlayer(playerOrder), playerNumber, depth - 1, ruleType);
				maxValue[playerOrder] += finishBonus;
				return maxValue;
			}
		}
		
		double[] maxValue = new double[7];
		maxValue[playerOrder] = Double.NEGATIVE_INFINITY;
		Random rand = new Random();
		int equalCount = 1;
		
		for (Position position : Rule.getMovableChess(playerOrder, cs)) {
			for (Action action : Rule.findReachableAction(playerOrder, position, cs, ruleType)) {
				
				ChessState newcs = new ChessState(cs);
				newcs.move(action);
				double[] value = findMaxValue(newcs, getNextPlayer(playerOrder), playerNumber, depth - 1, ruleType);
				
				if (Rule.isWin(playerOrder, cs)) value[playerOrder] += finishBonus;
				
				if (value[playerOrder] > maxValue[playerOrder]) {
					maxValue = value;
					equalCount = 1;
				} else if ((value[playerOrder] == maxValue[playerOrder]) && (rand.nextDouble() < 1.0/(++ equalCount))){ // 使各种可能均等出现
					maxValue = value;
				}
				
			}
		}
		return maxValue;
	}
	
	private static double[] finalEvaluation(ChessState cs, int playerOrder, int depth, int ruleType) {
		double[] evaluationResult = new double[7];
		while (depth-- > 0) {
			// 若该玩家已结束，则直接估值
			if (Rule.isWin(playerOrder, cs)) evaluationResult[playerOrder] = evaluator.getValue(cs, playerOrder);
			else { // 否则计算最大估值位置，并深入
				
				double maxValue = Double.NEGATIVE_INFINITY;
				Action maxAction = null;
				Random rand = new Random();
				int equalCount = 1;
				
				for (Position position : Rule.getMovableChess(playerOrder, cs)) {
					for (Action action : Rule.findReachableAction(playerOrder, position, cs, ruleType)) {
						
						ChessState newcs = new ChessState(cs);
						newcs.move(action);
						double evaluation = evaluator.getValue(newcs, playerOrder);
						
						if (evaluation > maxValue) {
							maxValue = evaluation;
							maxAction = action;
							equalCount = 1;
						} else if ((evaluation == maxValue) && (rand.nextDouble() < 1.0/(++ equalCount))){ // 使各种可能均等出现
							maxAction = action;
						}
						
					}
				}
				
				// 进行当前估值最大操作
				evaluationResult[playerOrder] = maxValue;
				ChessState newcs = new ChessState(cs);
				newcs.move(maxAction);
				cs = newcs;
			}
			playerOrder = getNextPlayer(playerOrder);
		}
		return evaluationResult;
	}
	
	// 获取下一位玩家
	private static int getNextPlayer(int playerOrder) {
		while (true) {
			playerOrder = (playerOrder % 6) + 1;
			if (playerTypes[playerOrder] >= 0) break;
		}
		return playerOrder;
	}
}

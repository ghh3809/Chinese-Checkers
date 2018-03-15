package entity;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 规则类，负责判断一方的可移动棋子、可移动位置及是否胜利
 * @author 豪豪
 *
 */
public class Rule {
	
	/**
	 * 获取玩家的可移动棋子
	 * @param playerOrder 需走棋的玩家编号
	 * @param chessState 当前棋子状态
	 * @return 所有可移动棋子位置的ArrayList
	 */
	public static ArrayList<Position> getMovableChess(int playerOrder, ChessState chessState) {
		ArrayList<Position> movableChess = new ArrayList<Position>();
		Position[] onesChess = chessState.getChess(playerOrder);
		for (Position p : onesChess) {
			for (int i = 1; i <= 6; i ++) {
				Position newposition = p.getSpecificPosition(i, 1);
				if (newposition.isLegal() && chessState.getGrid(newposition) == 0) {
					movableChess.add(p);
					break;
				}
				newposition = p.getSpecificPosition(i, 2);
				if (newposition.isLegal() && chessState.getGrid(newposition) == 0) {
					movableChess.add(p);
					break;
				}
			}
		}
		return movableChess;
	}
	
	/**
	 * 按照指定的规则，获取选择位置可到达的位置。若有重复，则只取最近的路径返回。
	 * @param playerOrder 需走棋的玩家编号
	 * @param chosenPosition 选定的位置（应与该玩家的某棋子位置重合）
	 * @param chessState 当前棋子状态
	 * @param ruleType 规则种类，1表示隔一子跳，2表示隔空跳
	 * @return 所有可以按照规则到达的位置，以Action表示。
	 */
	public static ArrayList<Action> findReachableAction(int playerOrder, Position chosenPosition, ChessState chessState, int ruleType) {
		ArrayList<Action> legalActions = new ArrayList<Action>();
		ArrayList<Position> neighbors = chosenPosition.getNeighbors();
		Queue<Action> nodes = new LinkedList<Action>();
		nodes.add(new Action(playerOrder, chosenPosition, chosenPosition));
		boolean[][] visit = new boolean[17][17];
		for (int i = 0; i < 17; i ++) {
			for (int j = 0; j < 17; j ++) {
				visit[i][j] = false;
			}
		}
		visit[8 - chosenPosition.getY()][chosenPosition.getX() + 8] = true;
		for (Position p : neighbors) {
			if (p.isLegal() && chessState.getGrid(p) == 0) {
				visit[8 - p.getY()][p.getX() + 8] = true;
				legalActions.add(new Action(playerOrder, chosenPosition, p));
			}
		}
		while (!nodes.isEmpty()) {
			if (ruleType == 1) findPosition1(visit, nodes, legalActions, chessState);
			else findPosition2(visit, nodes, legalActions, chessState);
		}
		
		return legalActions;
	}
	
	//按照隔一子跳法，添加可以跳跃到的位置。
	private static void findPosition1(boolean[][] visit, Queue<Action> nodes, ArrayList<Action> legalActions, ChessState chessState) {
		Action root = nodes.poll();
		Position proot = root.getEndPosition();
		for (int i = 1; i <= 6; i ++) {
			Position middle = proot.getSpecificPosition(i, 1);
			if (middle.isLegal() && chessState.getGrid(middle) != 0) {
				Position goal = proot.getSpecificPosition(i, 2);
				if (goal.isLegal() && chessState.getGrid(goal) == 0 && !visit[8 - goal.getY()][goal.getX() + 8]) {
					Action newAction = root.go(goal);
					nodes.add(newAction);
					legalActions.add(newAction);
					visit[8 - goal.getY()][goal.getX() + 8] = true;
				}
			}
		}
	}
	
	//按照隔空跳法，添加可以跳跃到的位置。
	private static void findPosition2(boolean[][] visit, Queue<Action> nodes, ArrayList<Action> legalActions, ChessState chessState) {
		Action root = nodes.poll();
		Position proot = root.getEndPosition();
		for (int i = 1; i <= 6; i ++) {
			boolean flag = false;
			Position middle;
			int dist = 1;
			for ( ; ; dist ++) {
				middle = proot.getSpecificPosition(i, dist);
				if (!middle.isLegal()) break;
				else if (chessState.getGrid(middle) != 0) {
					flag = true;
					break;
				}
			}
			// 如果存在跳板
			if (flag) {
				Position goal = proot.getMirrorPosition(middle);
				if (goal.isLegal() && chessState.getGrid(goal) == 0 && !visit[8 - goal.getY()][goal.getX() + 8]) {
					boolean flag2 = true;
					for (int j = dist + 1; j < 2 * dist; j ++) {
						Position search = proot.getSpecificPosition(i, j);
						if (chessState.getGrid(search) != 0) {
							flag2 = false;
							break;
						}
					}
					if (flag2) {
						Action newAction = root.go(goal);
						nodes.add(newAction);
						legalActions.add(newAction);
						visit[8 - goal.getY()][goal.getX() + 8] = true;
					}
				}
			}
		}
	}
	
	/**
	 * 判断指定玩家是否已经获胜（还没有写完，现在是最苛刻的判胜）
	 * @param playerOrder 指定玩家的序号（自下为1逆时针编号）
	 * @param chessState 当前棋盘状态
	 * @return 该玩家是否获胜，是则返回True，否则返回False
	 */
	public static boolean isWin(int playerOrder, ChessState chessState) {
		Position[] onesChess = chessState.getChess(playerOrder);
		for (Position p : onesChess) {
			if (p.getRegion() == 0) return false;
			else if (Math.abs(p.getRegion() - playerOrder) != 3) return false;
		}
		return true;
	}
	
}

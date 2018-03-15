package entity;

import java.io.Serializable;

/**
 * 棋子状态类
 * @author 豪豪
 *
 */
@SuppressWarnings("serial")
public class ChessState implements Serializable {
	
	private int[][] chess;  //棋局分布
	
	public ChessState() {
		this.chess = new int[17][17];
		for (int i = 0; i < 17; i ++) {
			for (int j = 0; j < 17; j ++) {
				this.chess[i][j] = 0;
			}
		}
	}
	
	public ChessState(int[][] chess) {
		this.chess = new int[17][17];
		for (int i = 0; i < 17; i ++) {
			for (int j = 0; j < 17; j ++) {
				this.chess[i][j] = chess[i][j];
			}
		}
	}
	
	public ChessState(ChessState cs) {
		this(cs.chess);
	}
	
	/**
	 * 获取当前棋面布局情况
	 * @return 棋面布局，值表示玩家编号
	 */
	public int[][] getChess() {
		return this.chess;
	}
	
	/**
	 * 获取某玩家的所有棋子
	 * @param playerOrder 需要获取的玩家编号
	 * @return 返回所有棋子位置组成的ArrayList
	 */
	public Position[] getChess(int playerOrder) {
		Position[] onesChess = new Position[10];
		int count = 0;
		for (int j = 0; j < 17; j ++) {
			for (int i = 0; i < 17; i ++) {
				if (chess[i][j] == playerOrder) {
					onesChess[count ++] = new Position(j-8, 8-i);
				}
			}
		}
		return onesChess;
	}
	
	/**
	 * 根据对应的Position读取该位置的棋子类别
	 * @param p 需要查询的Position
	 * @return 棋子类别，若该处没有棋子则返回0，否则返回对应棋子的玩家编号
	 */
	int getGrid(Position p) {
		return getGridByXY(p.getX(), p.getY());
	}
	
	/**
	 * 根据对应的Position设置对应的棋子类别
	 * @param p 需要设置的Position
	 * @param playerOrder 棋子类别，若该处设置为没有棋子则为0，否则为对应棋子的玩家编号
	 */
	void setGrid(Position p, int playerOrder) {
		setGridByXY(p.getX(), p.getY(), playerOrder);
	}
	
	/**
	 * 根据对应的x、y位置读取对应的棋子类别
	 * @param x 需要读取位置的x
	 * @param y 需要读取位置的y
	 * @return 棋子类别，若该处没有棋子则返回0，否则返回对应棋子的玩家编号
	 */
	int getGridByXY(int x, int y) {
		int i = 8 - y;
		int j = x + 8;
		return chess[i][j];
	}
	
	/**
	 * 根据对应的x、y位置设置对应的棋子类别
	 * @param x 需要设置位置的x
	 * @param y 需要设置位置的y
	 * @param playerOrder 棋子类别，若该处设置为没有棋子则为0，否则为对应棋子的玩家编号
	 */
	void setGridByXY(int x, int y, int playerOrder) {
		int i = 8 - y;
		int j = x + 8;
		chess[i][j] = playerOrder;
	}

	/**
	 * 根据玩家类型对棋盘做初始化
	 * @param playerTypes 玩家类型
	 */
	void ChessStateInitial(int[] playerTypes) {
		for (int i = 0; i < 17; i ++) {
			for (int j = 0; j < 17; j ++) {
				int x = j - 8;
				int y = 8 - i;
				int region = Position.getRegion(x, y, x - y);
				if (region <= 0 || playerTypes[region] >= 0) chess[i][j] = region;
			}
		}
	}
	
	/**
	 * 根据提供的Action进行移动操作
	 * @param action 要进行的操作
	 */
	public void move(Action action) {
		Position pStart = action.getStartPosition();
		Position pEnd = action.getEndPosition();
		setGridByXY(pStart.getX(), pStart.getY(), 0);
		setGridByXY(pEnd.getX(), pEnd.getY(), action.getPlayerOrder());
	}
	
	/**
	 * 打印当前棋盘状态
	 */
	void print() {
		String[] label = new String[]{".", "1", "2", "3", "4", "5", "6"};
		for (int j = 16; j >= 0; j --) {
			for (int k = 0; k < j; k ++) {
				System.out.print(" ");
			}
			for (int i = 0; i < 17; i ++) {
				if (chess[i][j] >= 0) {
					System.out.print(label[chess[i][j]] + " ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
	
}

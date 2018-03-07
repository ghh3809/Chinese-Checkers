package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * 棋盘类，用于保存游戏对局过程中的所有信息
 * @author 豪豪
 *
 */
@SuppressWarnings("serial")
public class ChessBoard implements Serializable {
	
	private ChessState chessState;  	//棋子状态
	private Player[] players;  			//玩家信息
	private LinkedList<Action> history; //历史记录
	private int presentPlayerOrder;  	//当前行棋玩家
	private int ruleType;  				//规则模式，1表示Type1（隔一子跳），2表示Type2（隔空跳）
	private int[] playerTypes;  		//玩家类型
	private int playerNum;				//玩家总数
	
	public ChessBoard(int[] playerTypes, int ruleType) {
		this.playerTypes = playerTypes;
		this.chessState = new ChessState();
  		this.chessState.ChessStateInitial(playerTypes);
		this.players = new Player[7];
		for (int i = 1; i < 7; i ++) {
			if (playerTypes[i] >= 0) {
				this.players[i] = new Player(i, playerTypes[i]);
				playerNum ++;
			}
		}
		this.history = new LinkedList<Action>();
		this.presentPlayerOrder = 0;
		this.nextPlayer();
		this.ruleType = ruleType;
	}

	public ChessState getChessState() {
		return this.chessState;
	}
	
	public Player[] getPlayers() {
		return this.players;
	}

	public int getPresentPlayerOrder() {
		return this.presentPlayerOrder;
	}
	
	public int getRuleType() {
		return ruleType;
	}
        
    public int[] getplayerTypes() {
		return playerTypes;
	}
    
    public int getPlayerNum() {
		return playerNum;
	}

	/**
	 * 按照指定的移动行棋
	 * @param action 指定的移动
	 */
	public void move(Action action) {
		this.chessState.move(action);
		if (history.size() > 100) {
			history.removeFirst();
		}
		this.history.add(action);
		this.players[this.presentPlayerOrder].addSteps();
		this.nextPlayer();
	}
	
	
	/**
	 * 获取当前玩家的可移动棋子
	 * @return 所有可移动棋子位置的ArrayList
	 */
	public ArrayList<Position> getMovableChess() {
		return Rule.getMovableChess(presentPlayerOrder, chessState);
	}
	
	/**
	 * 对指定位置的棋子，寻找所有可到达的位置
	 * @param chosenPosition 指定棋子位置
	 * @return 返回由所有移动组成的ArrayList
	 */
	public ArrayList<Action> getLegalActions(Position chosenPosition) {
		return Rule.findReachableAction(getPresentPlayerOrder(), chosenPosition, chessState, ruleType);
	}
	
	/**
	 * 悔棋
	 * @return 若可以悔棋，则返回True并悔棋，否则直接返回False
	 */
	public boolean regret() {
		if (this.history.isEmpty()) return false;
		Action lastAction = this.history.remove(this.history.size() - 1);
		this.lastPlayer();
		this.move(lastAction.rollBack());
		return true;
	}
	
	/**
	 * 打印当前棋盘状态
	 */
	public void print() {
		this.chessState.print();
	}
	
	/**
	 * 返回指定编号玩家是否已经胜利（仍要更改）
	 * @param playerOrder 玩家编号
	 * @return 若玩家胜利，返回True，否则返回False
	 */
	public boolean isWin(int playerOrder) {
		return Rule.isWin(playerOrder, getChessState());
	}
	
	/**
	 * 判断游戏是否结束（所有人均胜利）
	 * @return 若游戏可结束，则返回True，否则返回False
	 */
	public boolean gameTerminate() {
		for (int i = 1; i < 7; i ++) {
			if ((playerTypes[i] >= 0) && (!isWin(i))) return false;
		}
		return true;
	}
	
	/**
	 * 获取下一玩家编号
	 */
	private void nextPlayer() {
		if (this.gameTerminate()) {
			presentPlayerOrder = 0;
			return;
		}
		while (true) {
			presentPlayerOrder = (presentPlayerOrder % 6) + 1;
			if ((playerTypes[presentPlayerOrder] >= 0) && (!Rule.isWin(presentPlayerOrder, chessState))) break;
		}
	}
	
	/**
	 * 获取上一玩家编号
	 */
	private void lastPlayer() {
		while (true) {
			presentPlayerOrder = presentPlayerOrder == 1 ? 6 : presentPlayerOrder - 1;
			if ((playerTypes[presentPlayerOrder] >= 0) && (!Rule.isWin(presentPlayerOrder, chessState))) break;
		}
	}
	
}

package entity;

import java.io.Serializable;

/**
 * 游戏玩家类
 * @author 豪豪
 *
 */
@SuppressWarnings("serial")
public class Player implements Serializable {
	
	private int playerOrder;  	//玩家编号，以下方为1号玩家，逆时针旋转。
	private int playerType;  	//0表示个人玩家，1-4(有待商榷)表示不同等级的电脑。
	private String name;  		//玩家姓名
	private int steps;  		//该玩家已使用步数。
	
	public Player(int playerOrder, int playerType) {
		this(playerOrder, playerType, 0, "Player " + playerOrder);
	}
	
	public Player(int playerOrder, int playerType, int steps, String name) {
		this.playerOrder = playerOrder;
		this.playerType = playerType;
		this.steps = steps;
		this.name = name;
	}

	public int getPlayerOrder() {
		return this.playerOrder;
	}

	public int getPlayerType() {
		return this.playerType;
	}

	public int getSteps() {
		return this.steps;
	}
	
	public String getName() {
		return this.name;
	}
	
	void addSteps() {
		this.steps ++;
	}
	
	void minusSteps() {
		this.steps --;
	}
	
}

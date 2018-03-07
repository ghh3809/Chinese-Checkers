package entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 从一个位置到达另一个位置的移动
 * @author 豪豪
 *
 */
@SuppressWarnings("serial")
public class Action implements Serializable {
	
	private int playerOrder;  //玩家编号，以下方为1号玩家，逆时针旋转。
	private Position startPosition;  //开始跳跃位置
	private Position endPosition;  //结束跳跃位置
	private ArrayList<Position> path;  //途径位置
	
	public Action(int playerOrder, Position startPosition, Position endPosition) {
		this(playerOrder, startPosition, endPosition, new ArrayList<Position>());
	}
	
	public Action(int playerOrder, Position startPosition, Position endPosition, ArrayList<Position> path) {
		this.playerOrder = playerOrder;
		this.startPosition = startPosition;
		this.endPosition = endPosition;
		this.path = path;
	}

	public int getPlayerOrder() {
		return playerOrder;
	}

	public Position getStartPosition() {
		return startPosition;
	}

	public Position getEndPosition() {
		return endPosition;
	}

	public ArrayList<Position> getPath() {
		return path;
	}
	
	/**
	 * 倒序跳跃
	 * @return 返回与该对象跳跃顺序完全相反的新Action
	 */
	Action rollBack() {
		Action a = new Action(this.playerOrder, this.endPosition, this.startPosition);
		return a;
	}
	
	/**
	 * 向指定的目标继续行进
	 * @param p 指定的目标位置
	 * @return 返回在原对象基础上继续跳跃一步到目标位置的新Action
	 */
	Action go(Position p) {
		Action newAction = new Action(this.playerOrder, this.startPosition, this.endPosition, new ArrayList<Position>(this.path));
		newAction.path.add(newAction.endPosition);
		newAction.endPosition = p;
		return newAction;
	}

}

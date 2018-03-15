package entity;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 为棋盘的各位置进行编号，由三个变量x，y，z（仅有两个独立）确定棋盘上位置
 * 棋盘划分为7+1个区域，其中非法区域为-1，其余区域划分为：
 *         /\
 *    ____/04\____
 *    \05/¯¯¯¯\03/
 *     \/  00  \/
 *     /\  00  /\
 *    /06\____/02\
 *    ¯¯¯¯\01/¯¯¯¯
 *         \/
 * @author 豪豪
 *
 */
@SuppressWarnings("serial")
public class Position implements Serializable {

	private int x;  //以上方为x增大方向（沿—轴x不变），中心为0
	private int y;  //以左上方为y增大方向（沿/轴y不变），中心为0
	private int z;  //以右上方为z增大方向（沿\轴z不变），中心为0
	
	public Position(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Position(int x, int y) {
		this(x, y, x - y);
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}
	
	/**
	 * 返回该位置是否在公共区域
	 * @return 若该位置在公共区域，返回True，否则返回False
	 */
	public boolean isInPublic() {
		return Math.abs(this.x) <= 4 && Math.abs(this.y) <= 4 && Math.abs(this.z) <= 4;
	}
	
	/**
	 * 返回该位置是否属于合法位置（即在棋盘上）
	 * @return 若位置合法，返回True，否则返回False
	 */
	public boolean isLegal() {
		int xtemp = Math.abs(this.x);
		int ytemp = Math.abs(this.y);
		int ztemp = Math.abs(this.z);
		int max, max2;
		if (xtemp >= ytemp) {
			max = xtemp;
			max2 = ytemp;
		} else {
			max = ytemp;
			max2 = xtemp;
		}
		if (ztemp > max) {
			max2 = max;
			max = ztemp;
		} else if (ztemp > max2) {
			max2 = ztemp;
		}
		return max < 9 && max2 < 5;
	}
	
	/**
	 * 返回该位置当前所在的区域
	 * @return 若该位置不合法，则返回-1；若该位置在公共区域，则返回0；其他情况返回营地编号（下方为1，逆时针旋转）
	 */
	public int getRegion() {
		if (this.isInPublic()) return 0;
		else if (!this.isLegal()) return -1;
		else if (x < -4) return 1;
		else if (x > 4) return 4;
		else if (y < -4) return 2;
		else if (y > 4) return 5;
		else if (z < -4) return 6;
		else return 3;
	}
	
	/**
	 * 返回指定坐标所在的区域
	 * @param x 指定x坐标
	 * @param y 指定y坐标
	 * @param z 指定z坐标
	 * @return 若该位置不合法，则返回-1；若该位置在公共区域，则返回0；其他情况返回营地编号（下方为1，逆时针旋转）
	 */
	public static int getRegion(int x, int y, int z) {
		return new Position(x, y, z).getRegion();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		if (x != other.x)
			return false;
		if (y != other.y)
			return false;
		if (z != other.z)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "(" + x + ", " + y + ", " + z + ")";
	}
	
	/**
	 * 获取该位置的所有邻居位置
	 * @return 返回由所有邻居位置构成的ArrayList
	 */
	ArrayList<Position> getNeighbors() {
		ArrayList<Position> neighbors = new ArrayList<Position>();
		//成为邻居的必要条件：某一坐标一致，其他坐标相差1。
		neighbors.add(new Position(x, y-1));
		neighbors.add(new Position(x, y+1));
		neighbors.add(new Position(x+1, y));
		neighbors.add(new Position(x-1, y));
		neighbors.add(new Position(x+1, y+1));
		neighbors.add(new Position(x-1, y-1));
		return neighbors;
	}
	
	/**
	 * 获取该位置关于指定中心点的镜像点
	 * @param centerPosition 指定中心位置
	 * @return 关于指定中心点的镜像点
	 */
	Position getMirrorPosition(Position centerPosition) {
		return new Position(2*centerPosition.getX() - this.x, 2*centerPosition.getY() - this.y);
	}

	/**
	 * 返回指定方向指定距离的位置点
	 * @param direction 指定方向1-6，以向右为方向1，逆时针旋转。
	 * @param dist 指定距离，0表示原点
	 * @return 返回得到的位置点
	 */
	Position getSpecificPosition(int direction, int dist) {
		switch(direction) {
			case 1 : return new Position(this.x, this.y - dist);
			case 2 : return new Position(this.x + dist, this.y);
			case 3 : return new Position(this.x + dist, this.y + dist);
			case 4 : return new Position(this.x, this.y + dist);
			case 5 : return new Position(this.x - dist, this.y);
			case 6 : return new Position(this.x - dist, this.y - dist);
		}
		return null;
	}
	
}

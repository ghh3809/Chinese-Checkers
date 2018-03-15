package control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ai.*;
import entity.*;

/**
 * 控制游戏进行
 * @author 豪豪
 *
 */
public class Actioner {
	
	private ChessBoard chessBoard;
	
	/**
	 * 以对应的玩家类型和规则类型生成Actioner
	 * @param playerType 数组，指示玩家类型，-1表示空，0表示玩家，1-4(有待商榷)表示不同等级的电脑。
	 * @param ruleType 规则类型，1表示Type1（隔一子跳），2表示Type2（隔空跳）；
	 */
	public Actioner(int[] playerType, int ruleType) {
		this.chessBoard = new ChessBoard(playerType, ruleType);
	}
	
	/**
	 * 返回所有玩家信息，以Player数组表示，某元素为空表示此处无玩家
	 * @return 所有玩家
	 */
	public Player[] getPlayers() {
		return this.chessBoard.getPlayers();
	}
	
	/**
	 * 返回当前的棋子信息
	 * @return 当前棋子信息
	 */
	public ChessState getChessState() {
		return this.chessBoard.getChessState();
	}
	
	/**
	 * 返回当前该走子的玩家序号
	 * @return 该走子的玩家序号
	 */
	public int getPresentPlayerOrder() {
		return this.chessBoard.getPresentPlayerOrder();
	}
	
	/**
	 * 返回规则类型
	 * @return 规则类型，1为隔一子跳，2为隔空跳
	 */
	public int getRuleType() {
		return this.chessBoard.getRuleType();
	}
	
	/**
	 * 返回指定编号玩家是否已经胜利（仍要更改）
	 * @param playerOrder 玩家编号
	 * @return 若玩家胜利，返回True，否则返回False
	 */
	public boolean isWin(int playerOrder) {
		return this.chessBoard.isWin(playerOrder);
	}
	
	/**
	 * 判断游戏是否结束（所有人均胜利）
	 * @return 若游戏可结束，则返回True，否则返回False
	 */
	public boolean gameTerminate() {
		return this.chessBoard.gameTerminate();
	}
	
	/**
	 * 悔棋（当前同时打印了Regret字样）
	 * @return 若可以悔棋，则返回True并悔棋，否则直接返回False
	 */
	public boolean regret() {
		return this.chessBoard.regret();
	}
	
	/**
	 * 保存当前棋局（未完成）
	 * @param name 对应的存档名
	 * @param ID 对应的存档编号
	 * @return 保存成功返回True，否则返回False
	 */
	public boolean saveState(String name, int ID) {
		String[][] list = this.getAllArchive();
		list[ID] = new String[4];
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		list[ID][0] = name;
		list[ID][1] = sdf.format(date);
		list[ID][2] = String.valueOf(this.chessBoard.getPlayerNum());
		list[ID][3] = String.valueOf(this.getPlayers()[this.getPresentPlayerOrder()].getSteps());
		try {
			File fp = new File("archive");
			if (!fp.exists()) {
				fp.mkdir();
			}
			FileOutputStream fos = new FileOutputStream("archive/" + ID + ".dat");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(this.chessBoard);
			oos.close();
			FileOutputStream fos2 = new FileOutputStream("archive/list.dat");
			ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
			oos2.writeObject(list);
			oos2.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 读取存档列表
	 * @return 由各种信息组成的String[][]，每条信息的内容为[名字，时间，玩家人数，进行步数]或null
	 */
	public String[][] getAllArchive() {
		String[][] list = null;
		try {
			File fp = new File("archive");
			if (!fp.exists()) {
				fp.mkdir();
			}
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream("archive/list.dat"));
			list = (String[][])ois.readObject();
			ois.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			list = new String[5][];
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("archive/list.dat"));
				oos.writeObject(list);
				oos.close();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			return list;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	/**
	 * 读取存档
	 * @param ID 存档编号(0-4)
	 * @return 返回读档是否成功，成功返回True，否则返回False，文件找不到时同时删除该存档记录。
	 */
	public boolean loadArchive(int ID) {
		FileInputStream fs;
		try {
			fs = new FileInputStream("archive/" + ID + ".dat");
			ObjectInputStream os = new ObjectInputStream(fs);
			this.chessBoard = (ChessBoard)os.readObject();
			fs.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			String[][] list = this.getAllArchive();
			list[ID] = null;
			try {
				FileOutputStream fos = new FileOutputStream("archive/list.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				oos.writeObject(list);
				oos.close();
			} catch (Exception e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
			return false;			
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	/**
	 * 按照指定Action进行移动
	 * @param action 需要进行的移动操作
	 */
	public void move(Action action) {
		this.chessBoard.move(action);
	}
	
	/**
	 * 获取当前玩家可移动棋子
	 * @return 所有可移动棋子位置的ArrayList
	 */
	public ArrayList<Position> getMovableChess() {
		return this.chessBoard.getMovableChess();
	}
	
	/**
	 * 获取指定棋子的可行移动解
	 * @param chosenPosition 指定棋子位置
	 * @return 可行的移动组成的ArrayList
	 */
	public ArrayList<Action> getLegalActions(Position chosenPosition) {
		return this.chessBoard.getLegalActions(chosenPosition);
	}
	
	/**
	 * 获取特定深度下的最佳移动解
	 * @param depth 搜索深度
	 * @return 最佳解
	 */
	public Action getOptimusAction() {
		int depth = this.chessBoard.getPlayerNum();
		switch (this.chessBoard.getplayerTypes()[this.getPresentPlayerOrder()]) {
		case 4: depth ++;
		case 3: depth ++;
		case 2: depth ++;
		case 1: break;
		default: return null;
		}
		return SimpleDeepSearch.searchMaxAction(this.chessBoard, depth, new MyEvaluator());
	}
	
	/**
	 * 打印当前棋盘状态
	 */
	public void print() {
		this.chessBoard.print();
	}
	
}


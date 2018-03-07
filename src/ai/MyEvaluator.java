package ai;

import entity.ChessState;
import entity.Position;

public class MyEvaluator implements Evaluator {
	
	/**
	 * 评分由三部分构成：
	 * 1. 距离得分，根据所前进的目标方向确定
	 * 2. 偏移得分，偏移中心线应酌情扣分
	 * 3. 末子罚分，最后的棋子将会受到扣分
	 * @param cs 棋盘状态
	 * @param playerOrder 玩家编号
	 * @return
	 */
	public double getValue(ChessState cs, int playerOrder) {
		Position[] currentChess = cs.getChess(playerOrder);
		double sumDist = 0;			// 总距离得分
		double biasFine = 0.2; 		// 不能大于1/3，否则容易发生倒退现象
		double lastChessFine = 0.5;	// 最末棋子罚分
		double lastChess = 8;		// 最末棋子位置
		for (Position p : currentChess) {
			switch(playerOrder) {
			case 1: sumDist += p.getX() - biasFine * Math.abs(p.getY() - p.getZ()); lastChess =  p.getX() < lastChess ?  p.getX() : lastChess; break;
			case 4: sumDist += p.getX() + biasFine * Math.abs(p.getY() - p.getZ()); lastChess = -p.getX() < lastChess ? -p.getX() : lastChess; break;
			case 2: sumDist += p.getY() - biasFine * Math.abs(p.getZ() + p.getX()); lastChess =  p.getY() < lastChess ?  p.getY() : lastChess; break;
			case 5: sumDist += p.getY() + biasFine * Math.abs(p.getZ() + p.getX()); lastChess = -p.getY() < lastChess ? -p.getY() : lastChess; break;
			case 3: sumDist += p.getZ() + biasFine * Math.abs(p.getX() + p.getY()); lastChess = -p.getZ() < lastChess ? -p.getZ() : lastChess; break;
			case 6: sumDist += p.getZ() - biasFine * Math.abs(p.getX() + p.getY()); lastChess =  p.getZ() < lastChess ?  p.getZ() : lastChess; break;
			}
		}
		if ((playerOrder == 1) || (playerOrder == 2) || (playerOrder == 6)) {
			sumDist += 60 + biasFine * 14 + lastChessFine * lastChess;
		} else {
			sumDist = -sumDist + 60 + biasFine * 14 + lastChessFine * lastChess;
		}
		return sumDist;
	}
	
}

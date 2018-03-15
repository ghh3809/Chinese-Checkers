package present;

import ai.*;
import control.*;
import entity.*;

/*
 * 测试：1号玩家（搜索深度4），3号玩家（搜索深度5），5号玩家（搜索深度6）
 * 游戏模式：隔一子跳
 * 开始时间:8:07:59
 * 结束时间:8:37:48
 * 对战结果：1号玩家：47步，3号玩家：48步，5号玩家：36步
 */

/*				5号玩家获胜时的局面
                        1                         
                       1 .                       
                      1 1 .                     
                     . . . .                   
            . . . . 1 . 1 . . . 1 . .         
             . . . . . . . 3 . . . .         
              . . . . . . 1 . . . .         
               . . . . . . . . . .         
                . . . . 1 . . . .         
               3 . . 3 . 1 . . . 5       
              3 . 3 . . . . . . 5 5     
             3 3 . . . . . . . 5 5 5   
            3 3 3 . . . . . . 5 5 5 5 
                     . . . .         
                      . . .         
                       . .         
                        .         
 */

public class Test {
	public static void main(String[] args) {
		Actioner actioner = new Actioner(new int[] {0,3,3,3,3,3,3}, 2); // 0代表玩家，-1代表空，1~4代表不同难度的AI
		while (!actioner.gameTerminate()) {
			
			Action maxAction = actioner.getOptimusAction();
			actioner.move(maxAction);
			Evaluator myEvaluator = new MyEvaluator();
			for (int i = 1; i < 7; i ++) {
				Player player = actioner.getPlayers()[i];
				if (player.getPlayerType() > 0) {
					System.out.printf("%s: Steps = %d, Evaluation = %.1f\n", player.getName(), actioner.getPlayers()[i].getSteps()
							,myEvaluator.getValue(actioner.getChessState(), i));
				}
			}
			actioner.print();
			
		}
		System.out.println("Game Over!");
	}
}

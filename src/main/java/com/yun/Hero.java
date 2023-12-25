package com.yun;

import java.util.Vector;

/**
 * 功能描述
 *
 * @description: 玩家的坦克
 * @author: yun
 * @date: 2023年12月20日 14:28
 */
public class Hero extends Tank {
    
    // 定义射击对象(线程)
    Shot shot = null;
    // 多颗子弹
    Vector<Shot> shots = new Vector<>();
    
    public Hero(int x, int y) {
        super(x, y);
    }
    
    // 射击的方法
    public void shotEnemyTank() {
        // 最多五颗子弹
        if (shots.size() == 5) {
            return;
        }
        // 根据当前Hero对象的位置和方向来创建Shot
        switch (getDirect()) {
            case 0: // 向上
                shot = new Shot(getX() + 20, getY(), 0);
                break;
            case 1: // 向右
                shot = new Shot(getX() + 60, getY() + 20, 1);
                break;
            case 2: // 向下
                shot = new Shot(getX() + 20, getY() + 60, 2);
                break;
            case 3: // 向左
                shot = new Shot(getX(), getY() + 20, 3);
                break;
        }
        // 装入五颗子弹
        shots.add(shot);
        // 启动线程
        new Thread(shot).start();
    }
}

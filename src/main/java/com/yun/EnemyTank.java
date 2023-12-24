package com.yun;

import java.util.Vector;

/**
 * 功能描述
 *
 * @description: 敌方坦克
 * @author: yun
 * @date: 2023年12月20日 17:41
 */
public class EnemyTank extends Tank implements Runnable {
    // 使用Vector 保存多个Shot
    private Vector<Shot> shots = new Vector<>();
    private boolean isLive = true;
    
    public EnemyTank(int x, int y) {
        super(x, y);
    }
    
    public Vector<Shot> getShots() {
        return shots;
    }
    
    public void setShots(Vector<Shot> shots) {
        this.shots = shots;
    }
    
    public boolean isLive() {
        return isLive;
    }
    
    public void setLive(boolean live) {
        isLive = live;
    }
    
    
    @Override
    public void run() {
        while (true) {
            if (isLive && shots.size() < 1) {
                Shot shot = null;
                // 判断坦克的方向，创建对应的子弹
                switch (getDirect()) {
                    case 0: // 上
                        shot = new Shot(getX() + 20, getY(), 0);
                        break;
                    case 1: // 右
                        shot = new Shot(getX() + 60, getY() + 20, 1);
                        break;
                    case 2: // 下
                        shot = new Shot(getX() + 20, getY() + 60, 2);
                        break;
                    case 3: // 左
                        shot = new Shot(getX(), getY() + 20, 3);
                        break;
                }
                shots.add(shot);
                new Thread(shot).start();
            }
            
            // 根据坦克的方向来继续移动
            switch (getDirect()) {
                case 0: // 向上
                    for (int i = 0; i < 40; i++) {
                        if (getY() > 0) {
                            moveUp();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 1: // 向右
                    for (int i = 0; i < 20; i++) {
                        if (getX() + 60 < 1000) {
                            moveRight();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 2: // 向下
                    for (int i = 0; i < 30; i++) {
                        if (getY() + 60 < 750) {
                            moveDown();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
                case 3: // 向左
                    for (int i = 0; i < 10; i++) {
                        if (getX() > 0) {
                            moveLeft();
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    break;
            }
            // 随机改变坦克方向 0-3
            setDirect((int) (Math.random() * 4));
            // setDirect(3);
            if (!isLive) {
                break;
            }
        }
    }
}

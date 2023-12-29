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
    // 封装初始化的敌方坦克
    private Vector<EnemyTank> enemyTankVector = new Vector<>();
    
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
    
    public Vector<EnemyTank> getEnemyTankVector() {
        return enemyTankVector;
    }
    
    public void setEnemyTankVector(Vector<EnemyTank> enemyTankVector) {
        this.enemyTankVector = enemyTankVector;
    }
    
    @Override
    public void run() {
        while (true) {
            if (isLive && shots.size() < 10) {
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
                        if (getY() > 0 && !isTouchEnemyTank()) {
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
                        if (getX() + 60 < 1000 && !isTouchEnemyTank()) {
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
                        if (getY() + 60 < 750 && !isTouchEnemyTank()) {
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
                        if (getX() > 0 && !isTouchEnemyTank()) {
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
    
    /**
     * @description: 判断当前的这个敌人坦克，是否和 enemyTanks 中的其他坦克发生的重叠或者碰撞
     * @author: yun
     * @date: 2023/12/28 11:01
     * @return: boolean
     */
    public boolean isTouchEnemyTank() {
        // 判断当前坦克的方向
        switch (this.getDirect()) {
            case 0:  // 上
                for (int i = 0; i < enemyTankVector.size(); i++) {
                    EnemyTank enemyTank = enemyTankVector.get(i);
                    // 不和自己比较
                    if (enemyTank != this) {
                        // 方向是上下
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) {
                            // 当前坦克左上角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true;
                            }
                            // 当前坦克右上角的坐标
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true;
                            }
                        }
                        // 方向是左右
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) {
                            // 当前坦克左上角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true;
                            }
                            // 当前坦克右上角的坐标
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 1:  // 右
                for (int i = 0; i < enemyTankVector.size(); i++) {
                    EnemyTank enemyTank = enemyTankVector.get(i);
                    // 不和自己比较
                    if (enemyTank != this) {
                        // 方向是上下
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) {
                            // 当前坦克右上角的坐标
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true;
                            }
                            // 当前坦克右下角的坐标
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 40
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 60) {
                                return true;
                            }
                        }
                        // 方向是左右
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) {
                            // 当前坦克右上角的坐标
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true;
                            }
                            // 当前坦克右下角的坐标
                            if (this.getX() + 60 >= enemyTank.getX() && this.getX() + 60 <= enemyTank.getX() + 60
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 2:  // 下
                for (int i = 0; i < enemyTankVector.size(); i++) {
                    EnemyTank enemyTank = enemyTankVector.get(i);
                    // 不和自己比较
                    if (enemyTank != this) {
                        // 方向是上下
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) {
                            // 当前坦克左下角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 60) {
                                return true;
                            }
                            // 当前坦克右下角的坐标
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 40
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 60) {
                                return true;
                            }
                        }
                        // 方向是左右
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) {
                            // 当前坦克左下角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 40) {
                                return true;
                            }
                            // 当前坦克右下角的坐标
                            if (this.getX() + 40 >= enemyTank.getX() && this.getX() + 40 <= enemyTank.getX() + 60
                                    && this.getY() + 60 >= enemyTank.getY() && this.getY() + 60 <= enemyTank.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;
            case 3:  // 左
                for (int i = 0; i < enemyTankVector.size(); i++) {
                    EnemyTank enemyTank = enemyTankVector.get(i);
                    // 不和自己比较
                    if (enemyTank != this) {
                        // 方向是上下
                        if (enemyTank.getDirect() == 0 || enemyTank.getDirect() == 2) {
                            // 当前坦克左上角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 60) {
                                return true;
                            }
                            // 当前坦克右下角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 40
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 60) {
                                return true;
                            }
                        }
                        // 方向是左右
                        if (enemyTank.getDirect() == 1 || enemyTank.getDirect() == 3) {
                            // 当前坦克左上角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() >= enemyTank.getY() && this.getY() <= enemyTank.getY() + 40) {
                                return true;
                            }
                            // 当前坦克左下角的坐标
                            if (this.getX() >= enemyTank.getX() && this.getX() <= enemyTank.getX() + 60
                                    && this.getY() + 40 >= enemyTank.getY() && this.getY() + 40 <= enemyTank.getY() + 40) {
                                return true;
                            }
                        }
                    }
                }
                break;
        }
        return false;
    }
}

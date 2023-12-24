package com.yun;

/**
 * 功能描述
 *
 * @description: 射击子弹
 * @author: yun
 * @date: 2023-12-24 10:59
 */
public class Shot implements Runnable {
    private int x; // 子弹坐标 x
    private int y; // 子弹坐标 y
    private int direct = 0;  // 子弹默认方向
    private int speed = 2; // 子弹速度
    private boolean isLive = true; // 子弹是否还存活
    
    @Override
    public void run() { // 射击
        while (true) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // 根据方向来改变x，y坐标
            switch (direct) {
                case 0: // 上
                    y -= speed;
                    break;
                case 1: // 右
                    x += speed;
                    break;
                case 2: // 下
                    y += speed;
                    break;
                case 3: // 左
                    x -= speed;
                    break;
            }
            System.out.println("子弹的坐标 x=" + x + " y=" + y);
            // 当子弹移动到面板的边界时，就销毁
            if (!(x >= 0 && x <= 1000 && y >= 0 && y <= 750)) {
                System.out.println("子弹线程退出");
                isLive = false;
                break;
            }
        }
    }
    
    public Shot(int x, int y, int direct) {
        this.x = x;
        this.y = y;
        this.direct = direct;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public int getDirect() {
        return direct;
    }
    
    public void setDirect(int direct) {
        this.direct = direct;
    }
    
    public int getSpeed() {
        return speed;
    }
    
    public void setSpeed(int speed) {
        this.speed = speed;
    }
    
    public boolean isLive() {
        return isLive;
    }
    
    public void setLive(boolean live) {
        isLive = live;
    }
}

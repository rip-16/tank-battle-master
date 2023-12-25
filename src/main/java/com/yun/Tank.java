package com.yun;

/**
 * 功能描述
 *
 * @description: 坦克类
 * @author: yun
 * @date: 2023年12月20日 14:25
 */
public class Tank {
    // 坦克的横坐标
    private int x;
    // 坦克的纵坐标
    private int y;
    // 坦克的方向 0:上  1:右  下:2  左:3
    private int direct = 0;
    // 速度
    private int speed = 1;
    // 是否存活
    private boolean isLive = true;
    
    public Tank(int x, int y) {
        this.x = x;
        this.y = y;
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
    
    // 上右下左移动方法
    public void moveUp() {
        y -= speed;
    }
    
    public void moveRight() {
        x += speed;
    }
    
    public void moveDown() {
        y += speed;
    }
    
    public void moveLeft() {
        x -= speed;
    }
    
    public boolean isLive() {
        return isLive;
    }
    
    public void setLive(boolean live) {
        isLive = live;
    }
}

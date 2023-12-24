package com.yun;

/**
 * 功能描述
 *
 * @description: 炸弹
 * @author: yun
 * @date: 2023-12-24 16:23
 */
public class Bomb {
    // 炸弹的坐标
    private int x;
    private int y;
    // 炸弹的生命周期
    private int life = 9;
    // 炸弹是否存活
    private boolean isLive = true;
    
    public Bomb(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    // 减少生命值
    public void lifeDown() {
        if (life > 0) {
            life--;
        } else {
            isLive = false;
        }
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
    
    public int getLife() {
        return life;
    }
    
    public void setLife(int life) {
        this.life = life;
    }
    
    public boolean isLive() {
        return isLive;
    }
    
    public void setLive(boolean live) {
        isLive = live;
    }
}

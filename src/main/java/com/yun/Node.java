package com.yun;

/**
 * 功能描述
 *
 * @description: 一个node表示一个敌方坦克信息
 * @author: yun
 * @date: 2023年12月30日 8:56
 */
public class Node {
    private int X;
    private int Y;
    private int direct;
    
    public Node() {
    }
    
    public Node(int x, int y, int direct) {
        X = x;
        Y = y;
        this.direct = direct;
    }
    
    public int getX() {
        return X;
    }
    
    public void setX(int x) {
        X = x;
    }
    
    public int getY() {
        return Y;
    }
    
    public void setY(int y) {
        Y = y;
    }
    
    public int getDirect() {
        return direct;
    }
    
    public void setDirect(int direct) {
        this.direct = direct;
    }
}

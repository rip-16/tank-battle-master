package com.yun;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;

/**
 * 功能描述
 *
 * @description: 坦克大战的绘图区域
 * @author: yun
 * @date: 2023年12月20日 14:17
 */
public class MyPanel extends JPanel implements KeyListener {
    
    // 定义用户的坦克
    Hero hero = null;
    
    // 敌方坦克放入 Vector
    Vector<EnemyTank> enemyTanks = new Vector<>();
    int enemyTankSize = 3;
    
    // 初始化用户的坦克
    public MyPanel() {
        hero = new Hero(100, 100);
        // 创建敌方坦克
        for (int i = 0; i < enemyTankSize; i++) {
            EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
            // 设置方向
            enemyTank.setDirect(2);
            enemyTanks.add(enemyTank);
        }
    }
    
    @Override
    public void paint(Graphics g) {
        // 用户的坦克
        super.paint(g);
        // 填充矩形，默认黑色
        g.fillRect(0, 0, 1000, 750);
        // 画出坦克
        drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        // 敌方坦克
        for (EnemyTank enemyTank : enemyTanks) {
            drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
        }
        
    }
    
    /**
     * @param x:      坦克的左上角x坐标
     * @param y:      坦克的左上角y坐标
     * @param g:      画笔
     * @param direct: 坦克的方向（0:上  1:右  2:下  3:左）
     * @param type:   坦克的类型
     * @description: 画出坦克的方法
     * @author: yun
     * @date: 2023/12/20 14:36
     * @return: void
     */
    private void drawTank(int x, int y, Graphics g, int direct, int type) {
        // 根据不同类型坦克，设置不同颜色
        switch (type) {
            case 0: // 用户的坦克
                g.setColor(Color.cyan);
                break;
            case 1: // 敌人的坦克
                g.setColor(Color.yellow);
                break;
        }
        
        // 根据坦克方向来绘制
        switch (direct) {
            case 0: // 向上
                g.fill3DRect(x, y, 10, 60, false); // 坦克的左边轮子
                g.fill3DRect(x + 30, y, 10, 60, false); // 坦克的右边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // 坦克的矩形盖子
                g.fillOval(x + 10, y + 20, 20, 20);  // 坦克的圆形盖子
                g.drawLine(x + 20, y + 30, x + 20, y); // 炮筒
                break;
            case 1: // 向右
                g.fill3DRect(x, y, 60, 10, false); // 坦克的左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false); // 坦克的右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // 坦克的矩形盖子
                g.fillOval(x + 20, y + 10, 20, 20);  // 坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x + 60, y + 20); // 炮筒
                break;
            case 2: // 向下
                g.fill3DRect(x, y, 10, 60, false); // 坦克的左边轮子
                g.fill3DRect(x + 30, y, 10, 60, false); // 坦克的右边轮子
                g.fill3DRect(x + 10, y + 10, 20, 40, false); // 坦克的矩形盖子
                g.fillOval(x + 10, y + 20, 20, 20);  // 坦克的圆形盖子
                g.drawLine(x + 20, y + 30, x + 20, y + 60); // 炮筒
                break;
            case 3: // 向左
                g.fill3DRect(x, y, 60, 10, false); // 坦克的左边轮子
                g.fill3DRect(x, y + 30, 60, 10, false); // 坦克的右边轮子
                g.fill3DRect(x + 10, y + 10, 40, 20, false); // 坦克的矩形盖子
                g.fillOval(x + 20, y + 10, 20, 20);  // 坦克的圆形盖子
                g.drawLine(x + 30, y + 20, x, y + 20); // 炮筒
                break;
            default:
                System.out.println("暂时未处理！");
        }
    }
    
    // 有字符输出时，该方法就会触发
    @Override
    public void keyTyped(KeyEvent e) {
    
    }
    
    // 当某个键按下，该方法会触发
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_UP) {  // 按下W键
            // 改变坦克的方向
            hero.setDirect(0);
            // 修改坦克的坐标
            hero.moveUp();
        } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) { // 按下D键
            hero.setDirect(1);
            hero.moveRight();
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) { // 按下S键
            hero.setDirect(2);
            hero.moveDown();
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) { // 按下A键
            hero.setDirect(3);
            hero.moveLeft();
        }
        // 面板重绘
        this.repaint();
    }
    
    // 当某个键释放(松开)，该方法会触发
    @Override
    public void keyReleased(KeyEvent e) {
    
    }
}

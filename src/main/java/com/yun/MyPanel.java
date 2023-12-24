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
public class MyPanel extends JPanel implements KeyListener, Runnable {
    
    // 定义玩家的坦克
    Hero hero = null;
    
    // 敌方坦克放入 Vector
    Vector<EnemyTank> enemyTanks = new Vector<>();
    int enemyTankSize = 3;
    
    // 初始化坦克
    public MyPanel() {
        // 创建玩家坦克
        hero = new Hero(100, 100);
        // 创建敌方坦克
        for (int i = 0; i < enemyTankSize; i++) {
            EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
            // 设置方向
            enemyTank.setDirect(2);
            // 加入子弹
            Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
            enemyTank.getShots().add(shot);
            new Thread(shot).start();
            enemyTanks.add(enemyTank);
        }
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 填充矩形，默认黑色
        g.fillRect(0, 0, 1000, 750);
        // 画出玩家坦克
        drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        // hero射击的子弹
        if (hero.shot != null && hero.shot.isLive() == true) {
            g.draw3DRect(hero.shot.getX(), hero.shot.getY(), 1, 1, false);
        }
        // 敌方坦克
        for (int i = 0; i < enemyTanks.size(); i++) {
            EnemyTank enemyTank = enemyTanks.get(i);
            // 判断当前坦克是否还存活
            if (enemyTank.isLive()) {
                // 当坦克是存活的，才画出该坦克
                drawTank(enemyTank.getX(), enemyTank.getY(), g, enemyTank.getDirect(), 1);
                // enemyTank射击的子弹
                for (int j = 0; j < enemyTank.getShots().size(); j++) {
                    Shot shot = enemyTank.getShots().get(j);
                    if (shot.isLive()) {
                        g.draw3DRect(shot.getX(), shot.getY(), 1, 1, false);
                    } else {
                        enemyTank.getShots().remove(shot);
                    }
                }
            }
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
            case 0: // 玩家的坦克
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
        // 子弹发射
        if (e.getKeyCode() == KeyEvent.VK_J) {
            System.out.println("开始射击");
            hero.shotEnemyTank();
        }
        // 面板重绘
        this.repaint();
    }
    
    // 当某个键释放(松开)，该方法会触发
    @Override
    public void keyReleased(KeyEvent e) {
    
    }
    
    /**
     * @description: 每隔100毫秒，重绘区域，刷新绘图区域让子弹移动
     * @author: yun
     * @date: 2023/12/24 12:27
     * @return: void
     */
    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            this.repaint();
        }
    }
}

package com.yun;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
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
    // 定义一个Vector ,用于存放炸弹。当子弹击中坦克时，加入一个Bomb对象到bombs
    Vector<Bomb> bombs = new Vector<>();
    int enemyTankSize = 3;
    
    // 定义三张炸弹图片，用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    
    
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
        // 初始化图片对象
        String dir = "src\\main\\resources\\";
        image1 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_3.gif");
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
            g.fillRect(hero.shot.getX(), hero.shot.getY(), 4, 4);
        }
        // 如果bombs集合中有对象
        for (int i = 0; i < bombs.size(); i++) {
            // 取出
            Bomb bomb = bombs.get(i);
            // 根据当前这个bomb对象的life去画出对应的图片
            if (bomb.getLife() > 6) {
                g.drawImage(image1, bomb.getX(), bomb.getY(), 60, 60, this);
            } else if (bomb.getLife() > 3) {
                g.drawImage(image2, bomb.getX(), bomb.getY(), 60, 60, this);
            } else {
                g.drawImage(image3, bomb.getX(), bomb.getY(), 60, 60, this);
            }
            // 让这个炸弹的生命值减少
            bomb.lifeDown();
            // 如果life==0，就从集合中删除
            if (bomb.getLife() == 0) {
                bombs.remove(bomb);
            }
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
                        g.fillRect(shot.getX(), shot.getY(), 4, 4);
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
    
    /**
     * @description: 判断玩家子弹是否击中敌人坦克
     * @author: yun
     * @date: 2023/12/24 18:17
     * @return: void
     */
    public void hitEnemyTank() {
        // 单颗子弹。
        if (hero.shot != null && hero.shot.isLive()) { // 当玩家子弹还存活
            // 遍历敌人所有的坦克
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                hitTank(hero.shot, enemyTank);
            }
        }
    }
    
    /**
     * @param shot:
     * @param enemyTank:
     * @description: 判断子弹是否击中坦克
     * @author: yun
     * @date: 2023/12/24 18:13
     * @return: void
     */
    public void hitTank(Shot shot, EnemyTank enemyTank) {
        // 判断shot击中坦克
        switch (enemyTank.getDirect()) {
            case 0: // 上
            case 2: // 下
                if (shot.getX() > enemyTank.getX() && shot.getX() < enemyTank.getX() + 40
                        && shot.getY() > enemyTank.getY() && shot.getY() < enemyTank.getY() + 60) {
                    shot.setLive(false);
                    enemyTank.setLive(false);
                    // 当玩家子弹击中敌人坦克后，将enemyTank从Vector移除
                    enemyTanks.remove(enemyTank);
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1: // 右
            case 3: // 左
                if (shot.getX() > enemyTank.getX() && shot.getX() < enemyTank.getX() + 60
                        && shot.getY() > enemyTank.getY() && shot.getY() < enemyTank.getY() + 40) {
                    shot.setLive(false);
                    enemyTank.setLive(false);
                    // 当玩家子弹击中敌人坦克后，将enemyTank从Vector移除
                    enemyTanks.remove(enemyTank);
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(enemyTank.getX(), enemyTank.getY());
                    bombs.add(bomb);
                }
                break;
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
            if (hero.shot == null || !hero.shot.isLive()) {
                hero.shotEnemyTank();
            }
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
            // 判断玩家的子弹是否击中敌人坦克
            hitEnemyTank();
            this.repaint();
        }
    }
}

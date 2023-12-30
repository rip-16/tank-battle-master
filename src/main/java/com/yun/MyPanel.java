package com.yun;

import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.nio.file.Paths;
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
    // 用于恢复敌人坦克的坐标和方向
    Vector<Node> nodes = new Vector<>();
    int enemyTankSize = 3;
    
    // 定义三张炸弹图片，用于显示爆炸效果
    Image image1 = null;
    Image image2 = null;
    Image image3 = null;
    
    
    // 初始化坦克
    public MyPanel(String key) {
        // 判断myRecord.txt记录文件是否存在
        File file = new File(Recorder.getRecordFile());
        String fileName = Paths.get(file.toURI()).getFileName().toString();
        String fileSuffix = fileName.substring(fileName.lastIndexOf("."));
        // 如果存在，就正常执行，如果文件不存在，开启新游戏，key = "1"
        if (file.exists() && file.isFile() && fileSuffix.equals(".txt")) {
            // 获取存档信息
            nodes = Recorder.getNodesAndEnemyTankRec();
        } else {
            key = "1";
            System.out.println("无存档信息，开启新游戏");
        }
        // 指向Recorder对象的敌人坦克Vector，保存信息
        Recorder.setEnemyTanks(enemyTanks);
        // 创建玩家坦克
        hero = new Hero(500, 600);
        // 创建敌方坦克
        switch (key) {
            case "1":
                Recorder.setAllEnemyTankNum(0);
                for (int i = 0; i < enemyTankSize; i++) {
                    EnemyTank enemyTank = new EnemyTank((100 * (i + 1)), 0);
                    // 设置方向
                    enemyTank.setDirect((int) (Math.random() * 4));
                    enemyTankInitAndStart(enemyTank);
                }
                break;
            case "2":
                for (int i = 0; i < nodes.size(); i++) {
                    Node node = nodes.get(i);
                    EnemyTank enemyTank = new EnemyTank(node.getX(), node.getY());
                    // 设置方向
                    enemyTank.setDirect(node.getDirect());
                    enemyTankInitAndStart(enemyTank);
                }
                break;
            default:
                System.out.println("输入有误，请重新输入");
        }
        // 初始化图片对象
        String dir = "src\\main\\resources\\";
        image1 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_1.gif");
        image2 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_2.gif");
        image3 = Toolkit.getDefaultToolkit().getImage(dir + "bomb_3.gif");
    }
    
    /**
     * @param enemyTank:
     * @description: 初始化enemyTank并启动
     * @author: yun
     * @date: 2023/12/30 10:03
     * @return: void
     */
    private void enemyTankInitAndStart(EnemyTank enemyTank) {
        // 将初始化的enemyTanks放入 enemyTankVector集合
        enemyTank.setEnemyTankVector(enemyTanks);
        // 启动敌人坦克线程
        new Thread(enemyTank).start();
        // 加入子弹
        Shot shot = new Shot(enemyTank.getX() + 20, enemyTank.getY() + 60, enemyTank.getDirect());
        enemyTank.getShots().add(shot);
        new Thread(shot).start();
        enemyTanks.add(enemyTank);
    }
    
    /**
     * @param g:
     * @description: 显示玩家击毁坦克的信息
     * @author: yun
     * @date: 2023/12/29 15:45
     * @return: void
     */
    public void showInfo(Graphics g) {
        // 画出玩家的信息
        g.setColor(Color.BLACK);
        g.setFont(new Font("黑体", Font.BOLD, 25));
        g.drawString("您累计击毁敌方坦克", 1020, 30);
        // 画出敌方坦克
        drawTank(1020, 60, g, 0, 0);
        g.setColor(Color.BLACK);
        g.drawString(Recorder.getAllEnemyTankNum() + "", 1080, 100);
    }
    
    @Override
    public void paint(Graphics g) {
        super.paint(g);
        // 填充矩形，默认黑色
        g.fillRect(0, 0, 1000, 750);
        showInfo(g);
        // 画出玩家坦克
        if (hero != null && hero.isLive()) {
            drawTank(hero.getX(), hero.getY(), g, hero.getDirect(), 0);
        }
        // hero射击的子弹
        // if (hero.shot != null && hero.shot.isLive() == true) {
        //     g.fillRect(hero.shot.getX(), hero.shot.getY(), 4, 4);
        // }
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);
            if (shot != null && shot.isLive()) {
                g.fillRect(shot.getX(), shot.getY(), 4, 4);
            } else {
                hero.shots.remove(shot);
            }
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
        // if (hero.shot != null && hero.shot.isLive()) { // 当玩家子弹还存活
        //     // 遍历敌人所有的坦克
        //     for (int i = 0; i < enemyTanks.size(); i++) {
        //         EnemyTank enemyTank = enemyTanks.get(i);
        //         hitTank(hero.shot, enemyTank);
        //     }
        // }
        // 多颗子弹
        for (int j = 0; j < hero.shots.size(); j++) {
            Shot shot = hero.shots.get(j);
            if (shot != null && shot.isLive()) { // 当玩家子弹还存活
                // 遍历敌人所有的坦克
                for (int i = 0; i < enemyTanks.size(); i++) {
                    EnemyTank enemyTank = enemyTanks.get(i);
                    hitTank(shot, enemyTank);
                }
            }
        }
    }
    
    /**
     * @description: 判断敌方子弹是否击中玩家坦克
     * @author: yun
     * @date: 2023/12/25 20:22
     * @return: void
     */
    public void hitHero() {
        for (int j = 0; j < enemyTanks.size(); j++) {
            // 取出敌方坦克
            EnemyTank enemyTank = enemyTanks.get(j);
            for (int i = 0; i < enemyTank.getShots().size(); i++) {
                // 取出子弹
                Shot shot = enemyTank.getShots().get(i);
                // 判断是否击中玩家坦克
                if (hero.isLive() && shot.isLive()) {
                    hitTank(shot, hero);
                }
            }
            
        }
    }
    
    /**
     * @param shot:
     * @param tank:
     * @description: 判断子弹是否击中坦克
     * @author: yun
     * @date: 2023/12/24 18:13
     * @return: void
     */
    public void hitTank(Shot shot, Tank tank) {
        // 判断shot击中坦克
        switch (tank.getDirect()) {
            case 0: // 上
            case 2: // 下
                if (shot.getX() > tank.getX() && shot.getX() < tank.getX() + 40
                        && shot.getY() > tank.getY() && shot.getY() < tank.getY() + 60) {
                    shot.setLive(false);
                    tank.setLive(false);
                    // 当玩家子弹击中敌人坦克后，将enemyTank从Vector移除
                    enemyTanks.remove(tank);
                    // 玩家击毁一个敌方坦克时，就对数据allEnemyTankNum++
                    if (tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
                    bombs.add(bomb);
                }
                break;
            case 1: // 右
            case 3: // 左
                if (shot.getX() > tank.getX() && shot.getX() < tank.getX() + 60
                        && shot.getY() > tank.getY() && shot.getY() < tank.getY() + 40) {
                    shot.setLive(false);
                    tank.setLive(false);
                    // 当玩家子弹击中敌人坦克后，将enemyTank从Vector移除
                    enemyTanks.remove(tank);
                    // 玩家击毁一个敌方坦克时，就对数据allEnemyTankNum++
                    if (tank instanceof EnemyTank) {
                        Recorder.addAllEnemyTankNum();
                    }
                    // 创建Bomb对象，加入到bombs集合
                    Bomb bomb = new Bomb(tank.getX(), tank.getY());
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
            if (hero.getY() > 0) {
                hero.moveUp();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_D || e.getKeyCode() == KeyEvent.VK_RIGHT) { // 按下D键
            hero.setDirect(1);
            if (hero.getX() + 60 < 1000) {
                hero.moveRight();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_S || e.getKeyCode() == KeyEvent.VK_DOWN) { // 按下S键
            hero.setDirect(2);
            if (hero.getY() + 60 < 750) {
                hero.moveDown();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_A || e.getKeyCode() == KeyEvent.VK_LEFT) { // 按下A键
            hero.setDirect(3);
            if (hero.getX() > 0) {
                hero.moveLeft();
            }
        }
        // 子弹发射
        if (e.getKeyCode() == KeyEvent.VK_J) {
            System.out.println("开始射击");
            // if (hero.shot == null || !hero.shot.isLive()) {
            //     hero.shotEnemyTank();
            // }
            // 多颗子弹
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
            // 判断玩家的子弹是否击中敌人坦克
            hitEnemyTank();
            // 判断敌方的子弹是否击中玩家坦克
            hitHero();
            this.repaint();
        }
    }
}

package com.yun;

import javax.swing.JFrame;

/**
 * 功能描述
 *
 * @description: 坦克游戏的启动类
 * @author: yun
 * @date: 2023年12月20日 14:57
 */
public class TankGameApplication extends JFrame {
    
    // 定义用户的坦克
    private MyPanel myPanel;
    
    public static void main(String[] args) {
        TankGameApplication tankGameApplication = new TankGameApplication();
    }
    
    public TankGameApplication() {
        // 初始化面板
        myPanel = new MyPanel();
        // 把画板放入到窗口（画框）
        this.add(myPanel);
        // 窗口 JFrame 对象可以监听键盘事件, 即可以监听到面板发生的键盘事件
        this.addKeyListener(myPanel);
        // 设置窗口的大小
        this.setSize(1000, 750);
        // 当点击窗口的小 × 程序完全退出
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 显示
        this.setVisible(true);
    }
}

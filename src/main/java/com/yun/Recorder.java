package com.yun;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

/**
 * 功能描述
 *
 * @description: 记录相关信息和文件交互
 * @author: yun
 * @date: 2023年12月29日 14:58
 */
public class Recorder {
    // 记录玩家击毁坦克数
    private static int allEnemyTankNum = 0;
    private static BufferedWriter bufferedWriter = null;
    private static BufferedReader bufferedReader = null;
    private static String recordFile = "src\\main\\resources\\myRecord.txt";
    // 指向MyPanel对象的敌人坦克Vector
    private static Vector<EnemyTank> enemyTanks = null;
    // 保存敌人坦克的信息
    private static Vector<Node> nodes = new Vector<>();
    
    
    public static int getAllEnemyTankNum() {
        return allEnemyTankNum;
    }
    
    public static void setAllEnemyTankNum(int allEnemyTankNum) {
        Recorder.allEnemyTankNum = allEnemyTankNum;
    }
    
    public static String getRecordFile() {
        return recordFile;
    }
    
    public static void setEnemyTanks(Vector<EnemyTank> enemyTanks) {
        Recorder.enemyTanks = enemyTanks;
    }
    
    /**
     * @description: 退出时保存信息到recordFile
     * @author: yun
     * @date: 2023/12/29 15:33
     * @return: void
     */
    public static void saveRecord() {
        try {
            bufferedWriter = new BufferedWriter(new FileWriter(recordFile));
            bufferedWriter.write(allEnemyTankNum + "\r\n");
            for (int i = 0; i < enemyTanks.size(); i++) {
                EnemyTank enemyTank = enemyTanks.get(i);
                if (enemyTank.isLive()) {
                    // 保存相关信息
                    String record = enemyTank.getX() + " " + enemyTank.getY() + " " + enemyTank.getDirect();
                    bufferedWriter.write(record + "\r\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                assert bufferedWriter != null;
                bufferedWriter.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    /**
     * @description: 玩家击毁坦克后进行累加
     * @author: yun
     * @date: 2023/12/29 15:32
     * @return: void
     */
    public static void addAllEnemyTankNum() {
        Recorder.allEnemyTankNum++;
    }
    
    /**
     * @description: 读取recordFile, 恢复存档信息
     * @author: yun
     * @date: 2023/12/30 9:25
     * @return: java.util.Vector<com.yun.Node>
     */
    public static Vector<Node> getNodesAndEnemyTankRec() {
        try {
            String len = "";
            bufferedReader = new BufferedReader(new FileReader(recordFile));
            // 读取击毁敌方坦克数量
            allEnemyTankNum = Integer.parseInt(bufferedReader.readLine());
            // 读取坦克的坐标和方向
            while ((len = bufferedReader.readLine()) != null) {
                String[] nodeSpilt = len.split(" ");
                Node node = new Node(Integer.parseInt(nodeSpilt[0]), Integer.parseInt(nodeSpilt[1]), Integer.parseInt(nodeSpilt[2]));
                nodes.add(node);
            }
        } catch (IOException e) {
            System.out.println("recordFile 读取存档失败");
            throw new RuntimeException(e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                System.out.println("bufferedReader 关闭失败");
                throw new RuntimeException(e);
            }
        }
        return nodes;
    }
}

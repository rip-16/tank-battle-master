package com.yun;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.io.File;

/**
 * 功能描述
 *
 * @description: 音乐播放
 * @author: yun
 * @date: 2023年12月30日 15:35
 */
public class AePlayMusic extends Thread {
    private String fileName;
    
    public AePlayMusic(String fileName) {
        this.fileName = fileName;
    }
    
    @Override
    public void run() {
        File file = new File(fileName);
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        AudioFormat format = audioInputStream.getFormat();
        SourceDataLine auline = null;
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);
        try {
            auline = (SourceDataLine) AudioSystem.getLine(info);
            auline.open(format);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        auline.start();
        int nBytesRead = 0;
        // 这是缓冲
        byte[] abData = new byte[512];
        try {
            while (nBytesRead != -1) {
                nBytesRead = audioInputStream.read(abData, 0, abData.length);
                if (nBytesRead >= 0)
                    auline.write(abData, 0, nBytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            auline.drain();
            auline.close();
        }
    }
}

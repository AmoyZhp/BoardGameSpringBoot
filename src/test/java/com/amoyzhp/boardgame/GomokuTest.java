package com.amoyzhp.boardgame;

import java.io.*;
import java.util.Random;

/**
 * test
 *
 * @Author: Tuseday Boy
 * @CreatedDate: 2020/05/02
 */
public class GomokuTest {

    public static int[][][] readZorbistHashBoard(String filename) throws IOException{
        BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
        int[][][] zobristBoad = new int[15][15][3];
        for(int i = 0 ; i < 15; i++){
            for(int j = 0; j < 15; j++){
                for(int k = 0; k < 3; k++){
                    zobristBoad[i][j][k] = Integer.valueOf(br.readLine());
                }
            }
        }
        return zobristBoad;
    }

    public static void main(String[] args) {
        //写入中文字符时解决中文乱码问题
        try {
            Random random = new Random();
            FileOutputStream fos=new FileOutputStream(new File("./data/zorbist_hash_borad.data"));
            OutputStreamWriter osw=new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw=new BufferedWriter(osw);
            int[][][] zobristBoad = new int[15][15][3];
            for(int i = 0 ; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    for(int k = 0; k < 3; k++){
                        zobristBoad[i][j][k] = random.nextInt();
                        bw.write(zobristBoad[i][j][k] + "\n");
                    }
                }

            }
            bw.close();
            osw.close();
            fos.close();

            int[][][] temp = readZorbistHashBoard("./data/zorbist_hash_borad.data");
            for(int i = 0 ; i < 15; i++){
                for(int j = 0; j < 15; j++){
                    for(int k = 0; k < 3; k++){
                        if(zobristBoad[i][j][k] != temp[i][j][k]){
                            System.out.println("error " + zobristBoad[i][j][k] + " " + temp[i][j][k] + " ");
                        }
                    }
                }

            }

        } catch (Exception e){

        }
    }
}

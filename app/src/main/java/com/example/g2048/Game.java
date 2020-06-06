package com.example.g2048;

import java.util.Random;

public class Game {
    private boolean isGameOver = false;//游戏结束标志位
    private Random mRandom = new Random();
    private int score;//分数
    private int preScore;//存放当前状态之前的成绩
    private int countOfCreateBlank;//生成的个数
    private int[] groupOfCreateBlank = new int[2];//用来保存生成的是2还是4的小数组
    private int[][] numMap = new int[4][4];//二维数组存放生成的数字
    private int[][] preNumMap = new int[4][4];//存放当前状态之前的数字分布
    private int[] containerOfNums = new int[4];
    private Coordinate[] groupOfCoord = new Coordinate[2];//存放坐标的小数组

    //设定一个坐标的内部类
    public class Coordinate{
        public int x;
        public int y;
        public Coordinate(){x = 0; y = 0;}
        public Coordinate(int ix,int iy){this();x = ix;y = iy;}
    }

    public int[][] getNumMap() {
        return numMap;
    }

    public int getScore() {
        return score;
    }

    public boolean getIsGameOver() {
        return isGameOver;
    }

    public Game(){
        if (mRandom.nextBoolean()){
            countOfCreateBlank = 2;
        } else {
            countOfCreateBlank = 1;
        }
        for (int i = 0;i <countOfCreateBlank;i++){
            if (mRandom.nextDouble() >0.7){
                groupOfCreateBlank[i]  = 4;
            }else {
                groupOfCreateBlank[i] = 2;
            }
            //在界面中还有位置可以添加数字的情况下，填入数字
            //首先是获取可用的位置块
            groupOfCoord[i] = getAvailableCoord();
            //把值保存了
            numMap[groupOfCoord[i].x][groupOfCoord[i].y] = groupOfCreateBlank[i];
            saveState();//备份当前状态以备goback键使用

        }
    }

    private Coordinate getAvailableCoord(){
        //前面有一个判断调用，使用cannotMove,为true则输出游戏结束
        if (cannotMove()){
            //这里有一个处理输出游戏结束的字段
            throw new GameOverException("游戏结束");
        }
        Coordinate tmp = new Coordinate();
        do{
            tmp.x = mRandom.nextInt(4);
            tmp.y = mRandom.nextInt(4);
        }while (!isAvailableCoord(tmp));//不可填数即循环随机
        return tmp;//返回可用的位置坐标
    }
    //当格子为空可以填数，返回true
    private boolean isAvailableCoord(Coordinate coordinate){
        if(numMap[coordinate.x][coordinate.y] == 0){
            return true;
        }else {
            return false;
        }
    }
    //检查有无移动空间（默认为无）
    private boolean cannotMove(){
        for(int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                int num = numMap[i][j];
                if (num == 0){ return false; }//为0代表空
                //上下左右有移动空间
                if (i - 1 >= 0 && num == numMap[i - 1][j]){return false;}
                if (i + 1 < 4 && num == numMap[i + 1][j]){return false;}
                if (j - 1 >= 0 && num == numMap[i][j - 1]){return false;}
                if (j + 1 < 4 && num == numMap[i][j + 1]){return  false;}
            }
        }
        isGameOver = true;
        return true;
    }
    //回到上一步
    public void goback(){
        isGameOver = false;
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                numMap[i][j] = preNumMap[i][j];
            }
        }
        score = preScore;
    }
    //重开一局
    public void restart(){
        isGameOver = false;
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                numMap[i][j] = 0;
            }
        }
        score = 0;
        update();
        saveState();
    }
    //每次刷新屏幕的函数
    private void update(){
        int num = getNumOfAvailableBlank();
        if (num > 1){
            if (mRandom.nextBoolean()){
                countOfCreateBlank = 2;
            }else {
                countOfCreateBlank = 1;
            }
        }else if (num == 1){
            countOfCreateBlank = 1;
        }else if (!cannotMove()){
            countOfCreateBlank = 0;//?
        }
        //以下代码与Game的构造函数里的一样
        for (int i = 0; i < countOfCreateBlank;i++){
            if (mRandom.nextDouble() >0.7){
                groupOfCreateBlank[i]  = 4;
            }else {
                groupOfCreateBlank[i] = 2;
            }
            //在界面中还有位置可以添加数字的情况下，填入数字
            //首先是获取可用的位置块
            groupOfCoord[i] = getAvailableCoord();
            //把值保存了
            numMap[groupOfCoord[i].x][groupOfCoord[i].y] = groupOfCreateBlank[i];
        }
    }
    //获取空格子的个数
    private int getNumOfAvailableBlank(){
        int num = 0;
        for (int elements[] : numMap){
            for (int element : elements){
                if (element == 0){num++;}
            }
        }
        return num;
    }
    //移动之前预先保存数字状态以备goback键使用的函数
    private void saveState(){
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                preNumMap[i][j] = numMap[i][j];
            }
        }
        preScore = getScore();
    }
    //移动都会进行的操作函数
    private boolean move(){
        boolean isMove = false;//是否需要移动的标志位
        int flag = 0;
        //下面的循环依靠flag把containerOfNums中的0移动到了后面，方便了下一步合并
        for(int i = 3;i > 0;i--){
            if (containerOfNums[i] != 0 && containerOfNums[i - 1] == 0){
                containerOfNums[i - 1] = containerOfNums[i];
                containerOfNums[i] = 0;
                for (int j = i;flag > 0;j++){
                    containerOfNums[j] = containerOfNums[j + 1];
                    containerOfNums[j + 1] = 0;
                    flag--;
                }
            }else if (containerOfNums[i] != 0 && containerOfNums[i - 1] != 0){
                flag++;
            }
        }
        if (containerOfNums[0] == 0){return isMove;}//在0都移到数组后面的情况下第一位依旧是0，直接返回无需移动
        //合并相同的项
        for (int i = 0; i < 3; i++){
            if (containerOfNums[i] == containerOfNums[i + 1]){
                isMove = true;
                containerOfNums[i] *= 2;
                //合并一次非零项计分一次
                if (containerOfNums[i] != 0){
                    score += 2;
                }
                //把没合并的数向前顶
                for (int j = i + 1;j < 3;j++){
                    containerOfNums[j] = containerOfNums[j + 1];
                    containerOfNums[j + 1] = 0;
                }
            }
        }
        //数组里的四个数都一样的情况下，合并之后第一与第二个数相等需要进一步合并
        if (containerOfNums[0] == containerOfNums[1]){
            isMove = true;
            containerOfNums[0] *= 2;
            if (containerOfNums[0] != 0){
                score += 2;
            }
            containerOfNums[1] = 0;
        }
        return isMove;
    }
    //上移
    public void pressUp(){
        boolean flag = false;//负责判断是否刷新界面
        saveState();//每次滑动的时候都要把滑动前的状态备份
        for (int i = 0;i < 4;i++){
            for (int j = 0;j < 4;j++){
                containerOfNums[j] = numMap[j][i];
            }
            if (!flag){
                flag = move();
            }else {
                move();
            }
            for (int j = 0;j < 4;j++){
                numMap[j][i] = containerOfNums[j];
            }
        }
        if (flag){
            update();
        }else if (cannotMove()){
            //这里有一个处理输出游戏结束的字段
            throw new GameOverException("游戏结束");
        }
    }
    //下移
    public void pressDown(){
        boolean flag = false;
        saveState();
        for (int i = 0;i < 4;i++){
            for (int j = 0; j < 4;j++){
                containerOfNums[3-j] = numMap[j][i];
            }
            if (!flag){
                flag = move();
            }else {
                move();
            }
            for (int j = 0;j < 4;j++){
                numMap[j][i] = containerOfNums[3-j];
            }
        }
        if (flag){
            update();
        }else if (cannotMove()){
            //还是游戏结束。。
            throw new GameOverException("游戏结束");
        }
    }
    //左移
    public void pressLeft(){
        boolean flag = false;
        saveState();
        for (int i = 0; i < 4;i++){
            for (int j = 0;j < 4;j++){
                containerOfNums[j] = numMap[i][j];
            }
            if (!flag){
                flag = move();
            }else {
                move();
            }
            for (int j = 0; j < 4; j++){
                numMap[i][j] = containerOfNums[j];
            }
        }
        if (flag){
            update();
        }else if (cannotMove()){
            //还是游戏结束。。
            throw new GameOverException("游戏结束");
        }
    }
    //右移
    public void pressRight(){
        boolean flag = false;
        saveState();
        for (int i = 0; i < 4;i++){
            for (int j = 0;j < 4;j++){
                containerOfNums[3 - j] = numMap[i][j];
            }
            if (!flag){
                flag = move();
            }else {
                move();
            }
            for (int j = 0;j < 4;j++){
                numMap[i][j] = containerOfNums[3 - j];
            }
        }
        if (flag){
            update();
        }else if (cannotMove()){
            //还是游戏结束。。
            throw new GameOverException("游戏结束");
        }
    }
}

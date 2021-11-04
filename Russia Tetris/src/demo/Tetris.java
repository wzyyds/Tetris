package demo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Tetris extends JPanel {
    //声明当前正在下落的方块
    private Tetromino currentOne = Tetromino.randomOne();
    //声明下一个下落的方块
    private Tetromino nextOne = Tetromino.randomOne();
    //声明游戏主区域
    private Cell[][] wall = new Cell[16][9];
    //声明每个单元格像素
    private static final int CELL_SIZE = 40;
    int []score_cool={0,1,2,5,10};

    private int totalScore=0;
    private int totalLine=0;
    //声明游戏的三种状态
    public static final int PLAYING=0;
    public static final int PAUSE=1;
    public static final int GAMEOVER=2;
    private int game_state=0;
    //声明一个字符串数组，用来显示当前游戏的三种状态
    String []show_state={"P[暂停]","C[继续]","R[重玩]"};
    public static BufferedImage I;
    public static BufferedImage J;
    public static BufferedImage L;
    public static BufferedImage O;
    public static BufferedImage S;
    public static BufferedImage T;
    public static BufferedImage Z;
    public static BufferedImage backImage;

    public static void main(String[] args) {
        JFrame frame = new JFrame("俄罗斯方块");
        //创建游戏界面，也就是面板
        Tetris panel = new Tetris();
        //将面板嵌入到窗口中
        frame.add(panel);
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setSize(700, 799);
        frame.setLocation(200,0);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //调用start方法
        panel.start();
    }
    public void start(){
        game_state=PLAYING;
        KeyListener l=new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code=e.getKeyCode();
                switch (code){
                    case KeyEvent.VK_DOWN:
                        softDropAction();
                        break;
                    case KeyEvent.VK_RIGHT:
                        moveRightAction();
                        break;
                    case KeyEvent.VK_LEFT:
                        moveLeftAction();
                        break;
                    case KeyEvent.VK_1:
                        rotateRightAction();
                        break;
                    case KeyEvent.VK_2:
                        rotateLeftAction();
                        break;
                    case KeyEvent.VK_SPACE:
                        handDropAction();
                        break;
                    case KeyEvent.VK_P:
                        if(game_state==PLAYING){
                            game_state=PAUSE;
                        }
                        break;
                    case KeyEvent.VK_C:
                        if(game_state==PAUSE){
                            game_state=PLAYING;
                        }
                        break;
                    case KeyEvent.VK_R:
                        game_state=PLAYING;
                        wall=new Cell[16][9];
                        currentOne=Tetromino.randomOne();
                        nextOne=Tetromino.randomOne();
                        totalScore=0;
                        totalLine=0;
                        break;
                }
            }
        };
        //将俄罗斯方块窗口设置为焦点
        this.addKeyListener(l);
        this.requestFocus();
        while (true){
            //判断游戏状态是否在游戏中，若true则每隔0.5秒下落一格
            if(game_state==PLAYING){
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(canDrop()){
                    currentOne.moveDrop();
                }else {
                    landToWall();
                    destroyLine();
                    if(isGameOver()){
                        game_state=GAMEOVER;
                    }else {
                        currentOne=nextOne;
                        nextOne=Tetromino.randomOne();
                    }
                }
            }
            repaint();
        }
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(backImage, 0, 0, null);//画背景
        //绘制游戏主区域
        g.translate(22,15);//偏移

        paintWall(g);//画小方块
        paintCurrentOne(g);//画当前方块
        paintNextOne(g);//画下一个方块
        paintScore(g);//画得分
        paintState(g);//画游戏状态
    }
    //画游戏状态
    private void paintState(Graphics g) {
        g.drawString(show_state[game_state],400,540);
        if(game_state==GAMEOVER){
            g.setColor(Color.red);
            g.setFont(new Font(Font.DIALOG,Font.CENTER_BASELINE,50));
            g.drawString("GAMEOVER!",50,200);

        }
    }
    //将分数画在屏幕内
    private void paintScore(Graphics g) {
        g.setFont(new Font(Font.DIALOG,Font.CENTER_BASELINE,30));
        g.setColor(Color.yellow);
        g.drawString("得分:"+totalScore,400,240);
        g.drawString("行数:"+totalLine,400,390);
    }
    //画下一个方块
    private void paintNextOne(Graphics g) {
        g.setFont(new Font(Font.DIALOG,Font.CENTER_BASELINE,30));
        g.setColor(Color.yellow);
        g.drawString("下一个方块:",400,40);
        Cell[] cells=nextOne.cells;
        for (Cell cell : cells) {
            int x=cell.getCol()*CELL_SIZE+300;
            int y= cell.getRow()*CELL_SIZE+70;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }
    //画当前正在移动的方块
    private void paintCurrentOne(Graphics g){
        Cell[] cells= currentOne.cells;
        for (Cell cell : cells) {
            int x=cell.getCol()*CELL_SIZE;
            int y= cell.getRow()*CELL_SIZE;
            g.drawImage(cell.getImage(),x,y,null);
        }
    }
    //画单元格中的小方格
    private void paintWall(Graphics g) {
        for (int i = 0; i < wall.length; i++) {
            for (int j = 0; j < wall[i].length; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;
                Cell cell = wall[i][j];
                //判断当前单元格是否有小方块，如果没有则绘制图形，否则将小方块嵌入到墙中
                if (cell == null) {
                    g.drawRect(x, y, CELL_SIZE, CELL_SIZE);
                } else {
                    g.drawImage(cell.getImage(), x, y, null);
                }
            }
        }
    }
    //静态代码块,加载图片
    static {
        try {
            I = ImageIO.read(new File("images/I.png"));
            J = ImageIO.read(new File("images/J.png"));
            L = ImageIO.read(new File("images/L.png"));
            O = ImageIO.read(new File("images/O.png"));
            S = ImageIO.read(new File("images/S.png"));
            T = ImageIO.read(new File("images/T.png"));
            Z = ImageIO.read(new File("images/Z.png"));
            backImage = ImageIO.read(new File("images/backImage.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    //判断方块是否越界
    public boolean outOfBounds(){
        Cell []cells= currentOne.cells;
        for (Cell cell : cells) {
            int col=cell.getCol();
            int row=cell.getRow();
            if(row<0 || row> wall.length-1 ||col<0 || col>wall[0].length-1){
                return true;
            }
        }
        return false;
    }
    //判断方块是否重合
    public boolean coincide(){
        Cell []cells= currentOne.cells;
        for (Cell cell : cells) {
            int row= cell.getRow();
            int col=cell.getCol();
            if(wall[row][col]!=null){
                return true;
            }
        }
        return false;
    }
    //按键一次四方格左移一次
    public void moveLeftAction(){
        currentOne.moveLeft();
        if(outOfBounds()||coincide()){
            currentOne.moveRight();
        }
    }
    //按键一次四方格右移一次
    public void moveRightAction(){
        if(!outOfBounds() && !coincide()){
            currentOne.moveRight();
        }
    }
    //判断游戏是否结束
    public boolean isGameOver(){
        Cell []cells=nextOne.cells;
        for (Cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            if(wall[row][col]!=null){
                return true;
            }
        }
        return false;
    }
    //判断当前行是否已满
    public boolean isFullLine(int row){
        Cell[] cells=wall[row];
        for (Cell cell : cells) {
            if(cell==null){
                return false;
            }
        }
        return true;
    }
    //消行
    public void destroyLine(){
        int line=0;
        Cell []cells=currentOne.cells;
        for (Cell cell : cells) {
            int row=cell.getRow();
            if(isFullLine(row)){
                line++;
                for(int i=row;i>0;i--){
                    System.arraycopy(wall[i-1],0,wall[i],0,wall[0].length);
                }
                wall[0]=new Cell[9];
            }
        }
        totalScore+=score_cool[line];
        totalLine+=line;
    }
    //判断四方格能否下落
    public boolean canDrop(){
        Cell []cells= currentOne.cells;
        for (Cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            if(row == wall.length-1){
                return false;
            }else if(wall[row+1][col] != null && row<wall.length){
                return false;
            }
        }
        return true;
    }
    //四方格下移
    public void softDropAction(){
        if(canDrop()){
            //四方格下落一格
            currentOne.moveDrop();
        }else{
            //将四方格嵌入到墙中
            
            landToWall();
            destroyLine();
            if(isGameOver()){
                game_state=GAMEOVER;
            }else {
                //若游戏没有结束，则生成新的四方格
                currentOne=nextOne;
                nextOne=Tetromino.randomOne();
            }
        }
    }
    //将四方格嵌入到墙中
    private void landToWall() {
        Cell []cells= currentOne.cells;
        for (Cell cell : cells) {
            int row=cell.getRow();
            int col=cell.getCol();
            wall[row][col]=cell;
        }
    }
    //瞬间下落
    public void handDropAction(){

        while(true){
            //判断四方格能否下落
            if(canDrop()){
                currentOne.moveDrop();
            }else{
                break;
            }
        }
        landToWall();
        destroyLine();
        if(isGameOver()){
            game_state=GAMEOVER;
        }else {
            currentOne=nextOne;
            nextOne=Tetromino.randomOne();
        }
    }
    //四方块顺时针旋转
    public void rotateRightAction(){
        if(!outOfBounds() && !coincide() ){
            currentOne.rotateRight();
        }
    }
    //四方块逆时针旋转
    public void rotateLeftAction(){
        if(!outOfBounds() && !coincide() ){
            currentOne.rotateLeft();
        }
    }

}

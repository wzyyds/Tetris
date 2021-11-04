package demo;
/*
* 编写四方格父类
* 属性：有四个小方格
* 方法：整体左移、整体右移、整体下移
*/
public class Tetromino {
    protected Cell []cells=new Cell[4];
    //编写旋转状态
    protected State[] states;
    //声明旋转次数
    protected int times=1000;
    //编写四方格旋转状态的内部类
    class State{
        int row0,col0,row1,col1,row2,col2,row3,col3;

        public State() {
        }

        public State(int row0, int col0, int row1, int col1, int row2, int col2, int row3, int col3) {
            this.row0 = row0;
            this.col0 = col0;
            this.row1 = row1;
            this.col1 = col1;
            this.row2 = row2;
            this.col2 = col2;
            this.row3 = row3;
            this.col3 = col3;
        }

        @Override
        public String toString() {
            return "State{" +
                    "row0=" + row0 +
                    ", col0=" + col0 +
                    ", row1=" + row1 +
                    ", col1=" + col1 +
                    ", row2=" + row2 +
                    ", col2=" + col2 +
                    ", row3=" + row3 +
                    ", col3=" + col3 +
                    '}';
        }

        public int getRow0() {
            return row0;
        }

        public void setRow0(int row0) {
            this.row0 = row0;
        }

        public int getCol0() {
            return col0;
        }

        public void setCol0(int col0) {
            this.col0 = col0;
        }

        public int getRow1() {
            return row1;
        }

        public void setRow1(int row1) {
            this.row1 = row1;
        }

        public int getCol1() {
            return col1;
        }

        public void setCol1(int col1) {
            this.col1 = col1;
        }

        public int getRow2() {
            return row2;
        }

        public void setRow2(int row2) {
            this.row2 = row2;
        }

        public int getCol2() {
            return col2;
        }

        public void setCol2(int col2) {
            this.col2 = col2;
        }

        public int getRow3() {
            return row3;
        }

        public void setRow3(int row3) {
            this.row3 = row3;
        }

        public int getCol3() {
            return col3;
        }

        public void setCol3(int col3) {
            this.col3 = col3;
        }
    }
    public void moveLeft(){
        for (Cell cell : cells) {
            cell.left();
        }
    }
    public void moveRight(){
        for (Cell cell : cells) {
            cell.right();
        }
    }
    public void moveDrop(){
        for (Cell cell : cells) {
            cell.drop();
        }
    }
    //随机生成一个四方格
    public static Tetromino randomOne(){
        int num=(int)(Math.random() *7);
        Tetromino t=null;
        switch(num){
            case 0:
                t=new I();
                break;
            case 1:
                t=new J();
                break;
            case 2:
                t=new L();
                break;
            case 3:
                t=new O();
                break;
            case 4:
                t=new S();
                break;
            case 5:
                t=new T();
                break;
            case 6:
                t=new Z();
                break;
        }
        return t;
    }
    //编写顺时针旋转四方格方法
    public void rotateRight(){
        if(states.length==0){
            return;
        }
        times++;
        State s=states[times % states.length];
        Cell cell=cells[0];
        int row=cell.getRow();
        int col=cell.getCol();
        cells[1].setRow(row+s.row1);
        cells[1].setCol(col+s.col1);
        cells[2].setRow(row+s.row2);
        cells[2].setCol(col+s.col2);
        cells[3].setRow(row+s.row3);
        cells[3].setCol(col+s.col3);

    }
    //编写逆时针旋转四方格方法
    public void rotateLeft(){
        times--;
        State s=states[times % states.length];
        Cell cell=cells[0];
        int row=cell.getRow();
        int col=cell.getCol();
        cells[1].setRow(row+s.row1);
        cells[1].setCol(col+s.col1);
        cells[2].setRow(row+s.row2);
        cells[2].setCol(col+s.col2);
        cells[3].setRow(row+s.row3);
        cells[3].setCol(col+s.col3);
    }

}

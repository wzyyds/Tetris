package demo;

public class O extends Tetromino{
    public O(){
        cells[0]=new Cell(0,4,Tetris.J);
        cells[1]=new Cell(0,5,Tetris.J);
        cells[2]=new Cell(1,4,Tetris.J);
        cells[3]=new Cell(1,5,Tetris.J);
        //无法旋转
        states=new State[0];
    }
}

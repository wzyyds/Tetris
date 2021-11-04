package demo;

import java.awt.image.BufferedImage;
import java.util.Objects;
/*
编写小方格类
属性：行，列，图片
方法：左移，右移，下移

*/
public class Cell {
    private int row;
    private int col;
    private BufferedImage image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return row == cell.row && col == cell.col && image.equals(cell.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, image);
    }

    @Override
    public String toString() {
        return "Cell{" +
                "row=" + row +
                ", col=" + col +
                ", image=" + image +
                '}';
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public BufferedImage getImage() {
        return image;
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public Cell() {
    }

    public Cell(int row, int col, BufferedImage image) {
        this.row = row;
        this.col = col;
        this.image = image;
    }
    public void drop(){
        row++;
    }
    public void left(){
        col--;
    }
    public void right(){
        col++;
    }

}

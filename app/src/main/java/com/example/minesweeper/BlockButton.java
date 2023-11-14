package com.example.minesweeper;

import android.content.Context;
import android.widget.TableRow;
import androidx.appcompat.widget.AppCompatButton;

public class BlockButton extends AppCompatButton {
    private int x;
    private int y;
    private boolean mine;
    private boolean flag;
    private int neighborMines;

    public static int flags=10;
    public static int blocks=81;

    public int getBX() {
        return x;
    }
    public int getBY() {
        return y;
    }

    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.mine = false;
        this.flag = false;
        this.neighborMines = 0;
        setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
    }

    public boolean isMine(){
        return mine;
    }
    public void setMine(boolean mine){
        this.mine =mine;
    }
    public boolean isFlag(){
        return flag;
    }

    public void toggleFlag() {
        flag = !flag;
        if (flag) {
            flags++;
            setText("");
        } else {
            if(flags>0){
                flags--;
                setText("F");
            }
        }
    }

    public int getNeighborMines(){
        return neighborMines;
    }
    public void setNeighborMines(int neighborMines){
        this.neighborMines = neighborMines;
    }
    public boolean breakBlock(){
        setEnabled(false);
        blocks--;
        if(mine){
            setText("M");
            return true;
        }else{
            setText(String.valueOf(neighborMines));
            return false;
        }
    }
}

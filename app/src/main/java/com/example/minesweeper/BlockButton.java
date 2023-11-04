package com.example.minesweeper;

import android.content.Context;
import android.widget.Button;
import android.widget.TableRow;

public class BlockButton extends Button {

    private int x;
    private int y;
    private boolean mine;
    private boolean flag;
    private int neighborMines;

    public static int flags=0;
    public static int blocks=0;
    public BlockButton(Context context, int x, int y) {
        super(context);
        this.x = x;
        this.y = y;
        this.mine = false;
        this.flag = false;
        this.neighborMines = 0;
        flags = 0;
        blocks = 0;
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
        this.mine = mine;
    }
    public boolean isFlag(){
        return flag;
    }

    public void toggleFlag() {
        flag = !flag;
        if (flag) {
            flags++;
        } else {
            flags--;
        }
    }

    public int getNeighborMines(){
        return neighborMines;
    }
    public void setNeighborMines(int neighborMines){
        this.neighborMines = neighborMines;
    }
    public boolean breakBlock(){
        setClickable(false);

        blocks--;
        if(mine){
            setText("M");
            return true;
        }else{
            if (neighborMines>0){
                setText(String.valueOf(neighborMines));
            }else{
                
            }
        }
    }
}

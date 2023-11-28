package com.example.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.widget.TableRow;
import androidx.appcompat.widget.AppCompatButton;

public class BlockButton extends AppCompatButton {
    private int x;
    private int y;
    private boolean mine;
    private boolean flag;
    private int neighborMines;

    public static int flags;
    public static int blocks;

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
        setEnabled(true);
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
    public boolean isFlag(){return flag;}
    public void toggleFlag() {
        flag = !flag;
        if (flag) {
            flags--;
            setText("F");
        } else {
            flags++;
            setText("");
        }
    }

    public int getNeighborMines(){
        return neighborMines;
    }
    public void setNeighborMines(int neighborMines){
        this.neighborMines = neighborMines;
    }
    public boolean breakBlock(){
        setColor(this);
        setEnabled(false);
        blocks++;
        if(mine){
            setText("M");
        }else{
            setText(String.valueOf(neighborMines));
        }
        return true;
    }
    void setColor(BlockButton btn){
        switch (btn.getNeighborMines()){
            case 1: btn.setTextColor(Color.RED);
                break;
            case 2: btn.setTextColor(Color.rgb(255,165,0));
                break;
            case 3: btn.setTextColor(Color.GREEN);
                break;
            case 4: btn.setTextColor(Color.BLUE);
                break;
            case 5: btn.setTextColor(Color.YELLOW);
                break;
            case 6: btn.setTextColor(Color.rgb(0,0,128));
                break;
            case 7: btn.setTextColor(Color.rgb(128,0,128));
                break;
        }
    }
}

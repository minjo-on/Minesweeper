package com.example.minesweeper;

import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class MineSweeperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_sweeper);

        TableLayout table;
        TextView mines;
        ToggleButton tb;
        table = (TableLayout)findViewById(R.id.tableLayout);
        mines = (TextView)findViewById(R.id.mines);
        //tb = (ToggleButton)findViewById(R.id.)
        BlockButton[][] buttons = new BlockButton[9][9];

        for(int i=0;i<9;i++){
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f);
            for(int j=0;j<9;j++){
                buttons[i][j] = new BlockButton(this,i,j);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(true){//우클릭
                            ((BlockButton) view).toggleFlag();
                            mines.setText(10-BlockButton.flags+"");
                        }else{//좌클릭
                            if(((BlockButton)view).isMine()){//클릭 했는데 지뢰일 때
                                for(int i=0;i<buttons.length;i++){
                                    for(int j=0;j< buttons[i].length;j++){
                                        if(buttons[i][j].isMine()){
                                            buttons[i][j].setText('M');
                                            buttons[i][j].setEnabled(false);
                                        }else{
                                            buttons[i][j].setText(buttons[i][j].getNeighborMines()+"");
                                            buttons[i][j].setEnabled(false);
                                        }
                                    }
                                }
                            }else{
                                if(buttons[((BlockButton) view).getBX()][((BlockButton)view).getBY()].getNeighborMines()==0){
                                    uncoverNeighbors(((BlockButton) view).getBX(),((BlockButton) view).getBY(),view);
                                }else{
                                    buttons[((BlockButton) view).getBX()][((BlockButton)view).getBY()].breakBlock();
                                }
                            }
                        }
                    }
                    void uncoverNeighbors(int x, int y, View view){
                        boolean isOutOfBounds = (x<0||y<0||x>8||y>8);
                        if(isOutOfBounds) {
                            return;
                        }else{
                            boolean buttonEnabled = buttons[x][y].isEnabled()==false;
                            boolean isNearMine = buttons[x][y].getNeighborMines()>0;
                            if(buttonEnabled || isNearMine){
                                return;
                            }else{
                                buttons[x][y].breakBlock();
                                if (buttons[x][y].getNeighborMines()==0){
                                    uncoverNeighbors(x+1,y,view);
                                    uncoverNeighbors(x-1,y,view);
                                    uncoverNeighbors(x,y+1,view);
                                    uncoverNeighbors(x,y-1,view);
                                    uncoverNeighbors(x-1,y+1,view);
                                    uncoverNeighbors(x-1,y-1,view);
                                    uncoverNeighbors(x+1,y+1,view);
                                    uncoverNeighbors(x+1,y-1,view);
                                }
                            }
                        }
                    }
                });
            }
            table.addView(tableRow);
        }
        int mine = 0;
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
        while(mine<10){
            int i = (int)(Math.random()*9);
            int j = (int)(Math.random()*9);
            if(buttons[i][j].isMine()){
                continue;
            }else{
                //마인 설정
                buttons[i][j].setMine(true);
                //주변 이웃마인 추가
                for(int k=0;k<8;k++){
                    if(i+dx[k]<0 || j+dy[k]<0 || i+dx[k]>8||j+dy[k]>8){//마인이면 그냥 생략
                        continue;
                    }else{//마인이 아니면 이웃 마인 +1
                        buttons[i][j].setNeighborMines(buttons[i][j].getNeighborMines()+1);
                    }
                }
                mine++;
            }
        }
    }
}

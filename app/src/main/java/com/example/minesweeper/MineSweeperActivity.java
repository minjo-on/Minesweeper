package com.example.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MineSweeperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_sweeper);

        TableLayout table;
        TextView mines;
        ToggleButton breakButton;
        table = (TableLayout)findViewById(R.id.tableLayout);
        mines = (TextView)findViewById(R.id.mines);
        breakButton = (ToggleButton)findViewById(R.id.breakButton);
        BlockButton[][] buttons = new BlockButton[9][9];

        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dlg = new AlertDialog.Builder(MineSweeperActivity.this);
                dlg.setTitle("Pause"); //제목
                dlg.setMessage("중단"); // 메시지
                dlg.setPositiveButton("홈으로", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
                dlg.setNeutralButton("재개하기",null);
                dlg.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish(); // 현재 액티비티 종료
                        startActivity(getIntent());
                    }
                });
                dlg.show();
            }
        });


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

                //길게 터치시 플래그
                buttons[i][j].setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        ((BlockButton) view).toggleFlag();
                        mines.setText(BlockButton.flags+"");
                        return true;
                    }
                });

                //일반 클릭시 버튼 open
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
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

                        if (BlockButton.flags == 0 && BlockButton.blocks == 10) {
                            AlertDialog.Builder dlg = new AlertDialog.Builder(MineSweeperActivity.this);
                            dlg.setTitle("You Win"); //제목
                            dlg.setMessage("지뢰를 모두 발견했습니다."); // 메시지
                            dlg.setPositiveButton("홈으로", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), MineSweeperActivity.class);
                                    startActivity(intent);
                                }
                            });
                            dlg.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    finish(); // 현재 액티비티 종료
                                    startActivity(getIntent());
                                }
                            });
                            dlg.show();
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
            if(buttons[i][j].isMine()==false){
                buttons[i][j].setMine(true);
                //주변 이웃마인 설정
                for(int k=0;k<8;k++){
                    if(i+dx[k]>=0 && j+dy[k]>=0 && i+dx[k]<9&&j+dy[k]<9){
                        buttons[i+dx[k]][j+dy[k]].setNeighborMines(buttons[i+dx[k]][j+dy[k]].getNeighborMines()+1);
                    }
                }
                mine++;
            }
        }
    }
}

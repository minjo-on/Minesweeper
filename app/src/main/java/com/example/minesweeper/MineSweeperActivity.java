package com.example.minesweeper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MineSweeperActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_sweeper);

        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        TableLayout table;
        TextView mines;
        ToggleButton breakButton;
        table = (TableLayout)findViewById(R.id.tableLayout);
        mines = (TextView)findViewById(R.id.mines);
        breakButton = (ToggleButton)findViewById(R.id.breakButton);
        BlockButton[][] buttons = new BlockButton[9][9];

        BlockButton.flags = 10;
        BlockButton.blocks = 81;
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(breakButton.isEnabled());
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
                        recreate();
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
                        System.out.println(((BlockButton) view).isFlag()+" "+BlockButton.flags);
                        if(((BlockButton) view).isFlag() || BlockButton.flags>0){
                            ((BlockButton) view).toggleFlag();
                            mines.setText(BlockButton.flags+"");
                        }else{
                            AlertDialog.Builder flagWarning = new AlertDialog.Builder(MineSweeperActivity.this);
                            flagWarning.setTitle("Warning");
                            flagWarning.setMessage("최대 깃발 개수는 10개 입니다.");
                            flagWarning.setPositiveButton("확인",null);
                            flagWarning.show();
                        }
                        return true;
                    }
                });

                //일반 클릭시 버튼 open
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(((BlockButton)view).isMine()){//클릭 했는데 지뢰일 때
                            AlertDialog.Builder mineAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                            mineAlertDialog.setTitle("GameOver"); //제목
                            mineAlertDialog.setMessage("게임 종료"); // 메시지
                            mineAlertDialog.setPositiveButton("홈으로", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            mineAlertDialog.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recreate();
                                }
                            });
                            mineAlertDialog.show();

                            for(int i=0;i<buttons.length;i++){
                                for(int j=0;j< buttons[i].length;j++){
                                    if(buttons[i][j].isMine()){
                                        buttons[i][j].setText("M");
                                        buttons[i][j].setEnabled(false);
                                    }else{
                                        buttons[i][j].setText(buttons[i][j].getNeighborMines()+"");
                                        buttons[i][j].setEnabled(false);
                                    }
                                }
                            }
                        }else{
                            if(buttons[((BlockButton) view).getBX()][((BlockButton)view).getBY()].getNeighborMines()==0){
                                uncoverNeighbors(((BlockButton) view).getBX(),((BlockButton) view).getBY());
                            }else{
                                buttons[((BlockButton) view).getBX()][((BlockButton)view).getBY()].breakBlock();
                            }
                        }

                        if (BlockButton.flags == 0 && BlockButton.blocks == 10) {
                            AlertDialog.Builder winAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                            winAlertDialog.setTitle("You Win"); //제목
                            winAlertDialog.setMessage("지뢰를 모두 발견했습니다."); // 메시지
                            winAlertDialog.setPositiveButton("홈으로", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(getApplicationContext(), MineSweeperActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            winAlertDialog.setNegativeButton("다시하기", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    recreate();
                                }
                            });
                            winAlertDialog.show();
                        }
                    }
                    void uncoverNeighbors(int i, int j) {
                        boolean a = (i < 0 || i > 8 || j < 0 || j > 8);
                        if (a) {
                            return;
                        } else {
                            boolean b = !buttons[i][j].isEnabled();
                            boolean c = buttons[i][j].getNeighborMines() > 0;
                            if (b || c) {
                                return;
                            } else {
                                if (buttons[i][j].getNeighborMines() == 0) {
                                    buttons[i][j].breakBlock();
                                    for(int k=0;k<7;k++){
                                        if(i+dx[k]>=0 && i+dx[k]<9 && j+dy[k]>=0 &&j+dy[k]<9){
                                            if(buttons[i+dx[k]][j+dy[k]].isEnabled()){
                                                if(buttons[i+dx[k]][j+dy[k]].getNeighborMines()==0){
                                                    uncoverNeighbors(i+dx[k],j+dy[k]);
                                                }else{
                                                    buttons[i+dx[k]][j+dy[k]].breakBlock();
                                                }
                                            }
                                        }

                                    }
//                                    uncoverNeighbors(i + 1, j); //right
//                                    uncoverNeighbors(i, j + 1); //bottom
//                                    uncoverNeighbors(i + 1, j + 1);//rightBottom
//                                    uncoverNeighbors(i - 1, j); //left
//                                    uncoverNeighbors(i, j - 1); //top
//                                    uncoverNeighbors(i - 1, j - 1); //leftTop
//                                    uncoverNeighbors(i + 1, j - 1); //rightTop
//                                    uncoverNeighbors(i - 1, j + 1); //leftBottom
                                }
                            }
                        }
                    }
                });
            }
            table.addView(tableRow);
        }
        int mine = 0;
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

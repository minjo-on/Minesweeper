package com.example.minesweeper;

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

        TableLayout table;
        TextView mines;
        ToggleButton breakButton;
        table = (TableLayout)findViewById(R.id.tableLayout);
        mines = (TextView)findViewById(R.id.mines);
        breakButton = (ToggleButton)findViewById(R.id.breakButton);

        BlockButton[][] buttons = new BlockButton[9][9];
        BlockButton.flags = 10;
        BlockButton.blocks = 81;
        breakButton.setOnClickListener(view -> {
            AlertDialog.Builder dlg = new AlertDialog.Builder(MineSweeperActivity.this);
            dlg.setTitle("Pause"); //제목
            dlg.setMessage("중단"); // 메시지
            dlg.setPositiveButton("홈으로", (dialog, which) -> {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            });
            dlg.setNeutralButton("재개 하기",null);
            dlg.setNegativeButton("다시 하기", (dialogInterface, i) -> recreate());
            dlg.show();
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
                buttons[i][j].setOnLongClickListener(view -> {
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
                });

                //일반 클릭시 버튼 open
                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(((BlockButton)view).isFlag()){

                        }
                        else if(((BlockButton)view).isMine()){//클릭 했는데 지뢰일 때
                            AlertDialog.Builder mineAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                            mineAlertDialog.setTitle("GameOver"); //제목
                            mineAlertDialog.setMessage("게임 종료"); // 메시지
                            mineAlertDialog.setPositiveButton("홈으로", (dialog, which) -> {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            });
                            mineAlertDialog.setNegativeButton("다시 하기", (dialogInterface, i1) -> recreate());
                            mineAlertDialog.show();

                            for(int i=0;i<buttons.length;i++){
                                for(int j=0;j< buttons[i].length;j++){
                                    if(buttons[i][j].isMine()){
                                        buttons[i][j].setText("M");
                                    }else{
                                        buttons[i][j].setText(buttons[i][j].getNeighborMines()+"");
                                    }
                                    buttons[i][j].setEnabled(false);
                                }
                            }
                        }else{
                            BlockButton btn = buttons[((BlockButton) view).getBX()][((BlockButton)view).getBY()];
                            if(btn.getNeighborMines()==0){
                                uncoverNeighbors(btn.getBX(),btn.getBY());
                            }else{
                                btn.breakBlock();
                            }
                        }

                        if (BlockButton.flags == 0 && BlockButton.blocks == 10) {
                            AlertDialog.Builder winAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                            winAlertDialog.setTitle("You Win"); //제목
                            winAlertDialog.setMessage("지뢰를 모두 발견했습니다."); // 메시지
                            winAlertDialog.setPositiveButton("홈으로", (dialog, which) -> {
                                Intent intent = new Intent(getApplicationContext(), MineSweeperActivity.class);
                                startActivity(intent);
                                finish();
                            });
                            winAlertDialog.setNegativeButton("다시 하기", (dialogInterface, i12) -> recreate());
                            winAlertDialog.show();
                        }
                    }

                    void uncoverNeighbors(int i, int j) {
                        boolean range = (i >= 0 && i < 9 && j >= 0 && j < 9);
                        if (range) {
                            boolean click = buttons[i][j].isEnabled();
                            boolean neighbor = buttons[i][j].getNeighborMines() == 0;
                            if (click && neighbor) {
                                buttons[i][j].breakBlock();
                                for (int k = -1; k < 2; k++) {
                                    for(int l = -1; l < 2 ; l++){
                                        if (i + k >= 0 && i + k < 9 && j + l >= 0 && j + l < 9) {
                                            if (buttons[i + k][j + l].isEnabled()&&!buttons[i+k][j+l].isFlag()) {
                                                if (buttons[i + k][j + l].getNeighborMines() == 0) {
                                                    uncoverNeighbors(i + k, j + l);
                                                } else {
                                                    buttons[i + k][j + l].breakBlock();
                                                }
                                            }
                                        }
                                    }
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
            if(!buttons[i][j].isMine()){
                buttons[i][j].setMine(true);
                //주변 마인 설정
                for(int k=-1; k<2;k++){
                    for(int l=-1;l<2;l++){
                        if(i+k>=0 && j+l>=0 && i+k<9&&j+l<9){
                            buttons[i+k][j+l].setNeighborMines(buttons[i+k][j+l].getNeighborMines()+1);
                        }
                    }
                }
                buttons[i][j].setNeighborMines(0);
                mine++;
            }
        }
    }
}

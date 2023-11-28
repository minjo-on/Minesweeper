package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MineSweeperActivity extends AppCompatActivity {

    TableLayout table;
    TextView mines;
    TextView time;
    ToggleButton breakButton;
    Button home;
    Button reset;
    private long elapsedTime = 0;
    String timeElapsed;
    private CountDownTimer countDownTimer;
    private void startTimer() {
        countDownTimer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                elapsedTime += 1000;
                updateTimerText();
            }

            @Override
            public void onFinish() {

            }
        };

        countDownTimer.start();
    }

    private void updateTimerText() {
        long minutes = elapsedTime / 60000;
        long seconds = (elapsedTime % 60000) / 1000;
        if(minutes>0){
            timeElapsed = String.format("%d분 %d초", minutes, seconds);
        }else{
            timeElapsed = String.format("%d초", seconds);
        }
        time.setText("Time: "+timeElapsed);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.mine_sweeper);
        startTimer();
        table = (TableLayout)findViewById(R.id.tableLayout);
        mines = (TextView)findViewById(R.id.mines);
        breakButton = (ToggleButton)findViewById(R.id.breakButton);
        time = (TextView)findViewById(R.id.time);
        home = (Button)findViewById(R.id.home);
        reset = (Button)findViewById(R.id.reset);

        BlockButton[][] buttons = new BlockButton[9][9];
        BlockButton.flags = 10;
        BlockButton.blocks = 0;

        home.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        });

        reset.setOnClickListener(view -> recreate());

        //테이블 행 삽입
        for(int i=0;i<9;i++){
            TableRow tableRow = new TableRow(this);
            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f);

            //행마다 버튼 삽입
            for(int j=0;j<9;j++){
                buttons[i][j] = new BlockButton(this,i,j);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);

                buttons[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(breakButton.isChecked()){//Flag Click
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
                        }
                        else if(!((BlockButton) view).isFlag()){//Break Click
                            if(((BlockButton)view).isMine()){
                                //클릭 했는데 지뢰일 때
                                countDownTimer.cancel();
                                AlertDialog.Builder mineAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                                mineAlertDialog.setTitle("GameOver"); //제목
                                mineAlertDialog.setMessage("게임 종료"); // 메시지
                                mineAlertDialog.setPositiveButton("홈으로", (dialog, which) -> {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(intent);
                                });
                                mineAlertDialog.setNegativeButton("다시 하기", (dialogInterface, i1) -> recreate());
                                mineAlertDialog.show();

                                //전부 오픈
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
                        }

                        //지뢰 다 찾았을 때
                        if (BlockButton.blocks == 71) {
                            countDownTimer.cancel();
                            AlertDialog.Builder winAlertDialog = new AlertDialog.Builder(MineSweeperActivity.this);
                            winAlertDialog.setTitle("You Win"); //제목
                            winAlertDialog.setMessage("지뢰를 모두 발견했습니다.\n소요 시간 : "+timeElapsed); // 메시지
                            winAlertDialog.setPositiveButton("홈으로", (dialog, which) -> {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            });
                            winAlertDialog.setNegativeButton("다시 하기", (dialogInterface, i12) -> recreate());
                            winAlertDialog.show();
                        }
                    }

                    //주변에 지뢰 없으면 자동 오픈 메소드
                    void uncoverNeighbors(int i, int j) {
                        boolean range = (i >= 0 && i < 9 && j >= 0 && j < 9);
                        if (range) {
                            boolean click = buttons[i][j].isEnabled();
                            boolean neighbor = buttons[i][j].getNeighborMines() == 0;
                            if (click && neighbor) {// 아직 오픈 안됐고, 주변에 지뢰가 없을 때
                                buttons[i][j].breakBlock(); //본인 꺼 열고

                                for (int k = -1; k < 2; k++) {
                                    for(int l = -1; l < 2 ; l++){
                                        if (i + k >= 0 && i + k < 9 && j + l >= 0 && j + l < 9) {
                                            if (buttons[i + k][j + l].isEnabled()&&!buttons[i+k][j+l].isFlag()) {//블럭이 오픈 안됐고, 플래그 표시가 아닐 때
                                                if (buttons[i + k][j + l].getNeighborMines() == 0) {//그 블럭의 주변에 지뢰가 없으면 재귀
                                                    uncoverNeighbors(i + k, j + l);
                                                } else {//있으면 그거만 오픈
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

        //지뢰 설정
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

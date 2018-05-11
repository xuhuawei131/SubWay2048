package com.huawei.game2048.view;

import java.util.ArrayList;
import java.util.List;

import com.huawei.game2048.OnGameViewListener;
import com.huawei.game2048.util.MySharedPreferenceManager;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

public class GameView extends LinearLayout {

     private int score = 0;

    private OnGameViewListener listener;
    private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();
    private AnimLayer animLayer = null;
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public void setGameViewContext(OnGameViewListener activity) {
        this.listener = activity;
    }
    public void setGameViewAnimLayer(AnimLayer animLayer) {
        this.animLayer = animLayer;
    }


    private void initGameView() {
        score=MySharedPreferenceManager.getScroe();

        setOrientation(LinearLayout.VERTICAL);
        setBackgroundColor(0xffbbada0);
        setOnTouchListener(new View.OnTouchListener() {

            float startX, startY, offsetX, offsetY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        offsetX = event.getX() - startX;
                        offsetY = event.getY() - startY;
                        if (Math.abs(offsetX) > Math.abs(offsetY)) {
                            if (offsetX < -5) {
                                swipeLeft();
                                saveCard2Sp();
                            } else if (offsetX > 5) {
                                swipeRight();
                                saveCard2Sp();
                            }
                        } else {
                            if (offsetY < -5) {
                                swipeUp();
                                saveCard2Sp();
                            } else if (offsetY > 5) {
                                swipeDown();
                                saveCard2Sp();
                            }
                        }

                        break;
                }
                return true;
            }
        });
    }

    /**
     * 保存到本地
     */
    private void saveCard2Sp() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                sb.append(cardsMap[x][y].getNum() + ",");
            }
        }
        sb.deleteCharAt(sb.length() - 1);
        String cards = sb.toString();
        MySharedPreferenceManager.saveCardNum(cards);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Config.CARD_WIDTH = (Math.min(w, h) - 10) / Config.LINES;

        addCards(Config.CARD_WIDTH, Config.CARD_WIDTH);

        startGame();
    }

    /**
     * 添加卡牌的view
     * @param cardWidth
     * @param cardHeight
     */
    private void addCards(int cardWidth, int cardHeight) {
        Card c;
        LinearLayout line;
        LinearLayout.LayoutParams lineLp;

        for (int y = 0; y < Config.LINES; y++) {
            line = new LinearLayout(getContext());
            lineLp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, cardHeight);
            addView(line, lineLp);

            for (int x = 0; x < Config.LINES; x++) {
                c = new Card(getContext());
                line.addView(c, cardWidth, cardHeight);

                cardsMap[x][y] = c;
            }
        }
    }

    /**
     * 重新开始
     */
    public void startNewGame() {
        clearScore();

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                cardsMap[x][y].setNum(0);
            }
        }
        saveCard2Sp();
        addRandomNum();
        addRandomNum();
    }

    public void startGame() {

        String card = MySharedPreferenceManager.getCatdNum();
        if (card == null) {
            for (int y = 0; y < Config.LINES; y++) {
                for (int x = 0; x < Config.LINES; x++) {
                    cardsMap[x][y].setNum(0);
                }
            }
        } else {
            String cards[] = card.split(",");
            for (int y = 0; y < Config.LINES; y++) {
                for (int x = 0; x < Config.LINES; x++) {
                    int num = Integer.valueOf(cards[y * Config.LINES + x]);
                    cardsMap[x][y].setNum(num);
                }
            }
        }
    }

    /**
     * 添加随机数
     */
    private void addRandomNum() {
        emptyPoints.clear();

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() <= 0) {
                    emptyPoints.add(new Point(x, y));
                }
            }
        }

        if (emptyPoints.size() > 0) {

            Point p = emptyPoints.remove((int) (Math.random() * emptyPoints.size()));
            cardsMap[p.x][p.y].setNum(Math.random() > 0.1 ? 2 : 4);

            animLayer.createScaleTo1(cardsMap[p.x][p.y]);
        }
    }


    private void swipeLeft() {

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {

                for (int x1 = x + 1; x1 < Config.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {

                            animLayer.createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            animLayer.createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeRight() {

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = Config.LINES - 1; x >= 0; x--) {

                for (int x1 = x - 1; x1 >= 0; x1--) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            animLayer.createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            animLayer.createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    /**
     * 清空分数
     */
    private void clearScore() {
        score = 0;
        MySharedPreferenceManager.saveScroe(score);
        if (listener!=null){
            listener.showScore(score);
            listener.showBestScore(MySharedPreferenceManager.getBestScroe());
        }
    }

    /**
     * 添加分数
     * @param s
     */
    private void addScore(int s){
        score += s;

        if (listener!=null){
            listener.showScore(score);
        }


        MySharedPreferenceManager.saveScroe(score);
        if(score>getBestScore()){
            MySharedPreferenceManager.saveBestScroe(score);
            if (listener!=null){
                listener.showBestScore(score);
            }
        }
    }


    private int  getBestScore(){
        return MySharedPreferenceManager.getBestScroe();
    }

    /**
     * 获取分数
     * @return
     */
    public int getScore(){
        return score;
    }
    private void swipeUp() {

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = 0; y < Config.LINES; y++) {

                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            animLayer.createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            animLayer.createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;

                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void swipeDown() {

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = Config.LINES - 1; y >= 0; y--) {

                for (int y1 = y - 1; y1 >= 0; y1--) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            animLayer.createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            animLayer.createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            addScore(cardsMap[x][y].getNum());
                            merge = true;
                        }

                        break;
                    }
                }
            }
        }

        if (merge) {
            addRandomNum();
            checkComplete();
        }
    }

    private void checkComplete() {

        boolean complete = true;

        ALL:
        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {
                if (cardsMap[x][y].getNum() == 0 ||
                        (x > 0 && cardsMap[x][y].equals(cardsMap[x - 1][y])) ||
                        (x < Config.LINES - 1 && cardsMap[x][y].equals(cardsMap[x + 1][y])) ||
                        (y > 0 && cardsMap[x][y].equals(cardsMap[x][y - 1])) ||
                        (y < Config.LINES - 1 && cardsMap[x][y].equals(cardsMap[x][y + 1]))) {

                    complete = false;
                    break ALL;
                }
            }
        }

        if (complete) {
            new AlertDialog.Builder(getContext()).setTitle("你好").setMessage("游戏结束").setPositiveButton("重新开始", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startGame();
                }
            }).show();
        }

    }
    //	/**
//     * 1.static int getMode(int measureSpec):
//     * 2.static int getSize(int measureSpec):
//     * 3.static int makeMeasureSpec(int size,int mode):
//     */
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
//    }
//
//    private int measureWidth(int measureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//
//        if (specMode == MeasureSpec.EXACTLY) {
//            // We were told how big to be
//            result = specSize;
//        } else {
//            // Measure the text
//            result = (int) mPaint.measureText(text) + getPaddingLeft() + getPaddingRight();
//            if (specMode == MeasureSpec.AT_MOST) {
//                // Respect AT_MOST value if that was what is called for by
//                // measureSpec
//                result = Math.min(result, specSize);// 60,480
//            }
//        }
//
//        return result;
//    }
//
//    private int measureHeight(int measureSpec) {
//        int result = 0;
//        int specMode = MeasureSpec.getMode(measureSpec);
//        int specSize = MeasureSpec.getSize(measureSpec);
//
//        if (specMode == MeasureSpec.EXACTLY) {
//            // We were told how big to be
//            result = specSize;
//        } else {
//            // Measure the text (beware: ascent is a negative number)
//            if (specMode == MeasureSpec.AT_MOST) {
//                result = Math.min(result, specSize);
//            }
//        }
//        return result;
//    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


    }
}

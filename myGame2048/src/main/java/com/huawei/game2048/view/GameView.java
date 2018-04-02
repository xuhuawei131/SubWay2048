package com.huawei.game2048.view;

import java.util.ArrayList;
import java.util.List;

import com.huawei.game2048.MainActivity;
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
    private MainActivity activity;
    private Card[][] cardsMap = new Card[Config.LINES][Config.LINES];
    private List<Point> emptyPoints = new ArrayList<Point>();
    public GameView(Context context) {
        super(context);
        initGameView();
    }

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initGameView();
    }

    public void setGameViewContext(MainActivity activity) {
        this.activity = activity;
    }

    private void initGameView() {
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

    public void startNewGame() {

        activity.clearScore();
        activity.showBestScore(activity.getBestScore());

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

            activity.getAnimLayer().createScaleTo1(cardsMap[p.x][p.y]);
        }
    }


    private void swipeLeft() {

        boolean merge = false;

        for (int y = 0; y < Config.LINES; y++) {
            for (int x = 0; x < Config.LINES; x++) {

                for (int x1 = x + 1; x1 < Config.LINES; x1++) {
                    if (cardsMap[x1][y].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {

                            activity.getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);

                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x--;
                            merge = true;

                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            activity.getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);

                            activity.addScore(cardsMap[x][y].getNum());
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
                            activity.getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x1][y].getNum());
                            cardsMap[x1][y].setNum(0);

                            x++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x1][y])) {
                            activity.getAnimLayer().createMoveAnim(cardsMap[x1][y], cardsMap[x][y], x1, x, y, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x1][y].setNum(0);
                            activity.addScore(cardsMap[x][y].getNum());
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

    private void swipeUp() {

        boolean merge = false;

        for (int x = 0; x < Config.LINES; x++) {
            for (int y = 0; y < Config.LINES; y++) {

                for (int y1 = y + 1; y1 < Config.LINES; y1++) {
                    if (cardsMap[x][y1].getNum() > 0) {

                        if (cardsMap[x][y].getNum() <= 0) {
                            activity.getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y--;

                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            activity.getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            activity.addScore(cardsMap[x][y].getNum());
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
                            activity.getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y1].getNum());
                            cardsMap[x][y1].setNum(0);

                            y++;
                            merge = true;
                        } else if (cardsMap[x][y].equals(cardsMap[x][y1])) {
                            activity.getAnimLayer().createMoveAnim(cardsMap[x][y1], cardsMap[x][y], x, x, y1, y);
                            cardsMap[x][y].setNum(cardsMap[x][y].getNum() * 2);
                            cardsMap[x][y1].setNum(0);
                            activity.addScore(cardsMap[x][y].getNum());
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


}

package com.huawei.game2048;

import com.huawei.game2048.view.Card;

/**
 * Created by lingdian on 2018/5/11.
 */

public interface OnGameViewListener {
    public void showBestScore(int bestScore);
    public void createScaleTo1(Card target);
    public void showScore(int s);
}

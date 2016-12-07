package me.jp.sticker.model;

/**
 * Created by congwiny on 2016/12/7.
 */

public class StickerTextModel {

    private float width;//数字，与屏幕宽度比，最大值为1
    private float height;//数字，高度与宽度比，ratio = height / width
    private float textX;//文本左上角x坐标与背景图宽度比，x / width
    private float textY;//文本左上角y坐标与背景图高度比，y / height
    private float textWidth;//文本宽度与背景图宽度比，width / back_width
    private float textHeight;//文本高度与背景图高度比，height / back_height
    private String textColor;//颜色值，文本颜色值
    private float textMaxSize;//文本最大值
    private float textMinSize;

    public StickerTextModel() {
        width = 0.4f;
        height = 1.4f;
        textX = 0.2f;
        textY = 0.7f;
        textWidth = 0.7f;
        textHeight = 0.2f;
        textColor = "#fa3dce";
        textMaxSize = 14;
        textMinSize = 10;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getTextX() {
        return textX;
    }

    public void setTextX(float textX) {
        this.textX = textX;
    }

    public float getTextY() {
        return textY;
    }

    public void setTextY(float textY) {
        this.textY = textY;
    }

    public float getTextWidth() {
        return textWidth;
    }

    public void setTextWidth(float textWidth) {
        this.textWidth = textWidth;
    }

    public float getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(float textHeight) {
        this.textHeight = textHeight;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public float getTextMaxSize() {
        return textMaxSize;
    }

    public void setTextMaxSize(float textMaxSize) {
        this.textMaxSize = textMaxSize;
    }

    public float getTextMinSize() {
        return textMinSize;
    }

    public void setTextMinSize(float textMinSize) {
        this.textMinSize = textMinSize;
    }
}

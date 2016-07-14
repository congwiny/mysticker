package me.jp.sticker.model;

import java.io.Serializable;

/**
 * Created by congwiny on 2016/7/8.
 */
public class StickerModel implements Serializable{

    private static final int STICKER_TYPE_ICON = 1;
    private static final int STICKER_TYPE_TEXT = 2;

    private String stickerUrl;

    private String stickerText;

    private int stickerTextBGIndex;

    private float degree;
    //宽度占屏幕的宽度比例
    private float scaleWidth;
    //宽度/高度 比值
    private float aspectRatio;
    //缩放中心x坐标
    private float zoomCenterX;
    //缩放中心y坐标
    private float zoomCenterY;

    private int stickerType;//sticker的类型

    public String getStickerUrl() {
        return stickerUrl;
    }

    public void setStickerUrl(String stickerUrl) {
        this.stickerUrl = stickerUrl;
    }

    public float getDegree() {
        return degree;
    }

    public void setDegree(float degree) {
        this.degree = degree;
    }

    public float getScaleWidth() {
        return scaleWidth;
    }

    public void setScaleWidth(float scaleWidth) {
        this.scaleWidth = scaleWidth;
    }

    public float getAspectRatio() {
        return aspectRatio;
    }

    public void setAspectRatio(float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public float getZoomCenterX() {
        return zoomCenterX;
    }

    public void setZoomCenterX(float zoomCenterX) {
        this.zoomCenterX = zoomCenterX;
    }

    public float getZoomCenterY() {
        return zoomCenterY;
    }

    public void setZoomCenterY(float zoomCenterY) {
        this.zoomCenterY = zoomCenterY;
    }

    public String getStickerText() {
        return stickerText;
    }

    public void setStickerText(String stickerText) {
        this.stickerText = stickerText;
    }

    public int getStickerType() {
        return stickerType;
    }

    public void setStickerType(int stickerType) {
        this.stickerType = stickerType;
    }

    public int getStickerTextBGIndex() {
        return stickerTextBGIndex;
    }

    public void setStickerTextBGIndex(int stickerTextBGIndex) {
        this.stickerTextBGIndex = stickerTextBGIndex;
    }

    @Override
    public String toString() {
        return "StickerParam{" +
                "url='" + stickerUrl + '\'' +
                ", degree=" + degree +
                ", scaleWidth=" + scaleWidth +
                ", aspectRatio=" + aspectRatio +
                ", zoomCenterX=" + zoomCenterX +
                ", zoomCenterY=" + zoomCenterY +
                '}';
    }
}

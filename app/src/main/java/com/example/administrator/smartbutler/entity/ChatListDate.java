package com.example.administrator.smartbutler.entity;

/**
 * 项目名：    SmartButler
 * 包名：      com.example.administrator.smartbutler.entity
 * 创建者：    Kamh
 * 创建时间：  2017/12/517:02
 * 描述：      对话列表实体类
 */
public class ChatListDate {

    private int type;
    //文本
    private String text;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

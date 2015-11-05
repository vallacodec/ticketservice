package com.tm.model;

/**
 * Created by svallaban1 on 11/3/2015.
 */
public enum LevelEnum {

    BALCONY1(1);

    private Integer levelId;

    LevelEnum(int levelId) {
        this.levelId = levelId;
    }

    public int getSeat() {
        return this.levelId;
    }

}

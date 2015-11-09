package com.tm.model;

/**
 * Created by svallaban1 on 11/3/2015.
 * This enum represent the levelId and corresponding stage name
 */
public enum LevelEnum {

    ORCHESTRA(4),
    MAIN(3),
    BALCONY1(2),
    BALCONY2(1);

    private Integer levelId;

    LevelEnum(int levelId) {
        this.levelId = levelId;
    }

    public int getLevel() {
        return this.levelId;
    }

}

package com.tm.model;

/**
 * Created by svallaban1 on 11/3/2015.
 * This enum represent the levelId and corresponding stage name
 */
public enum LevelEnum {

    ORCHESTRA(1),
    MAIN(2),
    BALCONY1(3),
    BALCONY2(4);

    private Integer levelId;

    LevelEnum(int levelId) {
        this.levelId = levelId;
    }

    public int getSeat() {
        return this.levelId;
    }

}

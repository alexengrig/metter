package dev.alexengrig.metter.demo;

import dev.alexengrig.metter.annotation.MetadataGeneration;
import lombok.Getter;

@MetadataGeneration
public class Person {
    private final int constant = 100;
    private int integer;
    private String string;
    private boolean enable;
    private boolean withoutGetter;
    @Getter
    private String lombok;

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public int getInteger() {
        return integer;
    }

    public void setInteger(int integer) {
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getConstant() {
        return constant;
    }
}

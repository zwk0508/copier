package com.zwk.copier;

/**
 * @author zwk
 * @version 1.0
 * @date 2024/3/26 17:20
 */

public class Pair {
    private String left;
    private String right;

    private Class<?> leftClass;
    private Class<?> rightClass;

    public String getLeft() {
        return left;
    }

    public void setLeft(String left) {
        this.left = left;
    }

    public String getRight() {
        return right;
    }

    public void setRight(String right) {
        this.right = right;
    }

    public Class<?> getLeftClass() {
        return leftClass;
    }

    public void setLeftClass(Class<?> leftClass) {
        this.leftClass = leftClass;
    }

    public Class<?> getRightClass() {
        return rightClass;
    }

    public void setRightClass(Class<?> rightClass) {
        this.rightClass = rightClass;
    }
}

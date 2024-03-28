package com.zwk.copier;

public interface Copier<L, R> {
    void copy(L left, R right);
}

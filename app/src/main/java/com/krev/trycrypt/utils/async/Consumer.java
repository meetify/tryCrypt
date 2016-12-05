package com.krev.trycrypt.utils.async;

/**
 * Created by Dima on 28.10.2016.
 */
public interface Consumer<T> {
    void accept(T t);
}

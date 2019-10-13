/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 @param <T> the node type
 */
public interface NodeContext<T extends MenuNode> {

    T getNode();

    void refreshContext();

}

/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import java.util.Objects;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Timezone {

    private String zone;
    private String offset;

    public Timezone(String zone, String offset) {
        this.zone = zone;
        this.offset = offset;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.zone);
        hash = 53 * hash + Objects.hashCode(this.offset);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Timezone other = (Timezone) obj;
        if (!Objects.equals(this.zone, other.zone)) {
            return false;
        }
        return Objects.equals(this.offset, other.offset);
    }

    @Override
    public String toString() {
        return zone;
    }

}

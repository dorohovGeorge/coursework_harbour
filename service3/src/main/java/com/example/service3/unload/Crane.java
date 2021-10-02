package com.example.service3.unload;

import com.example.service3.generator.Ship;


public  class Crane {
    private final Ship.Cargo cargo;
    private boolean isFree;
    private long latency;

    public Crane(Ship.Cargo cargo, boolean isFree) {
        this.cargo = cargo;
        this.isFree = isFree;
        this.latency = (long) ((Math.random()*1441)/60);
    }

    public Ship.Cargo getCargo() {
        return cargo;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public long getLatency() {
        return latency;
    }

    public void setLatency(long latency) {
        this.latency = latency;
    }
}

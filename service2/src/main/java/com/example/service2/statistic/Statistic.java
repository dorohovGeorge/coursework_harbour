package com.example.service2.statistic;


import java.util.Arrays;
import java.util.List;

public class Statistic {
    private List<List<ShipStatistic>> statistics;
    private Summary summaryStatistics;

    public Statistic(List<List<ShipStatistic>> statistics, Summary summaryStatistics) {
        this.statistics = statistics;
        this.summaryStatistics = summaryStatistics;
    }

    public List<List<ShipStatistic>> getStatistics() {
        return statistics;
    }

    public Summary getSummaryStatistics() {
        return summaryStatistics;
    }

    public static class ShipStatistic {
        private String shipName;
        private String timeOfArrival;
        private long waitingTime;
        private long startTimeOfUnloading;
        private long durationOfUnloading;

        public ShipStatistic(String name, String timeOfArrival, long waitingTime,
                             long startTimeOfUnloading, long durationOfUnloading) {
            this.shipName = name;
            this.timeOfArrival = timeOfArrival;
            this.waitingTime = waitingTime;
            this.startTimeOfUnloading = startTimeOfUnloading;
            this.durationOfUnloading = durationOfUnloading;
        }

        public String getShipName() {
            return shipName;
        }

        public void setShipName(String shipName) {
            this.shipName = shipName;
        }

        public String getTimeOfArrival() {
            return timeOfArrival;
        }

        public void setTimeOfArrival(String timeOfArrival) {
            this.timeOfArrival = timeOfArrival;
        }

        public long getWaitingTime() {
            return waitingTime;
        }

        public void setWaitingTime(long waitingTime) {
            this.waitingTime = waitingTime;
        }

        public long getStartTimeOfUnloading() {
            return startTimeOfUnloading;
        }

        public void setStartTimeOfUnloading(long startTimeOfUnloading) {
            this.startTimeOfUnloading = startTimeOfUnloading;
        }

        public long getDurationOfUnloading() {
            return durationOfUnloading;
        }

        public void setDurationOfUnloading(long durationOfUnloading) {
            this.durationOfUnloading = durationOfUnloading;
        }

        @Override
        public String toString() {
            return "ShipStatistic{" +
                    "shipName='" + shipName + '\'' +
                    ", timeOfArrival=" + timeOfArrival +
                    ", waitingTime=" + waitingTime +
                    ", startTimeOfUnloading=" + startTimeOfUnloading +
                    ", durationOfUnloading=" + durationOfUnloading +
                    '}';
        }
    }

    public static class Summary {
        private int unloadedShip;
        private long averageWaitingTime;
        private long maxUnloadLatency;
        private long averageUnloadLatency;
        private long fine;
        private int[] requiredCountOfCranes;
        private int[] fineForTypesOfShips;


        public Summary() {
            this.requiredCountOfCranes = new int[3];
            this.fineForTypesOfShips = new int[3];
        }

        public int getUnloadedShip() {
            return unloadedShip;
        }

        public void setUnloadedShip(int unloadedShip) {
            this.unloadedShip = unloadedShip;
        }

        public long getAverageWaitingTime() {
            return averageWaitingTime;
        }

        public void setAverageWaitingTime(long averageWaitingTime) {
            this.averageWaitingTime = averageWaitingTime;
        }

        public long getMaxUnloadLatency() {
            return maxUnloadLatency;
        }

        public void setMaxUnloadLatency(long maxUnloadLatency) {
            this.maxUnloadLatency = maxUnloadLatency;
        }

        public long getAverageUnloadLatency() {
            return averageUnloadLatency;
        }

        public void setAverageUnloadLatency(long averageUnloadLatency) {
            this.averageUnloadLatency = averageUnloadLatency;
        }

        public long getFine() {
            return fine;
        }

        public void setFine(long fine) {
            this.fine = fine;
        }

        public int[] getRequiredCountOfCranes() {
            return requiredCountOfCranes;
        }

        public void setRequiredCountOfCranes(int type, int value) {
            this.requiredCountOfCranes[type] = value;
        }

        public int[] getFineForTypesOfShips() {
            return fineForTypesOfShips;
        }

        public void setFineForTypesOfShips(int type, int value) {
            this.fineForTypesOfShips[type] = value;
        }

        @Override
        public String toString() {
            return "Summary{" +
                    "unloadedShip=" + unloadedShip +
                    ", averageWaitingTime=" + averageWaitingTime +
                    ", maxUnloadDelay=" + maxUnloadLatency +
                    ", averageUnloadDelay=" + averageUnloadLatency +
                    ", fine=" + fine +
                    ", countOfShips=" + Arrays.toString(requiredCountOfCranes) +
                    ", fineForTypesOfShips=" + Arrays.toString(fineForTypesOfShips) +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Statistic{" +
                "statistics=" + statistics +
                ", summaryStatistics=" + summaryStatistics +
                '}';
    }
}

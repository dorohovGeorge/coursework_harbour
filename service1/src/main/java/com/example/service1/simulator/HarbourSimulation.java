package com.example.service1.simulator;


import com.example.service1.generator.*;
import com.example.service1.statistic.Statistic;
import com.example.service1.unload.Crane;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class HarbourSimulation {
    private final static int HOUR = 3600000;
    private final static int FINE_PER_HOUR = 100;
    private final static int PRICE_OF_CRANE = 30000;

    private static class ShipsUnloading implements Runnable {
        private final Crane crane;
        private final Schedule.ElementOfSchedule elementOfSchedule;
        private final Statistic.Summary summaryStatistic;

        public ShipsUnloading(Crane crane, Schedule.ElementOfSchedule elementOfSchedule, Statistic.Summary summaryStatistic) {
            this.crane = crane;
            this.elementOfSchedule = elementOfSchedule;
            this.summaryStatistic = summaryStatistic;
        }

        @Override
        public void run() {
            if (crane.getLatency() != 0) {
                crane.setLatency(crane.getLatency() - 1);
                return;
            }
            synchronized (elementOfSchedule) {
                elementOfSchedule.getShip().setDimension(
                        Math.max(elementOfSchedule.getShip().getDimension() - crane.getCargo().getSpeed(), 0));
                if (elementOfSchedule.getShip().getDimension() == 0) {
                    crane.setLatency((long) (Math.random() * 1441) / 60);
                    synchronized (summaryStatistic) {
                        summaryStatistic.setAverageUnloadLatency(
                                summaryStatistic.getAverageUnloadLatency() + crane.getLatency());
                        summaryStatistic.setMaxUnloadLatency(
                                Math.max(summaryStatistic.getMaxUnloadLatency(), crane.getLatency()));
                    }
                }
            }
        }
    }

    public static Statistic analyzeDate(Schedule schedule, int[] countOfCranes) {
        List<Queue<Crane>> cranes = new ArrayList<>();
        List<List<Schedule.ElementOfSchedule>> elementsOfSchedule = new ArrayList<>();
        List<List<Statistic.ShipStatistic>> statistics = new ArrayList<>();
        Statistic.Summary summaryStatistic = new Statistic.Summary();
        for (int i = 0; i < 3; i++) {
            elementsOfSchedule.add(new ArrayList<>());
            statistics.add(new ArrayList<>());
            cranes.add(new LinkedList<>());
            for (int j = 0; j < countOfCranes[i]; j++) {
                Crane crane = new Crane(Ship.Cargo.values()[i], true);
                cranes.get(i).add(crane);
                summaryStatistic.setAverageUnloadLatency(
                        summaryStatistic.getAverageUnloadLatency() + crane.getLatency());
                summaryStatistic.setMaxUnloadLatency(
                        Math.max(summaryStatistic.getMaxUnloadLatency(), crane.getLatency()));
            }
        }
        List<Schedule.ElementOfSchedule> list = schedule.getElementsOfSchedule();
        long startTime = Long.MAX_VALUE;
        long endTime = Long.MIN_VALUE;
        for (Schedule.ElementOfSchedule elem : list) {
            elem.getTime().getTimeOfArrival().changeDayForArrivalTime((int) (-7 + Math.random() * 8));
            startTime = Math.min(elem.getTime().getTimeOfArrival().toLong(), startTime);
            endTime = Math.max(
                    elem.getTime().getTimeOfArrival().toLong() + elem.getTime().getTimeOfDeparture().toLong(), endTime);
            if (elem.getShip().getTransportedCargo() == Ship.Cargo.LOOSE) {
                elementsOfSchedule.get(0).add(elem);
                statistics.get(0).add(new Statistic.ShipStatistic(elem.getShip().getNameOfShip(),
                        elem.getTime().getTimeOfArrival().getArrivalDateTime(),
                        0, 0, 0));
            }
            if (elem.getShip().getTransportedCargo() == Ship.Cargo.LIQUID) {
                elementsOfSchedule.get(1).add(elem);
                statistics.get(1).add(new Statistic.ShipStatistic(elem.getShip().getNameOfShip(),
                        elem.getTime().getTimeOfArrival().getArrivalDateTime(),
                        0, 0, 0));
            }
            if (elem.getShip().getTransportedCargo() == Ship.Cargo.SOLID) {
                elementsOfSchedule.get(2).add(elem);
                statistics.get(2).add(new Statistic.ShipStatistic(elem.getShip().getNameOfShip(),
                        elem.getTime().getTimeOfArrival().getArrivalDateTime(),
                        0, 0, 0));
            }
        }
        for (int i = 0; i < 3; i++) {
            elementsOfSchedule.get(i).sort(Comparator.comparingLong(a -> a.getTime().getTimeOfArrival().toLong()));
        }

        unloading(cranes, elementsOfSchedule, statistics, summaryStatistic, startTime, endTime);
        int[] neededCranes = summaryStatistic.getFineForTypesOfShips();
        for (int i = 0; i < 3; i++) {
            summaryStatistic.setRequiredCountOfCranes(i, neededCranes[i] / PRICE_OF_CRANE);
        }
        return new Statistic(statistics, summaryStatistic);
    }

    private static void unloading(List<Queue<Crane>> cranes,
                                  List<List<Schedule.ElementOfSchedule>> elementsOfSchedule,
                                  List<List<Statistic.ShipStatistic>> statistics,
                                  Statistic.Summary summaryStatistic,
                                  long startTime,
                                  long endTime) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        Queue<Crane> busyCranes = new LinkedList<>();
        for (long time = startTime; time < endTime; time += HOUR) {
            for (int i = 0; i < elementsOfSchedule.size(); i++) {
                for (int j = 0; j < elementsOfSchedule.get(i).size(); j++) {
                    Statistic.ShipStatistic statistic = statistics.get(i).get(j);
                    Schedule.ElementOfSchedule elem = elementsOfSchedule.get(i).get(j);
                    if (elem.getTime().getTimeOfArrival().toLong() < time) {
                        continue;
                    }
                    if (elem.getShip().getDimension() == 0) {
                        if (statistic.getDurationOfUnloading() == 0) {
                            statistic.setDurationOfUnloading(time - statistic.getStartTimeOfUnloading());
                        }
                        continue;
                    }
                    Crane crane1 = null;
                    Crane crane2 = null;

                    if (!cranes.get(i).isEmpty()) {
                        if (statistic.getStartTimeOfUnloading() == 0) {
                            statistic.setStartTimeOfUnloading(time);
                        }
                        crane1 = cranes.get(i).poll();
                        executorService.execute(new ShipsUnloading(crane1, elem, summaryStatistic));
                        if (!cranes.get(i).isEmpty()) {
                            crane2 = cranes.get(i).poll();
                            executorService.execute(new ShipsUnloading(crane2, elem, summaryStatistic));
                        }
                    } else {
                        statistic.setWaitingTime(statistic.getWaitingTime() + HOUR);
                    }
                    if (crane1 != null) {
                        busyCranes.add(crane1);
                    }
                    if (crane2 != null) {
                        busyCranes.add(crane2);
                    }
                }
                for (Crane crane : busyCranes) {
                    cranes.get(i).add(crane);
                }
                busyCranes.clear();
            }
        }

        executorService.shutdown();
        try {
            executorService.awaitTermination(5, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        int unloadedShips = 0;

        for (List<Schedule.ElementOfSchedule> elem : elementsOfSchedule) {
            unloadedShips += elem.size();
        }

        summaryStatistic.setUnloadedShip(unloadedShips);

        for (int i = 0; i < elementsOfSchedule.size(); i++) {
            for (Statistic.ShipStatistic statistic : statistics.get(i)) {
                summaryStatistic.setAverageWaitingTime(
                        summaryStatistic.getAverageWaitingTime() + statistic.getWaitingTime());
                summaryStatistic.setFine(summaryStatistic.getFine() +
                        (statistic.getWaitingTime() / HOUR) * FINE_PER_HOUR);
                summaryStatistic.setFineForTypesOfShips(i,
                        (int) (summaryStatistic.getFineForTypesOfShips()[i] +
                                (statistic.getWaitingTime() / HOUR) * FINE_PER_HOUR));
            }
        }
        summaryStatistic.setAverageUnloadLatency(
                summaryStatistic.getAverageUnloadLatency() / summaryStatistic.getUnloadedShip());
        summaryStatistic.setAverageWaitingTime(
                summaryStatistic.getAverageWaitingTime() / summaryStatistic.getUnloadedShip());
    }
}

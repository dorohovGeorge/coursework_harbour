package com.example.service2.generator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Schedule {
    public static class ElementOfSchedule {
        private Ship ship;
        private ShipTime time;

        ElementOfSchedule() {
            this.ship = new Ship();
            this.time = new ShipTime(this.ship);
        }

        public Ship getShip() {
            return ship;
        }

        public ShipTime getTime() {
            return time;
        }

        @Override
        public String toString() {
            return "ElementOfSchedule{" +
                    "ship=" + ship +
                    ", time=" + time +
                    '}';
        }
    }

    public static List<String> namesOfShips = new ArrayList<>();
    private List<ElementOfSchedule> elementsOfSchedule = new ArrayList<>();

    public Schedule(int countOfShips) {
        fillListOfNamesShips();
        for (int i = 0; i < countOfShips; i++) {
            elementsOfSchedule.add(new ElementOfSchedule());
        }
    }

    public List<ElementOfSchedule> getElementsOfSchedule() {
        return elementsOfSchedule;
    }

    public ElementOfSchedule getElem(int number) {
        return elementsOfSchedule.get(number);
    }

    private void fillListOfNamesShips() {
        try {
            File fileWithNames = new File("src/main/resources/NamesOfShips.txt");
            Scanner reader = new Scanner(fileWithNames);
            while (reader.hasNextLine()) {
                namesOfShips.add(reader.nextLine());
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Schedule{" +
                "elementsOfSchedule=" + elementsOfSchedule +
                '}';
    }
}

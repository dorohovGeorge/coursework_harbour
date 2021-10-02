package com.example.service1.generator;

import java.util.Random;

public class Ship {

    public enum Cargo {
        LOOSE(30), //зыбучий
        LIQUID(40), //жидкий
        SOLID(60); // твердый
        private int speed;

        Cargo(int speed) {
            this.speed = speed;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }
    }

    private Cargo transportedCargo;
    private int dimension;
    private String nameOfShip;

    public Ship(String ship, Cargo cargo, int weight) {
        this.transportedCargo = cargo;
        this.dimension = weight;
        this.nameOfShip = ship;
    }

    public Ship(Cargo cargo, int weight) {
        this.transportedCargo = cargo;
        this.dimension = weight;
        this.nameOfShip = getRandomNameOfShipFromFile();
    }

    public Ship() {
        this.transportedCargo = getRandomCargo();
        this.nameOfShip = getRandomNameOfShipFromFile();
        this.dimension = getRandomWeight();
    }

    public void setNameOfShip(String ship) {
        nameOfShip = ship;
    }

    public String getNameOfShip() {
        return nameOfShip;
    }

    private String getRandomNameOfShipFromFile() throws ArrayIndexOutOfBoundsException {
        if (Schedule.namesOfShips.isEmpty() || Schedule.namesOfShips.size() == 1) {
            return getRandomNameOfShip();
        }
        Random random = new Random(System.currentTimeMillis());
        int numberOurName = random.nextInt(Schedule.namesOfShips.size() - 1);
        String name = Schedule.namesOfShips.get(numberOurName);
        Schedule.namesOfShips.remove(numberOurName);
        return name;
    }

    private String getRandomNameOfShip() {
        StringBuilder stringBuilder = new StringBuilder();
        int length = (int) (Math.random() * 15 + 3);
        stringBuilder.append(Character.toUpperCase((char) (int) (Math.random() * 26 + 65)));
        for (int i = 0; i < length; i++) {
            stringBuilder.append((char) (int) (Math.random() * 26 + 97));
        }
        return stringBuilder.toString();
    }


    private Cargo getRandomCargo() {
        Random random = new Random(System.currentTimeMillis());
        int numberRandomCargo = (int) (Math.random() * (Cargo.values().length));
        return Cargo.values()[numberRandomCargo];
    }

    private int getRandomWeight() {
        Random random = new Random(System.currentTimeMillis());
        return (int) (Math.random() * (80 + 1)) + 20;
    }

    public Cargo getTransportedCargo() {
        return transportedCargo;
    }

    public void setTransportedCargo(Cargo transportedCargo) {
        this.transportedCargo = transportedCargo;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return "Ship{" +
                "transportedCargo=" + transportedCargo +
                ", dimension=" + dimension +
                ", nameOfShip='" + nameOfShip + '\'' +
                '}';
    }
}

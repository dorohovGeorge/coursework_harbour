package com.example.service3.generator;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ThreadLocalRandom;

public class ShipTime {
    private TimeOfArrival timeOfArrival;
    private TimeOfDeparture timeOfDeparture;

    public ShipTime(Ship ship) {
        this.timeOfArrival = new TimeOfArrival();
        this.timeOfDeparture = new TimeOfDeparture(ship);
    }

    public class TimeOfArrival {
        final static int NUMBER_OF_MONTHS_FOR_PLANE = 1;
        private String arrivalDateTime;

        TimeOfArrival() {
            this.arrivalDateTime = getRandomTime();
        }

        TimeOfArrival(LocalDateTime arrivalDateTime) {
            this.arrivalDateTime = arrivalDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        private String getRandomTime() {
            LocalDateTime currentDate = LocalDateTime.now();
            LocalDateTime finalDate = currentDate.plusMonths(NUMBER_OF_MONTHS_FOR_PLANE);
            ZoneId zoneId = ZoneId.systemDefault();

            long minDayWithHours = LocalDateTime.of(currentDate.toLocalDate(), currentDate.toLocalTime())
                    .atZone(zoneId)
                    .toEpochSecond();
            long maxDayWithHours = LocalDateTime.of(finalDate.toLocalDate(), finalDate.toLocalTime())
                    .atZone(zoneId)
                    .toEpochSecond();

            long randomDay = ThreadLocalRandom.current().nextLong(minDayWithHours, maxDayWithHours);
            LocalDateTime result = LocalDateTime.ofInstant(Instant.ofEpochSecond(randomDay),
                    TimeZone.getDefault().toZoneId());
            return result.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        public String getArrivalDateTime() {
            return arrivalDateTime;
        }

        public void setArrivalDateTime(String arrivalDateTime) {
            this.arrivalDateTime = arrivalDateTime;
        }

        public void changeDayForArrivalTime(int day) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            LocalDateTime tempTime = LocalDateTime.parse(arrivalDateTime, dateTimeFormatter);
            tempTime.plusDays(day);
            arrivalDateTime = tempTime.format(dateTimeFormatter);
        }

        public long toLong() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(arrivalDateTime, dateTimeFormatter).toInstant(ZoneOffset.UTC).toEpochMilli();
        }


        @Override
        public String toString() {
            return "TimeOfArrival{" +
                    "randomDateTime=" + arrivalDateTime +
                    '}';
        }
    }

    public class TimeOfDeparture {
        private String departureTime;

        public TimeOfDeparture(Ship ship) {
            this.departureTime = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            Date tempTime = new Date();
            tempTime.setTime(ship.getDimension() / ship.getTransportedCargo().getSpeed() + (ship.getDimension() % ship.getTransportedCargo().getSpeed() == 0 ? 0 : 1));
            this.departureTime = tempTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        }

        public String getDepartureTime() {
            return departureTime;
        }

        public long toLong() {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
            return LocalDateTime.parse(departureTime, dateTimeFormatter).toInstant(ZoneOffset.UTC).toEpochMilli();
        }

        @Override
        public String toString() {
            return "TimeOfDeparture{" +
                    "departureTime='" + departureTime + '\'' +
                    '}';
        }
    }

    public TimeOfArrival getTimeOfArrival() {
        return timeOfArrival;
    }

    public TimeOfDeparture getTimeOfDeparture() {
        return timeOfDeparture;
    }

    @Override
    public String toString() {
        return "ShipTime{" +
                "timeOfArrival=" + timeOfArrival +
                ", timeOfDeparture=" + timeOfDeparture +
                '}';
    }
}

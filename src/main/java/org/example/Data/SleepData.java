package org.example.Data;

import java.time.LocalDate;
import java.time.LocalTime;

public class SleepData extends HealthData {
    private LocalTime sleepTime;
    private LocalTime wakeUpTime;

    public SleepData(LocalDate date, LocalTime sleepTime, LocalTime wakeUpTime) {
        super(date);
        this.sleepTime = sleepTime;
        this.wakeUpTime = wakeUpTime;
    }

    public LocalTime getSleepTime() {
        return sleepTime;
    }

    public LocalTime getWakeUpTime() {
        return wakeUpTime;
    }

    @Override
    public String toString() {
        return String.format("%s,Sleep,%s,%s", getDate(), sleepTime, wakeUpTime);
    }
}

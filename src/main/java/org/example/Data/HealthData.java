package org.example.Data;

import java.time.LocalDate;

public abstract class HealthData {
    private LocalDate date;

    public HealthData(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    public abstract String toString();
}


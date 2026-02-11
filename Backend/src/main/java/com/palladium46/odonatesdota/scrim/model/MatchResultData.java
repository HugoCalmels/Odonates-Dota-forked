package com.palladium46.odonatesdota.scrim.model;

public class MatchResultData {
    private boolean played;
    private String report;

    public boolean isPlayed() {
        return played;
    }

    public void setPlayed(boolean played) {
        this.played = played;
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }
}

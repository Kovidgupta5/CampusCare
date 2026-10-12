package com.example.campuscare;

public class ComplaintModel {

    private String name, location, category, severity;

    public ComplaintModel(String name, String location, String category, String severity) {

        this.name = name;

        this.location = location;

        this.category = category;

        this.severity = severity;

    }

    public String getName() { return name; }

    public String getLocation() { return location; }

    public String getCategory() { return category; }

    public String getSeverity() { return severity; }

}

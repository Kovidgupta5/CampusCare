package com.example.campuscare;

public class ComplaintModel {
    private int id;
    private String name, location, category, severity, imageUri, status, resolutionImageUri;

    public ComplaintModel(int id, String name, String location, String category, String severity, String imageUri, String status, String resolutionImageUri) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.category = category;
        this.severity = severity;
        this.imageUri = imageUri;
        this.status = status;
        this.resolutionImageUri = resolutionImageUri;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getCategory() { return category; }
    public String getSeverity() { return severity; }
    public String getImageUri() { return imageUri; }
    public String getStatus() { return status; }
    public String getResolutionImageUri() { return resolutionImageUri; }
    public void setStatus(String status) { this.status = status; }
    public void setResolutionImageUri(String resolutionImageUri) { this.resolutionImageUri = resolutionImageUri; }
}
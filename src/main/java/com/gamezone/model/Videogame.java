package com.gamezone.model;

public class Videogame extends Product {
    private String platform;
    private String genre;
    private String ageRating;
    

    
    public Videogame(String id, String title, double price, int availability, String platform, String genre, String ageRating){
        super(id, title, price, availability);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getAgeRating() {
        return ageRating;
    }

    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    @Override
    public String getDescription() {
        return String.format("Video Game [ID: %s] %s | Platform: %s | Genre: %s | Rating: %s | Price: $%.2f | Stock: %d",
                getId(), getTitle(), platform, genre, ageRating, getPrice(), getAvailability());
    }
    
}

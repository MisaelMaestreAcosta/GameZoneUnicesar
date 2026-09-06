package com.gamezone.model;

/**
 *
 * @author Usuario
 */
public class Videogame extends Product {
    private String platform;
    private String genre;
    private String ageRating;
    
    /**
     * creates a new video game with the given shared and specific attributes
     * 
     * @param id the unique identifier for the video game
     * @param title the display title of the video game
     * @param price the selling price of the video game
     * @param availability the available stock quantity
     * @param platform the platform on which the game is playable
     * @param genre the genre of the game
     * @param ageRating the content rating classification
     */
    public Videogame(String id, String title, double price, int availability, String platform, String genre, String ageRating){
        super(id, title, price, availability);
        this.platform = platform;
        this.genre = genre;
        this.ageRating = ageRating;
    }

    /**
     * returns the platform this game runs on
     * @return the platform
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * sets the platform this game runs on.
     * @param platform the new platform
     */
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    /**
     * gets the genre of the video game
     * @return the game genre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * sets the genre of the video game
     * @param genre the new genre 
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * returns the age rating of this game
     * @return the ager rating
     */
    public String getAgeRating() {
        return ageRating;
    }

    /**
     * sets the age rating of this game
     * @param ageRating the new age rating
     */
    public void setAgeRating(String ageRating) {
        this.ageRating = ageRating;
    }
    
    /**
     * returns a formatted summary string containing detailed information
     * about the video game, including inherited product attributes and platform specifications
     * 
     * @return a formatted descriptive string representing the video game
     */
    @Override
    public String getDescription() {
        return String.format("Video Game [ID: %s] %s | Platform: %s | Genre: %s | Rating: %s | Price: $%.2f | Stock: %d",
                getId(), getTitle(), platform, genre, ageRating, getPrice(), getAvailability());
    }
    
}

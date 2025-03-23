package org.example.models;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InternetMagazine {

    private String name;
    private double averageRating;
    private String urlMagazine;
    private List<ReviewMagazine> reviews;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrlMagazine() {
        return urlMagazine;
    }

    public void setUrlMagazine(String urlMagazine) {
        this.urlMagazine = urlMagazine;
    }

    public double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    public List<ReviewMagazine> getReviews() {
        return reviews;
    }

    public void setReviews(List<ReviewMagazine> reviews) {
        this.reviews = reviews;
    }
}

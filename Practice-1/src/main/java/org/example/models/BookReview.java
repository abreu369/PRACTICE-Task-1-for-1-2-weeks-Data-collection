package org.example.models;
import lombok.*;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BookReview {
    private String title;
    private String linkBook;
    private String userAvatar;
    private String userComments;
    private String reviewDate;
    private String authorBook;
    private String imageBook;
    private String categoryBook;
    private String reviewText;
}


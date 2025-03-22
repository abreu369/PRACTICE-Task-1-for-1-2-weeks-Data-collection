package org.example.models;
import lombok.*;

@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    private String url;
    private String image;
    private String title;
    private String price;
    private String decryption;
    private double stars;
    private int numberComment;
}




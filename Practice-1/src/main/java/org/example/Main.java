package org.example;
import org.example.models.BookReview;
import org.example.models.Product;
import org.example.scraper.Scraper;

import java.io.File;
import java.util.List;

public class Main {

    public static final String VERDE = "\u001B[32m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String RESET = "\u001B[0m";


    public static void main(String[] args) {
        String destinationFolder = "download";
        File folder = new File(destinationFolder);
        if (!folder.exists()) {
            folder.mkdir();
            new File(destinationFolder+"/produts/").mkdir();
            new File(destinationFolder+"/books/").mkdir();
        }

        Scraper scraper = new Scraper();
        List<Product> productList = scraper.getDataProduct();
        List<BookReview> getDataBookReviewList = scraper.getDataBookReview();

        for (Product p : productList) {
            try {
                scraper.donwloadImage(p.getImage(), destinationFolder+"/produts/");
                System.out.println(VERDE+"Image "+p.getTitle() +" salved in folder=> "+destinationFolder+"/produts/"+RESET);
            } catch (Exception e) {
                System.out.println(VERMELHO+"Erro do downlaod the image: "+p.getTitle()+RESET);
            }
        }

        for (BookReview p : getDataBookReviewList) {
            try {
                scraper.donwloadImage(p.getImageBook(), destinationFolder+"/books/");
                System.out.println(VERDE+"Image "+p.getTitle() +" salved in folder=> "+destinationFolder+"/books/"+RESET);
            } catch (Exception e) {
                System.out.println(VERMELHO+"Erro do downlaod the image: "+p.getTitle()+RESET);
            }
        }
    }
}
package org.example;
import org.example.models.BookReview;
import org.example.models.InternetMagazine;
import org.example.models.Product;
import org.example.scraper.Scraper;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

public class Main {

    public static final String VERDE = "\u001B[32m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String RESET = "\u001B[0m";


    public static void main(String[] args) {
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-= PRATICE 1 -=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-==-");
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");

        System.out.println();
        System.out.println();
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-= TASK 1 ==-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-");
        System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
        try {
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
System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");
            for (Product p : productList) {
                try {
                    scraper.donwloadImage(p.getImage(), destinationFolder+"/produts/");
                    System.out.println(VERDE+"Image "+p.getTitle() +" salved in folder=> "+destinationFolder+"/produts/"+RESET);
                } catch (Exception e) {
                    System.out.println(VERMELHO+"Erro to downlaod the image: "+p.getTitle()+RESET);
                }
            }

            for (BookReview p : getDataBookReviewList) {
                try {
                    scraper.donwloadImage(p.getImageBook(), destinationFolder+"/books/");
                    System.out.println(VERDE+"Image "+p.getTitle() +" salved in folder=> "+destinationFolder+"/books/"+RESET);
                } catch (Exception e) {
                    System.out.println(VERMELHO+"Erro to downlaod the image: "+p.getTitle()+RESET);
                }
            }
            System.out.println();
            System.out.println();
            System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
            System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-= TASK 2 ==-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-");
            System.out.println("=-=-=--=-=--==-=-=-=-==-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-");

            List<InternetMagazine> getListInternetMagazine = scraper.getDataInternetMagazine();

            FileWriter writer = new FileWriter("lista_internet_magazine.txt");
            PrintWriter pw = new PrintWriter(writer);
            for (InternetMagazine internetMagazine : getListInternetMagazine) {
                try {
                    pw.println(internetMagazine.toString());
                    System.out.println(VERDE+"Information magazine "+internetMagazine.getName()+" saved in file => lista_internet_magazine.txt "+RESET);
                } catch (Exception e) {
                    System.out.println(VERMELHO+"Information magazine "+internetMagazine.getName()+" not saved in file => lista_internet_magazine.txt "+RESET);
                }
            }

            pw.close();

        } catch (Exception e) {
            System.out.println("Erro to conected the server.");
        }
    }
}
package org.example.scraper;
import org.example.models.BookReview;
import org.example.models.Product;
import  org.jsoup.*;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
public class Scraper {

    public List<Product> getDataProduct() {
        try {
            Document doc  = Jsoup.connect("https://webscraper.io/test-sites/e-commerce/allinone/computers/laptops").
                userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36").
                header("Accept-Language", "*").get();

            Elements productElements = doc.select("div.card.thumbnail");
            List<Product> result = new ArrayList<>();

            for (Element product : productElements) {
                Product p = new Product();
                Element titleElement = product.selectFirst(".caption h4 a");
                Element priceElement = product.selectFirst(".caption h4.price");
                String title = titleElement != null ? titleElement.text() : "Sem título";
                String price = priceElement != null ? priceElement.text() : "Sem preço";

                String image = product.selectFirst("img").attr("src");
                String url = titleElement != null ? titleElement.attr("href") : "#";
                String description = product.selectFirst(".description") != null ? product.selectFirst(".description").text() : "Sem descrição";
                Elements starsElements = product.select(".ratings p[data-rating] span");

                double stars = starsElements.size();

                // Pegando o número de comentários
                Element commentsElement = product.selectFirst(".review-count");
                int numberComment = commentsElement != null ? Integer.parseInt(commentsElement.text().replaceAll("\\D+", "")) : 0;

                p.setUrl("https://webscraper.io"+url);
                p.setImage("https://webscraper.io" +image);
                p.setTitle(title);
                p.setPrice(price);
                p.setDecryption(description);
                p.setNumberComment(numberComment);
                p.setStars(stars);

                result.add(p);
            }
            return result;

        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public List<BookReview> getDataBookReview(){
        try{
            int page = 2;
            List<BookReview> result = new ArrayList<>();
            while (true){
                Document doc  = Jsoup.connect("https://iknigi.net/otzivi-na-knigi/page/"+page+"/").
                        userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36").
                        header("Accept-Language", "*").get();

                Element commentsList = doc.selectFirst("div#dle-comments-list");

                if (commentsList != null) {
                    Elements reviewElements = commentsList.select("div[id^=comment-id-]");

                    if (reviewElements.isEmpty()) {
                        System.out.println("Nenhuma resenha encontrada na página " + page);
                        break;
                    }

                    for (Element reviews : reviewElements) {

                        BookReview review = new BookReview();

                        String titleBook = reviews.select("h4 a").text().trim();
                        String linkBook = reviews.select("h4 a").attr("href").trim();
                        String categoryBok = reviews.select("span[style]").text().trim();
                        String userAvatar = reviews.select("li.comment-avatar img").attr("src").trim();
                        String imageBook = reviews.select("div.comment img").attr("src").trim();
                        String userComments = reviews.select(".comment-user-group").text().replace("Комментариев: ", "");
                        String authorBook = reviews.select("#commentator_nick").text().trim();
                        String reviewDate = reviews.select("li.comm-date meta").attr("content");
                        String reviewText = reviews.select("div[id^=comm-id]").text().trim();

                        review.setTitle(titleBook);
                        review.setReviewDate(reviewDate);
                        review.setAuthorBook(authorBook);
                        review.setReviewText(reviewText);
                        review.setCategoryBook(categoryBok);
                        review.setImageBook("https://iknigi.net"+imageBook);
                        review.setLinkBook(linkBook);
                        review.setUserAvatar(userAvatar);
                        review.setUserComments(userComments);

                        result.add(review);
                    }
                } else {
                    System.out.println("Div 'dle-comments-list' não encontrada!");
                }
                if (page == 10){
                    break;
                }
                page++;
            }
            return result;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }


    public void donwloadImage(String urlImage, String destinationFolder){
        try {
            URL url = new URL(urlImage);

            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            String fileName = url.getFile();
            fileName = fileName.substring(fileName.lastIndexOf('/') + 1);
            FileOutputStream fileOutputStream = new FileOutputStream(destinationFolder+"/"+fileName);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private  String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}


package org.example.scraper;
import org.example.models.BookReview;
import org.example.models.InternetMagazine;
import org.example.models.Product;
import org.example.models.ReviewMagazine;
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

    final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/124.0.0.0 Safari/537.36";

    private Document getDocuments(String url){
        try {
            return Jsoup.connect(url).
                    userAgent(USER_AGENT).
                    header("Accept-Language", "*").get();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Product> getDataProduct()  {
        try {
            Document doc  = getDocuments("https://webscraper.io/test-sites/e-commerce/allinone/computers/laptops");

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

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<BookReview> getDataBookReview(){
        try{
            int page = 2;
            List<BookReview> result = new ArrayList<>();
            while (true){
                Document doc  = getDocuments("https://iknigi.net/otzivi-na-knigi/page/"+page+"/");

                Element commentsList = doc.selectFirst("div#dle-comments-list");

                if (commentsList != null) {
                    Elements reviewElements = commentsList.select("div[id^=comment-id-]");

                    if (reviewElements.isEmpty()) {
                        System.out.println("The revirew elements is empty. " + page);
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
                    System.out.println("Selector on Div 'dle-comments-list' not found!");
                }
                if (page == 10){
                    break;
                }
                page++;
            }
            return result;
        }catch (Exception e) {
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

    public List<InternetMagazine> getDataInternetMagazine(){
        try {
            int page = 1;
            List<InternetMagazine> result = new ArrayList<>();

            while (true){
                Document doc = getDocuments("https://na-negative.ru/internet-magaziny?page="+page+"/");

                Elements productElements = doc.select("div.find-list-box");

                for (Element element : productElements){
                    InternetMagazine magazine = new InternetMagazine();
                    List<ReviewMagazine> reviewsList = new ArrayList<>();

                    String name = removePrefix( element.select("a.ss").text());
                    int reviewsCount = Integer.parseInt(element.select("span.num").text());
                    String urlMagazine  = "https://na-negative.ru"+element.select("div.find-list-box a.ss").first().attr("href");


                    for (int i = 0; i < 5; i++) {
                        Document getInfoMagazinePage = getDocuments(urlMagazine+"?page="+i);

                        Elements reviewElements = getInfoMagazinePage.select("div.reviewers-box");

                        for (Element reviewElement : reviewElements) {
                            ReviewMagazine review = new ReviewMagazine();

                            int id = reviewElements.indexOf(reviewElement);
                            String text = reviewElement.select("table[itemprop=\"description\"] tbody tr td[itemprop=\"reviewBody\"]").text();
                            int rating = Integer.parseInt(reviewElement.select("header.head span.info span[itemprop=\"reviewRating\"] span[itemprop=\"ratingValue\"]").text());
                            String author = reviewElement.select("header.head span.info span[itemprop=\"author\"]").text();
                            String date = reviewElement.select("header.head span.info meta[itemprop=\"datePublished\"]").attr("content");
                            List<String> pros = new ArrayList<>();
                            List<String> cons = new ArrayList<>();

                            Elements prosElements = reviewElement.select("table[itemprop=\"description\"] tbody tr td[itemprop=\"pro\"]");
                            for (Element prosElement : prosElements) {
                                pros.add(prosElement.text());
                            }

                            Elements consElements = reviewElement.select("table[itemprop=\"description\"] tbody tr td[itemprop=\"contra\"]");
                            for (Element consElement : consElements) {
                                cons.add(consElement.text());
                            }

                            review.setId(id);
                            review.setAuthor(author);
                            review.setRating(rating);
                            review.setDate(date);
                            review.setPros(pros);
                            review.setCons(cons);
                            review.setText(text);

                            reviewsList.add(review);
                        }
                    }

                    magazine.setName(name);
                    magazine.setAverageRating(reviewsCount);
                    magazine.setUrlMagazine(urlMagazine);
                    magazine.setReviews(reviewsList);

                    result.add(magazine);
                }
                if (page == 1){
                    break;
                }
                page++;
            }

            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String removePrefix(String input) {
        String prefixo = "Отзывы о ";
        if (input.startsWith(prefixo)) {
            return input.substring(prefixo.length());
        } else {
            return input;
        }
    }
}


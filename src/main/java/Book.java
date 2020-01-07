
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


import java.io.IOException;

public class Book {
    private Document document;

    public Book(String href) {
        connect(href);
    }

    private void connect(String href) {
    try {
        document =  Jsoup.connect(href).get();
    }catch (Exception e){
        e.printStackTrace();
    }
    }

    public String getTitle(){
        return document.title();
    }

    public String  getLikes(){
        Element element = document.getElementById("likes");
        return element.text();
    }
    public String  getDescription(){
        Element element = document.getElementById("description");
        return element.text();
    }
    public String  getGeners(){
        Elements elements = document.getElementsByClass("genres d-block");
        return elements.text();
    }
    public String getCommentList(){
        Elements elements = document.getElementsByClass("comment_mv1_item");
        String comment = elements.text();

        comment = comment.replaceAll("Ответить","\n\n");
        comment = comment.replaceAll("Нравится","");
        comment = comment.replaceAll("\\d{4}-\\d{2}-\\d{2}","");
        comment = comment.replaceAll("\\d{2}:\\d{2}:\\d{2}","");

        return comment;
    }

    public String getImage(){
        Elements  elements = document.getElementsByClass("cover-book");

        String url  = elements.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');","");

        return  url;
    }
    public String getAuthorName(){
        Elements elements = document.getElementsByClass("text-decoration-none column-author-name bold max-w-140 text-overflow-ellipsis");
        return elements.text();
    }
}

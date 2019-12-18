import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import org.checkerframework.checker.index.qual.EnsuresLTLengthOf;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.security.interfaces.ECKey;
import java.util.ArrayList;

public class Author {
    private Document authorDoc;
    private Document booksDoc;
    private String nameAuthor = "";
    private int valuesLikesBooks;
    private int valuesViewsBooks;
    private int valuesCommentsBooks;

    public Author(String name) {
        this.nameAuthor = name;
        connect();
    }

    public void connect() {
        try {
            authorDoc = Jsoup.connect("http://www.surgebook.com/" + nameAuthor).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getNameAuthor() {
        Elements namePerson = authorDoc.getElementsByClass("author-name bold");
        if(namePerson.text().equals("")){
            namePerson = authorDoc.getElementsByClass("author-username");
        }
        return namePerson.text();
    }

    public String getBioAuthor() {
        Elements bioPerson = authorDoc.getElementsByClass("author-bio");
        return bioPerson.text();
    }

    public String getImageAuthor() {
        Elements imagePerson = authorDoc.getElementsByClass("user-avatar");
        String url = imagePerson.attr("style");
        url = url.replace("background-image: url('", "");
        url = url.replace("');", "");
        return url;
    }

    public String getInfoPerson() {
        String info = "";
        if(this.getNameAuthor().contains("@")) {
            info += "Нік на сайті: " + getNameAuthor() + "\n";
            info += "Імя: - \n";
        }else {
            info += "Імя: " + getNameAuthor() + "\n";
        }
        info += "Статус: " + getBioAuthor() + "\n";

        Elements names = authorDoc.getElementsByClass("info-status-name");
        Elements values = authorDoc.getElementsByClass("info-status-num");

        for (int i = 0; i < names.size(); i++) {
            info += names.get(i).text() + ": " + values.get(i).text() + "\n";

        }
        info += getBooks();
        return info;
    }

    public String getBooks() {
        try {
            booksDoc = Jsoup.connect("http://surgebook.com/" + nameAuthor + "/books/all").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String text = "\n Список книг \n";
        ArrayList<String> textUrlBooks = new ArrayList<>();

        Elements books = booksDoc.getElementsByClass("book_view_mv1v2_title");
        Elements booksUrl = booksDoc.getElementsByClass("book_view_mv1v2_cover");

        for (int i = 0; i < books.size(); i++) {
            text += books.get(i).text() + "\n";
            textUrlBooks.add(booksUrl.get(i).attr("href"));

        }
        getStatistics(textUrlBooks);

        text += "\n\n Кількість лайків на книгах: " + valuesLikesBooks + "\n";
        text += "Кількість переглядів на книгах: " + valuesViewsBooks + "\n";
        text += "Кількість коментів на книгах: " + valuesCommentsBooks + "\n";

        return text;
    }

    public String getStatistics(ArrayList list) {

        for (int i = 0; i < list.size(); i++) {
            try {
                booksDoc = Jsoup.connect(list.get(i).toString()).get();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Elements elements = booksDoc.getElementsByClass("font-size-14 color-white ml-5");
            valuesLikesBooks += Integer.valueOf(elements.get(0).text());
            valuesCommentsBooks += Integer.valueOf(elements.get(1).text());
            valuesViewsBooks += Integer.valueOf(elements.get(2).text());
        }


        return "";
    }
}

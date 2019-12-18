import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;


public class Bot extends TelegramLongPollingBot {

    Book book = new Book();
    private long chat_id;

    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());

        chat_id = update.getMessage().getChatId();
        sendMessage.setText(input(update.getMessage().getText()));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private String input(String msg) {
        if (msg.contains("Hi") || msg.contains("Hello") || msg.contains("Привіт")) {
            return "Привітулі";
        }
        if (msg.contains("bookkk")) {
            return getBookInfo();
        }
        if(msg.contains("/person")){
            msg = msg.replace("/person ", "");
            return getAuthorInfo(msg);
        }
        return msg;
    }

    public String getBotUsername() {
        return "@NazarHoliBot";
    }

    public String getBotToken() {
        return "906099272:AAEs_vkxgMyBWk2J7hzC01RIbVfjtonqvkw";
    }

    public String getBookInfo() {

//        SendPhoto sendPhoto = new SendPhoto();
//
//        try (InputStream in = new URL("https://surgebook.com/uploads/user_26849/covers/gb5hlrrvru_thumb_340").openStream()){
//            Files.copy(in, Paths.get("D:\\tmp"));
//            sendPhoto.setChatId(chat_id);
//            sendPhoto.setPhoto(new File("D:\\tmp"));
//            execute(sendPhoto);
//            Files.delete(Paths.get("D:\\tmp"));
//        } catch (IOException e) {
//
//        } catch (TelegramApiException e) {
//            e.printStackTrace();
//        }
        URL url = null;
        SendPhoto sendPhoto = new SendPhoto();
        BufferedImage img = null;
        try {
            url = new URL(book.getImage());
            img = ImageIO.read(url);
        } catch (MalformedURLException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File not read");
        }
        File file = new File("D:\\tmp/downloaded.jpg");
        try {
            ImageIO.write(img, "jpg", file);
            sendPhoto.setChatId(chat_id);
            sendPhoto.setPhoto(new File("D:\\tmp/downloaded.jpg"));
            execute(sendPhoto);
        } catch (IOException e) {
            System.out.println("FIle not write");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        file.delete();

        String info = book.getTitle()
                + "\nАвтор " + book.getAuthorName()
                + "\nЖанр " + book.getGeners()
                + "\nОписание\n " + book.getDescription()
                + "\n\nКоличество лайков " + book.getLikes()
                + "\n\nПоследние коментарии\n " + book.getCommentList();
        return info;
    }

    public String getAuthorInfo(String msg) {
        Author author = new Author(msg);
        URL url = null;
        SendPhoto sendPhoto = new SendPhoto();
        BufferedImage img = null;

        try {
            url = new URL(author.getImageAuthor());
            img = ImageIO.read(url);
        } catch (MalformedURLException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("File not read");
        }
        File file = new File("D:\\tmp/downloaded.jpg");
        try {
            ImageIO.write(img, "jpg", file);
            sendPhoto.setChatId(chat_id);
            sendPhoto.setPhoto(new File("D:\\tmp/downloaded.jpg"));
            execute(sendPhoto);
        } catch (IOException e) {
            System.out.println("FIle not write");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        file.delete();
        return author.getInfoPerson();
    }
}

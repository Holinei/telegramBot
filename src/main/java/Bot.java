import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Bot extends TelegramLongPollingBot {

    Top top = new Top();
    String lastMessage;
    private long chat_id;
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
    /**
     * Метод для приема сообщений.
     * @param update Содержит сообщение от пользователя.
     */
    public void onUpdateReceived(Update update) {
        update.getUpdateId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());

        chat_id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        sendMessage.setText(getMessage(update.getMessage().getText()));
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    //    private String input(String msg) {
//        if (msg.contains("Hi") || msg.contains("Hello") || msg.contains("Привіт")) {
//            return "Привітулі";
//        }
//        if (msg.contains("bookkk")) {
//            return getBookInfo();
//        }
//        if(msg.contains("/person")){
//            msg = msg.replace("/person ", "");
//            return getAuthorInfo(msg);
//        }
//        return "Анька піська";
//    }
    public String getMessage(String msg) {
        ArrayList keyboard = new ArrayList();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        if (msg.equals("Привіт") || msg.equals("Меню")) {
            keyboard.clear();
            keyboardFirstRow.add("Популярне");
            keyboardFirstRow.add("Новини");
            keyboardSecondRow.add("Корисна інфа");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Виберіть...";
        }
        if (msg.equals("Популярне")) {
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Вірші");
            keyboardFirstRow.add("Книги");
            keyboardFirstRow.add("Меню");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Виберіть...";
        }
        if (msg.equals("Вірші") || msg.equals("Книги")) {
            lastMessage = msg;
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("Сьогодні");
            keyboardFirstRow.add("За тиждень");
            keyboardFirstRow.add("За місяць");
            keyboardFirstRow.add("За весь час");
            keyboardFirstRow.add("Меню");
            keyboard.add(keyboardFirstRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Виберіть...";
        }
        if(lastMessage.equals(("Книги"))){
            if (msg.equals("Сьогодні")){
                return getBookInfo(top.getTopBook(msg));
            }
            if (msg.equals("За місяць")){
                return getBookInfo(top.getTopBook(msg));
            }
            if (msg.equals("За весь час")){
                return getBookInfo(top.getTopBook(msg));
            }
            if (msg.equals("За тиждень")){
                return getBookInfo(top.getTopBook(msg));
            }
        }else if(lastMessage.equals(("Вірші"))){
            if (msg.equals("Сьогодні")){
                return getBookInfo(top.getTopPoems(msg));
            }
            if (msg.equals("За місяць")){
                return getBookInfo(top.getTopPoems(msg));
            }
            if (msg.equals("За весь час")){
                return getBookInfo(top.getTopPoems(msg));
            }
            if (msg.equals("За тиждень")){
                return getBookInfo(top.getTopPoems(msg));
            }
        }
        return "Аня піська";
    }

    /**
     * Метод возвращает имя бота, указанное при регистрации.
     * @return имя бота
     */
    public String getBotUsername() {
        return "@NazarHoliBot";
    }
    /**
     * Метод возвращает token бота для связи с сервером Telegram
     * @return token для бота
     */
    public String getBotToken() {
        return "906099272:AAEs_vkxgMyBWk2J7hzC01RIbVfjtonqvkw";
    }

    public String getBookInfo(String href[]) {

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
        String info = "";
        for (int i = 0; i < href.length; i++) {
            info = "";
            Book book = new Book(href[i]);


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

             info = book.getTitle()
                    + "\nАвтор " + book.getAuthorName()
                    + "\nЖанр " + book.getGeners()
                    + "\nОписание\n " + book.getDescription()
                    + "\n\nКоличество лайков " + book.getLikes()
                    + "\n\nПоследние коментарии\n " + book.getCommentList();

            SendMessage sendMessage = new SendMessage().setChatId(chat_id);

            try {
                sendMessage.setText(info);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return "лулу";
    }

    public String getTopPoem(String[] text) {
        SendMessage sendMessage = new SendMessage().setChatId(chat_id);
        for (int i = 0; i < text.length; i++) {
            try {
                sendMessage.setText(text[i]);
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
        return "\uD83D\uDC40";
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

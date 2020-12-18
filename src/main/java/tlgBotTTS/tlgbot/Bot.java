package tlgBotTTS.tlgbot;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.Voice;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Bot extends TelegramLongPollingBot {
    private Logger log;
    private final String botToken;
    private final String botUsername;
    private VoiceToText voiceToText;

    public Bot(String botToken, String botUsername, VoiceToText voiceToText) {
        this.botToken = botToken;
        this.botUsername = botUsername;
        this.voiceToText = voiceToText;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    @Override
    public void onUpdateReceived(Update update) {
        String message = update.getMessage().getText();
        Voice voice = update.getMessage().getVoice();
        if (voice.getDuration() < 30) {
            String say = getUserName(update.getMessage()) + " сказал: " + getVoiceFile(voice);
            sendMsg(update.getMessage().getChatId().toString(), say);
        } else
            sendMsg(update.getMessage().getChatId().toString(), getUserName(update.getMessage()) + " тот еще пиздабол");
    }

    public synchronized void sendMsg(String chatId, String s) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.log(Level.SEVERE, "Exception: ", e.toString());
        }
    }

    private String getUserName(Message msg) {
        User user = msg.getFrom();
        String userName = user.getUserName();
        return (userName != null) ? userName : String.format("%s %s", user.getLastName(), user.getFirstName());
    }

    private String getVoiceFile(Voice voice) {
        String text = null;
        GetFile getFile = new GetFile();
        getFile.setFileId(voice.getFileId());
        java.io.File fileOut = null;
        try {
            fileOut = File.createTempFile("voice", null);
            String filePath = execute(getFile).getFilePath();
            downloadFile(filePath, fileOut);
        } catch (TelegramApiException | IOException e) {
            e.printStackTrace();
        }
        text = voiceToText.getVoiceToText(fileOut.getAbsolutePath());
        fileOut.deleteOnExit();
        return text;
    }
}

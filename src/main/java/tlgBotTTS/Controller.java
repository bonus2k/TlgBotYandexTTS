package tlgBotTTS;

import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import tlgBotTTS.speechkit.Cloud;
import tlgBotTTS.speechkit.exception.ClientException;
import tlgBotTTS.tlgbot.Bot;
import tlgBotTTS.tlgbot.VoiceToText;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;

public class Controller {
    public static void main(String[] args) throws InterruptedException, IOException, URISyntaxException, ClientException {

        File confFile = new File(args[0]);
        Properties conf = new Properties();
        conf.load(new FileInputStream(confFile));
        Cloud cloud = new Cloud(conf.getProperty("oAuthToken"), conf.getProperty("folderid"));
        VoiceToText voiceToText = new VoiceToText(cloud);
        Bot bot = new Bot(conf.getProperty("botToken"), conf.getProperty("botUsername"), voiceToText);
        TelegramBotsApi telegramBotsApi;

        {
            try {
                telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
                telegramBotsApi.registerBot(bot);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
}

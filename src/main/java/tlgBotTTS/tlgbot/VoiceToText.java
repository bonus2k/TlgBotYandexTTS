package tlgBotTTS.tlgbot;

import org.telegram.telegrambots.meta.api.interfaces.BotApiObject;
import tlgBotTTS.speechkit.*;

import java.io.IOException;
import java.net.URISyntaxException;



public class VoiceToText implements BotApiObject {
    private Cloud cloud;

    public VoiceToText(Cloud cloud) {
        this.cloud=cloud;
    }


    public String getVoiceToText(String pathToOggSpeech) {
        String voiceToText=null;
        Text text = new Text(pathToOggSpeech);
        text.setLang(Lang.RU);
        text.setTopic(Topic.GENERAL);
        text.setFilter(Filter.FALSE);
        text.setFormat(Format.OGGOPUS);
        try {
            byte[] oggFile = cloud.request(text);
            voiceToText = new String(oggFile);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return voiceToText.substring(10, voiceToText.length()-1);
    }
}

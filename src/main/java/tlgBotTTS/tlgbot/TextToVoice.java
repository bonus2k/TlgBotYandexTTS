package tlgBotTTS.tlgbot;

import tlgBotTTS.speechkit.Cloud;
import tlgBotTTS.speechkit.Speech;
import tlgBotTTS.speechkit.Voice;
import tlgBotTTS.speechkit.exception.ClientException;

import java.io.IOException;
import java.net.URISyntaxException;

public class TextToVoice {
    private Cloud cloud;

    public TextToVoice(Cloud cloud) {
        this.cloud = cloud;
    }

    public byte[] getTextToVoice(String text) {
        byte[] oggFile = null;
        try {
            Speech speech = new Speech(text);
            speech.setVoice(Voice.random());
            oggFile = cloud.request(speech);
        } catch (ClientException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return oggFile;
    }

}

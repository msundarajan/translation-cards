package org.mercycorps.translationcards.model;

import android.os.Parcel;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mercycorps.translationcards.BuildConfig;
import org.mercycorps.translationcards.porting.JsonKeys;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;

@Config(constants = BuildConfig.class, sdk = 21)
@RunWith(RobolectricGradleTestRunner.class)
public class TranslationTest {

    @Test
    public void shouldReturnJSONObjectWithTranslationContents() throws Exception {
        Translation translation = new Translation("label", false, "filename.mp3", 1L, "translated text");

        JSONObject json = translation.toJSON();

        assertEquals(translation.getLabel(), json.getString(JsonKeys.CARD_LABEL));
        assertEquals(translation.getFilePath(), json.getString(JsonKeys.CARD_DEST_AUDIO));
        assertEquals(translation.getTranslatedText(), json.getString(JsonKeys.CARD_DEST_TEXT));
    }

    @Test
    public void shouldReturnJSONObjectWithAudioFileName() throws Exception {
        Translation translation = new Translation("label", false, "/path/to/filename.mp3", 1L, "translated text");

        JSONObject json = translation.toJSON();

        assertEquals(translation.getLabel(), json.getString(JsonKeys.CARD_LABEL));
        assertEquals("filename.mp3", json.getString(JsonKeys.CARD_DEST_AUDIO));
        assertEquals(translation.getTranslatedText(), json.getString(JsonKeys.CARD_DEST_TEXT));
    }

    @Test
    public void shouldInflateFromParcel() throws Exception {
        Translation translation = new Translation("label", false, "/path/to/filename.mp3", 1L, "translated text");
        Parcel parcel = Parcel.obtain();

        translation.writeToParcel(parcel, 1);
        parcel.setDataPosition(0); //data position must be reset before reading from parcel
        Translation fromParcel = Translation.CREATOR.createFromParcel(parcel);

        assertEquals(translation.getLabel(), fromParcel.getLabel());
        assertEquals(translation.getTranslatedText(), fromParcel.getTranslatedText());
        assertEquals(translation.getFilePath(), fromParcel.getFilePath());
        assertEquals(translation.getIsAsset(), fromParcel.getIsAsset());
        assertEquals(translation.getDbId(), fromParcel.getDbId());
    }
}
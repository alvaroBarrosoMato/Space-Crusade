package alvaro.daniel.space_crusade;

import android.content.SharedPreferences;

import java.util.Set;

/**
 * Created by Dany on 05/11/2017.
 */

public class Preferences {
    //lista de items indexados
    public static final int ENGINE_V1 = 1;
    public static final int ENGINE_V2 = 2;
    public static final int ENGINE_V3 = 3;
    public static final int AERODYNAMIC_V1 = 4;
    public static final int AERODYNAMIC_V2 = 5;
    public static final int AERODYNAMIC_V3 = 6;
    public static final int FUEL_V1 = 7;
    public static final int FUEL_V2 = 8;
    public static final int FUEL_V3 = 9;
    public static final int SHELL_V1 = 10;
    public static final int SHELL_V2 = 11;
    public static final int SHELL_V3 = 12;

    //variables guardadas en el dispositivo
    int money;
    int theme;
    int record;
    //el volumen va en una escala 0-50
    int musicVol;
    int soundsVol;
    Set<Integer> upgrades;
    boolean firstGame;

    //variables auxiliares
    boolean musicMuted;
    boolean soundsMuted;
    public int musicBefore;
    public int soundsBefore;

    SharedPreferences myPrefs;
    SharedPreferences.Editor prefsEditor;

    public Preferences(SharedPreferences prefs){
        myPrefs = prefs;
        prefsEditor = myPrefs.edit();
        //prefsEditor.clear();
        //prefsEditor.commit();
        loadPrefsValues();
    }

    private void loadPrefsValues() {
        money = myPrefs.getInt("money", 0);
        theme = myPrefs.getInt("theme", 0);
        record = myPrefs.getInt("record", 0);
        musicVol = myPrefs.getInt("musicVol", 50);
        soundsVol = myPrefs.getInt("soundsVol", 50);
        firstGame = myPrefs.getBoolean("firstGame", false);
        musicMuted = musicVol == 0;
        soundsMuted = soundsVol == 0;
        musicBefore = Math.max(15, musicVol);
        soundsBefore = Math.max(15, soundsVol);
    }

    public int getMoney(){
        return money;
    }

    public void refreshMoney(int value){
        money = value;
        prefsEditor.putInt("money", money);
        prefsEditor.apply();
    }

    public void incrementMoney(int value){
        refreshMoney(money + value);
    }

    public int getRecord(){
        return record;
    }

    public void refreshRecord(int value){
        record = value;
        prefsEditor.putInt("record", record);
        prefsEditor.apply();
    }

    public void incrementRecord(int value){
        refreshRecord(record + value);
    }

    public int getTheme(){
        return theme;
    }

    public void refreshTheme(int value){
        theme = value;
        prefsEditor.putInt("theme", theme);
        prefsEditor.apply();
    }

    public int getMusicVol(){
        return musicVol;
    }

    public void refreshMusicVol(int value){
        if(musicVol >= 15)
            musicBefore = musicVol;
        musicVol = value;
        prefsEditor.putInt("musicVol", musicVol);
        prefsEditor.apply();
        musicMuted = musicVol == 0;
    }

    public int getSoundsVol(){
        return soundsVol;
    }

    public void refreshSoundsVol(int value){
        if(soundsVol >= 15)
            soundsBefore = soundsVol;
        soundsVol = value;
        prefsEditor.putInt("soundsVol", soundsVol);
        prefsEditor.apply();
        soundsMuted = soundsVol == 0;
    }

    public boolean isFirstGame() {
        return firstGame;
    }

    public void refreshFirstGame(boolean firstGame) {
        this.firstGame = firstGame;
        prefsEditor.putBoolean("firstGame", firstGame);
        prefsEditor.apply();
    }

    public boolean isMusicMuted(){
        return musicMuted;
    }

    public boolean isSoundsMuted(){
        return soundsMuted;
    }
}

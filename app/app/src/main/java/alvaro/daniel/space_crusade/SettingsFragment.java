package alvaro.daniel.space_crusade;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Created by Dany on 20/11/2017.
 */

public class SettingsFragment extends Fragment implements View.OnClickListener{
    View layout;
    ImageButton musicMute, soundsMute;
    ImageView darkTheme, lightTheme;
    Preferences myPrefs;
    SeekBar musicSlider, soundsSlider;
    View darkThemeBorder, lightThemeBorder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //cargamos el layout para este fragmento
        layout = inflater.inflate(R.layout.fragment_settings, container, false);
        ((BaseActivity)getActivity()).setFont(layout);
        return layout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        myPrefs = BaseActivity.myPrefs;
        super.onActivityCreated(savedInstanceState);
        musicMute = (ImageButton)layout.findViewById(R.id.musicMute);
        musicMute.setOnClickListener(this);
        soundsMute = (ImageButton)layout.findViewById(R.id.soundsMute);
        soundsMute.setOnClickListener(this);
        darkTheme = (ImageView)layout.findViewById(R.id.darkTheme);
        darkTheme.setOnClickListener(this);
        lightTheme = (ImageView)layout.findViewById((R.id.lightTheme));
        lightTheme.setOnClickListener(this);
        darkThemeBorder = (View) layout.findViewById(R.id.darkThemeBorder);
        lightThemeBorder = (View) layout.findViewById(R.id.lightThemeBorder);
        musicSlider = (SeekBar) layout.findViewById(R.id.musicSlider);
        musicSlider.setMax(50);
        soundsSlider = (SeekBar) layout.findViewById(R.id.soundsSlider);
        soundsSlider.setMax(50);

        musicSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                myPrefs.refreshMusicVol(i);
                BaseActivity.setVolume(i);
                refreshViews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        soundsSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){

            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                myPrefs.refreshSoundsVol(i);
                refreshViews();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        RoundedBitmapDrawable dr = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.dark_theme));
        dr.setCornerRadius(150);
        //dr.setCircular(true);
        darkTheme.setImageDrawable(dr);

        dr = RoundedBitmapDrawableFactory.create(getResources(), BitmapFactory.decodeResource(getResources(), R.drawable.light_theme));
        dr.setCornerRadius(150);
        lightTheme.setImageDrawable(dr);
        refreshViews();
    }

    @Override
    public void onClick(View view) {
        Animation pressed_anim = AnimationUtils.loadAnimation(getContext(), R.anim.button_pressed_anim);
        view.startAnimation(pressed_anim);
        switch(view.getId()){
            case R.id.darkTheme:{
                darkThemeBorder.startAnimation(pressed_anim);
                if(myPrefs.getTheme() == 1) {
                    myPrefs.refreshTheme(0);
                    ((BaseActivity) getActivity()).recreating = true;
                    ((BaseActivity) getActivity()).recreate();
                }
                break;
            }
            case R.id.lightTheme:{
                lightThemeBorder.startAnimation(pressed_anim);
                if(myPrefs.getTheme() == 0) {
                    myPrefs.refreshTheme(1);
                    ((BaseActivity) getActivity()).recreating = true;
                    ((BaseActivity) getActivity()).recreate();
                }
                break;
            }
            case R.id.musicMute:{
                if(myPrefs.isMusicMuted()){
                    myPrefs.refreshMusicVol(myPrefs.musicBefore);
                    BaseActivity.setVolume(myPrefs.musicBefore);
                }else {
                    myPrefs.refreshMusicVol(0);
                    BaseActivity.setVolume(0);
                }
                refreshViews();
                break;
            }
            case R.id.soundsMute:{
                if(myPrefs.isSoundsMuted())
                    myPrefs.refreshSoundsVol(myPrefs.soundsBefore);
                else
                    myPrefs.refreshSoundsVol(0);
                refreshViews();
                break;
            }
        }
    }

    private void refreshViews(){
        int theme = myPrefs.getTheme();

        if(myPrefs.isMusicMuted()){
            if(theme == 0) {
                musicMute.setImageResource(R.drawable.speaker_off_2);
            }else{
                musicMute.setImageResource(R.drawable.speaker_off);
            }
        }else{
            if(theme == 0) {
                musicMute.setImageResource(R.drawable.speaker);
            }else{
                musicMute.setImageResource(R.drawable.speaker_2);
            }
        }

        if(myPrefs.isSoundsMuted()){
            if(theme == 0) {
                soundsMute.setImageResource(R.drawable.speaker_off_2);
            }else{
                soundsMute.setImageResource(R.drawable.speaker_off);
            }
        }else{
            if(theme == 0) {
                soundsMute.setImageResource(R.drawable.speaker);
            }else{
                soundsMute.setImageResource(R.drawable.speaker_2);
            }
        }

        if(theme == 0){
            darkThemeBorder.setBackgroundResource(R.drawable.border_cornered_selected);
            lightThemeBorder.setBackgroundResource(R.drawable.border_cornered);
        }else{
            darkThemeBorder.setBackgroundResource(R.drawable.border_cornered);
            lightThemeBorder.setBackgroundResource(R.drawable.border_cornered_selected);
        }

        musicSlider.setProgress(myPrefs.getMusicVol());
        soundsSlider.setProgress(myPrefs.getSoundsVol());
    }
}

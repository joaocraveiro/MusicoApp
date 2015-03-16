package com.example.musico;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.example.musico.R;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends Activity {

	MediaPlayer mediaPlayer;
	MediaPlayer myMP;
	Button playButton;
	Button composeButton;		

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		mediaPlayer = MediaPlayer.create(this, R.raw.amor);

		playButton = (Button) findViewById(R.id.play_button);
		composeButton = (Button) findViewById(R.id.compose_button);			

		playButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mediaPlayer.isPlaying()) {
					mediaPlayer.start();
				} else {
					mediaPlayer.pause();
				}
			}
		});			
			
		composeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!mediaPlayer.isPlaying()) {
					mediaPlayer.stop();
					mediaPlayer.release();
				}
				compose();								
			}
		});

	}
	
	private void compose(){
		// 1. Create some MidiTracks
		MidiTrack tempoTrack = new MidiTrack();
		MidiTrack noteTrack = new MidiTrack();

		// 2. Add events to the tracks
		// Track 0 is the tempo map
		TimeSignature ts = new TimeSignature();
		ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

		Tempo tempo = new Tempo();
		tempo.setBpm(60);

		tempoTrack.insertEvent(ts);
		tempoTrack.insertEvent(tempo);

		// Track 1 will have some notes in it
		final int NOTE_COUNT = 20;

		int[] notes = Blues.blueScale(NOTE_COUNT);
		for(int i = 0; i < NOTE_COUNT; i++)
		{
		    int channel = 0;
		    int pitch = notes[i];
		    int velocity = 100;
		    long tick = i * 120;
		    long duration = 120;

		    noteTrack.insertNote(channel, pitch, velocity, tick, duration);
		}

		// 3. Create a MidiFile with the tracks we created
		List<MidiTrack> tracks = new ArrayList<MidiTrack>();
		tracks.add(tempoTrack);
		tracks.add(noteTrack);

		MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION);
		midi.addTrack(tempoTrack);
		midi.addTrack(noteTrack);

		// 4. Write the MIDI data to a file
		File output = new File(getFilesDir(),"example.mid");			
		try
		{
		    midi.writeToFile(output);		    		    
		}
		catch(IOException e)
		{
		    System.err.println(e);
		}		   
		output.setReadable(true, false);
		mediaPlayer.release();
		mediaPlayer = MediaPlayer.create(this,Uri.fromFile(output));			    	    
	}
}

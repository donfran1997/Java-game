import java.io.*;
import javax.sound.sampled.*;


public class Sound {
	private Clip c;

	public void startSound(boolean enableS){
		try{
			if (enableS == true) {
				AudioInputStream a = AudioSystem.getAudioInputStream(new File("resource/Images/hard.wav").getAbsoluteFile());
				Clip c = AudioSystem.getClip();
				c.open(a);
				FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
				volume.setValue(-21.0f); //reduce volume by 21 decibels
				c.loop(Clip.LOOP_CONTINUOUSLY);
				c.stop();
			}
		}
		catch (Exception e){
			System.out.println("Unable to play music");
		}
	}
	
	public void main(){
		try {
			AudioInputStream a = AudioSystem.getAudioInputStream(new File("resource/Images/easy.wav").getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(a);
			c.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-20.0f); //reduce volume by 20 decibels
			c.start();
		} 
		catch(Exception e){
			System.out.println("Unable to play");
		}
	}
	
	public void easyS(){
		try {
			AudioInputStream a = AudioSystem.getAudioInputStream(new File("resource/Images/easy.wav").getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(a);
			c.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-20.0f); //reduce volume by 20 decibels
			c.start();
		} 
		catch(Exception e){
			System.out.println("Unable to play");
		}
	}
	
	public void intermediateS(){
		try{
			AudioInputStream a = AudioSystem.getAudioInputStream(new File("resource/Images/soss.wav").getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(a);
			FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-9.0f); //reduce volumne by 9 decibels
			c.loop(Clip.LOOP_CONTINUOUSLY);
			c.start();
		}
		catch(Exception e){
			System.out.println("Unable to play music");
		}
	}
	
	public void hardS(){
		try{
			AudioInputStream a = AudioSystem.getAudioInputStream(new File("resource/Images/hard.wav").getAbsoluteFile());
			Clip c = AudioSystem.getClip();
			c.open(a);
			c.loop(Clip.LOOP_CONTINUOUSLY);
			FloatControl volume = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
			volume.setValue(-15.0f); //reduce volumne by 15 decibels
			c.start();
		} 
			catch(Exception e){
				System.out.println("Unable to play music");
			}
	}
	
	public void stopSound(){
		c.stop();
	}
	
	public void closeSound(){
		c.close();
	}
}

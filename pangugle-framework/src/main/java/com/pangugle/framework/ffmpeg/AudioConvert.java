package com.pangugle.framework.ffmpeg;

import java.io.File;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;

/**
 * 音频转换器
 * @author Administrator
 *
 */
public class AudioConvert {
	
	private static Log LOG = LogFactory.getLog(AudioConvert.class);
	
	public static final String DEFAULT_CODEC = "libmp3lame"; // aac 会比 libmp3lame更好
	public static final Integer DEFAULT_BITRATE = 32000;
	public static final Integer DEFAULT_CHANNELS = 2;
	public static final Integer DEFAULT_SAMPLING_RATE = 44100;
	
	public static final String DEFAULT_FORMAT = "mp3";
	
	
	public static boolean convertMP3(File source, File target) {
        
		boolean rs = false;
		try {

			// Audio Attributes
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec(DEFAULT_CODEC);
			audio.setBitRate(DEFAULT_BITRATE);
			audio.setChannels(DEFAULT_CHANNELS);
			audio.setSamplingRate(DEFAULT_SAMPLING_RATE);

			// Encoding attributes
			EncodingAttributes attrs = new EncodingAttributes();
//			attrs.setOutputFormat(DEFAULT_FORMAT);
			attrs.setFormat(DEFAULT_FORMAT);
			attrs.setAudioAttributes(audio);

			LOG.info("source exist = " + source.exists());
			
			if(target == null)
			{
				LOG.info("target null = ");
			}
			
			
			MultimediaObject obj = new MultimediaObject(source);
			
			if(obj == null)
			{
				LOG.info("target null = ");
			}
			
			// Encode
			Encoder encoder = new Encoder();
			encoder.encode(obj, target, attrs);
			rs = true;
		} catch (Exception e) {
			LOG.error("convertMP3 errpr : ", e);
		}
		return rs;
	}

}

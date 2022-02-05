package com.pangugle.framework.ffmpeg;

import java.io.File;
import java.io.IOException;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoSize;

public class FFmpegManager {
	
	public static void main(String[] args) {
	        
		try {
			File source = new File("D:/test/test.mp4");
			File target = new File("D:/test/test_compress.mp4");
			
			VideoConvert.convertMP4(source, target, 256000, 400);
			
			
			File thumb = new File("D:/test/test_compress.jpg");
			VideoConvert.convertVideoToPicture(source, thumb);
			
			long start = System.currentTimeMillis();
			File thumb2 = new File("D:/test/test_compress2.jpg");
			VideoConvert.convertVideoToPicture(source, thumb2);
			long end = System.currentTimeMillis();
			
			System.out.println(end - start);
			
//			zipVideo(source, target, 400, 300);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void zipVideo(File source, File target, int widht, int height) throws IOException {
        try {
            // 音频编码设置
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");
            audio.setBitRate(new Integer(64000));
            audio.setChannels(new Integer(1));
            audio.setSamplingRate(new Integer(22050));

            // 视频编码设置
            VideoAttributes video = new VideoAttributes();
            video.setCodec("mpeg4");
            video.setBitRate(new Integer(160000));
            video.setFrameRate(new Integer(15));
            video.setSize(new VideoSize(widht, height));

            // 视频转码编码设置
            EncodingAttributes attrs = new EncodingAttributes();
//            attrs.setOutputFormat("mp4");
            attrs.setFormat("mp4");
            attrs.setAudioAttributes(audio);
            attrs.setVideoAttributes(video);

            // 编码器
            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(source), target, attrs);
        } catch (EncoderException e) {
            e.printStackTrace();
        }
    }

}

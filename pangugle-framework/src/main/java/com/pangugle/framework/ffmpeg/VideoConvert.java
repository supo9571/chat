package com.pangugle.framework.ffmpeg;

import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameConverter;

import com.pangugle.framework.log.Log;
import com.pangugle.framework.log.LogFactory;
import com.pangugle.framework.utils.StringUtils;

import ws.schild.jave.AudioAttributes;
import ws.schild.jave.Encoder;
import ws.schild.jave.EncodingAttributes;
import ws.schild.jave.MultimediaInfo;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.VideoAttributes;
import ws.schild.jave.VideoInfo;
import ws.schild.jave.VideoSize;

public class VideoConvert {
	
	private static Log LOG = LogFactory.getLog(VideoConvert.class);
	
	private static final String DEFAULT_FORMAT = "mp4";
	
	private static final String DEFAULT_VIDEO_CODEC = "libx264"; 
	
	public static boolean convertMP4(File source, File target, Integer bitrate, int width)
	{
		boolean rs = false;
		try {
			// Audio Attributes
			AudioAttributes audio = new AudioAttributes();
			audio.setCodec(AudioConvert.DEFAULT_CODEC);
			audio.setBitRate(AudioConvert.DEFAULT_BITRATE); // 64000
			audio.setChannels(AudioConvert.DEFAULT_CHANNELS); // 1
			audio.setSamplingRate(AudioConvert.DEFAULT_SAMPLING_RATE); // 24000
						
			// Audio Attributes
			VideoAttributes video = new VideoAttributes();
			// h264编码格式的MP4视频在网页播放兼容性更好
			video.setCodec(DEFAULT_VIDEO_CODEC);
			// 设置比特率 【256000 | 512000】
			video.setBitRate(bitrate); 
			// 设置帧率, 目前网络视频的帧数一般都是30帧/秒,最低可以降到25帧/秒。高于30帧/秒,视频格式会过大。低于25帧/秒,视频会出现卡屏现象
			video.setFrameRate(25); 
			
			MultimediaObject mediaObject = new MultimediaObject(source);
		    MultimediaInfo info = mediaObject.getInfo();
		    VideoInfo videoInfo = info.getVideo();
		    VideoSize videoSize = videoInfo.getSize();
			
			if(width > 0)
			{
			    //LOG.debug("src width = " + videoSize.getWidth() + ", height = " + videoSize.getHeight());
			    
			    int height = videoSize.getHeight();
			    // 如果原视频宽度小于目标转换宽度，则用原视频宽度
			    if(videoSize.getWidth() < width)
			    {
			    	width = videoSize.getWidth();
			    }
			    else
			    {
			    	height = width * videoSize.getHeight() / videoSize.getWidth();
			        height = height % 2 == 0 ? height : height+1; // 视频宽高必须是偶数
					
			    }
			    video.setSize(new VideoSize(width, height));
				
			    //LOG.debug("target width = " + width + ", height = " + height);
			}
			
			// Encoding attributes
			EncodingAttributes  attrs = new EncodingAttributes();
//			attrs.setOutputFormat(DEFAULT_FORMAT);
			attrs.setFormat(DEFAULT_FORMAT);
			attrs.setAudioAttributes(audio);
			attrs.setVideoAttributes(video);
			
			// Encode to mp4
			Encoder encoder = new Encoder();
			encoder.encode(mediaObject, target, attrs);
			
			rs = true;
		} catch (Exception e) {
			LOG.error("convert error:", e);
		}
		return rs;
	}
	
	
	 /**
     * 获取视频第一帧, 建议用jpg格式
     * @param videoPath 视频地址
     * @param imgPath 生成图片的名字（包含全路径）
     * @throws Exception
     */
    public static boolean convertVideoToPicture(File videoFile,File imgFile){
    	
    	boolean rs = false;
    	FFmpegFrameGrabber ff  = null;
    	try {
			String ext = FilenameUtils.getExtension(imgFile.getName());
			if(!videoFile.exists() || StringUtils.isEmpty(ext))
			{
				LOG.error("video file not exit or img path error");
				return rs;
			}
			
			//判断保存的文件的文件夹是否存在，不存在创建。
			FileUtils.forceMkdirParent(imgFile);
			
			//实例化“截取视频首帧”对象
			ff = new FFmpegFrameGrabber(videoFile);
			ff.start();
			int ftp = ff.getLengthInFrames();
			int flag=0;
			Frame frame = null;
			while (flag <= ftp) {
			    //获取帧
			    frame = ff.grabImage();
			    //过滤前1帧，避免出现全黑图片
			    if ((flag>1)&&(frame != null)) {
			        break;
			    }
			    flag++;
			}
			ImageIO.write(frameToBufferedImage(frame), ext, imgFile);
			rs = true;
			
//			LOG.info("convert picture is success ....");
			
		} catch (Exception e) {
			LOG.error("convertVideoToPicture error: ", e);
		} 
    	finally 
    	{
			try {
				ff.stop();
				ff.close();
			} catch (Exception e) {
			}
    	}
    	
        return rs;
    }

    /**
     * 帧转为流
     * @param frame
     * @return
     */
    private static RenderedImage frameToBufferedImage(Frame frame) {
        //创建BufferedImage对象
        Java2DFrameConverter converter = new Java2DFrameConverter();
        BufferedImage bufferedImage = converter.getBufferedImage(frame);
        return bufferedImage;
    }

    public static void main(String[] args)
    {
    	double rs = 3840 / 2160;
    	System.out.println("rs = " + rs);
    	long height = 400 * 3840 / 2160;
    	System.out.println("height = " + height);
    }
    
}

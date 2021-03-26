package com.cmy.media;

import android.annotation.TargetApi;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.media.MediaCodec;
import android.media.MediaFormat;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Surface;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Decoder
{
	private static final String TAG    	= "Decoder";
	private static final int width     	= 640;
	private static final int height    	= 480;
//	private  int width     ;
//	private  int height    	;

//	private static final int bitrate   	= width*height*3;
//	private static final int framerate 	= 25;

	private long m_nCount = 0;
	private Surface mSurface;
	private MediaCodec decoder;

	// 鍒濆鍖�
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public int init(Surface surface, int width1, int height1)
	{
		Log.e("VideoClient", "---Decoder init---");
		try
		{
			m_nCount = 0;
			mSurface = surface;
			decoder = MediaCodec.createDecoderByType("video/avc");
			Log.e("VideoClient", "---Decoder createDecoderByType()---");
			MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", width, height);
			decoder.configure(mediaFormat, mSurface, null, 0);
			Log.e("VideoClient", "---Decoder configure()---");
			decoder.start();
			Log.e("VideoClient", "---Decoder start()---");
		}
		catch(Exception e)
		{
			Log.e("VideoClient1", "---Decoder init----Exception:"+e);
			Log.e("VideoClient1", "---Decoder init----Exception3:"+e.getMessage());
			e.printStackTrace();
			//uninit2();
			return -1;
		}
		return 0;
	}
	// 纭В鐮�
	public int decode(byte[] buff,int nLen)
	{
		if (decoder != null)
		{
			ByteBuffer[] inputBuffers = decoder.getInputBuffers();

			ByteBuffer[] outputBuffers = decoder.getOutputBuffers();
			//Log.e("VideoClient","dequeueInputBuffer");
			int inputBufferIndex = decoder.dequeueInputBuffer(0);//涔嬪墠鏄�-1
			//Log.e("VideoClient","inputBufferIndex:"+inputBufferIndex);
			if (inputBufferIndex >= 0)
			{
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				inputBuffer.clear();
				inputBuffer.put(buff, 0, nLen);

				long nts = m_nCount*1000000/15;//杩欓噷鍙兘闇�瑕佹敼涓�25
				if (nts <= 0)
				{
					nts = 0;
					m_nCount = 0;
				}
				decoder.queueInputBuffer(inputBufferIndex, 0, nLen, nts, 0);
				m_nCount++;
			}
			// 閲婃斁缂撳啿鍖�
			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo,0);
			while (outputBufferIndex >= 0)
			{
				decoder.releaseOutputBuffer(outputBufferIndex, true);
				outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
			}
		}

		return 0;
	}
	//瑙ｇ爜
	public void offerDecoder1(byte[] buf, int length) {
		ByteBuffer[] inputBuffers = decoder.getInputBuffers();
		int inputBufferIndex = decoder.dequeueInputBuffer(0);
		Log.e("VideoClient","inputBufferIndex:"+inputBufferIndex);
		if (inputBufferIndex >= 0) {
			ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
			inputBuffer.clear();
			inputBuffer.put(buf, 0, length);
			decoder.queueInputBuffer(inputBufferIndex, 0, length, m_nCount * 1000000 / 25, 0);
			m_nCount++;
		}
		MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
		int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo,0);
		while (outputBufferIndex >= 0) {
			decoder.releaseOutputBuffer(outputBufferIndex, true);
			outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
		}
	}
	//瑙ｇ爜
	public void offerDecoder(byte[] buf, int length) {
		try {
			ByteBuffer[] inputBuffers = decoder.getInputBuffers();
			int inputBufferIndex = decoder.dequeueInputBuffer(0);
			Log.e("VideoClient","inputBufferIndex:"+inputBufferIndex);
			if (inputBufferIndex >= 0) {
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				inputBuffer.clear();
				inputBuffer.put(buf, 0, length);
				decoder.queueInputBuffer(inputBufferIndex, 0, length, m_nCount * 1000000 / 25, 0);
				m_nCount++;
			}
			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo,0);
			while (outputBufferIndex >= 0) {
				decoder.releaseOutputBuffer(outputBufferIndex, true);
				outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
			}
		}catch (Exception e){
			Log.e("VideoClient", "-----3-----");
			Log.e("VideoClient", "Decoder decode fail:"+e);
		}

	}
	/**
	 * 瑙ｇ爜 澶勭悊鏁版嵁
	 *
	 * @param buf
	 * h264瀛楄妭瑙嗛鏁版嵁
	 *//*
	public void onFrame(byte[] buf) {
		ByteBuffer[] inputBuffers = decoder.getInputBuffers();
		int inputBufferIndex = decoder.dequeueInputBuffer(-1);
		Log.e(TAG, "inputBufferIndex===" + inputBufferIndex);
		if (inputBufferIndex >= 0) {
			ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
			inputBuffer.clear();
			inputBuffer.put(buf, 0, buf.length);
			decoder.queueInputBuffer(inputBufferIndex, 0, buf.length,
					presentationTimeStamp, 0);
			presentationTimeStamp++;
		}

		MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
		int outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);// 寤惰繜杈撳嚭缂撳啿鍖�                                         // 寰鏃堕棿
		while (outputBufferIndex >= 0) {
			decoder.releaseOutputBuffer(outputBufferIndex, true);
			outputBufferIndex = decoder.dequeueOutputBuffer(bufferInfo, 0);
		}

	}*/
	private void YUVToJPEG(byte[] bytes)
	{
		YuvImage yuv_image = new YuvImage(bytes, ImageFormat.NV21, 720, 576, null);

		// Convert YUV to Jpeg
		Rect rect = new Rect(0, 0, 720, 576);
		ByteArrayOutputStream output_stream = new ByteArrayOutputStream();
		yuv_image.compressToJpeg(rect, 100, output_stream);
		byte[] byt = output_stream.toByteArray();
		SaveJPEG(byt);
	}

	private void SaveJPEG(byte[] bytes)
	{
		FileOutputStream outStream = null;

		try
		{
			// Write to SD Card
			Log.e(TAG, "outputBuffers Write to SD Card");
			String url = Environment.getExternalStorageDirectory().getPath()
					+"/Image_"+ System.currentTimeMillis()+".jpg";

			outStream = new FileOutputStream(url);
			outStream.write(bytes);
			outStream.close();
		}
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}


	// 閲婃斁
	public void uninit()
	{
		if (decoder != null)
		{
			try
			{
				decoder.stop();
				decoder.release();
				decoder=null;//鍚庨潰鍔犱笂鍘荤殑

			}
			catch(Exception e)
			{
				Log.e("VideoClient","Exception:"+e);
				e.printStackTrace();
			}
		}
	}

	// 閲婃斁
	@TargetApi(Build.VERSION_CODES.LOLLIPOP)
	public void uninit2()
	{
		if (decoder != null)
		{
			try
			{
				decoder.stop();
				decoder.release();
				decoder.reset();
				decoder=null;//鍚庨潰鍔犱笂鍘荤殑

			}
			catch(Exception e)
			{
				Log.e("VideoClient","Exception:"+e);
				e.printStackTrace();
			}
		}
	}
}

package com.cmy.media;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.PreviewCallback;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.media.MediaFormat;
import android.util.Log;
import android.view.SurfaceHolder;

import java.nio.ByteBuffer;

public class Encoder
{

	public Camera m_camera;
	private MediaCodec encoder;

	byte[] m_info = null;
	private long m_nCount = 0;
	private int m_SupportColorFormat = -1;
	private static int m_width = 640;
	private static int m_height = 480;
	private byte[] yuv420 = new byte[m_width*m_height*3/2];
	private byte[] output = new byte[100*1024];

	
	

	// 鍒濆鍖栭瑙堣棰�
	public boolean initPreview(SurfaceHolder surfaceHolder)
	{
		try
		{
			m_camera = Camera.open();
			m_camera.setPreviewDisplay(surfaceHolder);
			Camera.Parameters parameters = m_camera.getParameters();
			/*
			List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
			if (sizeList.size() > 0)
			{
				for (int i = sizeList.size()-1; i >= 0; i--)
				{
					Camera.Size cur = sizeList.get(i);
					if (cur.width >= m_width  && cur.height >= m_height)
					{
						m_width = cur.width;
						m_height = cur.height;
						String strWH = String.format("Local Camera Width=%d Height=%d", m_width,m_height);
						Log.e("VideoClient", strWH);
						break;
					}
				}
			}
			*/
			parameters.setPreviewSize(m_width, m_height);
			parameters.setPictureSize(m_width, m_height);
			parameters.setPreviewFormat(ImageFormat.YV12);
			m_camera.setParameters(parameters);
			m_camera.setPreviewCallback(mPreviewCallback);
			m_camera.startPreview();
			m_camera.setDisplayOrientation(90);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// 鏈湴闀滃ご瑙嗛鏁版嵁鍥炶皟
	private PreviewCallback mPreviewCallback = new PreviewCallback()
	{
		public void onPreviewFrame(byte[] arg0, Camera arg1)
		{
			h264Encoder(arg0,0,arg0.length,0);
		}
	};

	// 閲婃斁棰勮瑙嗛
	public void uninitPreview()
	{
		try
		{
			if (m_camera!=null){
				m_camera.setPreviewCallback(null);
				m_camera.stopPreview();
				m_camera.release();
			}

		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	// 鍒濆鍖栫紪鐮佸櫒
	public boolean initEncoder()
	{
		try
		{
			m_nCount = 0;
			//m_SupportColorFormat = getSupportColorFormat();
			encoder = MediaCodec.createEncoderByType("video/avc");
            MediaFormat mediaFormat = MediaFormat.createVideoFormat("video/avc", m_width, m_height);
//			if (m_width == 640) {
//				mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 680 * 1024);
//			} else {
//				mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 960 * 1024);
//			}

            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 500000);
            mediaFormat.setInteger(MediaFormat.KEY_FRAME_RATE, 25);
//			mediaFormat.setInteger(MediaFormat.KEY_BITRATE_MODE, MediaCodecInfo.EncoderCapabilities.BITRATE_MODE_CQ);
            mediaFormat.setInteger(MediaFormat.KEY_COLOR_FORMAT, MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar);
            mediaFormat.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, 1);
            encoder.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            encoder.start();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		return true;
	}

	// h264缂栫爜
	public byte[]  h264Encoder(byte[] input, int offset, int length, int flag)
	{


		int nLen = 0;
		//swapYV12toI420(input, yuv420, m_width, m_height);
        System.arraycopy(input,0,yuv420,0,input.length);

		try
		{
			ByteBuffer[] inputBuffers = encoder.getInputBuffers();
			ByteBuffer[] outputBuffers = encoder.getOutputBuffers();
			int inputBufferIndex = encoder.dequeueInputBuffer(-1);
			if (inputBufferIndex >= 0)
			{
				ByteBuffer inputBuffer = inputBuffers[inputBufferIndex];
				inputBuffer.clear();
				inputBuffer.put(yuv420);

				long nts = m_nCount*1000000/15;
				if (nts <= 0)
				{
					nts = 0;
					m_nCount = 0;
				}
				encoder.queueInputBuffer(inputBufferIndex, 0, yuv420.length, m_nCount, 0);
				m_nCount++;
			}

			MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
			//鐩告満棰勮甯х巼涓�25锛屾晠璁剧疆瓒呮椂涓�40ms锛岃嫢璁剧疆0鍒檕utputBufferIndex鍙兘灏忎簬0瀵艰嚧椹禌鍏嬮棶棰�
			int outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 40000);

			while (outputBufferIndex >= 0)
			{
				ByteBuffer outputBuffer = outputBuffers[outputBufferIndex];

				byte[] outData = new byte[bufferInfo.size];
				outputBuffer.get(outData);

				if (m_info != null)
				{
					System.arraycopy(outData, 0, output, nLen, outData.length);
					nLen += outData.length;
				}
				else
				{
					// 淇濆瓨pps sps 鍙湁寮�濮嬫椂 绗竴涓抚閲屾湁锛� 淇濆瓨璧锋潵鍚庨潰鐢�
					ByteBuffer spsPpsBuffer = ByteBuffer.wrap(outData);
					if (spsPpsBuffer.getInt() == 0x00000001)
					{
						m_info = new byte[outData.length];
						System.arraycopy(outData, 0, m_info, 0, outData.length);
					}
					else
					{
						return new byte[0];
					}
				}

				encoder.releaseOutputBuffer(outputBufferIndex, false);
				outputBufferIndex = encoder.dequeueOutputBuffer(bufferInfo, 0);

				// key frame 缂栫爜鍣ㄧ敓鎴愬叧閿抚鏃跺彧鏈� 00 00 00 01 65锛� 娌℃湁pps sps锛� 瑕佸姞涓�
				if ((output[4] & 0x1F)==5)
				{
					//String strWH = String.format("IFrame=%X", m_info[4]);
					//Log.e("VideoClient", strWH);

					System.arraycopy(output, 0, yuv420, 0, nLen);
					System.arraycopy(m_info, 0, output, 0, m_info.length);
					System.arraycopy(yuv420, 0, output, m_info.length, nLen);
					nLen += m_info.length;
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}

		if (nLen > 0) {
			byte[] out = new byte[nLen];
			System.arraycopy(output, 0, out, 0, nLen);
		//	Log.e("$$$$$$$$$$$$$$$$", "h264Encoder: len->" + out.length);
			return out;
		}
		return new byte[0];
	}

	private void swapYV12toI420(byte[] yv12bytes, byte[] i420bytes, int width, int height) {
		Log.e("$$$$$$$$$$$", "swapYV12toI420: " + (width * height) + ",:" + (width * height / 4));
		System.arraycopy(yv12bytes, 0, i420bytes, 0, width * height);//src.length=307200 srcPos=384000 dst.length=460800 dstPos=307200 length=76800
		System.arraycopy(yv12bytes, width * height + width * height / 4, i420bytes, width * height, width * height / 4);
		System.arraycopy(yv12bytes, width * height, i420bytes, width * height + width * height / 4, width * height / 4);
	}

	// 鑾峰彇璁惧鏀寔棰滆壊鏍煎紡
	private int getSupportColorFormat()
	{
		int numCodecs = MediaCodecList.getCodecCount();
		MediaCodecInfo codecInfo = null;
		for (int i = 0; i < numCodecs && codecInfo == null; i++)
		{
			MediaCodecInfo info = MediaCodecList.getCodecInfoAt(i);
			if (!info.isEncoder())
				continue;

			String[] types = info.getSupportedTypes();
			boolean found = false;
			for (int j = 0; j < types.length && !found; j++)
			{
				if (types[j].equals("video/avc"))
				{
					System.out.println("found");
					found = true;
				}
			}
			if (!found)
				continue;

			codecInfo = info;
		}

		//Log.e("AvcEncoder", "Found " + codecInfo.getName() + " supporting " + "video/avc");

		// Find a color profile that the codec supports
		MediaCodecInfo.CodecCapabilities capabilities = codecInfo.getCapabilitiesForType("video/avc");
		//Log.e("AvcEncoder","length-" + capabilities.colorFormats.length + "==" + Arrays.toString(capabilities.colorFormats));

		for (int i = 0; i < capabilities.colorFormats.length; i++)
		{
			switch (capabilities.colorFormats[i])
			{
				case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420SemiPlanar:
				case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420Planar:
				case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedSemiPlanar:
				case MediaCodecInfo.CodecCapabilities.COLOR_FormatYUV420PackedPlanar:
				case MediaCodecInfo.CodecCapabilities.COLOR_QCOM_FormatYUV420SemiPlanar:
				case MediaCodecInfo.CodecCapabilities.COLOR_TI_FormatYUV420PackedSemiPlanar:

					Log.e("AvcEncoder", "supported color format::" + capabilities.colorFormats[i]);
					return capabilities.colorFormats[i];
				default:
					//Log.e("AvcEncoder", "unsupported color format " + capabilities.colorFormats[i]);
					break;
			}
		}

		return -1;
	}

	public static void YV12toYUV420SemiPlanar(final byte[] input, final byte[] output, final int width,final int height)
	{

		final int nLenY = width * height;
		final int nLenU = nLenY / 4;

		System.arraycopy(input, 0, output, 0, nLenY); // Y
		for (int i = 0; i < (nLenU); i++)
		{
			output[nLenY + i * 2] = input[nLenY + nLenU + i]; // Cr (V)
			output[nLenY + i * 2 + 1] = input[nLenY + i]; // Cb (U)
		}
	}

	// 閲婃斁缂栫爜鍣�
	public boolean uninitEncoder()
	{
		return true;
	}
}

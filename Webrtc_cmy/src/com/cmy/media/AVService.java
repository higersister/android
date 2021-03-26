package com.cmy.media;

public class AVService {

	static {
		System.loadLibrary("cmy_media");
	}

	public native boolean init();

	public native boolean startServer();

	public native void stopServer();

	public native void sendVideoToRemote(String ip, byte[] buff, int len);

	public native void sendAudioToRemote(String ip, short[] buff, int len);

	private Callback callback;

	public void setCallback(Callback callback) {
		this.callback = callback;
	}

	public void onVideoCallback(byte[] data, int len) {
		if (null != callback)
			callback.onVideoCallback(data, len);
	}

	public void onAudioCallback(short[] data, int len) {
		if (null != callback)
			callback.onAudioCallback(data, len);
	}

	public interface Callback {

		void onVideoCallback(byte[] data, int len);

		void onAudioCallback(short[] data, int len);

	}

}

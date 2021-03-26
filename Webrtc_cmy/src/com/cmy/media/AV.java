package com.cmy.media;

public class AV {

	static {
		System.loadLibrary("cmy_media");
	}

	// private AV() {
	//
	// }

	// public static AV getInstance(){
	// if(null == av){
	// synchronized (AV.class){
	// if(null == av)
	// av = new AV();
	// }
	// }
	// return av;
	// }
	//
	// private static AV av;

	private AVCallback avCallback;

	public void setAVCallback(AVCallback callback) {
		avCallback = callback;
	}

	public void onServerVideo(byte[] data, int len) {
		if (null != avCallback)
			avCallback.onServerVideo(data, len);
	}

	public void onClientAudio(short[] buff, int len) {
		if (null != avCallback) {
			avCallback.onClientAudio(buff, len);
		}
	}

	public native boolean startServer();

	public native void stopServer();

	public native boolean startConnectServer(String ip, int port);

	public native void disconnectServer();

	public native void sendDataToServer(String ip,byte[] data, int len);

	public native void sendDataToClient(String ip, short[] data, int len);

	public native void init();

	public interface AVCallback {

		void onServerVideo(byte[] data, int len);

		void onClientAudio(short[] data, int len);

	}

}

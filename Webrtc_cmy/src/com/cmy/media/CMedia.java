package com.cmy.media;

public class CMedia {

	private CMedia() {
	}

	static {
		System.loadLibrary("media");
	}
	
	public static native boolean startConnectServer(String ip,int port);

}

#include <jni.h>
#include "datatype.h"
#include "AVService.h"
#include "SpeexCoder.h"
#include "webrtc/modules/audio_processing/aecm/echo_control_mobile.h"

JavaVM* g_jvm = NULL;
jobject g_obj = NULL;
void* g_aecmInst = NULL;
char m_speexEncBuf[100];
short m_speexDecBuf[160];
short m_aecmBuf[160];
int g_nAudioRate = 16000;
static short g_outAudioBuf[160];

CSpeexCoder g_speex;

void CALLBACK onVideoCallback(unsigned char* pData, int nLen) {
	JNIEnv *env;
	g_jvm->AttachCurrentThread(&env, NULL);
	jclass cls = env->GetObjectClass(g_obj);

	jmethodID mid = env->GetMethodID(cls, "onVideoCallback", "([BI)V");
	if (mid == NULL) {
		LOGE("find method onServerVideo fail");
		return;
	}

	jbyteArray jbarray = env->NewByteArray(nLen);
	jbyte *jy = (jbyte*) pData;
	env->SetByteArrayRegion(jbarray, 0, nLen, jy);

	env->CallVoidMethod(g_obj, mid, jbarray, nLen);
	env->DeleteLocalRef(jbarray);
	g_jvm->DetachCurrentThread();
}

void CALLBACK onAudioCallback(unsigned char* pData, int nLen) {

	JNIEnv *env;
	g_jvm->AttachCurrentThread(&env, NULL);
	jclass cls = env->GetObjectClass(g_obj);

	jmethodID mid = env->GetMethodID(cls, "onAudioCallback", "([SI)V");
	if (mid == NULL) {
		LOGE("find method onClientAudio fail");
		return;
	}
	LOGE("onClientAudio.len->%d", nLen);
	char data[nLen];
	memcpy(data, pData, nLen);

	g_speex.Decoder(data, nLen, m_speexDecBuf);

	if (WebRtcAecm_BufferFarend(g_aecmInst, m_speexDecBuf, 160) != 0) {
		LOGE("WebRtcAecm_BufferFarend fail len=%d", 160);
		return;
	}

	jshortArray arr = env->NewShortArray(160);
	env->SetShortArrayRegion(arr, 0, 160, (const jshort*) m_speexDecBuf);
	env->CallVoidMethod(g_obj, mid, arr, 160);
	env->DeleteLocalRef(arr);
	g_jvm->DetachCurrentThread();
}

AVService service(onVideoCallback, onAudioCallback);

extern "C" {

bool jstringTostring1(JNIEnv *env, jstring ip, char *pIP, int nLen) {
	jclass clsstring = env->FindClass("java/lang/String");
	jstring strencode = env->NewStringUTF("utf-8");
	jmethodID mid = env->GetMethodID(clsstring, "getBytes",
			"(Ljava/lang/String;)[B");
	jbyteArray barr = (jbyteArray) env->CallObjectMethod(ip, mid, strencode);
	jsize alen = env->GetArrayLength(barr);
	jbyte* ba = env->GetByteArrayElements(barr, JNI_FALSE);
	if (alen <= 0 || alen >= nLen)
		return false;

	if (pIP != NULL)
		memcpy(pIP, ba, alen);

	env->ReleaseByteArrayElements(barr, ba, 0);
	return true;
}

/*
 * Class:     com_cmy_media_AVService
 * Method:    init
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_cmy_media_AVService_init(JNIEnv *env,
		jobject obj) {
	env->GetJavaVM(&g_jvm);
	g_obj = env->NewGlobalRef(obj);
	g_aecmInst = WebRtcAecm_Create();
	if (g_aecmInst == NULL) {
		LOGE("WebRtcAecm_Create fail");
		return false;
	}
	if (WebRtcAecm_Init(g_aecmInst, g_nAudioRate) == -1) {
		LOGE("WebRtcAecm_Init fail");
		WebRtcAecm_Free(g_aecmInst);
		g_aecmInst = NULL;
		return false;
	}
	if (!g_speex.Init()) {
		LOGE("Speex Init fail");
		return false;
	}
	LOGE("system init success");
	return true;
}

/*
 * Class:     com_cmy_media_AVService
 * Method:    startServer
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_com_cmy_media_AVService_startServer(
		JNIEnv * env, jobject obj) {
	service.startServer();
	return true;
}

/*
 * Class:     com_cmy_media_AVService
 * Method:    stopServer
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_com_cmy_media_AVService_stopServer
(JNIEnv *env, jobject obj) {
	service.stopServer();
}

/*
 * Class:     com_cmy_media_AVService
 * Method:    sendVideoToRemote
 * Signature: (Ljava/lang/String;[BI)V
 */
JNIEXPORT void JNICALL Java_com_cmy_media_AVService_sendVideoToRemote
(JNIEnv * env, jobject obj,jstring ip, jbyteArray data, jint len) {
	char pBuf[300 * 1024];
	char serverIp[50];
	memset(serverIp, 0, sizeof(serverIp));
	if (!jstringTostring1(env, ip, serverIp, sizeof(serverIp)))
	return;
	env->GetByteArrayRegion(data, 0, len, (jbyte*)pBuf);

	//LOGE(" send to ip=%s",serverIp);
	char type = '0';
	service.sendDataToRemote(serverIp,pBuf,len,type);
}

/*
 * Class:     com_cmy_media_AVService
 * Method:    sendAudioToRemote
 * Signature: (Ljava/lang/String;[SI)V
 */
JNIEXPORT void JNICALL Java_com_cmy_media_AVService_sendAudioToRemote
(JNIEnv *env, jobject obj, jstring ip, jshortArray data, jint len) {
	char clientIp[50];
	memset(clientIp, 0, sizeof(clientIp));
	if (!jstringTostring1(env, ip, clientIp, sizeof(clientIp)))
	return;

	env->GetShortArrayRegion(data, 0, len, (jshort*)g_outAudioBuf);

	if (WebRtcAecm_Process(g_aecmInst, g_outAudioBuf, NULL, m_aecmBuf, len, 50)
			!= 0) {
		LOGE("WebRtcAecm_Process fail len=%d", len);
		return;
	}

	int nEncLen = g_speex.Encoder(m_aecmBuf, m_speexEncBuf);
	char szAudio[100];
	memcpy(szAudio, m_speexEncBuf, nEncLen);

	//LOGE(" send to ip=%s",clientIp);
	char type = '1';
	service.sendDataToRemote(clientIp,szAudio,nEncLen,type);
}

}

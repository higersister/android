#ifndef __AV_SERVICE_H_
#define __AV_SERVICE_H_

#include "datatype.h"

typedef void (CALLBACK*callback)(unsigned char* pData, int nLen);

class AVService {

public:
	AVService(callback videoCallback, callback audioCallback);
	bool startServer();
	void stopServer();
	void sendDataToRemote(char* remoteIp, char* data, int len, char type);

private:
	int m_svrSocket;
	pthread_t m_workThread;
	callback m_pVideoCallBack;
	callback m_pAudioCallBack;
	static void *stWorkThread(void *arg);
	int OnSocketRead();
	void WorkThread();

};

#endif

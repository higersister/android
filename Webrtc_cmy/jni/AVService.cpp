#include "AVService.h"

AVService::AVService(callback videoCallback, callback audioCallback) {
	m_pVideoCallBack = videoCallback;
	m_pAudioCallBack = audioCallback;
	m_svrSocket = -1;
	m_workThread = -1;
}

bool AVService::startServer() {

	if (pthread_create(&m_workThread, NULL, stWorkThread, this) != 0) {
		LOGE("stEpollWaitThread create fail");
		close(m_svrSocket);
		m_svrSocket = -1;
		return false;
	}
	return true;

}

void *AVService::stWorkThread(void *arg) {
	AVService *pvs = (AVService*) arg;
	pvs->WorkThread();
	return NULL;
}

void AVService::WorkThread() {

	m_svrSocket = socket(AF_INET, SOCK_DGRAM, 0);
	if (m_svrSocket < 0) {
		LOGE( " create socket error: %s(errno: %d)", strerror(errno), errno);
		return;
	}
	struct sockaddr_in addr_serv;
	int len;
	memset(&addr_serv, 0, sizeof(struct sockaddr_in));
	addr_serv.sin_family = AF_INET;
	addr_serv.sin_port = htons(SERVER_PORT);
	addr_serv.sin_addr.s_addr =
			INADDR_ANY/*htonl(INADDR_ANY)*//*inet_addr("192.168.88.157")*/;
	len = sizeof(addr_serv);

	LOGE( " binding...");
	if (bind(m_svrSocket, (struct sockaddr *) &addr_serv, sizeof(addr_serv))
			< 0) {
		LOGE( " bind error: %s(errno: %d)", strerror(errno), errno);
		return;
	}

	LOGE( " starting receive...");
	struct timeval tv_out;
	fd_set fd_read;

	while (1) {
		if (m_svrSocket == -1) {
			LOGE("Exit  WorkThread");
			return;
		}
		tv_out.tv_sec = 1;
		tv_out.tv_usec = 0;
		FD_ZERO(&fd_read);
		if (m_svrSocket != -1)
			FD_SET(m_svrSocket, &fd_read);
		if (select(m_svrSocket + 1, &fd_read, NULL, NULL, &tv_out) == -1) {
			if (errno == EINTR)
				continue;
		}

		if (FD_ISSET(m_svrSocket, &fd_read)) {
			OnSocketRead();
		} else {
			LOGE("waiting read...");
		}

	}

}

int AVService::OnSocketRead() {
	struct sockaddr_in from;
	int receiveCount = 64 * 1024;
	unsigned char recvBuffer[receiveCount];
	int addrlen = sizeof(struct sockaddr_in);
	int ret = recvfrom(m_svrSocket, recvBuffer, receiveCount, 0,
			(struct sockaddr *) &from, &addrlen);
	if (ret > 0) {
		char type[1];
		memcpy(type, recvBuffer, sizeof(char));
		//LOGE("type->%c", type[0]);
		if (type[0] == '0')
			m_pVideoCallBack(recvBuffer + sizeof(char), ret - sizeof(char));
		else
			m_pAudioCallBack(recvBuffer + sizeof(char), ret - sizeof(char));
	} else {
		if (errno == EAGAIN)
			return 0;

		LOGE("OnSocketRead recv error: %s(errno: %d)", strerror(errno), errno);
		shutdown(m_svrSocket, SHUT_RDWR);
		close(m_svrSocket);
		m_svrSocket = -1;
		return -1;
	}
	return 0;
}

void AVService::sendDataToRemote(char* remoteIp, char* data, int len,
		char type) {
	if (m_svrSocket < 0) {
		LOGE( "server socket error ->%d", m_svrSocket);
		return;
	}
	//LOGE("sizeof(data)->%d", sizeof(data));
	struct sockaddr_in addr_serv;
	memset(&addr_serv, 0, sizeof(addr_serv));
	addr_serv.sin_family = AF_INET;
//	LOGE("sendDataToRemote.remoteIp->%s",remoteIp);
	addr_serv.sin_port = htons(SERVER_PORT);
	addr_serv.sin_addr.s_addr = inet_addr(remoteIp);
	char buff[len + sizeof(char)];
	//LOGE("TYPE->%c",type);
	memcpy(buff, &type, sizeof(char));
	memcpy(buff + sizeof(char), data, len);

	int ret = sendto(m_svrSocket, buff, len + sizeof(char), 0,
			(struct sockaddr *) &addr_serv, sizeof(addr_serv));
	if (ret < 0) {
		LOGE( " send data errno %d\r\n", errno);
		return;
	}
	//LOGE("send data length->%d", len + sizeof(char));

}

void AVService::stopServer() {
	LOGE(" disconnect------>");
	if (m_svrSocket != -1) {
		shutdown(m_svrSocket, SHUT_RDWR);
		close(m_svrSocket);
		m_svrSocket = -1;
	}

	if (m_workThread != -1) {
		pthread_join(m_workThread, NULL);
		m_workThread = -1;
	}
}

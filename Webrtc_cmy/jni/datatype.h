#ifndef __DATATYPE_H_
#define __DATATYPE_H_

#include <stdio.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <fcntl.h>
#include <unistd.h>
#include <pthread.h>
#include <string.h>
#include <errno.h>
#include <asm/ioctls.h>

#include<Android/log.h>

#include <string>
#include <deque>
#include <map>
using namespace std;

#define CALLBACK

#define SERVER_PORT   9527

#define TAG    		"$$$$$AV$$$$$" // 这个是自定义的LOG的标识
#define LOGD(...)	__android_log_print(ANDROID_LOG_DEBUG,TAG,__VA_ARGS__)
#define LOGE(...)   __android_log_print(ANDROID_LOG_ERROR,TAG,__VA_ARGS__)

#endif

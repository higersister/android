#ifndef _WEBRTC_NS_H_
#define _WEBRTC_NS_H_

#include <android/log.h>

#define TAG    "$$$$webrtc_ns$$$$"
#define LOGE(format, ...)  __android_log_print(ANDROID_LOG_ERROR,  TAG, format, ##__VA_ARGS__)
#define LOGD(format, ...)  __android_log_print(ANDROID_LOG_DEBUG,  TAG, format, ##__VA_ARGS__)
#define LOGI(format, ...)  __android_log_print(ANDROID_LOG_INFO,  TAG, format, __VA_ARGS__)


#ifdef __cplusplus
extern "C" {
#endif

int w_create();

int w_free();

int w_process(short* inData,short* outData);


#ifdef __cplusplus
}
#endif

#endif

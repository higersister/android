LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)

LOCAL_MODULE := webrtc_ns
LOCAL_CFLAGS += -DWEBRTC_POSIX

LOCAL_SRC_FILES := $(wildcard $(LOCAL_PATH)/*.c)


LOCAL_LDLIBS :=-llog

include $(BUILD_SHARED_LIBRARY)

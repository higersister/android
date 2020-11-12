LOCAL_PATH:= $(call my-dir)
include $(CLEAR_VARS)
APP_PROJECT_PATH := $(call my-dir)/../
APP_STL := gnustl_static

APP_BUILD_SCRIPT:=$(call my-dir)/Android.mk
APP_PLATFORM := android-22
APP_ABI := armeabi-v7a,armeabi
APP_CFLAGS:=-DDISABLE_NEON 



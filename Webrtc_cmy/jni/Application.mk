#APP_ABI := all

include $(CLEAR_VARS)
APP_ABI := armeabi armeabi-v7a

# ndk ʹ��vector��string��stl��ķ���   
#APP_STL := stlport_shared
#APP_STL := stlport_static
APP_STL := gnustl_static
APP_CFLAGS:=-DDISABLE_NEON 


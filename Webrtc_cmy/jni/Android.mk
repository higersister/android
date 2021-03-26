# Copyright (C) 2009 The Android Open Source Project
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#      http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#
LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

LOCAL_MODULE 	:= cmy_media
LOCAL_CFLAGS 	:= -DWEBRTC_POSIX -DFIXED_POINT -DUSE_KISS_FFT -DEXPORT="" -UHAVE_CONFIG_H
AUDIO_SRC_PATH 	:=$(LOCAL_PATH)
LOCAL_CPPFLAGS 	:=-std=c++11
LOCAL_LDLIBS 	:=-llog -lc

LOCAL_C_INCLUDES := \
$(LOCAL_PATH)/webrtc \
$(LOCAL_PATH)/speex/include \
$(LOCAL_PATH)/ \

LOCAL_SRC_FILES  := \
$(LOCAL_PATH)/webrtc_ns.c \
$(LOCAL_PATH)/SpeexCoder.cpp \
$(LOCAL_PATH)/AVService.cpp \
$(LOCAL_PATH)/com_cmy_media_AVService.cpp \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/aecm/echo_control_mobile.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/aecm/aecm_core.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/utility/delay_estimator_wrapper.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/aecm/aecm_core_c.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/utility/delay_estimator.cc \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/spl_init.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/ring_buffer.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/real_fft.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/division_operations.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/min_max_operations.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/cross_correlation.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/downsample_fast.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/vector_scaling_operations.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/complex_bit_reverse.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/complex_fft.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/randomization_functions.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/spl_sqrt_floor.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/ns/noise_suppression.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/ns/noise_suppression_x.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/ns/ns_core.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/ns/nsx_core.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/ns/nsx_core_c.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/fft4g.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/copy_set_operations.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/energy.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/get_scaling_square.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/resample_by_2.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/spl_sqrt.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/dot_product_with_scale.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/agc/utility.cc \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/agc/legacy/analog_agc.c \
$(AUDIO_SRC_PATH)/webrtc/modules/audio_processing/agc/legacy/digital_agc.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/vad/webrtc_vad.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/vad/vad_sp.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/vad/vad_core.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/vad/vad_gmm.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/vad/vad_filterbank.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/resample_48khz.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/resample_by_2_internal.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/resample_fractional.c \
$(AUDIO_SRC_PATH)/webrtc/common_audio/signal_processing/splitting_filter.c \
speex/libspeex/bits.c \
speex/libspeex/buffer.c \
speex/libspeex/cb_search.c \
speex/libspeex/exc_5_64_table.c \
speex/libspeex/exc_5_256_table.c \
speex/libspeex/exc_8_128_table.c \
speex/libspeex/exc_10_16_table.c \
speex/libspeex/exc_10_32_table.c \
speex/libspeex/exc_20_32_table.c \
speex/libspeex/fftwrap.c \
speex/libspeex/filterbank.c \
speex/libspeex/filters.c \
speex/libspeex/gain_table.c \
speex/libspeex/gain_table_lbr.c \
speex/libspeex/hexc_10_32_table.c \
speex/libspeex/hexc_table.c \
speex/libspeex/high_lsp_tables.c \
speex/libspeex/jitter.c \
speex/libspeex/kiss_fft.c \
speex/libspeex/kiss_fftr.c \
speex/libspeex/lpc.c \
speex/libspeex/lsp.c \
speex/libspeex/lsp_tables_nb.c \
speex/libspeex/ltp.c \
speex/libspeex/mdf.c \
speex/libspeex/modes.c \
speex/libspeex/modes_wb.c \
speex/libspeex/nb_celp.c \
speex/libspeex/preprocess.c \
speex/libspeex/quant_lsp.c \
speex/libspeex/resample.c \
speex/libspeex/sb_celp.c \
speex/libspeex/scal.c \
speex/libspeex/smallft.c \
speex/libspeex/speex.c \
speex/libspeex/speex_callbacks.c \
speex/libspeex/speex_header.c \
speex/libspeex/stereo.c \
speex/libspeex/vbr.c \
speex/libspeex/vq.c \
speex/libspeex/window.c


include $(BUILD_SHARED_LIBRARY)

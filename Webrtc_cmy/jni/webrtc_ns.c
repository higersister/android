#include "webrtc_ns.h"
#include "webrtc/modules/audio_processing/ns/noise_suppression.h"
#include "webrtc/modules/audio_processing/aecm/echo_control_mobile.h"
#include "webrtc/modules/audio_processing/ns/ns_core.h"
#include <stdlib.h>
#include <time.h>

NsHandle* ns_inst;
void* aecm_inst;

float** f_inData;
float** f_outData;

int w_create() {
	ns_inst = WebRtcNs_Create();
	aecm_inst = WebRtcAecm_Create();
	LOGE("$$$$$$WebRtcNs_Create$$$$$$");
	LOGE("$$$$$$WebRtcAecm_Create$$$$$$");
	if (ns_inst == NULL) {
		LOGE("WebRtcNs_Create fail");
		return -1;
	}

	if (WebRtcNs_Init(ns_inst, 16000) == -1) {
		LOGE("WebRtcNs_Init fail");
		WebRtcNs_Free(ns_inst);
		ns_inst = NULL;
		return -1;
	}

	LOGE("$$$$$$WebRtcNs_Init$$$$$$");

	if (WebRtcAecm_Init(aecm_inst, 16000) == -1) {
		LOGE("WebRtcAecm_Init fail");
		WebRtcAecm_Free(aecm_inst);
		aecm_inst = NULL;
		return -1;
	}

	LOGE("$$$$$$WebRtcAecm_Init$$$$$$");

	if (WebRtcNs_set_policy(ns_inst, 2) == -1) {
		LOGE("WebRtcNs_set_policy fail");
		WebRtcNs_Free(ns_inst);
		ns_inst = NULL;
		return -1;
	}
	LOGE("$$$$$$WebRtcNs_set_policy$$$$$$");

	f_inData = (float**) malloc(160 * sizeof(float*));
	f_inData[0] = (float*) malloc(160 * sizeof(float));
	f_outData = (float**) malloc(160 * sizeof(float*));
	f_outData[0] = (float*) malloc(160 * sizeof(float));

	return (int) ns_inst;
}

int w_free() {
	if (ns_inst != NULL) {
		WebRtcNs_Free(ns_inst);
		ns_inst = NULL;
		return 1;
	}
	if (aecm_inst != NULL) {
		WebRtcAecm_Free(aecm_inst);
		aecm_inst = NULL;
		return 1;
	}
	return -1;
}

int w_process(short* inData, short* outData) {
	if (NULL == ns_inst)
		return -1;
	int len = 160;
	short *aecmData = (short*) malloc(160 * sizeof(short));
	float *nsData = (float*) malloc(160 * sizeof(float));

	LOGE("$$$$$$$WebRtcAecm_Process$$$$$$$Delay->50s");
	WebRtcAecm_Process(aecm_inst, inData, NULL, aecmData, len, 50);

	//WebRtcAecm_BufferFarend(aecm_inst, outData, 160);

	for (int i = 0; i < len; i++) {
		//	f_inData[0][i] = (float) aecmData[i];
		//	LOGE("$$$$$$$WebRtcNs_Process$$$$$$$,inData[%d]:%d", i, aecmData[i]);
		nsData[i] = (short) aecmData[i];
	}
	WebRtcNs_AnalyzeCore((NoiseSuppressionC*) ns_inst, nsData);
//	WebRtcNs_Process(ns_inst, f_inData, 1, f_outData);
	for (int i = 0; i < len; i++) {
		outData[i] = (short) nsData[i];
	}

	return 1;
}


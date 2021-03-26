#ifndef __SPEEXCODER_H_
#define __SPEEXCODER_H_

extern "C"{
#include <speex/speex.h>
};

class CSpeexCoder
{
public:
	CSpeexCoder();
	virtual ~CSpeexCoder(void);
	
	bool Init();
	int Encoder(short *pInputData, char *pOutData);
	int Decoder(char *pInputData, int nInputLen, short *pOutData);
	void Close();

private:
	char m_cbits[100];
	void *m_pEncoder;
	void *m_pDecoder;
	SpeexBits m_EncoderBits;
	SpeexBits m_DecoderBits;
};

#endif

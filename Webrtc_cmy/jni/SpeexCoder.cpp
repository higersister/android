#include "SpeexCoder.h"
#include "datatype.h"

///////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////
CSpeexCoder::CSpeexCoder()
{
	m_pEncoder = NULL;
	m_pDecoder = NULL;
}

CSpeexCoder::~CSpeexCoder(void)
{

}

bool CSpeexCoder::Init()
{
	speex_bits_init(&m_EncoderBits);
	m_pEncoder = speex_encoder_init(&speex_nb_mode);
	if (m_pEncoder == NULL)
		return false;

	int quality = 10;
	speex_encoder_ctl(m_pEncoder, SPEEX_SET_QUALITY, &quality);

	int dtx = 1;
	speex_encoder_ctl(m_pEncoder, SPEEX_SET_VAD, &dtx);
	LOGE("$$$$$$$$$SPEEX_SET_VAD$$$$$$$$$$$");
	//speex_encoder_ctl(m_pEncoder, SPEEX_SET_DTX, &dtx);

	speex_bits_init(&m_DecoderBits);
	m_pDecoder = speex_decoder_init(&speex_nb_mode);
	if (m_pDecoder == NULL)
		return false;

	return true;
}

int CSpeexCoder::Encoder(short *pInputData, char *pOutData)
{
	int nbBytes = 0;
	if (m_pEncoder != NULL)
	{
		speex_bits_reset(&m_EncoderBits);
		speex_encode_int(m_pEncoder, pInputData, &m_EncoderBits);
		nbBytes = speex_bits_write(&m_EncoderBits, pOutData, 100);
		//LOGE("speex_encode len=%d",nbBytes);
	}
	return nbBytes;
}
	
int CSpeexCoder::Decoder(char *pInputData, int nInputLen, short *pOutData)
{
	int nRet = 0;
	if (m_pDecoder != NULL)
	{
		speex_bits_reset(&m_DecoderBits);
		speex_bits_read_from(&m_DecoderBits, pInputData, nInputLen);
		nRet = speex_decode_int(m_pDecoder, &m_DecoderBits, pOutData);
		//LOGE("speex_decode len=%d",nRet);
	}
	return nRet;
}

void CSpeexCoder::Close()
{
	if (m_pEncoder != NULL)
	{
		speex_bits_destroy(&m_EncoderBits);
		speex_encoder_destroy(m_pEncoder);
		m_pEncoder = NULL;
	}
	
	if (m_pDecoder != NULL)
	{
		speex_bits_destroy(&m_DecoderBits);
		speex_decoder_destroy(m_pDecoder);
		m_pDecoder = NULL;
	}
}

package cn.cmy.rxjavaretrofit;

import android.util.Log;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class CmyResponseBody extends ResponseBody {

    private ResponseBody responseBody;

    private DownLoadListener downLoadListener;

    // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
    private BufferedSource bufferedSource;

    public CmyResponseBody(ResponseBody responseBody, DownLoadListener downLoadListener) {
        this.responseBody = responseBody;
        this.downLoadListener = downLoadListener;
    }

    @Override
    public MediaType contentType() {
        return responseBody.contentType();
    }

    @Override
    public long contentLength() {
        return responseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (bufferedSource == null) {
            bufferedSource = Okio.buffer(responseBody.source());
        }
        return bufferedSource;
    }

    private Source source(Source source) {

        return new ForwardingSource(source) {
            long totalBytesRead = 0L;

            @Override
            public long read(Buffer sink, long byteCount) throws IOException {
                long bytesRead = super.read(sink, byteCount);
                totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                Log.e("download", "read: " + (int) (totalBytesRead * 100 / responseBody.contentLength()));
                if (null != downLoadListener) {
                    if (bytesRead!=-1){
                        downLoadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                    }
                }


                return bytesRead;
            }
        };

    }


}

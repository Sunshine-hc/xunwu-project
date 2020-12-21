package com.match.service.house;

import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;

@Service
public class QiNiuServiceImpl implements IQiNiuService, InitializingBean {
    @Autowired
    private UploadManager uploadManager;

    @Autowired
    private BucketManager bucketManager;

    @Autowired
    private Auth auth;

    @Value("${qiniu.Bucket}")
    private String bucket;

    private StringMap putPolicy;

    /**
     * 本地上传图片至七牛云
     * @param file
     * @return
     * @throws QiniuException
     */
    @Override
    public Response uploadFile(File file) throws QiniuException {
        Response response = this.uploadManager.put(file,null,getUploadToken());
        int retry = 0;
        while (response.needRetry() && retry < 3){
            response = this.uploadManager.put(file,null,getUploadToken());
            retry++;
        }
        return response;
    }

    /**
     * 直接转换成字节上传至七牛云（不用从本地获取）
     * @param inputStream
     * @return
     * @throws QiniuException
     */
    @Override
    public Response uploadFile(InputStream inputStream) throws QiniuException {
        Response response = this.uploadManager.put(inputStream,null,getUploadToken(),null,null);
        int retry = 0;
        while (response.needRetry() && retry < 3){
            response = this.uploadManager.put(inputStream,null,getUploadToken(),null,null);
            retry++;
        }
        return response;
    }

    /**
     * 删除七牛云的图片
     * @param key
     * @return
     * @throws QiniuException
     */
    @Override
    public Response delete(String key) throws QiniuException {
        Response response = bucketManager.delete(this.bucket,key);
        int retry = 0;
        while (response.needRetry() && retry++ <3){
            response = bucketManager.delete(bucket,key);
        }
        return response;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"width\":$(imageInfo.width),\"height\":${imageInfo.height}}");
    }

    /**
     *获取上传凭证
     * @return
     */
    private String getUploadToken(){
        return this.auth.uploadToken(bucket,null,3600,putPolicy);
    }
}

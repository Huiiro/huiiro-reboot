package com.huii.oss.core.template;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.huii.oss.config.properties.OssProperties;
import com.huii.oss.entity.UploadResult;
import com.huii.oss.enums.AccessType;
import com.huii.oss.utils.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Component
@RequiredArgsConstructor
public class OssTemplate implements BaseTemplate {

    private final String[] CLOUD_SERVICE = new String[]{"aliyun", "qcloud", "qiniu", "obs"};
    private final OssProperties properties;
    private AmazonS3 amazonS3;

    @Override
    public void afterPropertiesSet() {
        AwsClientBuilder.EndpointConfiguration endpointConfiguration =
                new AwsClientBuilder.EndpointConfiguration(properties.getEndpoint(), properties.getRegion());
        AWSCredentials awsCredentials =
                new BasicAWSCredentials(properties.getAccessKey(), properties.getSecretKey());
        AWSCredentialsProvider awsCredentialsProvider = new AWSStaticCredentialsProvider(awsCredentials);
        ClientConfiguration clientConfiguration = new ClientConfiguration();
        clientConfiguration.setMaxConnections(properties.getMaxConnections());
        clientConfiguration.setProtocol(properties.getHttps() ? Protocol.HTTPS : Protocol.HTTP);
        this.amazonS3 = AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfiguration)
                .withCredentials(awsCredentialsProvider)
                .withClientConfiguration(clientConfiguration)
                .withPathStyleAccessEnabled(properties.getPathStyleAccess())
                .disableChunkedEncoding().build();
    }

    @Override
    @SneakyThrows
    public void createBucket(String bucketName) {
        if (!amazonS3.doesBucketExistV2(bucketName)) {
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);
            AccessType accessType = AccessType.getType(properties.getAccessPolicy());
            createBucketRequest.setCannedAcl(accessType.getAcl());
            amazonS3.createBucket(createBucketRequest);
        }
    }

    @Override
    @SneakyThrows
    public void removeBucket(String bucketName) {
        amazonS3.deleteBucket(bucketName);
    }

    @Override
    @SneakyThrows
    public List<Bucket> getAllBuckets() {
        return amazonS3.listBuckets();
    }

    @Override
    @SneakyThrows
    public Bucket getBucket(String bucketName) {
        Optional<Bucket> optional = amazonS3.listBuckets().stream()
                .filter(b -> b.getName().equals(bucketName)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        throw new RuntimeException("bucket not exist");
    }

    @Override
    @SneakyThrows
    public UploadResult putObject(String bucketName, String objectName, InputStream stream, String contextType) {
        String fileName = FileUtils.initRandomFileName() + FileUtils.getFileSuffixWithDot(objectName);
        byte[] bytes = IOUtils.toByteArray(stream);
        String md5 = DigestUtils.md5Hex(stream);
        ObjectMetadata objectMetadata = new ObjectMetadata();
        //设置stream.available可能会导致流长度读取不一致问题，忽略该参数让s3自行计算
        objectMetadata.setContentType(contextType);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, byteArrayInputStream, objectMetadata);
        AccessType accessType = AccessType.getType(properties.getAccessPolicy());
        putObjectRequest.setCannedAcl(accessType.getAcl());
        amazonS3.putObject(putObjectRequest);
        return new UploadResult(getUrl() + fileName, fileName, objectName, FileUtils.formatFileSize(stream.available()), md5);
    }

    @Override
    public UploadResult putObject(String bucketName, String objectName, InputStream stream) {
        return putObject(bucketName, objectName, stream, "application/octet-stream");
    }

    @Override
    @SneakyThrows
    public S3Object getObject(String bucketName, String objectName) {
        return amazonS3.getObject(bucketName, objectName);
    }

    @Override
    @SneakyThrows
    public InputStream getStream(String bucketName, String objectName) {
        S3Object object = amazonS3.getObject(bucketName, objectName);
        return object.getObjectContent();
    }

    @Override
    @SneakyThrows
    public String getDirectUrl(String bucketName, String objectName) {
        S3Object object = getObject(bucketName, objectName);
        return object.getObjectContent().getHttpRequest().getURI().toString();
    }

    @Override
    @SneakyThrows
    public String getPreSignedUrl(String bucketName, String objectName, Integer expires) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, expires);
        return amazonS3.generatePresignedUrl(bucketName, objectName, calendar.getTime()).toString();
    }

    @Override
    @SneakyThrows
    public void deleteObject(String bucketName, String objectName) {
        amazonS3.deleteObject(bucketName, objectName);
    }

    @Override
    public boolean exists(String bucketName, String fileName) {
        return amazonS3.doesObjectExist(bucketName, fileName);
    }

    private String getUrl() {
        if (StringUtils.isNoneEmpty(properties.getAccessUrl())) {
            //default minio
            return properties.getAccessUrl() + "/" + properties.getBucketName() + "/";
        }
        String isHttps = properties.getHttps() ? "https://" : "http://";
        String domain = properties.getDomain();
        String endpoint = properties.getEndpoint();
        //cloud service
        if (StringUtils.containsAny(endpoint, CLOUD_SERVICE)) {
            if (StringUtils.isNotBlank(domain)) {
                return isHttps + domain;
            }
            return isHttps + properties.getBucketName() + "." + endpoint + "/";
        }
        //minio
        if (StringUtils.isNotBlank(domain)) {
            return isHttps + domain + "/" + properties.getBucketName() + "/";
        }
        return isHttps + endpoint + "/" + properties.getBucketName() + "/";
    }
}

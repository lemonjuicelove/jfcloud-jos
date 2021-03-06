package com.github.jfcloud.jos.core.operation.upload.product;

import com.github.jfcloud.jos.core.constant.StorageTypeEnum;
import com.github.jfcloud.jos.core.constant.UploadFileStatusEnum;
import com.github.jfcloud.jos.core.exception.operation.UploadException;
import com.github.jfcloud.jos.core.operation.upload.Uploader;
import com.github.jfcloud.jos.core.operation.upload.domain.UploadFile;
import com.github.jfcloud.jos.core.operation.upload.domain.UploadFileResult;
import com.github.jfcloud.jos.core.operation.upload.request.QiwenMultipartFile;
import com.github.jfcloud.jos.core.util.CusFileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

@Component
public class LocalStorageUploader extends Uploader {

    public static Map<String, String> FILE_URL_MAP = new HashMap<>();

    protected UploadFileResult doUploadFlow(QiwenMultipartFile qiwenMultipartFile, UploadFile uploadFile) {
        UploadFileResult uploadFileResult = new UploadFileResult();
        try {
            String fileUrl = CusFileUtils.getUploadFileUrl(uploadFile.getIdentifier(), qiwenMultipartFile.getExtendName());
            if (StringUtils.isNotEmpty(FILE_URL_MAP.get(uploadFile.getIdentifier()))) {
                fileUrl = FILE_URL_MAP.get(uploadFile.getIdentifier());
            } else {
                FILE_URL_MAP.put(uploadFile.getIdentifier(), fileUrl);
            }
            String tempFileUrl = fileUrl + "_tmp";
            String confFileUrl = fileUrl.replace("." + qiwenMultipartFile.getExtendName(), ".conf");

            File file = new File(CusFileUtils.getStaticPath() + fileUrl);
            File tempFile = new File(CusFileUtils.getStaticPath() + tempFileUrl);
            File confFile = new File(CusFileUtils.getStaticPath() + confFileUrl);

            //????????? ???????????????????????????
            RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
            //????????? ????????????
            try {
                FileChannel fileChannel = raf.getChannel();
                //????????? ???????????????
                long position = (uploadFile.getChunkNumber() - 1) * uploadFile.getChunkSize();
                //????????? ??????????????????
                byte[] fileData = qiwenMultipartFile.getUploadBytes();
                //????????? ????????????
                fileChannel.position(position);
                fileChannel.write(ByteBuffer.wrap(fileData));
                fileChannel.force(true);
                fileChannel.close();
            } finally {
                IOUtils.closeQuietly(raf);
            }

            //????????????????????????????????????????????????????????????
            boolean isComplete = checkUploadStatus(uploadFile, confFile);
            uploadFileResult.setFileUrl(fileUrl);
            uploadFileResult.setFileName(qiwenMultipartFile.getFileName());
            uploadFileResult.setExtendName(qiwenMultipartFile.getExtendName());
            uploadFileResult.setFileSize(uploadFile.getTotalSize());
            uploadFileResult.setStorageType(StorageTypeEnum.LOCAL);

            if (uploadFile.getTotalChunks() == 1) {
                uploadFileResult.setFileSize(qiwenMultipartFile.getSize());
            }

            if (isComplete) {
                tempFile.renameTo(file);
                FILE_URL_MAP.remove(uploadFile.getIdentifier());

                if (CusFileUtils.isImageFile(uploadFileResult.getExtendName())) {

                    InputStream is = null;
                    try {
                        is = new FileInputStream(CusFileUtils.getLocalSaveFile(fileUrl));

                        BufferedImage src = ImageIO.read(is);
                        uploadFileResult.setBufferedImage(src);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        IOUtils.closeQuietly(is);
                    }

                }

                uploadFileResult.setStatus(UploadFileStatusEnum.SUCCESS);
            } else {
                uploadFileResult.setStatus(UploadFileStatusEnum.UNCOMPLATE);
            }
        } catch (IOException e) {
            throw new UploadException(e);
        }


        return uploadFileResult;
    }

    @Override
    public void cancelUpload(UploadFile uploadFile) {
        // TODO
    }

    @Override
    protected void doUploadFileChunk(QiwenMultipartFile qiwenMultipartFile, UploadFile uploadFile) throws IOException {

    }

    @Override
    protected UploadFileResult organizationalResults(QiwenMultipartFile qiwenMultipartFile, UploadFile uploadFile) {
        return null;
    }

}

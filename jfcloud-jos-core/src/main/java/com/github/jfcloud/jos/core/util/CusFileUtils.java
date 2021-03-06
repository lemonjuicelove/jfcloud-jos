package com.github.jfcloud.jos.core.util;

import com.github.jfcloud.jos.core.constant.FileConstant;
import com.github.jfcloud.jos.core.exception.CommException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.github.jfcloud.jos.core.operation.upload.Uploader.FILE_SEPARATOR;
import static com.github.jfcloud.jos.core.operation.upload.Uploader.ROOT_PATH;

public class CusFileUtils {

    public static Map<String, String> PATH_MAP = new HashMap<>();

    public static String LOCAL_STORAGE_PATH;

    public static final String[] IMG_FILE = {"bmp", "jpg", "png", "tif", "gif", "jpeg"};
    public static final String[] DOC_FILE = {"doc", "docx", "ppt", "pptx", "xls", "xlsx", "txt", "hlp", "wps", "rtf", "html", "pdf"};
    public static final String[] VIDEO_FILE = {"avi", "mp4", "mpg", "mov", "swf"};
    public static final String[] MUSIC_FILE = {"wav", "aif", "au", "mp3", "ram", "wma", "mmf", "amr", "aac", "flac"};
    public static final String[] TXT_FILE = {"txt", "html", "java", "xml", "js", "css", "json"};
    public static final int IMAGE_TYPE = 1;
    public static final int DOC_TYPE = 2;
    public static final int VIDEO_TYPE = 3;
    public static final int MUSIC_TYPE = 4;
    public static final int OTHER_TYPE = 5;
    public static final int SHARE_FILE = 6;
    public static final int RECYCLE_FILE = 7;

    public static List<String> getFileExtendsByType(int fileType) {

        List<String> fileExtends;
        switch (fileType) {
            case IMAGE_TYPE:
                fileExtends = Arrays.asList(IMG_FILE);
                break;
            case DOC_TYPE:
                fileExtends = Arrays.asList(DOC_FILE);
                break;
            case VIDEO_TYPE:
                fileExtends = Arrays.asList(VIDEO_FILE);
                break;
            case MUSIC_TYPE:
                fileExtends = Arrays.asList(MUSIC_FILE);
                break;
            default:
                fileExtends = new ArrayList<>();
                break;


        }
        return fileExtends;
    }

    /**
     * ???????????????????????????
     *
     * @param extendName ???????????????
     * @return ?????????????????????
     */
    public static boolean isImageFile(String extendName) {
        for (String extend : IMG_FILE) {
            if (extendName.equalsIgnoreCase(extend)) {
                return true;
            }
        }
        return false;
    }

    /**
     * ???????????????????????????
     * @param extendName ?????????
     * @return ?????????????????????
     */
    public static boolean isVideoFile(String extendName) {
        for (String extend : VIDEO_FILE) {
            if (extendName.equalsIgnoreCase(extend)) {
                return true;
            }
        }
        return false;
    }



    public static String pathSplitFormat(String filePath) {
        return filePath.replace("///", "/")
                .replace("//", "/")
                .replace("\\\\\\", "\\")
                .replace("\\\\", "\\");
    }

    /**
     * ?????????????????????
     *
     * @param fileName ?????????
     * @return ???????????????
     */
    public static String getFileExtendName(String fileName) {
        return FilenameUtils.getExtension(fileName);
    }

    /**
     * ????????????????????????????????????
     *
     * @param fileName ?????????
     * @return ??????????????????????????????
     */
    public static String getFileNameNotExtend(String fileName) {
        return FilenameUtils.removeExtension(fileName);
    }

    public static File getLocalSaveFile(String fileUrl) {
        String localSavePath = CusFileUtils.getStaticPath() + fileUrl;
        return new File(localSavePath);
    }

    public static File getCacheFile(String fileUrl) {
        String cachePath = CusFileUtils.getStaticPath() + "cache" + File.separator + fileUrl;

        return new File(cachePath);
    }

    public static File getTempFile(String fileUrl) {
        String tempPath = CusFileUtils.getStaticPath() + "temp" + File.separator + fileUrl;
        File tempFile = new File(tempPath);
        File parentFile = tempFile.getParentFile();
        if (!parentFile.exists()) {
            boolean result = parentFile.mkdirs();
            if (!result) {
                throw new CommException("??????temp??????????????????????????????"+ parentFile.getPath());
            }
        }

        return tempFile;
    }

    public static File getProcessFile(String fileUrl) {
        String processPath = CusFileUtils.getStaticPath() + "temp" + File.separator + "process" + File.separator + fileUrl;
        File processFile = new File(processPath);
        File parentFile = processFile.getParentFile();
        if (!parentFile.exists()) {
            boolean result = parentFile.mkdirs();
            if (!result) {
                throw new CommException("??????process??????????????????????????????"+ parentFile.getPath());
            }
        }
        return processFile;
    }

    /**
     * ???????????????????????????????????? resources??????
     * @return ??????
     */
    public static String getProjectRootPath() {
        String absolutePath = null;
        try {
            String url = ResourceUtils.getURL("classpath:").getPath();
            absolutePath = urlDecode(new File(url).getAbsolutePath()) + File.separator;
        } catch (FileNotFoundException e) {
            throw new CommException(e);
        }

        return absolutePath;
    }

    /**
     * ????????????
     * @param url url
     * @return ??????
     */
    public static String urlDecode(String url){
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return  decodeUrl;
    }

    /**
     * ??????static??????
     *
     * @return ??????
     */
    public static String getStaticPath() {
        String localStoragePath = LOCAL_STORAGE_PATH;
        if (StringUtils.isNotEmpty(localStoragePath)) {

            return new File(localStoragePath).getPath() + File.separator;
        }else {
            String projectRootAbsolutePath = getProjectRootPath();

            int index = projectRootAbsolutePath.indexOf("file:");
            if (index != -1) {
                projectRootAbsolutePath = projectRootAbsolutePath.substring(0, index);
            }

            return new File(projectRootAbsolutePath + "static").getPath() + File.separator;
        }


    }

    /**
     * ????????????????????????
     * ?????????????????? ???upload/yyyyMMdd/???
     * @param identifier ????????????????????????md5???uuid,????????????????????????
     * @param extendName ???????????????
     * @return ????????????????????????
     */
    public static String getUploadFileUrl(String identifier, String extendName) {

        SimpleDateFormat formater = new SimpleDateFormat("yyyyMMdd");
        String path = ROOT_PATH + FILE_SEPARATOR + formater.format(new Date()) + FILE_SEPARATOR;

        File dir = new File(CusFileUtils.getStaticPath() + path);

        if (!dir.exists()) {

            boolean result = dir.mkdirs();
            if (!result) {
                throw new CommException("??????upload??????????????????????????????"+ dir.getPath());
            }

        }

        path = path + identifier + "." + extendName;

        return path;
    }

    public static String getAliyunObjectNameByFileUrl(String fileUrl) {
        if (fileUrl.startsWith("/") || fileUrl.startsWith("\\")) {
            fileUrl = fileUrl.substring(1);
        }
        return fileUrl;
    }

    public static String getParentPath(String path) {
        return path.substring(0, path.lastIndexOf(FileConstant.pathSeparator));
    }

}

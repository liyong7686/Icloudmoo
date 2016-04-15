package com.icloudmoo.business.file.upload.deal;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.icloudmoo.business.file.upload.util.FileTools;
import com.icloudmoo.business.file.upload.vo.TransferRequest;
import com.icloudmoo.business.file.upload.vo.TransferResponse;

/**
 * @description TODO
 * @author liyong
 * @date 2013-2-20
 */
public class Command {

    private static Logger logger = Logger.getLogger(Command.class);

    /**
     * @description 执行命令
     * @param request
     * @return TransferResponse
     * @throws
     */
    public static boolean execCmd(TransferRequest request, InputStream input, TransferResponse response) {

        return upload(request, input, response);

    }

    /**
     * @description 上传文件
     * @param request
     * @return TransferResponse
     */
    private static boolean upload(TransferRequest request, InputStream input, TransferResponse response) {
        try {
            String filePath = FileTools.writeFile(input, request.getFileSize(), request.getFileType(),
                    request.getFileName());
            response.setPath(filePath);

        }
        catch (Exception e) {
            logger.error("上传文件异常", e);
            response.setStatus(CmdConst.STATUS_FILE_SAVE_ERROR);
            return false;
        }
        return true;
    }

}

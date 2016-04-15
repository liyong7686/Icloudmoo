package com.icloudmoo.business.file.upload.vo;

import com.icloudmoo.common.vo.ValueObject;

/**
 * @description TODO
 * @author
 * @date 2013-2-20
 */
public class TransferRequest extends ValueObject {
    private static final long serialVersionUID = 1L;

    /**
     * 操作命令
     */
    private String cmd = "";

    /**
     * 文件类型
     */
    private String fileType;
    /**
     * 文件大小
     */
    private Integer fileSize;
     
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件内容
     */
    private byte[] data;

    /**
     * @return the cmd
     */
    public String getCmd() {
        return cmd;
    }

    /**
     * @param cmd
     *            the cmd to set
     */
    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return fileType;
    }

    /**
     * @param fileType
     *            the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * @return the fileSize
     */
    public Integer getFileSize() {
        return fileSize;
    }

    /**
     * @param fileSize
     *            the fileSize to set
     */
    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    /**
     * @return the data
     */
    public byte[] getData() {
        return data;
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    
    

}

package com.icloudmoo.business.file.upload;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.icloudmoo.business.file.upload.netty.NettyNIOServer;
import com.icloudmoo.common.bo.AbstractBusinessObject;
import com.icloudmoo.common.exception.BusinessServiceException;

/**
 * TODO:
 * @author liyong
 * @Date 2015年12月22日 下午2:13:15
 */
@Service("transferListener")
public class TransferListener extends AbstractBusinessObject {
    private static Logger logger = Logger.getLogger(TransferListener.class);

    private NettyNIOServer server;

    public void destroy() throws Exception {
        server.shutDown();

    }

    public void afterPropertiesSet() throws BusinessServiceException {
        server = new NettyNIOServer();
        try {
            server.statNettyServer();

        }catch (Exception e) {
            logger.error("图片服务器启动异常", e);
        }
    }
}

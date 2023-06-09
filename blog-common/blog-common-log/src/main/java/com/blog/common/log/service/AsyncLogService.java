//package com.blog.common.log.service;
//
//
//import com.blog.sytem.api.RemoteLogService;
//import com.blog.sytem.api.domin.SysOperLog;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.stereotype.Service;
//
///**
// * 异步调用日志服务
// *
// * @author IKUN
// *@since 2023-05-31 21:25:43
// */
//@Service
//public class AsyncLogService
//{
//    @Autowired
//    private RemoteLogService remoteLogService;
//
//    /**
//     * 保存系统日志记录
//     */
//    @Async
//    public void saveSysLog(SysOperLog sysOperLog) throws Exception
//    {
//        remoteLogService.saveLog(sysOperLog, SecurityConstants.INNER);
//    }
//}

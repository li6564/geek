package cn.lico.geek.modules.notice.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.notice.form.NoticeListForm;
import cn.lico.geek.modules.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2022/11/25 14:13
 */
@RestController
@RequestMapping("/notice")
public class NoticeApi {
    @Autowired
    private NoticeService noticeService;

    /**
     * 获取当前登录用户的通知消息数量
     * @return
     */
    @RequestMapping("/getUserReceiveNoticeCount")
    public ResponseResult getUserReceiveNoticeCount(){
        return noticeService.getUserReceiveNoticeCount();
    }

    /**
     * 获取消息列表
     * @param noticeListForm
     * @return
     */
    @RequestMapping("/getList")
    public ResponseResult getList(@RequestBody NoticeListForm noticeListForm){
        return noticeService.getList(noticeListForm);
    }
}

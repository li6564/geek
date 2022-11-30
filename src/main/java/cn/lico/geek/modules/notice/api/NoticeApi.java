package cn.lico.geek.modules.notice.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.notice.exception.NoticeServiceException;
import cn.lico.geek.modules.notice.form.NoticeDeleteForm;
import cn.lico.geek.modules.notice.form.NoticeListForm;
import cn.lico.geek.modules.notice.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @GetMapping("/getUserReceiveNoticeCount")
    public ResponseResult getUserReceiveNoticeCount(){
        return noticeService.getUserReceiveNoticeCount();
    }

    /**
     * 获取消息列表
     * @param noticeListForm
     * @return
     */
    @PostMapping("/getList")
    public ResponseResult getList(@RequestBody NoticeListForm noticeListForm){
        return noticeService.getList(noticeListForm);
    }

    /**
     * 删除通知消息
     * @param noticeDeleteForm
     * @return
     */
    @PostMapping("/delete")
    public ResponseResult delete(@RequestBody NoticeDeleteForm noticeDeleteForm){
        try {
            return noticeService.delete(noticeDeleteForm);
        } catch (NoticeServiceException e) {
            e.printStackTrace();
            return new ResponseResult("删除失败!", AppHttpCodeEnum.ERROR.getMsg());
        }
    }

    /**
     * 清空所有消息
     * @param
     * @return
     */
    @PostMapping("/deleteBatch")
    public ResponseResult deleteBatch(@RequestBody List<Map<String,String>> list){
        List<String> uids = new ArrayList<>();
        for (Map<String, String> map : list) {
            String uid = map.get("uid");
            uids.add(uid);
        }
        return noticeService.deleteBatch(uids);
    }
}

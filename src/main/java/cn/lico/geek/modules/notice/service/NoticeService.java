package cn.lico.geek.modules.notice.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.notice.entity.Notice;
import cn.lico.geek.modules.notice.exception.NoticeServiceException;
import cn.lico.geek.modules.notice.form.NoticeDeleteForm;
import cn.lico.geek.modules.notice.form.NoticeListForm;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * @Author：linan
 * @Date：2022/11/25 14:15
 */
public interface NoticeService extends IService<Notice> {
    ResponseResult getUserReceiveNoticeCount();

    ResponseResult getList(NoticeListForm noticeListForm);

    ResponseResult delete(NoticeDeleteForm noticeDeleteForm) throws NoticeServiceException;

    ResponseResult deleteBatch(List uids);
}

package cn.lico.geek.modules.report.service.Impl;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.core.emuns.AppHttpCodeEnum;
import cn.lico.geek.modules.report.entity.Report;
import cn.lico.geek.modules.report.form.ReportSubmitForm;
import cn.lico.geek.modules.report.mapper.ReportMapper;
import cn.lico.geek.modules.report.service.ReportService;
import cn.lico.geek.utils.BeanCopyUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2023/3/1 19:34
 */
@Service
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {
    @Override
    public ResponseResult submit(ReportSubmitForm reportSubmitForm) {
        Report report = BeanCopyUtils.copyBean(reportSubmitForm, Report.class);
        boolean flag = save(report);
        if (!flag){
            return new ResponseResult("举报失败", AppHttpCodeEnum.ERROR);
        }
        return new ResponseResult(AppHttpCodeEnum.SUCCESS);
    }
}

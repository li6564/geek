package cn.lico.geek.modules.report.service;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.report.entity.Report;
import cn.lico.geek.modules.report.form.ReportSubmitForm;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @Author：linan
 * @Date：2023/3/1 19:33
 */
public interface ReportService extends IService<Report> {
    ResponseResult submit(ReportSubmitForm reportSubmitForm);
}

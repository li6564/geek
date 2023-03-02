package cn.lico.geek.modules.report.api;

import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.report.form.ReportSubmitForm;
import cn.lico.geek.modules.report.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author：linan
 * @Date：2023/3/1 19:36
 */
@RestController
@RequestMapping("/report")
public class ReportApi {

    @Autowired
    private ReportService reportService;

    @PostMapping("/submit")
    public ResponseResult submit(@RequestBody ReportSubmitForm reportSubmitForm){
        return reportService.submit(reportSubmitForm);
    }
}

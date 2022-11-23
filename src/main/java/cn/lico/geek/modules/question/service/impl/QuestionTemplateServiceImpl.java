package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.QuestionTemplate;
import cn.lico.geek.modules.question.form.PageForm;
import cn.lico.geek.modules.question.mapper.QuestionTemplateMapper;
import cn.lico.geek.modules.question.service.QuestionTemplateService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/23 14:52
 */
@Service
public class QuestionTemplateServiceImpl extends ServiceImpl<QuestionTemplateMapper, QuestionTemplate> implements QuestionTemplateService {
    /**
     * 获取问答模板
     * @param pageForm
     * @return
     */
    @Override
    public ResponseResult getTemplateList(PageForm pageForm) {
        LambdaQueryWrapper<QuestionTemplate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionTemplate::getStatus,1);
        //根据分页查询
        Page<QuestionTemplate> page = new Page<>(pageForm.getCurrentPage(),pageForm.getPageSize());
        page(page,queryWrapper);
        //将查询结果封装到PageDto中
        PageDTO<QuestionTemplate> pageDTO = new PageDTO<>();
        pageDTO.setRecords(page.getRecords());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setSize((int)page.getSize());
        pageDTO.setCurrent((int)page.getCurrent());
        return new ResponseResult(pageDTO);
    }
}

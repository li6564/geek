package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.common.dto.PageDTO;
import cn.lico.geek.core.api.ResponseResult;
import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.form.PageForm;
import cn.lico.geek.modules.question.mapper.QuestionTagMapper;
import cn.lico.geek.modules.question.service.QuestionTagService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/21 20:29
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements QuestionTagService {
    /**
     * 获取问答标签列表
     * @param pageForm
     * @return
     */
    @Override
    public ResponseResult getTagList(PageForm pageForm) {
        LambdaQueryWrapper<QuestionTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(QuestionTag::getStatus,1);
        queryWrapper.orderByDesc(QuestionTag::getSort);
        //根据分页查询
        Page<QuestionTag> page = new Page<>(pageForm.getCurrentPage(), pageForm.getPageSize());
        page(page,queryWrapper);
        //将结果分装到PageDto中
        PageDTO<QuestionTag> pageDTO = new PageDTO<>();
        pageDTO.setRecords(page.getRecords());
        pageDTO.setTotal((int)page.getTotal());
        pageDTO.setCurrent((int)page.getCurrent());
        pageDTO.setSize((int)page.getSize());
        return new ResponseResult(pageDTO);
    }
}

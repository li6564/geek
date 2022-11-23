package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.modules.question.entity.QuestionTags;
import cn.lico.geek.modules.question.mapper.QuestionTagsMapper;
import cn.lico.geek.modules.question.service.QuestionService;
import cn.lico.geek.modules.question.service.QuestionTagsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/23 16:20
 */
@Service
public class QuestionTagsServiceImpl extends ServiceImpl<QuestionTagsMapper, QuestionTags> implements QuestionTagsService {
}

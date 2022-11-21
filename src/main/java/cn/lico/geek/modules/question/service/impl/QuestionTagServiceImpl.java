package cn.lico.geek.modules.question.service.impl;

import cn.lico.geek.modules.question.entity.QuestionTag;
import cn.lico.geek.modules.question.mapper.QuestionMapper;
import cn.lico.geek.modules.question.mapper.QuestionTagMapper;
import cn.lico.geek.modules.question.service.QuestionTagService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/21 20:29
 */
@Service
public class QuestionTagServiceImpl extends ServiceImpl<QuestionTagMapper, QuestionTag> implements QuestionTagService {
}

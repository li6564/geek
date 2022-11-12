package cn.lico.geek.modules.tag.service.Impl;

import cn.lico.geek.modules.tag.entity.Tag;
import cn.lico.geek.modules.tag.mapper.TagMapper;
import cn.lico.geek.modules.tag.service.TagService;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @Author：linan
 * @Date：2022/11/12 12:56
 */
@Service
public class TagServiceImpl extends ServiceImpl<TagMapper,Tag> implements TagService {
}

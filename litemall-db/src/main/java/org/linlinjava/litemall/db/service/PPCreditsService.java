package org.linlinjava.litemall.db.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.db.dao.PpCreditsRuleMapper;
import org.linlinjava.litemall.db.dao.PpUserCreditsLogMapper;
import org.linlinjava.litemall.db.dao.PpUserCreditsMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PPCreditsService {

    @Resource
    private PpUserCreditsMapper ppUserCreditsMapper;

    @Resource
    private PpUserCreditsLogMapper ppUserCreditsLogMapper;

    public PpUserCredits getMyCreditsList(Integer userId){
        PpUserCreditsExample example = new PpUserCreditsExample();
        example.newAndCreateCriteria().andUserIdEqualTo(userId);
        return ppUserCreditsMapper.selectOneByExample(example);
    }

    public List<PpUserCreditsLog> getMyCreditsLogList(Integer userId, Integer page, Integer size){
        PpUserCreditsLogExample example = new PpUserCreditsLogExample();
        example.newAndCreateCriteria().andUserIdEqualTo(userId);
        example.orderBy("gain_date desc");

        PageHelper.startPage(page, size);
        return ppUserCreditsLogMapper.selectByExample(example);
    }

    public void addCredits(){

    }

}

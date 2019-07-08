package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.PpCreditsRuleMapper;
import org.linlinjava.litemall.db.domain.PpCreditsRule;
import org.linlinjava.litemall.db.domain.PpCreditsRuleExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
public class CreditsRuleService {

    @Resource
    private PpCreditsRuleMapper ppCreditsRuleMapper;

    public PpCreditsRule getCreditsRule(){
        return ppCreditsRuleMapper.getRule();
    }

    @Transactional
    public int saveCreditsRule(PpCreditsRule ppCreditsRule){
        PpCreditsRule rule = ppCreditsRuleMapper.getRule();
        if(rule != null){
            ppCreditsRule.setId(rule.getId());
            ppCreditsRule.setUpdateTime(LocalDateTime.now());
            return ppCreditsRuleMapper.updateByPrimaryKey(ppCreditsRule);
        } else {
            ppCreditsRule.setAddTime(LocalDateTime.now());
            ppCreditsRule.setUpdateTime(LocalDateTime.now());
            return ppCreditsRuleMapper.insertSelective(ppCreditsRule);
        }

    }


}

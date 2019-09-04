package org.linlinjava.litemall.db.service;

import org.linlinjava.litemall.db.dao.PpUserCreditsLogMapper;
import org.linlinjava.litemall.db.dao.PpUserCreditsMapper;
import org.linlinjava.litemall.db.domain.PpUserCredits;
import org.linlinjava.litemall.db.domain.PpUserCreditsExample;
import org.linlinjava.litemall.db.domain.PpUserCreditsLog;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class LitemallCreditsService {

    @Resource
    private PpUserCreditsMapper ppUserCreditsMapper;

    @Resource
    private PpUserCreditsLogMapper ppUserCreditsLogMapper;

    @Transactional
    public void getBackCredits(Integer userId, BigDecimal credits) {
        PpUserCreditsExample example =
                PpUserCreditsExample.newAndCreateCriteria().andUserIdEqualTo(userId).example();
        PpUserCredits userCredits = ppUserCreditsMapper.selectOneByExample(example);
        if (userCredits != null) {
            PpUserCreditsLog log = new PpUserCreditsLog();
            log.setUserId(userId);
            log.setCredits(credits);
            log.setGainDate(LocalDate.now());
            log.setAddTime(LocalDateTime.now());
            log.setState(2);
            log.setEvent("积分返还");
            ppUserCreditsLogMapper.insert(log);

            userCredits.setCredits(userCredits.getCredits().add(credits));
            userCredits.setUpdateTime(LocalDateTime.now());
            ppUserCreditsMapper.updateByPrimaryKey(userCredits);
        }
    }

    @Transactional
    public void useCredits(Integer userId, BigDecimal credits) {
        if(credits == null || credits.compareTo(BigDecimal.ZERO)==0){
            return;
        }
        PpUserCreditsExample example =
                PpUserCreditsExample.newAndCreateCriteria().andUserIdEqualTo(userId).example();
        PpUserCredits userCredits = ppUserCreditsMapper.selectOneByExample(example);
        if (userCredits != null) {
            PpUserCreditsLog log = new PpUserCreditsLog();
            log.setUserId(userId);
            log.setCredits(credits);
            log.setGainDate(LocalDate.now());
            log.setAddTime(LocalDateTime.now());
            log.setState(0);
            log.setEvent("积分抵扣");
            ppUserCreditsLogMapper.insert(log);

            userCredits.setCredits(userCredits.getCredits().subtract(credits));
            userCredits.setUpdateTime(LocalDateTime.now());
            ppUserCreditsMapper.updateByPrimaryKey(userCredits);
        }
    }

}

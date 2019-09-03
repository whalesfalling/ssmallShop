package org.linlinjava.litemall.wx.service;

import com.github.pagehelper.PageHelper;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.dao.PpCreditsRuleMapper;
import org.linlinjava.litemall.db.dao.PpUserCreditsLogMapper;
import org.linlinjava.litemall.db.dao.PpUserCreditsMapper;
import org.linlinjava.litemall.db.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.linlinjava.litemall.wx.util.WxResponseCode.CREDITS_UP;

@Service
public class WxCreditsService {

    @Resource
    private PpUserCreditsMapper ppUserCreditsMapper;

    @Resource
    private PpUserCreditsLogMapper ppUserCreditsLogMapper;

    @Resource
    private PpCreditsRuleMapper ppCreditsRuleMapper;

    public PpUserCredits getMyCredits(Integer userId) {
        PpUserCreditsExample example = PpUserCreditsExample.newAndCreateCriteria().andUserIdEqualTo(userId).example();
        PpUserCredits ppUserCredits = ppUserCreditsMapper.selectOneByExample(example);
        return ppUserCredits == null ? new PpUserCredits() : ppUserCredits;
    }

    public List<PpUserCreditsLog> getMyCreditsLogList(Integer userId, Integer page, Integer limit) {
        PpUserCreditsLogExample example =
                PpUserCreditsLogExample.newAndCreateCriteria().andUserIdEqualTo(userId).example();
        example.orderBy("add_time desc");

        PageHelper.startPage(page, limit);
        return ppUserCreditsLogMapper.selectByExample(example);
    }

//    public boolean isUp(Integer userId) {
//        int count = ppUserCreditsLogMapper.countByToday(userId);
//        PpCreditsRule ppCreditsRule = ppCreditsRuleMapper.getRule();
//        if (count < ppCreditsRule.getNumber()) {
//            return false;
//        }else {
//            return true;
//        }
//    }

    @Transactional
    public Object addCredits(Integer userId) {
        int count = ppUserCreditsLogMapper.countByToday(userId);
        PpCreditsRule ppCreditsRule = ppCreditsRuleMapper.getRule();
        if (count < ppCreditsRule.getNumber()) {
            PpUserCreditsLog log = new PpUserCreditsLog();
            log.setUserId(userId);
            log.setCredits(ppCreditsRule.getCredits());
            log.setGainDate(LocalDate.now());
            log.setAddTime(LocalDateTime.now());
            log.setState(1);
            log.setEvent("分享获得");
            ppUserCreditsLogMapper.insert(log);

            PpUserCreditsExample example =
                    PpUserCreditsExample.newAndCreateCriteria().andUserIdEqualTo(userId).example();
            PpUserCredits userCredits = ppUserCreditsMapper.selectOneByExample(example);
            int result = 0;
            if (userCredits != null) {
                userCredits.setCredits(userCredits.getCredits().add(ppCreditsRule.getCredits()));
                userCredits.setUpdateTime(LocalDateTime.now());
                result = ppUserCreditsMapper.updateByPrimaryKey(userCredits);
            } else {
                userCredits = new PpUserCredits();
                userCredits.setUserId(userId);
                userCredits.setCredits(ppCreditsRule.getCredits());
                userCredits.setAddTime(LocalDateTime.now());
                userCredits.setUpdateTime(LocalDateTime.now());
                result = ppUserCreditsMapper.insert(userCredits);
            }
            if (result == 1) {
                return ResponseUtil.ok(ppCreditsRule.getCredits());
            } else {
                return ResponseUtil.updatedDataFailed();
            }
        } else {
            return ResponseUtil.fail(CREDITS_UP, "今日获取次数已达上限");
        }
    }

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

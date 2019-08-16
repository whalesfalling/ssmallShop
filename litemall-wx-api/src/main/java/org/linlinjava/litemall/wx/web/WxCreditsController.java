package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.wx.annotation.LoginUser;
import org.linlinjava.litemall.wx.service.WxCreditsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 我的积分
 */
@RestController
@RequestMapping("/wx/credits")
@Validated
public class WxCreditsController {
    private final Log logger = LogFactory.getLog(WxCreditsController.class);

    @Autowired
    private WxCreditsService creditsService;


    @GetMapping("myInfo")
    public Object myInfo(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.ok(creditsService.getMyCredits(userId).getCredits());
    }

    @GetMapping("myLogList")
    public Object myLogList(@LoginUser Integer userId,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer limit) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return ResponseUtil.okList(creditsService.getMyCreditsLogList(userId, page, limit));
    }

    @GetMapping("addCredits")
    public Object addCredits(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        return creditsService.addCredits(userId);
    }

}

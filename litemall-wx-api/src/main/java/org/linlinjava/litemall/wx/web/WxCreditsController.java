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
    public Object myInfo(@LoginUser Integer userId,
                         @RequestParam(defaultValue = "1") Integer page,
                         @RequestParam(defaultValue = "10") Integer size) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("myCredits", creditsService.getMyCredits(userId));
        map.put("logList", creditsService.getMyCreditsLogList(userId, page, size));
        return ResponseUtil.ok(map);
    }

    @GetMapping("addCredits")
    public Object addCredits(@LoginUser Integer userId) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }

        return creditsService.addCredits(userId);
    }

}

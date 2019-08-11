package org.linlinjava.litemall.wx.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.LitemallCategory;
import org.linlinjava.litemall.db.service.CreditsRuleService;
import org.linlinjava.litemall.db.service.LitemallCategoryService;
import org.linlinjava.litemall.wx.service.HomeCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 类目服务
 */
@RestController
@RequestMapping("/wx/credits")
@Validated
public class WxCreditsController {
    private final Log logger = LogFactory.getLog(WxCreditsController.class);

    @Autowired
    private CreditsRuleService creditsRuleService;


    @GetMapping("myInfo")
    public Object myInfo(Integer id) {

        return null;
    }

}

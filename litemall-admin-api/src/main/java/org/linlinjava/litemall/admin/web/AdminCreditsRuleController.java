package org.linlinjava.litemall.admin.web;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.linlinjava.litemall.admin.annotation.RequiresPermissionsDesc;
import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.domain.PpCreditsRule;
import org.linlinjava.litemall.db.service.CreditsRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/creditsRule")
@Validated
public class AdminCreditsRuleController {

    @Autowired
    private CreditsRuleService service;

    /**
     * 获取积分规则
     * @return
     */
    @RequiresPermissions("admin:creditsRule:getCreditsRule")
    @RequiresPermissionsDesc(menu = {"推广管理", "积分规则"}, button = "默认")
    @PostMapping("/getCreditsRule")
    public Object getCreditsRule(){
        return ResponseUtil.ok(service.getCreditsRule());
    }

    /**
     * 保存积分规则
     * @param ppCreditsRule
     * @return
     */
    @RequiresPermissions("admin:creditsRule:saveCreditsRule")
    @RequiresPermissionsDesc(menu = {"推广管理", "积分规则"}, button = "确定")
    @PostMapping("/saveCreditsRule")
    public Object saveCreditsRule(@RequestBody PpCreditsRule ppCreditsRule){
        service.saveCreditsRule(ppCreditsRule);
        return ResponseUtil.ok();
    }
}

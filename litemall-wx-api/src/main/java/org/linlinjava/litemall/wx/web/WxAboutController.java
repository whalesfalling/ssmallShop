package org.linlinjava.litemall.wx.web;

import org.linlinjava.litemall.core.util.ResponseUtil;
import org.linlinjava.litemall.db.service.LitemallSystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Auther: T-luot
 * @Date: 2019/8/9 17:08
 * @Description: 商场信息展示
 */
@RestController
@RequestMapping("/wx/local")
@Validated
public class WxAboutController {

    @Autowired
    private LitemallSystemConfigService systemConfigService;

    /**
     * 查询商场信息
     * @return
     */
    @GetMapping("/about")
    public Object listMall() {
        Map<String, String> data = systemConfigService.listMail();
        return ResponseUtil.ok(data);
    }
}

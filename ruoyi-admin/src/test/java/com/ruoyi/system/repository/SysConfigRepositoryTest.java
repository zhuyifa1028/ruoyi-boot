package com.ruoyi.system.repository;

import com.ruoyi.system.entity.SysConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
class SysConfigRepositoryTest {

    @Resource
    SysConfigRepository sysConfigRepository;

    @Order(1)
    @Test
    public void deleteTest() {
        log.debug("测试删除...");

        List<SysConfig> configList = sysConfigRepository.findAll();
        sysConfigRepository.deleteAll(configList);
    }

    @Order(2)
    @Test
    public void insertTest() {
        log.debug("测试新增...");

        SysConfig u1 = new SysConfig();
        u1.setConfigName("主框架页-默认皮肤样式名称");
        u1.setConfigKey("sys.index.skinName");
        sysConfigRepository.save(u1);

        SysConfig u2 = new SysConfig();
        u2.setConfigName("用户管理-账号初始密码");
        u2.setConfigKey("sys.user.initPassword");

        SysConfig u3 = new SysConfig();
        u3.setConfigName("主框架页-侧边栏主题");
        u3.setConfigKey("sys.index.sideTheme");

        SysConfig u4 = new SysConfig();
        u4.setConfigName("账号自助-验证码开关");
        u4.setConfigKey("sys.account.captchaEnabled");

        SysConfig u5 = new SysConfig();
        u5.setConfigName("账号自助-是否开启用户注册功能");
        u5.setConfigKey("sys.account.registerUser");

        SysConfig u6 = new SysConfig();
        u6.setConfigName("用户登录-黑名单列表");
        u6.setConfigKey("sys.login.blackIPList");

        sysConfigRepository.saveAll(Arrays.asList(u2, u3, u4, u5, u6));
    }

    @Order(3)
    @Test
    public void updateTest() {
        log.debug("测试修改...");

        SysConfig u1 = sysConfigRepository.selectByConfigKey("sys.index.skinName");
        u1.setConfigValue("skin-blue");
        u1.setConfigType("Y");
        u1.setRemark("蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow");
        sysConfigRepository.save(u1);

        SysConfig u2 = sysConfigRepository.selectByConfigKey("sys.user.initPassword");
        u2.setConfigValue("123456");
        u2.setConfigType("Y");
        u2.setRemark("初始化密码 123456");
        sysConfigRepository.save(u2);

        SysConfig u3 = sysConfigRepository.selectByConfigKey("sys.index.sideTheme");
        u3.setConfigValue("theme-dark");
        u3.setConfigType("Y");
        u3.setRemark("深色主题theme-dark，浅色主题theme-light");
        sysConfigRepository.save(u3);

        SysConfig u4 = sysConfigRepository.selectByConfigKey("sys.account.captchaEnabled");
        u4.setConfigValue("true");
        u4.setConfigType("Y");
        u4.setRemark("是否开启验证码功能（true开启，false关闭）");
        sysConfigRepository.save(u4);

        SysConfig u5 = sysConfigRepository.selectByConfigKey("sys.account.registerUser");
        u5.setConfigValue("false");
        u5.setConfigType("Y");
        u5.setRemark("是否开启注册用户功能（true开启，false关闭）");
        sysConfigRepository.save(u5);

        SysConfig u6 = sysConfigRepository.selectByConfigKey("sys.login.blackIPList");
        u6.setConfigValue("");
        u6.setConfigType("Y");
        u6.setRemark("设置登录IP黑名单限制，多个匹配项以;分隔，支持匹配（*通配、网段）");
        sysConfigRepository.save(u6);
    }
}

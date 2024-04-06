package com.mizore.easybuy;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;

@SpringBootTest
class EasyBuyApplicationTests {

    public static void main(String[] args) {

        FastAutoGenerator.create("jdbc:mysql://localhost:3306/EasyBuy?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true",
                        "mizore",
                        "mizore")
                .globalConfig(builder -> {
                    builder.author("mizore") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .outputDir("/home/mizore/IdeaProjects/EasyBuy/src/main/java/"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.mizore.easybuy") // 设置父包名
//                            .moduleName("model") // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml, "/home/mizore/IdeaProjects/EasyBuy/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(
                            String.format("%s,%s,%s,%s,%s,%s,%s,%s",
                                    "tb_user",
                                    "tb_seller",
                                    "tb_item",
                                    "tb_item_image",
                                    "tb_address",
                                    "tb_category",
                                    "tb_order",
                                    "tb_order_detail")
                            ) // 设置需要生成的表名
//							.addTablePrefix("foo_"); // 设置过滤表前缀
                            .controllerBuilder().enableFileOverride()
                            .mapperBuilder().enableFileOverride()
                            .serviceBuilder().enableFileOverride()
                            .entityBuilder().enableFileOverride();

                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

    @Test
    void contextLoads() {
    }

}

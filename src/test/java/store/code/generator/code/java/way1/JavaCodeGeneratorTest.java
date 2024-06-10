/*
 * Copyright (c) 2018. the original author or authors.
 * Kunlun is licensed under the "LICENSE" file in the project's root directory.
 */

package store.code.generator.code.java.way1;

import kunlun.renderer.support.VelocityTextRenderer;
import org.junit.Ignore;
import org.junit.Test;
import store.code.jdbc.way1.DatabaseClient;
import store.code.jdbc.way1.SimpleDataSource;

@Ignore
public class JavaCodeGeneratorTest {
    private static DatabaseClient databaseClient = new DatabaseClient(new SimpleDataSource());

    @Test
    public void test1() {
        JavaCodeGenerator generator = new JavaCodeGenerator().newCreator()
                .setDatabaseClient(databaseClient)
//                .setBaseTemplatePath("classpath:templates/generator/java/custom")
                .setBaseOutputPath("src\\test\\java")
                .setBasePackageName("kunlun.generator.out")
                .setTextRenderer(new VelocityTextRenderer())
                .addRemovedTableNamePrefixes("t_")
//                .addExcludedTables("t_15_user")
//                .addReservedTables("t_user")
                ;
        generator.addAttribute("author", "Kahle");
        generator.generate();
    }

}

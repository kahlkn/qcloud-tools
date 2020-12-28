package store.code.fake.way1;

import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.RandomUtils;
import artoria.util.StringUtils;

import static artoria.common.Constants.*;

public class NameFaker implements Faker {
    private static final char[] NAME_CHAR_ARRAY = "abcdefghijklmnopqrstuvwxyz".toCharArray();

    @Override
    public int can(String express, Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if (StringUtils.isBlank(express)) { return ZERO; }
        express = express.toLowerCase();
        boolean contains = express.contains("name")
                && express.endsWith("name");
        boolean assignable = String.class.isAssignableFrom(clazz);
        return contains && assignable ? FIFTY_ONE : ZERO;
    }

    @Override
    public <T> T fake(String express, Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        int count = RandomUtils.nextInt(TWO) + ONE;
        System.out.println(">>>>  "+count);
        StringBuilder builder = new StringBuilder();
        for (int i = ZERO; i < count; i++) {
            int length = RandomUtils.nextInt(SIX) + THREE;
            String str = RandomUtils.nextString(NAME_CHAR_ARRAY, length);
            str = StringUtils.capitalize(str);
            builder.append(str).append(BLANK_SPACE);
        }
        int length = builder.length();
        if (length > ZERO) { builder.deleteCharAt(length - ONE); }
        return ObjectUtils.cast(builder.toString(), clazz);
    }

}

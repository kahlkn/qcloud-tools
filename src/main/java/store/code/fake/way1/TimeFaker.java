package store.code.fake.way1;

import artoria.convert.type.TypeConvertUtils;
import artoria.time.DateTime;
import artoria.time.DateUtils;
import artoria.util.Assert;
import artoria.util.ObjectUtils;
import artoria.util.RandomUtils;
import artoria.util.StringUtils;

import java.sql.Timestamp;
import java.util.Date;

import static artoria.common.Constants.*;

public class TimeFaker implements Faker {
    private Integer bound;

    public TimeFaker() {

        this(FIVE);
    }

    public TimeFaker(Integer bound) {
        if (bound == null || bound <= ZERO) {
            throw new IllegalArgumentException(
                    "Parameter \"bound\" must not null and greater than 0. "
            );
        }
        this.bound = bound;
    }

    @Override
    public int can(String express, Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        boolean notBlank = StringUtils.isNotBlank(express);
        express = notBlank ? express.toLowerCase() : express;
        boolean contains = notBlank && (express.contains("time")
                || express.contains("date")
                || express.contains("timestamp"));
        boolean assignable = Date.class.isAssignableFrom(clazz)
                || java.sql.Date.class.isAssignableFrom(clazz)
                || Timestamp.class.isAssignableFrom(clazz);
        if (contains && assignable) { return SIXTY; }
        if (assignable) { return FIFTY_ONE; }
        return ZERO;
    }

    @Override
    public <T> T fake(String express, Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        int minYear = DateUtils.getYear(new Date()) - bound;
        int year = RandomUtils.nextInt(bound * TWO) + minYear;
        int month = RandomUtils.nextInt(THIRTEEN);
        int day = RandomUtils.nextInt(THIRTY_ONE);
        int hour = RandomUtils.nextInt(TWENTY_FOUR);
        int minute = RandomUtils.nextInt(SIXTY);
        int second = RandomUtils.nextInt(SIXTY);
        int millisecond = RandomUtils.nextInt(NINE_HUNDRED_NINETY_NINE);
        DateTime dateTime = DateUtils.create(
                year, month, day, hour, minute, second, millisecond
        );
        Date date = dateTime.getDate();
        Object convert = TypeConvertUtils.convert(date, clazz);
        return ObjectUtils.cast(convert, clazz);
    }

}

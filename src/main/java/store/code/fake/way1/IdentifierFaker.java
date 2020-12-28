package store.code.fake.way1;

import artoria.convert.type.TypeConvertUtils;
import artoria.identifier.IdentifierUtils;
import artoria.util.Assert;
import artoria.util.ClassUtils;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;

import static artoria.common.Constants.SIXTY;
import static artoria.common.Constants.ZERO;

public class IdentifierFaker implements Faker {

    @Override
    public int can(String express, Class<?> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        if (StringUtils.isBlank(express)) { return ZERO; }
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        express = express.toLowerCase();
        boolean contains = express.contains("id")
                || express.contains("identifier");
        boolean assignable = String.class.isAssignableFrom(wrapper)
                || Long.class.isAssignableFrom(wrapper);
        return contains && assignable ? SIXTY : ZERO;
    }

    @Override
    public <T> T fake(String express, Class<T> clazz) {
        Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
        Class<?> wrapper = ClassUtils.getWrapper(clazz);
        Object object;
        if (String.class.isAssignableFrom(wrapper)) {
            object = IdentifierUtils.nextStringIdentifier();
        }
        else if (Number.class.isAssignableFrom(wrapper)) {
            object = IdentifierUtils.nextLongIdentifier();
        }
        else {
            return null;
        }
        Object convert = TypeConvertUtils.convert(object, wrapper);
        return ObjectUtils.cast(convert, clazz);
    }

}

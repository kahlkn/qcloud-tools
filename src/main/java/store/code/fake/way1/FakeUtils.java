package store.code.fake.way1;

import artoria.exception.ExceptionUtils;
import artoria.logging.Logger;
import artoria.logging.LoggerFactory;
import artoria.util.Assert;
import artoria.util.ClassUtils;

import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static artoria.common.Constants.*;

public class FakeUtils {
    private static Logger log = LoggerFactory.getLogger(artoria.fake.FakeUtils.class);
    private static FakeProvider fakeProvider;

    public static FakeProvider getFakeProvider() {
        if (fakeProvider != null) { return fakeProvider; }
        synchronized (artoria.fake.FakeUtils.class) {
            if (fakeProvider != null) { return fakeProvider; }
            FakeUtils.setFakeProvider(new SimpleFakeProvider());
            return fakeProvider;
        }
    }

    public static void setFakeProvider(FakeProvider fakeProvider) {
        Assert.notNull(fakeProvider, "Parameter \"fakeProvider\" must not null. ");
        log.info("Set fake provider: {}", fakeProvider.getClass().getName());
        FakeUtils.fakeProvider = fakeProvider;
    }

    public static <T> T fake(Class<T> clazz) {

        return getFakeProvider().fake(EMPTY_STRING, clazz);
    }

    public static <T> T fake(String express, Class<T> clazz) {

        return getFakeProvider().fake(express, clazz);
    }

    public interface FakeProvider extends Faker {

        void register(Faker faker);

        void unregister(Faker faker);

    }

    public static class SimpleFakeProvider extends SimpleFaker implements FakeProvider {
        private static final List<Faker> FAKERS = new CopyOnWriteArrayList<Faker>();

        public SimpleFakeProvider() {
            register(new SimpleFaker());
            register(new NameFaker());
            register(new TimeFaker());
            register(new IdentifierFaker());
        }

        @Override
        public void register(Faker faker) {

            FAKERS.add(faker);
        }

        @Override
        public void unregister(Faker faker) {

            FAKERS.remove(faker);
        }

        @Override
        public int can(String express, Class<?> clazz) {

            return ONE_HUNDRED;
        }

        @Override
        public <T> T fake(String express, Class<T> clazz) {
            Assert.notNull(clazz, "Parameter \"clazz\" must not null. ");
            Class<?> wrapper = ClassUtils.getWrapper(clazz);
            verifyUnsupportedClass(wrapper);
            //
            boolean basic = Number.class.isAssignableFrom(wrapper)
                    || Boolean.class.isAssignableFrom(wrapper)
                    || Character.class.isAssignableFrom(wrapper)
                    || Date.class.isAssignableFrom(wrapper)
                    || String.class.isAssignableFrom(wrapper);
            if (basic) {
                Faker targetFaker = null; int maxScore = ZERO;
                for (Faker faker : FAKERS) {
                    int score = faker.can(express, clazz);
                    if (score < FIFTY) { continue; }
                    if (score >= ONE_HUNDRED) {
                        targetFaker = faker;
                        break;
                    }
                    if (score > maxScore) {
                        targetFaker = faker;
                        maxScore = score;
                    }
                }
                if (targetFaker == null) { return null; }
                return targetFaker.fake(express, clazz);
            }
            else {
                try {
                    return fakeBeanByClass(clazz);
                }
                catch (Exception e) {
                    throw ExceptionUtils.wrap(e);
                }
            }
        }

    }

}

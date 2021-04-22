package store.code.fake.way2.extend;

import artoria.util.RandomUtils;
import store.code.fake.way2.NameFaker;

import static artoria.common.Constants.EMPTY_STRING;

public class ChineseNameFaker extends NameFaker {
    private static final String[] LAST_NAME_ARRAY = new String[] {"王", "李", "张", "刘", "陈", "杨", "黄", "吴", "赵", "周", "徐", "孙", "马", "朱", "胡", "林", "郭", "何", "高", "罗", "郑", "梁", "谢", "宋", "唐", "许", "邓", "冯", "韩", "曹", "曾", "彭", "萧", "蔡", "潘", "田", "董", "袁", "于", "余", "叶", "蒋", "杜", "苏", "魏", "程", "吕", "丁", "沈", "任", "姚", "卢", "傅", "钟", "姜", "崔", "谭", "廖", "范", "汪", "陆", "金", "石", "戴", "贾", "韦", "夏", "邱", "方", "侯", "邹", "熊", "孟", "秦", "白", "江", "阎", "薛", "尹", "段", "雷", "黎", "史", "龙", "陶", "贺", "顾", "毛", "郝", "龚", "邵", "万", "钱", "严", "赖", "覃", "洪", "武", "莫", "孔", "欧阳"};
    private static final String[] FIRST_NAME_ARRAY = new String[] {"绍齐", "博文", "梓晨", "胤祥", "瑞霖", "明哲", "天翊", "凯瑞", "健雄", "耀杰", "潇然", "子涵", "越彬", "钰轩", "智辉", "致远", "俊驰", "雨泽", "烨磊", "晟睿", "文昊", "修洁", "黎昕", "远航", "旭尧", "鸿涛", "伟祺", "荣轩", "越泽", "浩宇", "瑾瑜", "皓轩", "擎苍", "擎宇", "志泽", "子轩", "睿渊", "弘文", "哲瀚", "雨泽", "楷瑞", "建辉", "晋鹏", "天磊", "绍辉", "泽洋", "鑫磊", "鹏煊", "昊强", "伟宸", "博超", "君浩", "子骞", "鹏涛", "炎彬", "鹤轩", "越彬", "风华", "靖琪", "明辉", "伟诚", "明轩", "健柏", "修杰", "志泽", "弘文", "峻熙", "嘉懿", "煜城", "懿轩", "烨伟", "苑博", "伟泽", "熠彤", "鸿煊", "博涛", "烨霖", "烨华", "煜祺", "智宸", "正豪", "昊然", "明杰", "立诚", "立轩", "立辉", "峻熙", "弘文", "熠彤", "鸿煊", "烨霖", "哲瀚", "鑫鹏", "昊天", "思聪", "展鹏", "笑愚", "志强", "炫明", "雪松", "思源", "智渊", "思淼", "晓啸", "天宇", "浩然", "文轩", "鹭洋", "振家", "乐驹", "晓博", "文博", "昊焱", "立果", "金鑫", "锦程", "嘉熙", "鹏飞", "子默", "思远", "浩轩", "语堂", "聪健", "明", "文", "果", "思", "鹏", "驰", "涛", "琪", "浩", "航", "彬", "娜"};

    @Override
    protected String firstName(String language) {
        if (isEnglish(language)) { return super.firstName(language); }
        else {
            int firstNameIndex = RandomUtils.nextInt(FIRST_NAME_ARRAY.length);
            return FIRST_NAME_ARRAY[firstNameIndex];
        }
    }

    @Override
    protected String middleName(String language) {
        if (isEnglish(language)) { return super.middleName(language); }
        else {
            return EMPTY_STRING;
        }
    }

    @Override
    protected String lastName(String language) {
        if (isEnglish(language)) { return super.lastName(language); }
        else {
            int lastNameIndex = RandomUtils.nextInt(LAST_NAME_ARRAY.length);
            return LAST_NAME_ARRAY[lastNameIndex];
        }
    }

    @Override
    protected String fullName(String language) {
        if (isEnglish(language)) { return super.fullName(language); }
        else {
            return lastName(language) + firstName(language);
        }
    }

}

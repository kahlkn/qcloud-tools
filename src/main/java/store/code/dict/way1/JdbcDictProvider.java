package store.code.dict.way1;

import artoria.beans.BeanUtils;
import artoria.util.Assert;
import artoria.util.CollectionUtils;
import artoria.util.ObjectUtils;
import artoria.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static artoria.common.Constants.*;

public class JdbcDictProvider implements DictProvider {
    private static Logger log = LoggerFactory.getLogger(JdbcDictProvider.class);
    private JdbcTemplate jdbcTemplate;
    private String groupColumnName;
    private String nameColumnName;
    private String valueColumnName;
    private String tableName;
    private String whereContent;

    public JdbcDictProvider(JdbcTemplate jdbcTemplate,
                            String groupColumnName,
                            String nameColumnName,
                            String valueColumnName,
                            String tableName,
                            String whereContent) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupColumnName = groupColumnName;
        this.nameColumnName = nameColumnName;
        this.valueColumnName = valueColumnName;
        this.tableName = tableName;
        this.whereContent = whereContent;
    }

    private String createOrAppendWhereSql(String whereSql,
                                          List<Object> args,
                                          String columnName,
                                          Object columnVal) {
        if (ObjectUtils.isEmpty(columnVal)) {
            return whereSql;
        }
        if (StringUtils.isBlank(whereSql)) {
            whereSql = String.format("where `%s` = ?", columnName);
        }
        else {
            if (!whereSql.endsWith(BLANK_SPACE)) {
                whereSql += BLANK_SPACE;
            }
            whereSql += String.format("and `%s` = ?", columnName);
        }
        args.add(columnVal);
        return whereSql;
    }

    @Override
    public void add(Dict dict) {
        boolean existDict = exist(new Dict(dict.getGroup(), dict.getName(), NULL_OBJ));
        Assert.state(!existDict, "Record already exist. ");
        String insertSql = String.format(
                "insert into `%s` (`%s`, `%s`, `%s`) values (?, ?, ?)",
                tableName, groupColumnName, nameColumnName, valueColumnName
        );
        List<Object> args = new ArrayList<Object>();
        args.add(dict.getGroup());
        args.add(dict.getName());
        args.add(dict.getValue());
        int insert = jdbcTemplate.update(insertSql, args.toArray());
        Assert.state(insert == ONE, "Add record failure. ");
    }

    @Override
    public void edit(Dict dict) {
        Assert.notBlank(dict.getName(), "Parameter \"name\" must not blank. ");
        Assert.notNull(dict.getValue(), "Parameter \"value\" must not null. ");
        String updateSql = String.format("update `%s` set `%s` = ?", tableName, valueColumnName);
        List<Object> args = new ArrayList<Object>();
        String whereSql = whereContent;
        args.add(dict.getValue());
        whereSql = createOrAppendWhereSql(whereSql, args, nameColumnName, dict.getName());
        whereSql = createOrAppendWhereSql(whereSql, args, groupColumnName, dict.getGroup());
        String sql = String.format("%s %s", updateSql, whereSql);
        int update = jdbcTemplate.update(sql, args.toArray());
        Assert.state(update == ONE, "Edit record failure. ");
    }

    @Override
    public void delete(Dict dict) {
        Assert.notBlank(dict.getName(), "Parameter \"name\" must not blank. ");
        String deleteSql = String.format("delete from `%s`", tableName);
        List<Object> args = new ArrayList<Object>();
        String whereSql = whereContent;
        whereSql = createOrAppendWhereSql(whereSql, args, nameColumnName, dict.getName());
        whereSql = createOrAppendWhereSql(whereSql, args, groupColumnName, dict.getGroup());
        whereSql = createOrAppendWhereSql(whereSql, args, valueColumnName, dict.getValue());
        String sql = String.format("%s %s", deleteSql, whereSql);
        int delete = jdbcTemplate.update(sql, args.toArray());
        Assert.state(delete == ONE, "Delete record failure. ");
    }

    @Override
    public boolean exist(Dict dict) {
        String selectSql = String.format("select count(0) from `%s`", tableName);
        List<Object> args = new ArrayList<Object>();
        String whereSql = whereContent;
        whereSql = createOrAppendWhereSql(whereSql, args, groupColumnName, dict.getGroup());
        whereSql = createOrAppendWhereSql(whereSql, args, nameColumnName, dict.getName());
        whereSql = createOrAppendWhereSql(whereSql, args, valueColumnName, dict.getValue());
        String sql = String.format("%s %s", selectSql, whereSql);
        Integer count = jdbcTemplate.queryForObject(sql, args.toArray(), Integer.class);
        return count != null && count > ZERO;
    }

    @Override
    public Dict find(Dict dict) {
        String selectSql = String.format(
                "select `%s` as `group`, `%s` as `name`, `%s` as `value` from `%s`",
                groupColumnName, nameColumnName, valueColumnName, tableName
        );
        List<Object> args = new ArrayList<Object>();
        String whereSql = whereContent;
        whereSql = createOrAppendWhereSql(whereSql, args, groupColumnName, dict.getGroup());
        whereSql = createOrAppendWhereSql(whereSql, args, nameColumnName, dict.getName());
        whereSql = createOrAppendWhereSql(whereSql, args, valueColumnName, dict.getValue());
        String sql = String.format("%s %s limit 1", selectSql, whereSql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, args.toArray());
        if (CollectionUtils.isEmpty(mapList)) { return null; }
        return BeanUtils.mapToBean(mapList.get(ZERO), Dict.class);
    }

    @Override
    public List<Dict> findList(Dict dict) {
        String selectSql = String.format(
                "select `%s` as `group`, `%s` as `name`, `%s` as `value` from `%s`",
                groupColumnName, nameColumnName, valueColumnName, tableName
        );
        List<Object> args = new ArrayList<Object>();
        String whereSql = whereContent;
        whereSql = createOrAppendWhereSql(whereSql, args, groupColumnName, dict.getGroup());
        whereSql = createOrAppendWhereSql(whereSql, args, nameColumnName, dict.getName());
        whereSql = createOrAppendWhereSql(whereSql, args, valueColumnName, dict.getValue());
        String sql = String.format("%s %s", selectSql, whereSql);
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList(sql, args.toArray());
        return BeanUtils.mapToBeanInList(mapList, Dict.class);
    }

}

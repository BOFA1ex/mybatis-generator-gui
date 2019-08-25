package com.bofa.management.dao.entity;

import java.util.*;

/**
 * @author bofa1ex
 * @version 1.0
 * @since 2019-08-25
 */
public class DbconfigExample {
    protected String orderByClause;
    protected boolean distinct;
    protected List<Criteria> oredCriteria = new ArrayList<>();

    public DbconfigExample() {
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return this.distinct;
    }

    public List<DbconfigExample.Criteria> getOredCriteria() {
        return this.oredCriteria;
    }

    public void or(DbconfigExample.Criteria criteria) {
        this.oredCriteria.add(criteria);
    }

    public DbconfigExample.Criteria or() {
        DbconfigExample.Criteria criteria = this.createCriteriaInternal();
        this.oredCriteria.add(criteria);
        return criteria;
    }

    public DbconfigExample.Criteria createCriteria() {
        DbconfigExample.Criteria criteria = this.createCriteriaInternal();
        if (this.oredCriteria.size() == 0) {
            this.oredCriteria.add(criteria);
        }

        return criteria;
    }

    protected DbconfigExample.Criteria createCriteriaInternal() {
        DbconfigExample.Criteria criteria = new DbconfigExample.Criteria();
        return criteria;
    }

    public void clear() {
        this.oredCriteria.clear();
        this.orderByClause = null;
        this.distinct = false;
    }

    public static class Criterion {
        private String condition;
        private Object value;
        private Object secondValue;
        private boolean noValue;
        private boolean singleValue;
        private boolean betweenValue;
        private boolean listValue;
        private String typeHandler;

        public String getCondition() {
            return this.condition;
        }

        public Object getValue() {
            return this.value;
        }

        public Object getSecondValue() {
            return this.secondValue;
        }

        public boolean isNoValue() {
            return this.noValue;
        }

        public boolean isSingleValue() {
            return this.singleValue;
        }

        public boolean isBetweenValue() {
            return this.betweenValue;
        }

        public boolean isListValue() {
            return this.listValue;
        }

        public String getTypeHandler() {
            return this.typeHandler;
        }

        protected Criterion(String condition) {
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }

        }

        protected Criterion(String condition, Object value) {
            this(condition, value, (String) null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, (String) null);
        }
    }

    public static class Criteria extends DbconfigExample.GeneratedCriteria {
        protected Criteria() {
        }
    }

    protected abstract static class GeneratedCriteria {
        protected List<DbconfigExample.Criterion> criteria = new ArrayList();

        protected GeneratedCriteria() {
        }

        public boolean isValid() {
            return this.criteria.size() > 0;
        }

        public List<DbconfigExample.Criterion> getAllCriteria() {
            return this.criteria;
        }

        public List<DbconfigExample.Criterion> getCriteria() {
            return this.criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            } else {
                this.criteria.add(new DbconfigExample.Criterion(condition));
            }
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            } else {
                this.criteria.add(new DbconfigExample.Criterion(condition, value));
            }
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 != null && value2 != null) {
                this.criteria.add(new DbconfigExample.Criterion(condition, value1, value2));
            } else {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
        }

        public DbconfigExample.Criteria andIdIsNull() {
            this.addCriterion("ID is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdIsNotNull() {
            this.addCriterion("ID is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdEqualTo(Long value) {
            this.addCriterion("ID =", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdNotEqualTo(Long value) {
            this.addCriterion("ID <>", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdGreaterThan(Long value) {
            this.addCriterion("ID >", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdGreaterThanOrEqualTo(Long value) {
            this.addCriterion("ID >=", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdLessThan(Long value) {
            this.addCriterion("ID <", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdLessThanOrEqualTo(Long value) {
            this.addCriterion("ID <=", value, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdIn(List<Long> values) {
            this.addCriterion("ID in", values, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdNotIn(List<Long> values) {
            this.addCriterion("ID not in", values, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdBetween(Long value1, Long value2) {
            this.addCriterion("ID between", value1, value2, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andIdNotBetween(Long value1, Long value2) {
            this.addCriterion("ID not between", value1, value2, "id");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaIsNull() {
            this.addCriterion("DBSCHEMA is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaIsNotNull() {
            this.addCriterion("DBSCHEMA is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaEqualTo(String value) {
            this.addCriterion("DBSCHEMA =", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaNotEqualTo(String value) {
            this.addCriterion("DBSCHEMA <>", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaGreaterThan(String value) {
            this.addCriterion("DBSCHEMA >", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBSCHEMA >=", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaLessThan(String value) {
            this.addCriterion("DBSCHEMA <", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaLessThanOrEqualTo(String value) {
            this.addCriterion("DBSCHEMA <=", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaLike(String value) {
            this.addCriterion("DBSCHEMA like", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaNotLike(String value) {
            this.addCriterion("DBSCHEMA not like", value, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaIn(List<String> values) {
            this.addCriterion("DBSCHEMA in", values, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaNotIn(List<String> values) {
            this.addCriterion("DBSCHEMA not in", values, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaBetween(String value1, String value2) {
            this.addCriterion("DBSCHEMA between", value1, value2, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbschemaNotBetween(String value1, String value2) {
            this.addCriterion("DBSCHEMA not between", value1, value2, "dbschema");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeIsNull() {
            this.addCriterion("DBTYPE is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeIsNotNull() {
            this.addCriterion("DBTYPE is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeEqualTo(String value) {
            this.addCriterion("DBTYPE =", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeNotEqualTo(String value) {
            this.addCriterion("DBTYPE <>", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeGreaterThan(String value) {
            this.addCriterion("DBTYPE >", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBTYPE >=", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeLessThan(String value) {
            this.addCriterion("DBTYPE <", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeLessThanOrEqualTo(String value) {
            this.addCriterion("DBTYPE <=", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeLike(String value) {
            this.addCriterion("DBTYPE like", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeNotLike(String value) {
            this.addCriterion("DBTYPE not like", value, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeIn(List<String> values) {
            this.addCriterion("DBTYPE in", values, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeNotIn(List<String> values) {
            this.addCriterion("DBTYPE not in", values, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeBetween(String value1, String value2) {
            this.addCriterion("DBTYPE between", value1, value2, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbtypeNotBetween(String value1, String value2) {
            this.addCriterion("DBTYPE not between", value1, value2, "dbtype");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionIsNull() {
            this.addCriterion("DESCRIPTION is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionIsNotNull() {
            this.addCriterion("DESCRIPTION is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionEqualTo(String value) {
            this.addCriterion("DESCRIPTION =", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionNotEqualTo(String value) {
            this.addCriterion("DESCRIPTION <>", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionGreaterThan(String value) {
            this.addCriterion("DESCRIPTION >", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionGreaterThanOrEqualTo(String value) {
            this.addCriterion("DESCRIPTION >=", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionLessThan(String value) {
            this.addCriterion("DESCRIPTION <", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionLessThanOrEqualTo(String value) {
            this.addCriterion("DESCRIPTION <=", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionLike(String value) {
            this.addCriterion("DESCRIPTION like", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionNotLike(String value) {
            this.addCriterion("DESCRIPTION not like", value, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionIn(List<String> values) {
            this.addCriterion("DESCRIPTION in", values, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionNotIn(List<String> values) {
            this.addCriterion("DESCRIPTION not in", values, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionBetween(String value1, String value2) {
            this.addCriterion("DESCRIPTION between", value1, value2, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDescriptionNotBetween(String value1, String value2) {
            this.addCriterion("DESCRIPTION not between", value1, value2, "description");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlIsNull() {
            this.addCriterion("DBURL is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlIsNotNull() {
            this.addCriterion("DBURL is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlEqualTo(String value) {
            this.addCriterion("DBURL =", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlNotEqualTo(String value) {
            this.addCriterion("DBURL <>", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlGreaterThan(String value) {
            this.addCriterion("DBURL >", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBURL >=", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlLessThan(String value) {
            this.addCriterion("DBURL <", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlLessThanOrEqualTo(String value) {
            this.addCriterion("DBURL <=", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlLike(String value) {
            this.addCriterion("DBURL like", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlNotLike(String value) {
            this.addCriterion("DBURL not like", value, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlIn(List<String> values) {
            this.addCriterion("DBURL in", values, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlNotIn(List<String> values) {
            this.addCriterion("DBURL not in", values, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlBetween(String value1, String value2) {
            this.addCriterion("DBURL between", value1, value2, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDburlNotBetween(String value1, String value2) {
            this.addCriterion("DBURL not between", value1, value2, "dburl");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveIsNull() {
            this.addCriterion("DBDRIVE is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveIsNotNull() {
            this.addCriterion("DBDRIVE is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveEqualTo(String value) {
            this.addCriterion("DBDRIVE =", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveNotEqualTo(String value) {
            this.addCriterion("DBDRIVE <>", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveGreaterThan(String value) {
            this.addCriterion("DBDRIVE >", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBDRIVE >=", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveLessThan(String value) {
            this.addCriterion("DBDRIVE <", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveLessThanOrEqualTo(String value) {
            this.addCriterion("DBDRIVE <=", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveLike(String value) {
            this.addCriterion("DBDRIVE like", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveNotLike(String value) {
            this.addCriterion("DBDRIVE not like", value, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveIn(List<String> values) {
            this.addCriterion("DBDRIVE in", values, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveNotIn(List<String> values) {
            this.addCriterion("DBDRIVE not in", values, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveBetween(String value1, String value2) {
            this.addCriterion("DBDRIVE between", value1, value2, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbdriveNotBetween(String value1, String value2) {
            this.addCriterion("DBDRIVE not between", value1, value2, "dbdrive");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameIsNull() {
            this.addCriterion("DBNAME is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameIsNotNull() {
            this.addCriterion("DBNAME is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameEqualTo(String value) {
            this.addCriterion("DBNAME =", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameNotEqualTo(String value) {
            this.addCriterion("DBNAME <>", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameGreaterThan(String value) {
            this.addCriterion("DBNAME >", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBNAME >=", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameLessThan(String value) {
            this.addCriterion("DBNAME <", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameLessThanOrEqualTo(String value) {
            this.addCriterion("DBNAME <=", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameLike(String value) {
            this.addCriterion("DBNAME like", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameNotLike(String value) {
            this.addCriterion("DBNAME not like", value, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameIn(List<String> values) {
            this.addCriterion("DBNAME in", values, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameNotIn(List<String> values) {
            this.addCriterion("DBNAME not in", values, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameBetween(String value1, String value2) {
            this.addCriterion("DBNAME between", value1, value2, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbnameNotBetween(String value1, String value2) {
            this.addCriterion("DBNAME not between", value1, value2, "dbname");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordIsNull() {
            this.addCriterion("DBPASSWORD is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordIsNotNull() {
            this.addCriterion("DBPASSWORD is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordEqualTo(String value) {
            this.addCriterion("DBPASSWORD =", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordNotEqualTo(String value) {
            this.addCriterion("DBPASSWORD <>", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordGreaterThan(String value) {
            this.addCriterion("DBPASSWORD >", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordGreaterThanOrEqualTo(String value) {
            this.addCriterion("DBPASSWORD >=", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordLessThan(String value) {
            this.addCriterion("DBPASSWORD <", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordLessThanOrEqualTo(String value) {
            this.addCriterion("DBPASSWORD <=", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordLike(String value) {
            this.addCriterion("DBPASSWORD like", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordNotLike(String value) {
            this.addCriterion("DBPASSWORD not like", value, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordIn(List<String> values) {
            this.addCriterion("DBPASSWORD in", values, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordNotIn(List<String> values) {
            this.addCriterion("DBPASSWORD not in", values, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordBetween(String value1, String value2) {
            this.addCriterion("DBPASSWORD between", value1, value2, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andDbpasswordNotBetween(String value1, String value2) {
            this.addCriterion("DBPASSWORD not between", value1, value2, "dbpassword");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateIsNull() {
            this.addCriterion("CREATE_DATE is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateIsNotNull() {
            this.addCriterion("CREATE_DATE is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateEqualTo(Date value) {
            this.addCriterion("CREATE_DATE =", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateNotEqualTo(Date value) {
            this.addCriterion("CREATE_DATE <>", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateGreaterThan(Date value) {
            this.addCriterion("CREATE_DATE >", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateGreaterThanOrEqualTo(Date value) {
            this.addCriterion("CREATE_DATE >=", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateLessThan(Date value) {
            this.addCriterion("CREATE_DATE <", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateLessThanOrEqualTo(Date value) {
            this.addCriterion("CREATE_DATE <=", value, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateIn(List<Date> values) {
            this.addCriterion("CREATE_DATE in", values, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateNotIn(List<Date> values) {
            this.addCriterion("CREATE_DATE not in", values, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateBetween(Date value1, Date value2) {
            this.addCriterion("CREATE_DATE between", value1, value2, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andCreateDateNotBetween(Date value1, Date value2) {
            this.addCriterion("CREATE_DATE not between", value1, value2, "createDate");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusIsNull() {
            this.addCriterion("STATUS is null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusIsNotNull() {
            this.addCriterion("STATUS is not null");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusEqualTo(String value) {
            this.addCriterion("STATUS =", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusNotEqualTo(String value) {
            this.addCriterion("STATUS <>", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusGreaterThan(String value) {
            this.addCriterion("STATUS >", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusGreaterThanOrEqualTo(String value) {
            this.addCriterion("STATUS >=", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusLessThan(String value) {
            this.addCriterion("STATUS <", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusLessThanOrEqualTo(String value) {
            this.addCriterion("STATUS <=", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusLike(String value) {
            this.addCriterion("STATUS like", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusNotLike(String value) {
            this.addCriterion("STATUS not like", value, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusIn(List<String> values) {
            this.addCriterion("STATUS in", values, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusNotIn(List<String> values) {
            this.addCriterion("STATUS not in", values, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusBetween(String value1, String value2) {
            this.addCriterion("STATUS between", value1, value2, "status");
            return (DbconfigExample.Criteria) this;
        }

        public DbconfigExample.Criteria andStatusNotBetween(String value1, String value2) {
            this.addCriterion("STATUS not between", value1, value2, "status");
            return (DbconfigExample.Criteria) this;
        }
    }
}

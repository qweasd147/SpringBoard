package com.joo.api.common;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Enum과 Mybatis 맵핑 중간 역할을 한다.
 * @param <E>
 */
public class EnumTypeHandler <E extends Enum<E> & EnumCodeType> implements TypeHandler<E>{

    private Class <E> enumCodeType;

    public EnumTypeHandler(Class <E> enumCodeType) {
        this.enumCodeType = enumCodeType;
    }

    @Override
    public void setParameter(PreparedStatement ps, int i, E parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, parameter.getCode());
    }

    @Override
    public E getResult(ResultSet rs, String columnName) throws SQLException {
        String code = rs.getString(columnName);
        return getEnumByCode(code);
    }

    @Override
    public E getResult(ResultSet rs, int columnIndex) throws SQLException {
        String code = rs.getString(columnIndex);
        return getEnumByCode(code);
    }

    @Override
    public E getResult(CallableStatement cs, int columnIndex) throws SQLException {
        String code = cs.getString(columnIndex);
        return getEnumByCode(code);
    }

    protected E getEnumByCode(String code) {
        try {
            E[] enumConstants = enumCodeType.getEnumConstants();
            for (E enumCodeType: enumConstants) {
                if (enumCodeType.getCode().equals(code)) {
                    return enumCodeType;
                }
            }
            return null;
        } catch (Exception e) {
            throw new TypeException("Can't make enum object '" + enumCodeType + "'", e);
        }
    }

    /*
    protected E getEnumByCode(String code){
        EnumSet<E> enums = EnumSet.allOf(enumCodeType);

        for(E enumElem : enums){
            if(enumElem.getCode().equals(code))
                return enumElem;
        }

        return null;
    }
    */
}
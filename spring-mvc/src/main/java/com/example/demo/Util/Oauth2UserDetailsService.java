package com.example.demo.Util;

import org.apache.log4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class Oauth2UserDetailsService implements UserDetailsService {
    private final static Logger logger = Logger.getLogger(Oauth2UserDetailsService.class);
    private DataSource dataSource;
    private final JdbcTemplate jdbcTemplate;
    private final static String selectUserByUserNameSql = "SELECT user_name ,password ,role ,enabled  FROM student where user_name =? ";
    public Oauth2UserDetailsService(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource required");
        this.dataSource = dataSource;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    /***
     * loadUser
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Get Data for User: "+ username);
        UserDetails userDetails;
        try {
            userDetails = jdbcTemplate.queryForObject(selectUserByUserNameSql, new UserDetailsRowMapper(), username);
        }
        catch (EmptyResultDataAccessException e) {
            throw new UsernameNotFoundException("UsernameNotFound :"+ username);
        }

        logger.info("userDetail: " + userDetails);
        return userDetails;
    }

    /***
     *
     * @author HRFman
     * @date   2019 - 13:12:32
     * @mail   faez.ham@gmail.com
     */
    private static class UserDetailsRowMapper implements RowMapper<UserDetails> {

        public UserDetails mapRow(ResultSet rs, int indx) throws SQLException {

            Set<GrantedAuthority> authorities =  new HashSet();
            GrantedAuthority authority = new SimpleGrantedAuthority(rs.getString("role")); // ROLE_APP

            authorities.add(authority);
            //String pass = rs.getString("password");
            AppUser appUser = new AppUser(rs.getString("user_name"), rs.getString("password"), rs.getBoolean("enabled"), true, true, true, authorities);
            return appUser;
        }
    }
}

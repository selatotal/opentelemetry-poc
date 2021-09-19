package br.com.selat.appsample.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseService.class);
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public DatabaseService(DataSource datasource) {
        this.jdbcTemplate = new JdbcTemplate(datasource);
    }

    public Integer health(){
        LOGGER.info("Starting database health");
        String sql = "SELECT 1 FROM DUAL";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}

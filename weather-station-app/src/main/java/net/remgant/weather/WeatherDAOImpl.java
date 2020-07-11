package net.remgant.weather;

import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class WeatherDAOImpl implements WeatherDAO {

    private final JdbcTemplate jdbcTemplate;

    public WeatherDAOImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public WeatherDAOImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @PostConstruct
    public void init() {
        try {
            jdbcTemplate.queryForObject("select count(*) from updates", Integer.class);
            return;
        }  catch (BadSqlGrammarException e) {

        }

        jdbcTemplate.update("create table updates (" +
                "timestamp int," +
                "tempF float," +
                "tempC float," +
                "humidity float," +
                "pressure float)");
    }

    @Override
    public void update(WeatherUpdate update) {
        jdbcTemplate.update("insert into updates (timestamp,tempF,tempC,humidity,pressure) values(?,?,?,?,?)",
                new Object[]{update.getTimestamp(), update.getTempF(), update.getTempC(), update.getHumidity(), update.getPressure()});

    }

    @Override
    public Collection<WeatherUpdate> findUpdatesBetween(Instant start, Instant end) {
        return jdbcTemplate.query(
                conn -> conn.prepareStatement("select timestamp, tempF, tempC, humidity, pressure from updates " +
                        "where timestamp > ? and timestamp <= ? order by timestamp"),
                ps -> {
                    ps.setLong(1, start.getEpochSecond());
                    ps.setLong(2, end.getEpochSecond());
                },
                rs -> {
                    List<WeatherUpdate> list = new ArrayList<>();
                    while (rs.next()) {
                        list.add(new WeatherUpdate(rs.getLong(1), rs.getDouble(2), rs.getDouble(3), rs.getDouble(4), rs.getDouble(5)));
                    }
                    return list;
                });
    }
}

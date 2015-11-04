/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lateralthoughts.service.impl;

import com.github.lateralthoughts.domain.Rabbit;
import com.github.lateralthoughts.service.RabbitService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author den
 */
@Service
public class RabbitServiceImpl implements RabbitService {

    private final DataSource dataSource;

    @Autowired
    public RabbitServiceImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public List<Rabbit> findAllRabbits() {
        try {
            return new JdbcTemplate(dataSource).query(
                    "SELECT name, color FROM Rabbits",
                    new BeanPropertyRowMapper<Rabbit>(Rabbit.class)
            );
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return new ArrayList();
        }
    }

    public Rabbit getRabbit(String rabbit) {
        try {
            List<Rabbit> rabbits = new JdbcTemplate(dataSource).query(
                    "SELECT name, color FROM Rabbits WHERE name='" + rabbit + "'",
                    new BeanPropertyRowMapper<Rabbit>(Rabbit.class)
            );
            if (rabbits.size() > 0) {
                return rabbits.get(0);
            } else {
                return new Rabbit();
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return new Rabbit();
        }
    }

    public Rabbit getMovePeriodRabbit(String rabbit, String range, Date period) {
        try {
            List<Rabbit> moveRabbits = new JdbcTemplate(dataSource).query(
                    "SELECT name, color FROM Rabbits WHERE name='" + rabbit + "' AND range='" + range + "' AND period='" + period + "'",
                    new BeanPropertyRowMapper<Rabbit>(Rabbit.class)
            );
            if (moveRabbits.size() > 0) {
                return moveRabbits.get(0);
            } else {
                return new Rabbit();
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return new Rabbit();
        }
    }

    public String createRabbit(String rabbitStr) {
        Gson gson = new Gson();
        try {
            String insertSql = "INSERT INTO Rabbits (name, color, age, photo)  VALUES(?,?,?,?);";

            Rabbit rabbit = gson.fromJson(rabbitStr, Rabbit.class);
            new JdbcTemplate(dataSource).update(insertSql, new Object[]{rabbit.getName(), rabbit.getColor(), rabbit.getAge(), rabbit.getPhoto()});
            return rabbitStr;

        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            //TODO:Error class
            return "{\"status\":\"error\"}";
        }
    }

    public String deleteRabbit(String rabbitName) {
        try {

            String sql = "DELETE FROM rabbits WHERE name LIKE '" + rabbitName + "'";
            Gson gson = new Gson();

            new JdbcTemplate(dataSource).update(sql);
            return "{\"status\":\"success\"}";

        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return "{\"status\":\"error\"}";
        }
    }

    public String findRabbitByName(String name) {
        Gson gson = new Gson();
        Rabbit rabbit = getRabbit(name);
        return gson.toJson(rabbit);
    }

    public String updateRabbit(String rabbitStr) {
        Gson gson = new Gson();
        try {
            String sql = "UPDATE Rabbits SET color=?, age=?, photo=? WHERE name LIKE (?)";
            Rabbit rabbit = gson.fromJson(rabbitStr, Rabbit.class);
            new JdbcTemplate(dataSource).update(sql, new Object[]{rabbit.getColor(), rabbit.getAge(), rabbit.getPhoto(), rabbit.getName()});
            return rabbitStr;

        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            //TODO:Error class
            return "{\"status\":\"error\"}";
        }
    }

    public Rabbit getPastureRabbit(String rabbit, String pasture_type) {
        try {
            List<Rabbit> rabbits = new JdbcTemplate(dataSource).query(
                    "SELECT name, color FROM Rabbits WHERE name='" + rabbit + "' AND pasture_type='" + pasture_type + "'",
                    new BeanPropertyRowMapper<Rabbit>(Rabbit.class)
            );
            if (rabbits.size() > 0) {
                return rabbits.get(0);
            } else {
                return new Rabbit();
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return new Rabbit();
        }
    }

}

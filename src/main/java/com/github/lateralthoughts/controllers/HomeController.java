package com.github.lateralthoughts.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.lateralthoughts.domain.Rabbit;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.Date;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    private final Integer RABBIT_ENERGY = 100;
    private final DataSource dataSource;

    @Autowired
    public HomeController(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @RequestMapping(value = "/rabbits", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String welcome() {

        JSONArray jsonRabbits = new JSONArray();
        for (Rabbit rabbit : findAllRabbits()) {
            JSONObject jsonRabbit = new JSONObject();
            jsonRabbit.put("name", rabbit.getName());
            jsonRabbit.put("color", rabbit.getColor());
            jsonRabbits.add(jsonRabbit);
        }
        return jsonRabbits.toString();
    }

    @RequestMapping(value = "/rabbits/{rabbit}/energy", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String energy(@PathVariable("rabbit") String rabbit) {
        Rabbit foundRabbit = getRabbit(rabbit);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", foundRabbit.getName());
        jsonRabbit.put("color", foundRabbit.getColor());
        jsonRabbit.put("energy", RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    private List<Rabbit> findAllRabbits() {
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

    private Rabbit getRabbit(String rabbit) {
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

    @RequestMapping(value = "/rabbits/{rabbit}/pasture/{pasture_type}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String pasture(@PathVariable("rabbit") String rabbit, @PathVariable("pasture_type") String pasture_type) {
        Rabbit pastureRabbit = getPastureRabbit(rabbit, pasture_type);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", pastureRabbit.getName());
        jsonRabbit.put("color", pastureRabbit.getColor());
        jsonRabbit.put("energy", RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    private Rabbit getPastureRabbit(String rabbit, String pasture_type) {
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

    @RequestMapping(value = "/rabbits/{rabbit}/move/{range}/period/{period}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String move(@PathVariable("rabbit") String rabbit,
            @PathVariable("range") String range,
            @PathVariable("period") Date period) {
        Rabbit moveRabbit = getMovePeriodRabbit(rabbit, range, period);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", moveRabbit.getName());
        jsonRabbit.put("color", moveRabbit.getColor());
        jsonRabbit.put("energy", RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    private Rabbit getMovePeriodRabbit(String rabbit, String range, Date period) {
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

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    String create(@RequestBody String rabbitEntry) {
        return createRabbit(rabbitEntry);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String delete(@PathVariable("name") String name) {
        return deleteRabbit(name);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String findByName(@PathVariable("name") String name) {

        return findRabbitByName(name);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String update(@RequestBody String rabbit) {

        String updated = updateRabbit(rabbit);
        return updated;
    }

    private String createRabbit(String rabbitStr) {
        Gson gson = new Gson();
        try {
            String insertSql = "INSERT INTO Rabbits (name, color, age, photo)  VALUES(?,?,?,?);";

            Rabbit rabbit = gson.fromJson(rabbitStr, Rabbit.class);
            //String name = ;
            //String color = rabbit.getColor();

            new JdbcTemplate(dataSource).update(insertSql, new Object[]{rabbit.getName(), rabbit.getColor(), rabbit.getAge(), rabbit.getPhoto()});
            return rabbitStr;

        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            //TODO:Error class
            return "{\"status\":\"error\"}";
        }
    }

    private String deleteRabbit(String rabbitName) {
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

    private String findRabbitByName(String name) {
        Gson gson = new Gson();
        Rabbit rabbit = getRabbit(name);
        return gson.toJson(rabbit);
    }

    private String updateRabbit(String rabbitStr) {
        Gson gson = new Gson();
        try {
            String sql = "UPDATE Rabbits SET color=?, age=?, photo=? WHERE name LIKE (?)";

            Rabbit rabbit = gson.fromJson(rabbitStr, Rabbit.class);
            //String name = ;
            //String color = rabbit.getColor();

            new JdbcTemplate(dataSource).update(sql, new Object[]{rabbit.getColor(), rabbit.getAge(), rabbit.getPhoto(), rabbit.getName()});
            return rabbitStr;

        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            //TODO:Error class
            return "{\"status\":\"error\"}";
        }
    }

}

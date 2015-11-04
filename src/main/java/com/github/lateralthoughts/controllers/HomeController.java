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
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    private final Integer RABBIT_ENERGY=100;
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
        Rabbit foundRabbit=getRabbit(rabbit);
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
            List<Rabbit> rabbits= new JdbcTemplate(dataSource).query(
                    "SELECT name, color FROM Rabbits WHERE name='"+rabbit+"'",
                    new BeanPropertyRowMapper<Rabbit>(Rabbit.class)
            );
            if(rabbits.size()>0){
                return rabbits.get(0);
            }else{
                return new Rabbit();
            }
        } catch (Exception e) {
            System.out.println("Exception:" + e.getLocalizedMessage());
            return new Rabbit();
        }
    }
    
}
package com.github.lateralthoughts.controllers;

import static org.springframework.web.bind.annotation.RequestMethod.GET;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.lateralthoughts.domain.Rabbit;
import static com.github.lateralthoughts.domain.Rabbit.RABBIT_ENERGY;
import com.github.lateralthoughts.service.RabbitService;
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

    private final RabbitService service;

    @Autowired
    public HomeController(RabbitService service) {
        this.service = service;
    }

    @RequestMapping(value = "/rabbits", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String welcome() {
        JSONArray jsonRabbits = new JSONArray();
        for (Rabbit rabbit : service.findAllRabbits()) {
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
        Rabbit foundRabbit = service.getRabbit(rabbit);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", foundRabbit.getName());
        jsonRabbit.put("color", foundRabbit.getColor());
        jsonRabbit.put("energy", Rabbit.RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    @RequestMapping(value = "/rabbits/{rabbit}/pasture/{pasture_type}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String pasture(@PathVariable("rabbit") String rabbit, @PathVariable("pasture_type") String pasture_type) {
        Rabbit pastureRabbit = service.getPastureRabbit(rabbit, pasture_type);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", pastureRabbit.getName());
        jsonRabbit.put("color", pastureRabbit.getColor());
        jsonRabbit.put("energy", RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    @RequestMapping(value = "/rabbits/{rabbit}/move/{range}/period/{period}", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String move(@PathVariable("rabbit") String rabbit,
            @PathVariable("range") String range,
            @PathVariable("period") Date period) {
        Rabbit moveRabbit = service.getMovePeriodRabbit(rabbit, range, period);
        JSONObject jsonRabbit = new JSONObject();
        jsonRabbit.put("name", moveRabbit.getName());
        jsonRabbit.put("color", moveRabbit.getColor());
        jsonRabbit.put("energy", RABBIT_ENERGY);
        return jsonRabbit.toString();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.CREATED)
    String create(@RequestBody String rabbitEntry) {
        return service.createRabbit(rabbitEntry);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String delete(@PathVariable("name") String name) {
        return service.deleteRabbit(name);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String findByName(@PathVariable("name") String name) {

        return service.findRabbitByName(name);
    }

    @RequestMapping(value = "/rabbit/{name}", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    String update(@RequestBody String rabbit) {

        String updated = service.updateRabbit(rabbit);
        return updated;
    }

}

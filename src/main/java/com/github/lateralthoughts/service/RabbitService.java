/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.github.lateralthoughts.service;

import com.github.lateralthoughts.domain.Rabbit;
import java.util.Date;
import java.util.List;

/**
 *
 * @author den
 */
public interface RabbitService {

    public List<Rabbit> findAllRabbits();

    public Rabbit getRabbit(String rabbit);

    public Rabbit getMovePeriodRabbit(String rabbit, String range, Date period);

    public String createRabbit(String rabbitStr);

    public String deleteRabbit(String rabbitName);

    public String findRabbitByName(String name);

    public String updateRabbit(String rabbitStr);

    public Rabbit getPastureRabbit(String rabbit, String pasture_type);
}

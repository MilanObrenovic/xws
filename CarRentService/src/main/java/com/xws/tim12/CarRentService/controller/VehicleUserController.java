package com.xws.tim12.CarRentService.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.xws.tim12.CarRentService.service.VehicleUserService;

@RestController
@RequestMapping(value = "/api/vehicleUser", produces = MediaType.APPLICATION_JSON_VALUE)
public class VehicleUserController {
	
	@Autowired
	private VehicleUserService vehicleUserService;
	
}
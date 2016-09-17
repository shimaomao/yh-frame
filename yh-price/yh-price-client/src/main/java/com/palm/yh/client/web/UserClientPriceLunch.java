package com.palm.yh.client.web;


import com.palm.vertx.core.application.Johan;

public class UserClientPriceLunch {
	public static void main(String[] args) {
		Johan.lunch(args, initResult -> {
			if (initResult) {
			}
		});
	}
}

package com.palm.yh.price.server;


import com.palm.vertx.core.application.Johan;

public class UserPriceLunch {
	public static void main(String[] args) {
		Johan.lunch(args, initResult -> {
			if (initResult) {
			}
		});
	}
}

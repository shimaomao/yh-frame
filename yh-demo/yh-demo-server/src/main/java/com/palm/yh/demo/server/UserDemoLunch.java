package com.palm.yh.demo.server;


import com.palm.vertx.core.application.Johan;

public class UserDemoLunch {
	public static void main(String[] args) {
		Johan.lunch(args, initResult -> {
			if (initResult) {
			}
		});
	}
}

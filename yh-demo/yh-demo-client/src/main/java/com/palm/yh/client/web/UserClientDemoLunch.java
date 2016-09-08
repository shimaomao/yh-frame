package com.palm.yh.client.web;


import com.palm.vertx.core.application.Johan;

public class UserClientDemoLunch {
	public static void main(String[] args) {
		Johan.lunch(args, initResult -> {
			if (initResult) {
			}
		});
	}
}

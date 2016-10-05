package com.palm.yh.client.web.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by kaishui on 16-10-4.
 */
public class Jdk8_collector_groupby_test {

    private static class User {
        private String name;
        private String password;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static void main(String[] args) {
        List<User> userList = constructUserList();

        //使用JDK8 按照名字分组
        Map<String, List<User>> userMaps = userList.stream().collect(Collectors.groupingBy(u -> u.getName()));
        //按照密码分组
        Map<String, List<User>> userPasswordMaps = userList.stream().collect(Collectors.groupingBy(u -> u.getPassword()));

        userMaps.forEach((key, groupUser) -> {
            System.out.println("---------------- group by user name = " + key + "----------");
            groupUser.forEach(u -> System.out.println(u.getPassword()));
        });

        userPasswordMaps.forEach((key, groupUser) -> {
            System.out.println("----------------  group by user password = " + key + "----------");
            groupUser.forEach(u -> System.out.println(u.getName()));
        });

        //上面的例子想想用JKD7写,表示博主很懒
        Map<String, List<User>> userJdk7Map = new HashMap<String, List<User>>();
        //按照名字分组
        for (User u : userList) {
            if (userJdk7Map.containsKey(u.getName())) {//如果map中已经存在则add 到groupList中
                List<User> groupTempUserList = userJdk7Map.get(u.getName());
                groupTempUserList.add(u);
            }else{//增加一个新的
                List<User> groupTempUserList = new ArrayList<User>();
                groupTempUserList.add(u);
                userJdk7Map.put(u.getName(), groupTempUserList);
            }
        }

        //按照密码分组 大写略 参考名字分组

        //jdk7 打印
        for (Map.Entry<String, List<User>> entry : userJdk7Map.entrySet()) {
            System.out.println("---------------- jdk7 group by user name = " + entry.getKey() + "----------");
            for (User user : entry.getValue()) {
                System.out.println( user.getPassword());
            }
        }

    }

    /**
     * 构造一个用户列表
     * @return
     */
    private static List<User> constructUserList() {
        List<User> userList = new ArrayList<User>();
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                User u = new User();
                u.setPassword("password" + j);
                u.setName("name" + i);
                userList.add(u);
            }
        }
        return userList;
    }
}

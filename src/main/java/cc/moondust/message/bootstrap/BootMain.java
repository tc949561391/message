package cc.moondust.message.bootstrap;

import cc.moondust.message.netty.MessageSocketServer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 程序的入口
 * Created by Tristan on 16/7/17.
 */
public class BootMain {
    private static ApplicationContext applicationContext;
    public static void main(String[] strs){
        applicationContext=new ClassPathXmlApplicationContext("spring/spring-context.xml");
        MessageSocketServer socketServer = applicationContext.getBean(MessageSocketServer.class);
        socketServer.run();
    }
}

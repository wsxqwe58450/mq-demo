package cn.itcast.mq.helloworld;

import com.rabbitmq.client.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


public class ConsumerTest {



    public static void main(String[] args) throws IOException, TimeoutException {
        // 1.建立连接
        ConnectionFactory factory = new ConnectionFactory();
        // 1.1.设置连接参数，分别是：主机名、端口号、vhost、用户名、密码
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        factory.setVirtualHost("/");    //vitural host：虚拟主机，是对queue、exchange等资源的逻辑分组
        factory.setUsername("root");
        factory.setPassword("root");
        // 1.2.建立连接
        Connection connection = factory.newConnection();

        // 2.创建通道Channel
        Channel channel = connection.createChannel();

        // 3.创建队列
        String queueName = "simple.queue";
        channel.queueDeclare(queueName, false, false, false, null);

        // 4.订阅消息
        channel.basicConsume(queueName, true, new DefaultConsumer(channel){
            //回调函数的机制（异步），只有队列有值，才会执行下面的代码。
            //所以执行顺序是：先执行后面的 System.out.println("等待接收消息。。。。");
            //后执行 System.out.println("接收到消息：【" + message + "】");
            //回调机制可以不阻塞后续的代码
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                // 5.处理消息
                String message = new String(body);
                System.out.println("接收到消息：【" + message + "】");
            }
        });
        System.out.println("等待接收消息。。。。");
    }
}

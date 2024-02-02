import java.io.*;
import java.net.Socket;
import java.net.SocketException;

//this is where the server is initialized
public class Main {


    public static void main(String[] args) throws IOException {

        var controller = new Controller(args[0]);
        Controller.logger.log(controller.startMessage);

        while (Controller.run) {
            //System.out.println(Controller.threadCount);
            Socket socket = null;
            try {
                socket = Controller.server.accept();
            } catch (SocketException e) {
                Controller.logger.log("the server socket changed");
            }


            if (Controller.threadCount > Controller.conf.getInt("maxThread")) {
                Controller.logger.log("The max thread count is reached");
                while (Controller.threadCount > Controller.conf.getInt("threadCount")) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        Controller.logger.log("Error while sleeping main thread");
                    }
                }
            }
            if (socket != null) {
                var worker = new Worker(socket);
                //worker.start(); //for some fuckin godly reason this just not working so please paint the ceiling with my brain
                new Thread(() -> worker.start()).start();//why???? I mean there is a thread in a thread!!!! my brain is just not braining at this point :c
            }
        }

        Controller.logger.log("http server stopped");
    }
}
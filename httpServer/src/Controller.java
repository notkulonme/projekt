import Logger.Logger;
import RSON.RSON;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Scanner;

public class Controller {

    static int threadCount = 0;
    //these variables are just read only

    public static ServerSocket server = null;
    private static String confPath;
    public static RSON conf = null;
    public static int PORT = -1;
    public static String WEBROOT = null;
    public static volatile boolean run = true;
    public static int maxThread = -1;
    public static Logger logger = null;
    public String startMessage = "The server is started";

    public Controller(String confPath) {
        Controller.confPath = confPath;
        load();
        try {
            server = new ServerSocket(PORT);
        } catch (IOException e) {
            logger.log("wrong server port");
            run = false;
        }
        Thread uc = new Thread(this::userController);
        uc.start();
    }

    private void load() {
        Controller.conf = new RSON(Controller.confPath);
        Controller.PORT = conf.getInt("port");
        Controller.WEBROOT = conf.getValue("webroot");
        Controller.maxThread = conf.getInt("maxThread");
        logger = new Logger(conf.getValue("loggerpath"));
    }


    private void userController() {

        Scanner scanner = new Scanner(System.in);
        while (run) {
            String command = scanner.nextLine();
            switch (command) {
                case "reload" -> {
                    logger.log("user command: reload");
                    load();
                }
                case "q", "stop" -> {
                    logger.log("user command: stop");
                    run = false;
                    try {
                        server.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case "thread count" -> {
                    logger.log("user command: thread count");
                    logger.log("Currently running threads: " + Controller.threadCount);
                }
                case "status" -> {
                    logger.log("user command: status");
                    logger.log("The server is running");
                }
                case "reload server" -> {
                    logger.log("user command: reload server");
                    load();
                    try {
                        server.close();
                        server = new ServerSocket(PORT);
                    } catch (IOException e) {
                        logger.log("wrong server port");
                        server = null;
                        run = false;
                    }
                }
                case "help" -> logger.log("All commands:\n\t\treload -> reloads the config file\n\t\treload server -> reloads the whole server with the server socket" +
                            "\n\t\tstatus -> prints the status\n\t\tthread count -> prints the running thread\n\t\tstop -> stops the server");

                default -> {
                    logger.log("invalid command type 'help' for help");
                }
            }
        }
    }

}

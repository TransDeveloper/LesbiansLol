package com.finnacorp.service;

public class StartService {
    private JettyServer jettyServer;
    private Logger logger;

    public static void main(String[] args) throws Exception{
        StartService servers = new StartService(884);

        Thread threadJetty = new Thread(servers.jettyServer);
        threadJetty.start();
    }

    public StartService(Integer port){
        jettyServer = new JettyServer(port);
        logger = new Logger("Main");
        logger.Log(String.format("Using port %s", port));
    }
}

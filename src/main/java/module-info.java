open module tv.memoryleakdeath.magentabreeze {
    requires java.desktop;
    requires java.sql;
    requires args4j;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires java.naming;
    requires org.eclipse.jetty.server;
    requires org.eclipse.jetty.ee10.webapp;
    requires org.eclipse.jetty.ee10.annotations;
    requires org.apache.commons.lang3;
    requires com.h2database;

    exports tv.memoryleakdeath.magentabreeze.app;
}
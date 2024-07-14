open module tv.memoryleakdeath.magentabreeze {
    requires java.desktop;
    requires args4j;
    requires org.slf4j;
    requires ch.qos.logback.classic;
    requires ch.qos.logback.core;
    requires java.naming;

    exports tv.memoryleakdeath.magentabreeze.app;
}
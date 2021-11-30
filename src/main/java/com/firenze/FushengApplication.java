package com.firenze;

import com.firenze.resolve.Resolver;
import java.io.File;
import java.util.List;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.startup.Tomcat;

public class FushengApplication {
    public static Tomcat tomcat;

    public static void run(Class<?> primaryClass, String[] args) {
        try {
            String servletName = "dispatcherServlet";
            ResourceLoader resourceLoader = new ResourceLoader();
            List<Resolver> resolvers = resourceLoader.load(primaryClass);
            DispatcherServlet dispatcherServlet = new DispatcherServlet(resolvers);
            tomcat = new Tomcat();
            tomcat.setPort(8080);
            tomcat.setBaseDir("fusheng.server");
            Context ctx = tomcat.addContext("/", new File(".").getAbsolutePath());
            Tomcat.addServlet(ctx, servletName, dispatcherServlet);
            ctx.addServletMappingDecoded("/*", servletName);
            tomcat.start();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            System.out.println("Start error, " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void showDown() {
        try {
            tomcat.stop();
            tomcat.destroy();
        } catch (LifecycleException e) {
            System.out.println("Stop error, " + e.getMessage());
            e.printStackTrace();
        }
    }
}

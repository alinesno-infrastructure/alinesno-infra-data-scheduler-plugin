package com.alinesno.infra.data.scheduler.tools;

import org.noear.solon.Solon;
import org.noear.solon.annotation.SolonMain;

@SolonMain
public class App {
    public static void main(String[] args) {
        Solon.start(App.class, args);
    }
}
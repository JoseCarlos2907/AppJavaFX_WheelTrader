#!/bin/bash
java -Dprism.order=sw -Dprism.forceGPU=true -Dsun.java2d.xrender=true --module-path /usr/lib/wheeltrader/javafx --add-modules javafx.controls,javafx.fxml,javafx.graphics -jar /usr/lib/wheeltrader/wheeltrader-1.0-jar-with-dependencies.jar

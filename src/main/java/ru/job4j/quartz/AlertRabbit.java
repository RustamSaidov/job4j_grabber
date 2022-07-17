package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {

    private final Map<String, String> values = new HashMap<String, String>();

    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            AlertRabbit alertRabbit = new AlertRabbit();
            alertRabbit.load();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(alertRabbit.values.get("rabbit.interval")))
                    .repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    public void load() {
        try (BufferedReader read = new BufferedReader(new FileReader("C:\\projects\\job4j_grabber\\src\\main\\resources\\rabbit.properties"))) {
            for (String line = read.readLine(); line != null; line = read.readLine()) {
                if (!line.isEmpty()
                        && line.length() - 1 == line.lastIndexOf("=")
                        && line.indexOf("=") == line.lastIndexOf("=")
                        || !line.isEmpty() && 0 == line.indexOf("=")
                        || line.equals("=")
                ) {
                    throw new IllegalArgumentException();
                }
                if (line.indexOf("#") != 0 && line.contains("=")) {
                    String key = line.substring(0, line.indexOf('='));
                    String val = line.substring(line.indexOf('=') + 1);
                    values.put(key, val);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}

package ru.job4j.quartz;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(readFile())
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

    private static int readFile() {
        int rsl = -1;
        try (BufferedReader in = new BufferedReader(new FileReader("./src/main/resources/rabbit.properties"))) {
            String text = in.readLine();
            String[] arText = text.split("=", 2);
            rsl = Integer.parseInt(arText[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (rsl <= 0) {
            throw new IllegalArgumentException("некорректно задан временной интревал");
        }
        return rsl;
    }

    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
        }
    }
}
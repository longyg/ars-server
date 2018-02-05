package com.longyg.frontend.model.ars.counter;

import java.util.ArrayList;
import java.util.List;

public class CounterMeas implements Comparable<CounterMeas> {
    private String name;

    private List<ArsCounter> counters = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArsCounter> getCounters() {
        return counters;
    }

    public void setCounters(List<ArsCounter> counters) {
        this.counters = counters;
    }

    public void addCounter(ArsCounter counter) {
        if (!counters.contains(counter)) {
            counters.add(counter);
        }
    }

    @Override
    public int compareTo(CounterMeas o) {
        return this.name.compareTo(o.getName());
    }
}

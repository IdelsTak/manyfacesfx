/*
 Copyright 2019.
 */
package com.github.idelstak.manyfacesfx.ui;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**

 @author Hiram K <hiram.kamau@outlook.com>
 */
public class Timezones {

    private boolean sortByZone = true;

    public Timezones() {
    }

    public Collection<Timezone> getTimezones(boolean sortbyZone) {
        this.sortByZone = sortbyZone;
        List<Timezone> result = getTimeOffsetZones()
                .entrySet()
                .stream()
                .map(entry -> new Timezone(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());

        return Collections.unmodifiableCollection(result);
    }

    private Map<String, String> getTimeOffsetZones() {
        LocalDateTime now = LocalDateTime.now();

        return ZoneId.getAvailableZoneIds()
                .stream()
                .map(ZoneId::of)
                .map(zoneId -> new SimpleEntry<>(zoneId.toString(), now.atZone(zoneId)
                .getOffset()
                .getId()
                .replaceAll("Z", "+00:00")))
                .sorted(sortByZone
                        ? Map.Entry.comparingByKey()
                        : Map.Entry.<String, String>comparingByValue().reversed())
                .collect(Collectors.toMap(SimpleEntry::getKey,
                        SimpleEntry::getValue,
                        (oldValue, newValue) -> oldValue,
                        LinkedHashMap::new));
    }
}

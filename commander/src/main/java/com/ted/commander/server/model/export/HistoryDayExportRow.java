package com.ted.commander.server.model.export;

import com.ted.commander.common.enums.DataExportFileType;
import com.ted.commander.common.enums.MTUType;
import com.ted.commander.common.model.VirtualECCMTU;
import com.ted.commander.common.model.history.HistoryDay;
import com.ted.commander.common.model.history.HistoryMTUDay;
import com.ted.commander.common.model.history.WeatherHistory;
import com.ted.commander.server.util.CalendarUtils;

import java.io.Serializable;
import java.util.TimeZone;

/**
 * Created by pete on 2/2/2015.
 */
public class HistoryDayExportRow extends HistoryExportRow implements Serializable {


    public HistoryDayExportRow(HistoryDay history, MTUType mtuType, String timeZone) {
        super(mtuType);
        timeZoneId = timeZone;

        date = CalendarUtils.fromCalendarKey(history.getCalendarKey(), timeZone).getTime();
        powerFactor = history.getPfTotal() / history.getPfSampleCount();
        powerFactor /= 100.0;

        peakDemandTime = CalendarUtils.fromCalendarKey(history.getDemandPeakCalendarKey(), timeZone).getTime();

        peakDemand = history.getDemandPeak();
        minVoltageTime = CalendarUtils.fromCalendarKey(history.getMinVoltageCalendarKey(), timeZone).getTime();
        minVoltage = history.getMinVoltage();
        peakVoltageTime = CalendarUtils.fromCalendarKey(history.getPeakVoltageCalendarKey(), timeZone).getTime();
        peakVoltage = history.getPeakVoltage();
        switch (mtuType) {
            case NET: {
                watts = history.getNet();
                cost = history.getNetCost();
                break;
            }
            case LOAD: {
                watts = history.getLoad();
                cost = history.getLoadCost();
                break;
            }
            case GENERATION: {
                watts = history.getGeneration();
                cost = history.getGenCost();
                break;
            }
        }
    }

    public HistoryDayExportRow(HistoryMTUDay history, VirtualECCMTU mtu, String timeZone) {
        name = mtu.getMtuDescription();
        timeZoneId = timeZone;
        date = CalendarUtils.fromCalendarKey(history.getCalendarKey(), timeZone).getTime();
        peakDemandTime = CalendarUtils.fromCalendarKey(history.getDemandPeakCalendarKey(), timeZone).getTime();
        peakDemand = history.getDemandPeak();
        peakVoltage = history.getPeakVoltage();
        peakVoltageTime = CalendarUtils.fromCalendarKey(history.getPeakVoltageCalendarKey(), timeZone).getTime();
        minVoltage = history.getMinVoltage();
        minVoltageTime = CalendarUtils.fromCalendarKey(history.getMinVoltageCalendarKey(), timeZone).getTime();
        powerFactor = history.getPfTotal() / history.getPfSampleCount();
        powerFactor /= 100.0;
        isSpyder = false;
        watts = history.getEnergy();
        cost = history.getCost();
    }

    public static String format(MTUType mtuType, DataExportFileType dataExportFileType, HistoryDay dto, WeatherHistory weatherHistory, TimeZone timeZone){
        HistoryDayExportRow row = new HistoryDayExportRow(dto, mtuType, timeZone.getID());
        if (weatherHistory != null) row.setWeather(weatherHistory);
        return row.format(dataExportFileType, timeZone, weatherHistory != null, false);
    }

    public static String format(DataExportFileType dataExportFileType, HistoryMTUDay dto, VirtualECCMTU mtu, WeatherHistory weatherHistory, TimeZone timeZone){
        HistoryDayExportRow row = new HistoryDayExportRow(dto, mtu, timeZone.getID());
        if (weatherHistory != null) row.setWeather(weatherHistory);
        return row.format(dataExportFileType, timeZone, weatherHistory != null, false);
    }
}

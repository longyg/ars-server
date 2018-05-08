package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.fm.Alarm;
import com.longyg.backend.adaptation.fm.FmAdaptation;
import com.longyg.backend.adaptation.main.AdaptationRepository;
import com.longyg.frontend.model.ars.ARS;
import com.longyg.frontend.model.ars.ArsConfig;
import com.longyg.frontend.model.ars.alarm.AlarmSpec;
import com.longyg.frontend.model.ars.alarm.ArsAlarm;
import com.longyg.frontend.service.ArsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Component
public class AlarmGenerator {
    private static final Logger LOG = Logger.getLogger(AlarmGenerator.class.getName());
    @Autowired
    private ArsService arsService;

    private ARS ars;

    private AdaptationRepository adaptationRepository;

    public String generateAndSave(ARS ars, AdaptationRepository adaptationRepository) {
        this.ars = ars;
        this.adaptationRepository = adaptationRepository;

        AlarmSpec spec = generateAndSave();

        return spec.getId();
    }

    private AlarmSpec generateAndSave() {
        AlarmSpec spec = new AlarmSpec();
        spec.setNeType(this.ars.getNeType());
        spec.setNeVersion(this.ars.getNeVersion());

        for (Map.Entry<String, List<FmAdaptation>> entry : adaptationRepository.getFmAdaptations().entrySet()) {
            String adaptationId = entry.getKey();

            createArsAlarms(adaptationId, entry.getValue(), spec);
        }

        compareAlarmWithLastVersion(spec);

        for (List<ArsAlarm> alarms : spec.getAlarmMap().values()) {
            Collections.sort(alarms);
        }

        spec = arsService.saveAlarm(spec);

        return spec;
    }

    private void compareAlarmWithLastVersion(AlarmSpec spec) {
        for (Map.Entry<String, List<ArsAlarm>> entry : spec.getAlarmMap().entrySet()) {
            String adaptationId = entry.getKey();

            for (ArsAlarm arsAlarm : entry.getValue()) {
                if (arsAlarm.isSupported()) {
                    Alarm alarmOfLastRel = getAlarmForLastVersion(arsAlarm.getSpecificProblem(), adaptationId);
                    if (null != alarmOfLastRel) {
                        checkAlarmProperties(arsAlarm, alarmOfLastRel);
                    }
                }
            }
        }
    }

    private void checkAlarmProperties(ArsAlarm arsAlarm, Alarm alarmOfLastRel) {
        arsAlarm.setLastAlarmText(alarmOfLastRel.getAlarmText());
        arsAlarm.setAlarmTextChanged(!arsAlarm.getAlarmText().equals(alarmOfLastRel.getAlarmText()));

        arsAlarm.setLastAlarmType(alarmOfLastRel.getAlarmType());
        arsAlarm.setAlarmTypeChanged(!arsAlarm.getAlarmType().equals(alarmOfLastRel.getAlarmType()));

        arsAlarm.setLastCancelling(alarmOfLastRel.getCancelling());
        arsAlarm.setCancellingChanged(!arsAlarm.getCancelling().equals(alarmOfLastRel.getCancelling()));

        arsAlarm.setLastInstructions(alarmOfLastRel.getInstructions());
        arsAlarm.setInstructionsChanged(!arsAlarm.getInstructions().equals(alarmOfLastRel.getInstructions()));

        arsAlarm.setLastMeaning(alarmOfLastRel.getMeaning());
        arsAlarm.setMeaningChanged(!arsAlarm.getMeaning().equals(alarmOfLastRel.getMeaning()));

        arsAlarm.setLastProbableCause(alarmOfLastRel.getProbableCause());
        arsAlarm.setProbableCauseChanged(!arsAlarm.getProbableCause().equals(alarmOfLastRel.getProbableCause()));

        arsAlarm.setLastPerceivedSeverityInfo(alarmOfLastRel.getPerceivedSeverityInfo());
        arsAlarm.setSeverityChanged(!arsAlarm.getPerceivedSeverityInfo().equals(alarmOfLastRel.getPerceivedSeverityInfo()));
    }

    private Alarm getAlarmForLastVersion(String alarmNumber, String adaptationId) {
        if (adaptationRepository.getFmAdaptations().containsKey(adaptationId)) {
            List<FmAdaptation> fmAdaptations = adaptationRepository.getFmAdaptations().get(adaptationId);
            for (FmAdaptation fmAdaptation : fmAdaptations) {
                if (fmAdaptation.getAdapRelease().equals(this.ars.getLastNeVersion())) {
                    for (Alarm alarm : fmAdaptation.getAlarms()) {
                        if (alarm.getAlarmNumber().equals(alarmNumber)) {
                            return alarm;
                        }
                    }
                }
            }
        }
        return null;
    }

    private void createArsAlarms(String adaptationId, List<FmAdaptation> fmAdaptations, AlarmSpec spec) {
        createArsAlarmForCurrentVersion(adaptationId, fmAdaptations, spec);

        for (FmAdaptation fmAdaptation : fmAdaptations) {
            if (!fmAdaptation.getAdapRelease().equals(this.ars.getNeVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    ArsAlarm arsAlarm = findOrCreateArsAlarm(alarm, spec, adaptationId);

                    arsAlarm.addSupportedPreviousVersion(fmAdaptation.getAdapRelease());

                    spec.addAlarm(adaptationId, arsAlarm);
                }
            }
        }
    }

    private void createArsAlarmForCurrentVersion(String adaptationId, List<FmAdaptation> fmAdaptations, AlarmSpec spec) {
        for (FmAdaptation fmAdaptation : fmAdaptations) {
            if (fmAdaptation.getAdapRelease().equals(this.ars.getNeVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    ArsAlarm arsAlarm = findOrCreateArsAlarm(alarm, spec, adaptationId);

                    arsAlarm.setSupported(true);

                    spec.addAlarm(adaptationId, arsAlarm);
                }
            }
        }
    }

    private ArsAlarm findOrCreateArsAlarm(Alarm alarm, AlarmSpec spec, String adaptationId) {
        ArsAlarm arsAlarm = findArsAlarm(alarm, spec, adaptationId);
        if (null == arsAlarm) {
            arsAlarm = createArsAlarm(alarm);
        }
        return arsAlarm;
    }

    private ArsAlarm findArsAlarm(Alarm alarm, AlarmSpec spec, String adaptationId) {
        for (ArsAlarm arsAlarm : spec.getAlarmList(adaptationId)) {
            if (arsAlarm.getSpecificProblem().equals(alarm.getAlarmNumber())) {
                return arsAlarm;
            }
        }
        return null;
    }

    private ArsAlarm createArsAlarm(Alarm alarm) {
        ArsAlarm arsAlarm = new ArsAlarm();
        arsAlarm.setSpecificProblem(alarm.getAlarmNumber());
        arsAlarm.setAlarmText(alarm.getAlarmText());
        arsAlarm.setAlarmType(alarm.getAlarmType());
        arsAlarm.setCancelling(alarm.getCancelling());
        arsAlarm.setInstructions(alarm.getInstructions());
        arsAlarm.setMeaning(alarm.getMeaning());
        arsAlarm.setPerceivedSeverityInfo(alarm.getPerceivedSeverityInfo());
        arsAlarm.setProbableCause(alarm.getProbableCause());
        return arsAlarm;
    }
}
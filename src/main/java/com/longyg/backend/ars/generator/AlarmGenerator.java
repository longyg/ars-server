package com.longyg.backend.ars.generator;

import com.longyg.backend.adaptation.fm.Alarm;
import com.longyg.backend.adaptation.fm.FmAdaptation;
import com.longyg.backend.adaptation.main.AdaptationRepository;
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

    private ArsConfig config;

    private AdaptationRepository adaptationRepository;

    public String generateAndSave(ArsConfig config, AdaptationRepository adaptationRepository) {
        this.config = config;
        this.adaptationRepository = adaptationRepository;

        AlarmSpec spec = generateAndSave();

        return spec.getId();
    }

    private AlarmSpec generateAndSave() {
        AlarmSpec spec = new AlarmSpec();
        spec.setNeType(config.getNeType());
        spec.setNeVersion(config.getNeVersion());

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
        arsAlarm.setAlarmTextChanged(!arsAlarm.getAlarmText().equals(alarmOfLastRel.getAlarmText()));
        arsAlarm.setAlarmTypeChanged(!arsAlarm.getAlarmType().equals(alarmOfLastRel.getAlarmType()));
        arsAlarm.setCancellingChanged(!arsAlarm.getCancelling().equals(alarmOfLastRel.getCancelling()));
        arsAlarm.setInstructionsChanged(!arsAlarm.getInstructions().equals(alarmOfLastRel.getInstructions()));
        arsAlarm.setMeaningChanged(!arsAlarm.getMeaning().equals(alarmOfLastRel.getMeaning()));
        arsAlarm.setProbableCauseChanged(!arsAlarm.getProbableCause().equals(alarmOfLastRel.getProbableCause()));
        arsAlarm.setSeverityChanged(!arsAlarm.getPerceivedSeverityInfo().equals(alarmOfLastRel.getPerceivedSeverityInfo()));
    }

    private Alarm getAlarmForLastVersion(String alarmNumber, String adaptationId) {
        if (adaptationRepository.getFmAdaptations().containsKey(adaptationId)) {
            List<FmAdaptation> fmAdaptations = adaptationRepository.getFmAdaptations().get(adaptationId);
            for (FmAdaptation fmAdaptation : fmAdaptations) {
                if (fmAdaptation.getAdapRelease().equals(config.getLastVersion())) {
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
            if (!fmAdaptation.getAdapRelease().equals(config.getNeVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    ArsAlarm arsAlarm = createArsAlarm(alarm);

                    arsAlarm.addSupportedPreviousVersion(fmAdaptation.getAdapRelease());

                    spec.addAlarm(adaptationId, arsAlarm);
                }
            }
        }
    }

    private void createArsAlarmForCurrentVersion(String adaptationId, List<FmAdaptation> fmAdaptations, AlarmSpec spec) {
        for (FmAdaptation fmAdaptation : fmAdaptations) {
            if (fmAdaptation.getAdapRelease().equals(config.getNeVersion())) {
                for (Alarm alarm : fmAdaptation.getAlarms()) {
                    ArsAlarm arsAlarm = createArsAlarm(alarm);

                    arsAlarm.setSupported(true);

                    spec.addAlarm(adaptationId, arsAlarm);
                }
            }
        }
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
package ru.netology.patient.service.medical;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {
    private PatientInfoRepository patientInfoRepository;
    private String id1;
    private String message;
    private SendAlertService alertService;
    private MedicalService medicalService;

    @BeforeEach
    public void setup() {
        id1 = "123";
        patientInfoRepository = Mockito.mock(PatientInfoRepository.class);

        Mockito.when(patientInfoRepository.getById(id1))
                .thenReturn(new PatientInfo(id1, "Иван", "Петров", LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80)))
                );

        message = String.format("Warning, patient with id: %s, need help", id1);
        alertService = Mockito.mock(SendAlertServiceImpl.class);

        medicalService = new MedicalServiceImpl(patientInfoRepository,alertService);

    }

    @Test
    public void  checkBloodPressureTest(){
        medicalService.checkBloodPressure(id1,new BloodPressure(120, 80));

        Mockito.verify(alertService, Mockito.times(0)).send(message);
    }

    @Test
    public void  checkBloodPressureTestAlert(){
        medicalService.checkBloodPressure(id1,new BloodPressure(130, 70));

        Mockito.verify(alertService, Mockito.times(1)).send(Mockito.eq(message));
    }

    @Test
    public void  checkTemperatureTest(){

        medicalService.checkTemperature(id1,new BigDecimal("36.65"));
        Mockito.verify(alertService, Mockito.times(0)).send(message);

    }
    @Test
    public void  checkTemperatureTestAlert(){

        medicalService.checkTemperature(id1,new BigDecimal("36.65"));
        Mockito.verify(alertService, Mockito.times(0)).send(Mockito.eq(message));

    }


}

//package com.swingit.crm.server.mechanism;
//
//import com.swingit.crm.server.model.entity.impl.*;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import swingit.server.crm.data.api.v2.dto.*;
//
//@ContextConfiguration(classes = {SwingItModelMapperTest.SpringTestConfig.class})
//@RunWith(SpringJUnit4ClassRunner.class)
//public class SwingItModelMapperTest
//{
//    @Autowired
//    private SwingItModelMapper modeler;
//
//    @Configuration
//    @ComponentScan(basePackageClasses = {SwingItModelMapperTest.class, SwingItModelMapper.class})
//    public static class SpringTestConfig {
//    }
//
//
//    @Test
//    public void test()
//    {
//        UserDto user = modeler.toDto(new User());
//        ActivityDto activity = modeler.toDto(new Activity());
//        ActivityOccurrenceDto activityOcc = modeler.toDto(new ActivityOccurrence());
//        CycleDto cycle = modeler.toDto(new Cycle());
//        DancerDto dancer = modeler.toDto(new Dancer());
//        FulfilledRequirementDto fulfu = modeler.toDto(new FulfilledRequirement());
//        ParticipationDto userDto = modeler.toDto(new Participation());
//        PaymentDto payment = modeler.toDto(new Payment());
//        PrivateDto privat = modeler.toDto(new Private());
//        SessionDto session = modeler.toDto(new Session());
//        RegistrationDto reg = modeler.toDto(new Registration());
//        TransactionDto transaction = modeler.toDto(new Transaction());
//    }
//}
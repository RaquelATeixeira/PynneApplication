package com.pyyne.challenge.bank.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
@AnalyzeClasses(packages = "com.pyyne.challenge.bank")
public class CoreArchitectureTest {

    // Core cannot depend on vendors
    @ArchTest
    static final ArchRule core_must_not_depend_on_vendors =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.domain..",
                                                  "com.pyyne.challenge.bank.ports..",
                                                  "com.pyyne.challenge.bank.application..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.bank1..", "com.bank2..");

    // Core cannot depend of Web (Spring MVC) or adapters
    @ArchTest
    static final ArchRule core_must_not_depend_on_web_or_adapters =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.domain..",
                                                  "com.pyyne.challenge.bank.ports..",
                                                  "com.pyyne.challenge.bank.application..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.pyyne.challenge.bank.web..",
                                           "com.pyyne.challenge.bank.adapters..");

    // Domain cannot depend of Spring
    @ArchTest
    static final ArchRule domain_should_be_framework_free =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.domain..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("org.springframework..");

    // Ports cannot depend of Spring
    @ArchTest
    static final ArchRule ports_should_be_framework_free =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.ports..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("org.springframework..");

    // Application can use Spring (@Service),but not Web MVC
    @ArchTest
    static final ArchRule application_should_not_depend_on_webmvc =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.application..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("org.springframework.web..");
}

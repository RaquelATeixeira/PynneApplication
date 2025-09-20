package com.pyyne.challenge.bank.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.pyyne.challenge.bank.web")
public class WebArchitectureTest {

    // Web cannot depend on vendors directly
    @ArchTest
    static final ArchRule web_must_not_depend_on_vendors =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.web..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.bank1..", "com.bank2..");

    // Web cannot depend on adapters (only of application/service)
    @ArchTest
    static final ArchRule web_must_not_depend_on_adapters =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.web..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.pyyne.challenge.bank.adapters..");
}

package com.pyyne.challenge.bank.adapters.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(packages = "com.pyyne.challenge.bank")
public class AdaptersArchitectureTest {

    // SOnly adapters classes can know vendors
    @ArchTest
    static final ArchRule only_adapters_may_depend_on_vendors =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank..")
                       .and().resideOutsideOfPackage("com.pyyne.challenge.bank.adapters..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.bank1..", "com.bank2..");

    // Adapters cannot depend on web module
    @ArchTest
    static final ArchRule adapters_must_not_depend_on_web =
            noClasses().that().resideInAnyPackage("com.pyyne.challenge.bank.adapters..")
                       .should().dependOnClassesThat()
                       .resideInAnyPackage("com.pyyne.challenge.bank.web..");
}
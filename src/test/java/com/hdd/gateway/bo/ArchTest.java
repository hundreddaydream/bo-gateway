package com.hdd.gateway.bo;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.hdd.gateway.bo");

        noClasses()
            .that()
            .resideInAnyPackage("com.hdd.gateway.bo.service..")
            .or()
            .resideInAnyPackage("com.hdd.gateway.bo.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.hdd.gateway.bo.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}

package org.springframework.samples.petclinic.customers.model;

import java.util.Date;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class PetTest {

    @Test
    void shouldSetAndGetAllFields() {
        Pet pet = new Pet();
        Date birthDate = new Date();
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");

        pet.setId(1);
        pet.setName("Leo");
        pet.setBirthDate(birthDate);
        pet.setType(type);
        pet.setOwner(owner);

        assertThat(pet.getId()).isEqualTo(1);
        assertThat(pet.getName()).isEqualTo("Leo");
        assertThat(pet.getBirthDate()).isEqualTo(birthDate);
        assertThat(pet.getType()).isEqualTo(type);
        assertThat(pet.getOwner()).isEqualTo(owner);
    }

    @Test
    void shouldBeEqualWhenAllFieldsMatch() {
        Date birthDate = new Date();
        PetType type = new PetType();
        type.setId(1);

        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("Leo");
        pet1.setBirthDate(birthDate);
        pet1.setType(type);

        Pet pet2 = new Pet();
        pet2.setId(1);
        pet2.setName("Leo");
        pet2.setBirthDate(birthDate);
        pet2.setType(type);

        assertThat(pet1).isEqualTo(pet2);
        assertThat(pet1.hashCode()).isEqualTo(pet2.hashCode());
    }

    @Test
    void shouldNotBeEqualWhenIdsDiffer() {
        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("Leo");

        Pet pet2 = new Pet();
        pet2.setId(2);
        pet2.setName("Leo");

        assertThat(pet1).isNotEqualTo(pet2);
    }

    @Test
    void shouldNotBeEqualWhenNamesDiffer() {
        Pet pet1 = new Pet();
        pet1.setId(1);
        pet1.setName("Leo");

        Pet pet2 = new Pet();
        pet2.setId(1);
        pet2.setName("Basil");

        assertThat(pet1).isNotEqualTo(pet2);
    }

    @Test
    void shouldNotBeEqualToNull() {
        Pet pet = new Pet();
        pet.setId(1);

        assertThat(pet).isNotEqualTo(null);
    }

    @Test
    void shouldNotBeEqualToDifferentType() {
        Pet pet = new Pet();
        pet.setId(1);

        assertThat(pet).isNotEqualTo("not a pet");
    }

    @Test
    void shouldHaveConsistentHashCode() {
        Pet pet = new Pet();
        pet.setId(1);
        pet.setName("Leo");

        int hashCode1 = pet.hashCode();
        int hashCode2 = pet.hashCode();

        assertThat(hashCode1).isEqualTo(hashCode2);
    }
}

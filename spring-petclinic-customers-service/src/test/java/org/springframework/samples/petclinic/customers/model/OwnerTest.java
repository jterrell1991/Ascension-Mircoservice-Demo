package org.springframework.samples.petclinic.customers.model;

import java.util.List;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OwnerTest {

    @Test
    void shouldAddPetAndSetBidirectionalRelationship() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Buddy");

        owner.addPet(pet);

        assertThat(owner.getPets()).hasSize(1);
        assertThat(owner.getPets().get(0).getName()).isEqualTo("Buddy");
        assertThat(pet.getOwner()).isSameAs(owner);
    }

    @Test
    void shouldReturnPetsSortedByName() {
        Owner owner = new Owner();

        Pet charlie = new Pet();
        charlie.setName("Charlie");

        Pet alpha = new Pet();
        alpha.setName("Alpha");

        Pet bravo = new Pet();
        bravo.setName("Bravo");

        owner.addPet(charlie);
        owner.addPet(alpha);
        owner.addPet(bravo);

        List<Pet> pets = owner.getPets();
        assertThat(pets).extracting(Pet::getName)
            .containsExactly("Alpha", "Bravo", "Charlie");
    }

    @Test
    void shouldReturnUnmodifiablePetsList() {
        Owner owner = new Owner();
        Pet pet = new Pet();
        pet.setName("Buddy");
        owner.addPet(pet);

        List<Pet> pets = owner.getPets();

        assertThatThrownBy(() -> pets.add(new Pet()))
            .isInstanceOf(UnsupportedOperationException.class);
    }

    @Test
    void shouldReturnEmptyListWhenNoPets() {
        Owner owner = new Owner();

        List<Pet> pets = owner.getPets();

        assertThat(pets).isEmpty();
    }

    @Test
    void shouldProduceToStringWithAllFields() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Franklin");
        owner.setAddress("110 W. Liberty St.");
        owner.setCity("Madison");
        owner.setTelephone("6085551023");

        String result = owner.toString();

        assertThat(result).contains("firstName", "George");
        assertThat(result).contains("lastName", "Franklin");
        assertThat(result).contains("address", "110 W. Liberty St.");
        assertThat(result).contains("city", "Madison");
        assertThat(result).contains("telephone", "6085551023");
    }
}

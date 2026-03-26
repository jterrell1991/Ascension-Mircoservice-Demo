package org.springframework.samples.petclinic.customers.web;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.samples.petclinic.customers.model.Owner;
import org.springframework.samples.petclinic.customers.model.OwnerRepository;
import org.springframework.samples.petclinic.customers.model.Pet;
import org.springframework.samples.petclinic.customers.model.PetRepository;
import org.springframework.samples.petclinic.customers.model.PetType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Maciej Szarlinski
 */
@WebMvcTest(PetResource.class)
@ActiveProfiles("test")
class PetResourceTest {

    @Autowired
    MockMvc mvc;

    @MockitoBean
    PetRepository petRepository;

    @MockitoBean
    OwnerRepository ownerRepository;

    @Test
    void shouldGetAPetInJSonFormat() throws Exception {

        Pet pet = setupPet();

        given(petRepository.findById(2)).willReturn(Optional.of(pet));


        mvc.perform(get("/owners/2/pets/2").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(jsonPath("$.id").value(2))
            .andExpect(jsonPath("$.name").value("Basil"))
            .andExpect(jsonPath("$.type.id").value(6));
    }

    @Test
    void shouldGetPetTypes() throws Exception {
        PetType cat = new PetType();
        cat.setId(1);
        cat.setName("cat");

        PetType dog = new PetType();
        dog.setId(2);
        dog.setName("dog");

        given(petRepository.findPetTypes()).willReturn(List.of(cat, dog));

        mvc.perform(get("/petTypes").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("cat"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("dog"));
    }

    @Test
    void shouldCreatePet() throws Exception {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Bush");

        Pet savedPet = new Pet();
        savedPet.setId(1);
        savedPet.setName("New Pet");
        savedPet.setBirthDate(new Date());
        PetType type = new PetType();
        type.setId(1);
        type.setName("cat");
        savedPet.setType(type);
        owner.addPet(savedPet);

        given(ownerRepository.findById(1)).willReturn(Optional.of(owner));
        given(petRepository.findPetTypeById(1)).willReturn(Optional.of(type));
        given(petRepository.save(any(Pet.class))).willReturn(savedPet);

        mvc.perform(post("/owners/1/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "id": 0,
                        "name": "New Pet",
                        "birthDate": "2020-01-01",
                        "typeId": 1
                    }
                    """))
            .andExpect(status().isCreated());
    }

    @Test
    void shouldReturn404WhenCreatingPetForNonExistentOwner() throws Exception {
        given(ownerRepository.findById(999)).willReturn(Optional.empty());

        mvc.perform(post("/owners/999/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "id": 0,
                        "name": "New Pet",
                        "birthDate": "2020-01-01",
                        "typeId": 1
                    }
                    """))
            .andExpect(status().isNotFound());
    }

    @Test
    void shouldUpdatePet() throws Exception {
        Pet existingPet = setupPet();

        given(petRepository.findById(2)).willReturn(Optional.of(existingPet));
        given(petRepository.findPetTypeById(6)).willReturn(Optional.of(existingPet.getType()));
        given(petRepository.save(any(Pet.class))).willReturn(existingPet);

        mvc.perform(put("/owners/2/pets/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                    {
                        "id": 2,
                        "name": "Updated Basil",
                        "birthDate": "2012-08-06",
                        "typeId": 6
                    }
                    """))
            .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn404WhenGettingNonExistentPet() throws Exception {
        given(petRepository.findById(999)).willReturn(Optional.empty());

        mvc.perform(get("/owners/1/pets/999").accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    private Pet setupPet() {
        Owner owner = new Owner();
        owner.setFirstName("George");
        owner.setLastName("Bush");

        Pet pet = new Pet();

        pet.setName("Basil");
        pet.setId(2);

        PetType petType = new PetType();
        petType.setId(6);
        pet.setType(petType);

        owner.addPet(pet);
        return pet;
    }
}

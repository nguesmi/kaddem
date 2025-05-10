package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Departement;
import tn.esprit.spring.kaddem.repositories.DepartementRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DepartementServiceImplTest {

    @Mock
    private DepartementRepository departementRepository;

    @InjectMocks
    private DepartementServiceImpl departementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllDepartements_shouldReturnList() {
        List<Departement> list = Arrays.asList(new Departement(), new Departement());
        when(departementRepository.findAll()).thenReturn(list);

        List<Departement> result = departementService.retrieveAllDepartements();

        assertEquals(2, result.size());
        verify(departementRepository).findAll();
    }

    @Test
    void addDepartement_shouldReturnSavedDepartement() {
        Departement d = new Departement();
        when(departementRepository.save(d)).thenReturn(d);

        Departement result = departementService.addDepartement(d);

        assertEquals(d, result);
        verify(departementRepository).save(d);
    }

    @Test
    void updateDepartement_shouldReturnUpdatedDepartement() {
        Departement d = new Departement();
        when(departementRepository.save(d)).thenReturn(d);

        Departement result = departementService.updateDepartement(d);

        assertEquals(d, result);
        verify(departementRepository).save(d);
    }

    @Test
    void retrieveDepartement_shouldReturnDepartementById() {
        Departement d = new Departement();
        when(departementRepository.findById(1)).thenReturn(Optional.of(d));

        Departement result = departementService.retrieveDepartement(1);

        assertEquals(d, result);
        verify(departementRepository).findById(1);
    }

    @Test
    void deleteDepartement_shouldRemoveDepartement() {
        Departement d = new Departement();
        when(departementRepository.findById(1)).thenReturn(Optional.of(d));

        departementService.deleteDepartement(1);

        verify(departementRepository).delete(d);
    }
}

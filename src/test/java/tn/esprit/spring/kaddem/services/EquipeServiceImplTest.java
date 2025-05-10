package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Equipe;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Niveau;
import tn.esprit.spring.kaddem.repositories.EquipeRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EquipeServiceImplTest {

    @Mock
    private EquipeRepository equipeRepository;

    @InjectMocks
    private EquipeServiceImpl equipeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllEquipes_shouldReturnList() {
        List<Equipe> equipes = Arrays.asList(new Equipe(), new Equipe());
        when(equipeRepository.findAll()).thenReturn(equipes);

        List<Equipe> result = equipeService.retrieveAllEquipes();

        assertEquals(2, result.size());
        verify(equipeRepository).findAll();
    }

    @Test
    void addEquipe_shouldReturnSavedEquipe() {
        Equipe e = new Equipe();
        when(equipeRepository.save(e)).thenReturn(e);

        Equipe result = equipeService.addEquipe(e);

        assertEquals(e, result);
        verify(equipeRepository).save(e);
    }

    @Test
    void deleteEquipe_shouldCallDelete() {
        Equipe e = new Equipe();
        when(equipeRepository.findById(1)).thenReturn(Optional.of(e));

        equipeService.deleteEquipe(1);

        verify(equipeRepository).delete(e);
    }

    @Test
    void retrieveEquipe_shouldReturnEquipeById() {
        Equipe e = new Equipe();
        when(equipeRepository.findById(1)).thenReturn(Optional.of(e));

        Equipe result = equipeService.retrieveEquipe(1);

        assertEquals(e, result);
        verify(equipeRepository).findById(1);
    }

    @Test
    void updateEquipe_shouldReturnUpdatedEquipe() {
        Equipe e = new Equipe();
        when(equipeRepository.save(e)).thenReturn(e);

        Equipe result = equipeService.updateEquipe(e);

        assertEquals(e, result);
        verify(equipeRepository).save(e);
    }

    @Test
    void evoluerEquipes_shouldUpgradeEquipeLevelWhenConditionsMet() {
        // Mock a contract more than 1 year old and not archived
        Contrat contrat = new Contrat();
        contrat.setDateFinContrat(new Date(System.currentTimeMillis() - (2L * 365 * 24 * 60 * 60 * 1000))); // 2 years ago
        contrat.setArchive(false);

        // Mock etudiant with one active contract
        Etudiant etudiant = new Etudiant();
        etudiant.setContrats(Set.of(contrat));

        // Create 3 etudiants to satisfy the >=3 condition
        List<Etudiant> etudiants = Arrays.asList(etudiant, etudiant, etudiant);

        // Equipe with JUNIOR level
        Equipe equipe = new Equipe();
        equipe.setNiveau(Niveau.JUNIOR);
        equipe.setEtudiants((Set<Etudiant>) etudiants);

        when(equipeRepository.findAll()).thenReturn(List.of(equipe));
        when(equipeRepository.save(any())).thenReturn(equipe);

        equipeService.evoluerEquipes();

        assertEquals(Niveau.SENIOR, equipe.getNiveau());
        verify(equipeRepository).save(equipe);
    }
}

package tn.esprit.spring.kaddem.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import tn.esprit.spring.kaddem.entities.Contrat;
import tn.esprit.spring.kaddem.entities.Etudiant;
import tn.esprit.spring.kaddem.entities.Specialite;
import tn.esprit.spring.kaddem.repositories.ContratRepository;
import tn.esprit.spring.kaddem.repositories.EtudiantRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContratServiceImplTest {

    @Mock
    private ContratRepository contratRepository;

    @Mock
    private EtudiantRepository etudiantRepository;

    @InjectMocks
    private ContratServiceImpl contratService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void retrieveAllContrats_shouldReturnList() {
        List<Contrat> list = Arrays.asList(new Contrat(), new Contrat());
        when(contratRepository.findAll()).thenReturn(list);
        List<Contrat> result = contratService.retrieveAllContrats();
        assertEquals(2, result.size());
    }

    @Test
    void updateContrat_shouldUpdateAndReturnContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat result = contratService.updateContrat(contrat);
        assertEquals(contrat, result);
    }

    @Test
    void addContrat_shouldReturnSavedContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.save(contrat)).thenReturn(contrat);
        Contrat result = contratService.addContrat(contrat);
        assertEquals(contrat, result);
    }

    @Test
    void retrieveContrat_shouldReturnContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        Contrat result = contratService.retrieveContrat(1);
        assertEquals(contrat, result);
    }

    @Test
    void removeContrat_shouldDeleteContrat() {
        Contrat contrat = new Contrat();
        when(contratRepository.findById(1)).thenReturn(Optional.of(contrat));
        contratService.removeContrat(1);
        verify(contratRepository).delete(contrat);
    }

    @Test
    void affectContratToEtudiant_shouldAffectAndReturnContrat() {
        Etudiant etudiant = new Etudiant();
        Contrat contrat = new Contrat();
        contrat.setIdContrat(1);
        contrat.setArchive(false);
        Set<Contrat> contrats = new HashSet<>();
        etudiant.setContrats(contrats);

        when(etudiantRepository.findByNomEAndPrenomE("nom", "prenom")).thenReturn(etudiant);
        when(contratRepository.findByIdContrat(1)).thenReturn(contrat);
        when(contratRepository.save(contrat)).thenReturn(contrat);

        Contrat result = contratService.affectContratToEtudiant(1, "nom", "prenom");
        assertEquals(etudiant, result.getEtudiant());
    }

    @Test
    void nbContratsValides_shouldReturnCount() {
        Date start = new Date();
        Date end = new Date();
        when(contratRepository.getnbContratsValides(start, end)).thenReturn(5);
        int count = contratService.nbContratsValides(start, end);
        assertEquals(5, count);
    }

    @Test
    void retrieveAndUpdateStatusContrat_shouldArchiveIfNeeded() {
        Date now = new Date();
        Date fifteenDaysAgo = new Date(now.getTime() - 15L * 24 * 60 * 60 * 1000);
        Date today = now;

        Contrat contrat15 = new Contrat();
        contrat15.setArchive(false);
        contrat15.setDateFinContrat(fifteenDaysAgo);

        Contrat contratToday = new Contrat();
        contratToday.setArchive(false);
        contratToday.setDateFinContrat(today);

        when(contratRepository.findAll()).thenReturn(Arrays.asList(contrat15, contratToday));

        contratService.retrieveAndUpdateStatusContrat();

        verify(contratRepository).save(contratToday);
    }

    @Test
    void getChiffreAffaireEntreDeuxDates_shouldCalculateCorrectly() {
        Date start = new Date(0); // epoch
        Date end = new Date(30L * 24 * 60 * 60 * 1000); // approx 1 month

        Contrat c1 = new Contrat();
        c1.setSpecialite(Specialite.IA);
        Contrat c2 = new Contrat();
        c2.setSpecialite(Specialite.CLOUD);
        Contrat c3 = new Contrat();
        c3.setSpecialite(Specialite.RESEAUX);
        Contrat c4 = new Contrat();
        c4.setSpecialite(Specialite.SECURITE);

        when(contratRepository.findAll()).thenReturn(Arrays.asList(c1, c2, c3, c4));

        float result = contratService.getChiffreAffaireEntreDeuxDates(start, end);
        assertEquals(300 + 400 + 350 + 450, result); // 1500.0
    }
}

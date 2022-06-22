package ua.epan.elearn.selection.committee.spring.model.service;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ua.epan.elearn.selection.committee.spring.model.dto.RecruitmentDto;
import ua.epan.elearn.selection.committee.spring.model.entity.Faculty;
import ua.epan.elearn.selection.committee.spring.model.entity.Recruitment;
import ua.epan.elearn.selection.committee.spring.model.exception.RecruitmentNameIsReservedException;
import ua.epan.elearn.selection.committee.spring.model.repository.FacultyRepository;
import ua.epan.elearn.selection.committee.spring.model.repository.RecruitmentRepository;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Log4j2
@Service
public class RecruitmentService {
    private ApplicationService applicationService;

    private RecruitmentRepository recruitmentRepository;
    private FacultyRepository facultyRepository;


    public List<Recruitment> getAllNowOpenedRecruitmentsByFacultyId(Faculty facultyId) {
        return recruitmentRepository.findOpenedRecruitmentByFacultyId(facultyId);
    }

    public Long getRemainingBudgetSpotsInThisYearByFacultyId(Faculty facultyId) {
        List<Recruitment> recruitmentList = recruitmentRepository.findAllRecruitmentWhereCurrentYearByFacultyId(facultyId);
        Long usedBudgetSpots = recruitmentList.stream().mapToLong(Recruitment::getBudgetCapacity).sum();
        Faculty faculty = facultyRepository.findById(facultyId.getId()).get();
        return faculty.getBudgetCapacity() - usedBudgetSpots;
    }

    public Long getRemainingGeneralSpotsInThisYearByFacultyId(Faculty facultyId) {
        List<Recruitment> recruitmentList = recruitmentRepository.findAllRecruitmentWhereCurrentYearByFacultyId(facultyId);
        Long usedBudgetSpots = recruitmentList.stream().mapToLong(Recruitment::getGeneralCapacity).sum();
        Faculty faculty = facultyRepository.findById(facultyId.getId()).get();
        return faculty.getGeneralCapacity() - usedBudgetSpots;
    }

    public Page<Recruitment> getRecruitmentPagination(Pageable pageable) {
        return recruitmentRepository.findAll(pageable);
    }

    public Page<Recruitment> getRecruitmentPaginationWithFilter(String[] filters, Pageable pageable) {
        Integer[] array = parseFilter(filters);
        return recruitmentRepository.findAllWithFilterAndPagination(array[0], array[1], array[2], pageable);
    }


    public Recruitment getRecruitmentById(Long recruitmentId) {
        return recruitmentRepository.getReferenceById(recruitmentId);
    }

    public Recruitment getRecruitmentByApplicationId(Long applicationId) {
        return recruitmentRepository.findByApplicationId(applicationId);
    }

    public void addNewRecruitment(RecruitmentDto recruitmentDto) throws RecruitmentNameIsReservedException {
        Recruitment recruitment = new Recruitment(recruitmentDto);
        checkRecruitmentNameIsUnique(recruitment);
        log.info("New recruitment {} has been created", recruitment);
        recruitmentRepository.save(recruitment);
    }

    public void closeRecruitment(Recruitment recruitment) {
        recruitment.setEndDate(LocalDate.now());
        recruitment.setClosed(true);
        recruitmentRepository.save(recruitment);
        log.info("Recruitment ({}) was closed", recruitment);
    }

    public void scheduleCloseRecruitmentAndFinalizeApplications() {

        List<Recruitment> recruitmentList = recruitmentRepository.findAllOverdueOpenedRecruitment();
        for (Recruitment recruitment : recruitmentList) {

            log.info("Scheduled found an expired recruitment {} ", recruitment);
            recruitment.setClosed(true);
            recruitmentRepository.save(recruitment);
            applicationService.finalizeApplications(recruitment);

        }
    }


    private Integer[] parseFilter(String[] arr) {
        Integer[] array = {2, 2, 2};

        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals("previous")) {
                array[2] = 1;
            } else if (arr[i].equals("current")) {
                array[1] = 1;
            } else if (arr[i].equals("future")) {
                array[0] = 1;
            }

        }
        return array;
    }

    private void checkRecruitmentNameIsUnique(Recruitment recruitment) throws RecruitmentNameIsReservedException {
        if (recruitmentRepository.existsByNameAndFacultyId(recruitment.getName(), recruitment.getFacultyId())) {
            log.warn("Recruitment name '{}' is reserved", recruitment.getName());
            throw new RecruitmentNameIsReservedException();
        }

    }
}


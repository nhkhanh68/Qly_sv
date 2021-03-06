package uet.usercontroller.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import uet.usercontroller.model.Student;

import java.util.List;


/**
 * Created by Tu on 20-May-16.
 */
@Repository
public interface StudentRepository extends CrudRepository<Student, Integer>, PagingAndSortingRepository<Student, Integer> {
    Student findById(int id);

    Student findByJobSkillsId(int jobSkills);

    Student findByInternshipId(int id);

    Student findByStudentInfoId(int id);

    Student findByInfoBySchoolId(int id);
}
package uet.usercontroller.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uet.usercontroller.DTO.StudentInfoDTO;
import uet.usercontroller.model.Role;
import uet.usercontroller.model.Student;
import uet.usercontroller.model.StudentInfo;
import uet.usercontroller.model.User;
import uet.usercontroller.repository.StudentInfoRepository;
import uet.usercontroller.repository.StudentRepository;
import uet.usercontroller.repository.UserRepository;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by root on 7/20/16.
 */
@Service
public class StudentInfoService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentInfoRepository studentInfoRepository;

    @Autowired
    private UserRepository userRepository;

    // show all student information
    public List<HashMap<String, String>> getAllStudentInfo() {
        List<StudentInfo> allInfo = (List<StudentInfo>) studentInfoRepository.findAll();
        List<HashMap<String, String>> listPartnerInfo = new ArrayList<HashMap<String, String>>();
        for (StudentInfo studentInfo : allInfo){
            HashMap<String, String> lStudentInfo = new HashMap<String, String>();
            Student student = studentRepository.findByStudentInfoId(studentInfo.getId());
            User user = userRepository.findByStudentId(student.getId());
            lStudentInfo.put("userId", String.valueOf(user.getId()));
            lStudentInfo.put("status", user.getStatus());
            lStudentInfo.put("studentInfoId", String.valueOf(studentInfo.getId()));
            lStudentInfo.put("address", studentInfo.getAddress());
            lStudentInfo.put("avatar", studentInfo.getAvatar());
            lStudentInfo.put("birthday", studentInfo.getBirthday());
            lStudentInfo.put("desire", studentInfo.getDesire());
            lStudentInfo.put("email", studentInfo.getEmail());
            lStudentInfo.put("fullName", studentInfo.getFullName());
            lStudentInfo.put("phoneNumber", studentInfo.getPhoneNumber());
            lStudentInfo.put("skype", studentInfo.getSkype());
            listPartnerInfo.add(lStudentInfo);
        }
        return listPartnerInfo;
    }

    // show info of a student
    public StudentInfo getStudentInfo( int id, String token) {
        User user = userRepository.findByToken(token);
        Student student = user.getStudent();
        StudentInfo studentInfo = studentInfoRepository.findOne(id);
        if (user.getRole()==Role.STUDENT) {
            if (student.getStudentInfo().equals(studentInfo)) {
                return studentInfo;
            } else{
                throw new NullPointerException("No result");
            }
        }
        else {
            return studentInfo;
        }

    }

    //edit info of a student
    public StudentInfo editStudentInfo(int id, StudentInfoDTO studentInfoDTO, String token) throws IOException {
        User user = userRepository.findByToken(token);
        Student student = user.getStudent();
        String username = user.getUserName();
        StudentInfo studentinfo = studentInfoRepository.findOne(id);
        if (student.getStudentInfo().equals(studentinfo)){
            if (studentInfoDTO.getBirthday()!=null){
                studentinfo.setBirthday(studentInfoDTO.getBirthday());
            }
            if (studentInfoDTO.getAddress()!=null) {
                studentinfo.setAddress(studentInfoDTO.getAddress());
            }
            if (studentInfoDTO.getEmail()!=null) {
                studentinfo.setEmail(studentInfoDTO.getEmail());
            }
            if (studentInfoDTO.getPhoneNumber()!=null) {
                studentinfo.setPhoneNumber(studentInfoDTO.getPhoneNumber());
            }
            if (studentInfoDTO.getSkype()!=null) {
                studentinfo.setSkype(studentInfoDTO.getSkype());
            }
            if (studentInfoDTO.getDesire()!=null) {
                studentinfo.setDesire(studentInfoDTO.getDesire());
            }
//            if (studentInfoDTO.getAvatar() != null) {
//                String pathname = "../Qly_SV_client/app/users_data/student/" + username + "/";
////                String directoryName = pathname.concat(this.getClassName());
//                String directoryName = "users_data/student/" + username + "/";
//                String fileName = username + "_avatar.jpg";
//                File directory = new File(pathname);
//                if (! directory.exists()) {
//                    directory.mkdir();
//                }
//                byte[] btDataFile = new sun.misc.BASE64Decoder().decodeBuffer(studentInfoDTO.getAvatar());
//                File of = new File( pathname + fileName);
//                FileOutputStream osf = new FileOutputStream(of);
//                osf.write(btDataFile);
//                osf.flush();
//                studentinfo.setAvatar("http://localhost:8000/" + directoryName + username + "_avatar.jpg");
//            }
            return studentInfoRepository.save(studentinfo);
        } else {
            throw new NullPointerException("Error ");
        }
    }

    //change Avatar
    public void changeAva (StudentInfoDTO studentInfoDTO, String token) throws IOException {
        User user = userRepository.findByToken(token);
        Student student = user.getStudent();
        String username = user.getUserName();
        StudentInfo studentInfo = student.getStudentInfo();
        //code đổi tên image thành student_id.jpg và save vào database
        String pathname = "../Qly_SV_client/app/users_data/student/" + username + "/";
//                String directoryName = pathname.concat(this.getClassName());
        String directoryName = "users_data/student/" + username + "/";
        String fileName = username + "_avatar.jpg";
        File directory = new File(pathname);
        if (! directory.exists()) {
            directory.mkdir();
        }
        byte[] btDataFile = DatatypeConverter.parseBase64Binary(studentInfoDTO.getAvatar());
        File of = new File( pathname + fileName);
        FileOutputStream osf = new FileOutputStream(of);
        osf.write(btDataFile);
        osf.flush();
        studentInfo.setAvatar("http://112.137.130.47:8000/" + directoryName + username + "_avatar.jpg");
        studentInfoRepository.save(studentInfo);
    }

    //delete info of a student
    public void deleteStudentInfo(int id, String token) {
        User user = userRepository.findByToken(token);
        Student student = user.getStudent();
        StudentInfo studentinfo = studentInfoRepository.findOne(id);
        if (user.getRole()==Role.STUDENT) {
            if (student.getStudentInfo().equals(studentinfo)) {
                studentinfo.setFullName(null);
                studentinfo.setBirthday(null);
                studentinfo.setPhoneNumber(null);
                studentinfo.setAddress(null);
                studentinfo.setEmail(null);
                studentinfo.setSkype(null);
                studentinfo.setDesire(null);
                studentinfo.setAvatar(null);
            }
        } else {
            studentinfo.setFullName(null);
            studentinfo.setBirthday(null);
            studentinfo.setPhoneNumber(null);
            studentinfo.setAddress(null);
            studentinfo.setEmail(null);
            studentinfo.setSkype(null);
            studentinfo.setDesire(null);
            studentinfo.setAvatar(null);
        }
        studentInfoRepository.save(studentinfo);
    }

}


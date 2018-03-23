package com.zwd.app.controller;

import com.alibaba.fastjson.JSON;
import com.zwd.app.domain.*;
import com.zwd.app.service.*;
import com.zwd.app.util.AddPtDcInfo;
import com.zwd.app.util.LoginInfo;
import com.zwd.app.util.RespInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.print.Doc;
import java.util.Date;

/**
 * @author zwd
 * @date 2018/3/22 16:23
 * @Email lovejavazwd@gmail.com
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private DoctorService doctorService;
    @Autowired
    private DrugService drugService;
    @Autowired
    private DPService dpService;
    @Autowired
    private DPDService dpdService;
    @RequestMapping("addPatient")
    public String addPatient(AddPtDcInfo addPtDcInfo) {
        RespInfo respInfo = new RespInfo();
        Patient patient = addPtDcInfo.getPatient();
        Doctor doctor = addPtDcInfo.getDoctor();
        Drug drug = addPtDcInfo.getDrug();
        userService.insertSeletive(patient);
        doctorService.insertSeletive(doctor);
        drugService.insertSeletive(drug);
        DoctorPatient doctorPatient = new DoctorPatient();
        doctorPatient.setDoctorid(doctor.getId());
        doctorPatient.setPatientid(patient.getId());
        doctorPatient.setTime(new Date());
        doctorPatient.setPathogeny(addPtDcInfo.getPathogeny());
        dpService.insertSelective(doctorPatient);
        DtPtDrug dtPtDrug = new DtPtDrug();
        dtPtDrug.setDoctorpatientid(doctorPatient.getId());
        dtPtDrug.setNum(addPtDcInfo.getNumber());
        dtPtDrug.setDrugid(drug.getId());
        dpdService.insertSelective(dtPtDrug);
        respInfo.setMsg("添加成功！");
        return JSON.toJSONString(respInfo);
    }

    @RequestMapping("patientlogin")
    public String PatientLogin(Patient patient) {
        RespInfo respInfo = new RespInfo();
        if (userService.selectByPhoneAndPassword(patient)==null)
        {
            respInfo.setMsg("此人不存在");

        }
        else {
            respInfo.setMsg("登录成功");
        }

        return JSON.toJSONString(respInfo);
    }

    @RequestMapping("doctorlogin")
    public String DoctorLogin(Doctor doctor) {
        RespInfo respInfo = new RespInfo();
        if (doctorService.selectByPhoneAndPassword(doctor)==null)
        {
            respInfo.setMsg("此人不存在");

        }
        else {
            respInfo.setMsg("登录成功");
        }

        return JSON.toJSONString(respInfo);
    }

    @RequestMapping("login")
    public String Login(LoginInfo loginInfo) {
        if (loginInfo.getStatus()==0) {
            Patient patient = new Patient();
            patient.setPhone(loginInfo.getPhone());
            patient.setPassword(loginInfo.getPassword());
            return PatientLogin(patient);
        } else {
            Doctor doctor = new Doctor();
            doctor.setPhone(loginInfo.getPhone());
            doctor.setPassword(loginInfo.getPassword());
            return DoctorLogin(doctor);
        }
    }


}